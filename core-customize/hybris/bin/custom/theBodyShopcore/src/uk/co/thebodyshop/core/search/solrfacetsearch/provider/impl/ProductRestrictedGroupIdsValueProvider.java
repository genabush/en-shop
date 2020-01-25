/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.search.solrfacetsearch.provider.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import de.hybris.platform.variants.model.VariantProductModel;

import uk.co.thebodyshop.core.model.TbsBaseProductModel;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;

public class ProductRestrictedGroupIdsValueProvider extends AbstractValueResolver<ItemModel, Object, Object>
{

	@Override
	protected void addFieldValues(final InputDocument document, final IndexerBatchContext batchContext, final IndexedProperty indexedProperty, final ItemModel model, final ValueResolverContext<Object, Object> resolverContext)
			throws FieldValueProviderException
	{
		if (model instanceof ProductModel)
		{
			final ProductModel product = (ProductModel) model;

			final List<String> restrictedGroupUids = getPropertyValue(product, indexedProperty);

			if (CollectionUtils.isNotEmpty(restrictedGroupUids))
			{
				document.addField(indexedProperty, restrictedGroupUids, resolverContext.getFieldQualifier());
			}
		}

	}

	protected List<String> getPropertyValue(final ProductModel product, final IndexedProperty indexedProperty)
	{
		final List<String> restrictedGroupIdList = new ArrayList<>();
		Set<PrincipalModel> restrictedGroupList = new HashSet<>();
		if (product instanceof TbsVariantProductModel)
		{
			final TbsVariantProductModel variant = (TbsVariantProductModel) product;
			restrictedGroupList = null != variant.getRestrictedFor() ? variant.getRestrictedFor() : null;
		}
		else if (product instanceof TbsBaseProductModel)
		{
			final TbsBaseProductModel baseProduct = (TbsBaseProductModel) product;
			final Collection<VariantProductModel> variants = baseProduct.getVariants();
			if (variants.size() >= 1)
			{
				for (final VariantProductModel variantProductModel : variants)
				{
					if (null != variantProductModel.getRestrictedFor())
					{
						restrictedGroupList = variantProductModel.getRestrictedFor();
					}
				}
			}
		}
		if (CollectionUtils.isNotEmpty(restrictedGroupList))
		{
			for (final PrincipalModel principal : restrictedGroupList)
			{
				restrictedGroupIdList.add(principal.getUid());
			}
		}
		else
		{
			restrictedGroupIdList.add("NO_RESTRICTED_GROUP");
		}
		return restrictedGroupIdList;
	}

}
