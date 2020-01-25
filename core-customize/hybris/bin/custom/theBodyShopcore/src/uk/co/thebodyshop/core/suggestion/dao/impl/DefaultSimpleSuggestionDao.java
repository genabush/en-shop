/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package uk.co.thebodyshop.core.suggestion.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.category.constants.CategoryConstants;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.springframework.util.Assert;

import uk.co.thebodyshop.core.suggestion.dao.SimpleSuggestionDao;


/**
 * Default implementation of {@link SimpleSuggestionDao}.
 *
 * Finds products that are related products that the user has bought.
 */
public class DefaultSimpleSuggestionDao extends AbstractItemDao implements SimpleSuggestionDao
{
	private static final int DEFAULT_LIMIT = 100;
	private static final String REF_QUERY_PARAM_CATEGORY = "category";
	private static final String REF_QUERY_PARAM_PRODUCTS = "products";
	private static final String REF_QUERY_PARAM_USER = "user";
	private static final String REF_QUERY_PARAM_TYPE = "referenceType";
	private static final String REF_QUERY_PARAM_TYPES = "referenceTypes";

	private static final String REF_QUERY_CATEGORY_START = "SELECT {p:" + ProductModel.PK + "} FROM {" + ProductModel._TYPECODE + " AS p  LEFT JOIN " + ProductReferenceModel._TYPECODE + " AS r ON {p:" + ProductModel.PK + "}={r:"
			+ ProductReferenceModel.TARGET + "} LEFT JOIN " + OrderEntryModel._TYPECODE + " AS e ON {r:" + ProductReferenceModel.SOURCE + "}={e:" + OrderEntryModel.PRODUCT + "} LEFT JOIN " + OrderModel._TYPECODE + " AS o ON {e:" + OrderEntryModel.ORDER
			+ "}={o:" + OrderModel.PK + "} LEFT JOIN " + CategoryConstants.Relations.CATEGORYPRODUCTRELATION + " AS c2p ON {r:" + ProductReferenceModel.SOURCE + "}={c2p:" + ProductReferenceModel.TARGET + "} LEFT JOIN " + CategoryModel._TYPECODE
			+ " AS c ON {c2p:" + ProductReferenceModel.SOURCE + "}={c:" + CategoryModel.PK + "}} WHERE {o:" + OrderModel.USER + "}=?user AND {c:" + CategoryModel.PK + "}=?category";

	private static final String REF_QUERY_PRODUCT_START = "SELECT DISTINCT {p:" + ProductModel.PK + "}, COUNT({p:" + ProductModel.PK + "}) AS NUM" + " FROM {" + ProductModel._TYPECODE + " AS p LEFT JOIN " + ProductReferenceModel._TYPECODE
			+ " AS r ON {p:" + ProductModel.PK + "}={r:" + ProductReferenceModel.TARGET + "}} WHERE {r:" + ProductReferenceModel.SOURCE + "} IN (?products) AND {r:" + ProductReferenceModel.TARGET + "} NOT IN (?products)";

	private static final String REF_QUERY_TYPE = " AND {r:" + ProductReferenceModel.REFERENCETYPE + "} IN (?referenceType)";
	private static final String REF_QUERY_TYPES = " AND {r:" + ProductReferenceModel.REFERENCETYPE + "} IN (?referenceTypes)";
	private static final String REF_QUERY_SUB = " AND NOT EXISTS ({{"
			+ " SELECT 1 FROM {" + OrderEntryModel._TYPECODE + " AS e2 LEFT JOIN " + OrderModel._TYPECODE + " AS o2 ON {e2:" + OrderEntryModel.ORDER + "}={o2:" + OrderModel.PK + "}} " + " WHERE {e2:" + OrderEntryModel.PRODUCT + "}={r:"
			+ ProductReferenceModel.TARGET + "} AND {o2:" + OrderModel.USER + "}=?user }})";

	private static final String REF_QUERY_CATEGORY_ORDER = " ORDER BY {o:" + OrderModel.CREATIONTIME + "} DESC";

	private static final String REF_QUERY_PRODUCT_GROUP = " GROUP BY {p:" + ProductModel.PK + "}";
	private static final String REF_QUERY_PRODUCT_ORDER = " ORDER BY NUM DESC";

	@Override
	public List<ProductModel> findProductsRelatedToPurchasedProductsByCategory(final CategoryModel category,
			final List<ProductReferenceTypeEnum> referenceTypes, final UserModel user, final boolean excludePurchased,
			final Integer limit)
	{
		Assert.notNull(category);
		Assert.notNull(user);

		final int maxResultCount = limit == null ? DEFAULT_LIMIT : limit.intValue();

		final Map<String, Object> params = new HashMap<>();
		final StringBuilder builder = new StringBuilder(REF_QUERY_CATEGORY_START);
		if (excludePurchased)
		{
			builder.append(REF_QUERY_SUB);
		}
		if (CollectionUtils.isNotEmpty(referenceTypes))
		{
			builder.append(REF_QUERY_TYPES);
			params.put(REF_QUERY_PARAM_TYPES, referenceTypes);
		}
		builder.append(REF_QUERY_CATEGORY_ORDER);

		params.put(REF_QUERY_PARAM_USER, user);
		params.put(REF_QUERY_PARAM_CATEGORY, category);

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.addQueryParameters(params);
		query.setNeedTotal(false);
		query.setCount(maxResultCount);

		final SearchResult<ProductModel> result = getFlexibleSearchService().search(query);
		return result.getResult();
	}

	@Override
	public List<ProductModel> findProductsRelatedToProducts(final List<ProductModel> products,
			final List<ProductReferenceTypeEnum> referenceTypes, final UserModel user, final boolean excludePurchased,
			final Integer limit)
	{
		Assert.notNull(products);
		Assert.notNull(user);

		final int maxResultCount = limit == null ? DEFAULT_LIMIT : limit.intValue();

		final Map<String, Object> params = new HashMap<>();
		final StringBuilder builder = new StringBuilder(REF_QUERY_PRODUCT_START);
		if (excludePurchased)
		{
			builder.append(REF_QUERY_SUB);
		}
		if (CollectionUtils.isNotEmpty(referenceTypes))
		{
			builder.append(REF_QUERY_TYPES);
			params.put(REF_QUERY_PARAM_TYPES, referenceTypes);
		}
		builder.append(REF_QUERY_PRODUCT_GROUP);
		builder.append(REF_QUERY_PRODUCT_ORDER);

		params.put(REF_QUERY_PARAM_USER, user);
		params.put(REF_QUERY_PARAM_PRODUCTS, products);

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.addQueryParameters(params);
		query.setNeedTotal(false);
		query.setCount(maxResultCount);

		final SearchResult<ProductModel> result = getFlexibleSearchService().search(query);
		return result.getResult();
	}

	/**
	 * @deprecated Since 5.0. Use
	 *             {@link #findProductsRelatedToPurchasedProductsByCategory(CategoryModel, List, UserModel, boolean, Integer)}
	 */
	@Deprecated(since = "5.0")
	@Override
	public List<ProductModel> findProductsRelatedToPurchasedProductsByCategory(final CategoryModel category, final UserModel user,
			final ProductReferenceTypeEnum referenceType, final boolean excludePurchased, final Integer limit)
	{
		Assert.notNull(category);
		Assert.notNull(user);

		final int maxResultCount = limit == null ? DEFAULT_LIMIT : limit.intValue();

		final Map<String, Object> params = new HashMap<>();
		final StringBuilder builder = new StringBuilder(REF_QUERY_CATEGORY_START);
		if (excludePurchased)
		{
			builder.append(REF_QUERY_SUB);
		}
		if (referenceType != null)
		{
			builder.append(REF_QUERY_TYPE);
			params.put(REF_QUERY_PARAM_TYPE, referenceType);
		}
		builder.append(REF_QUERY_CATEGORY_ORDER);

		params.put(REF_QUERY_PARAM_USER, user);
		params.put(REF_QUERY_PARAM_CATEGORY, category);

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.addQueryParameters(params);
		query.setNeedTotal(false);
		query.setCount(maxResultCount);

		final SearchResult<ProductModel> result = getFlexibleSearchService().search(query);
		return result.getResult();
	}
}
