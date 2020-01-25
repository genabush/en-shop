/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.search.solrfacetsearch.provider.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.variants.model.VariantProductModel;

import uk.co.thebodyshop.core.model.TbsBaseProductModel;
import uk.co.thebodyshop.core.product.visibility.services.ProductVisibilityService;
import uk.co.thebodyshop.core.services.UserPriceGroupService;

/**
 * @author Jagadeesh
 */
public class BaseProductMultiVariantValueResolver extends AbstractValueResolver<TbsBaseProductModel, Object, Object>
{
	private static final Logger LOG = Logger.getLogger(BaseProductMultiVariantValueResolver.class);

	private final ProductVisibilityService productVisibilityService;

	private final UserPriceGroupService userPriceGroupService;

	public BaseProductMultiVariantValueResolver(final ProductVisibilityService productVisibilityService, final UserPriceGroupService userPriceGroupService)
	{
		this.productVisibilityService = productVisibilityService;
		this.userPriceGroupService = userPriceGroupService;
	}

	@Override
	protected void addFieldValues(final InputDocument document, final IndexerBatchContext batchContext, final IndexedProperty indexedProperty, final TbsBaseProductModel product, final ValueResolverContext<Object, Object> resolverContext)
			throws FieldValueProviderException
	{
		final IndexConfig indexConfig = batchContext.getFacetSearchConfig().getIndexConfig();
		final BaseSiteModel baseSiteModel = indexConfig.getBaseSite();
		if (baseSiteModel != null && baseSiteModel.getStores() != null && !baseSiteModel.getStores().isEmpty())
		{
			final BaseStoreModel baseStore = baseSiteModel.getStores().get(0);
			if (Objects.nonNull(baseStore))
			{
				if (CollectionUtils.isNotEmpty(product.getVariants()))
				{
					userPriceGroupService.setUserPriceGroupForSite(indexConfig.getBaseSite());
					final List<VariantProductModel> visibleVariants = product.getVariants().stream().filter(this::isVariantVisible).collect(Collectors.toList());
					document.addField(indexedProperty, visibleVariants.size() >= 2, resolverContext.getFieldQualifier());
				}
			}
		}
	}

	private boolean isVariantVisible(final VariantProductModel variantProductModel)
	{
		return getProductVisibilityService().getVisibiltyInfo(variantProductModel.getCode(), variantProductModel.getCatalogVersion()).isVisible();
	}

	protected ProductVisibilityService getProductVisibilityService()
	{
		return productVisibilityService;
	}

	protected UserPriceGroupService getUserPriceGroupService()
	{
		return userPriceGroupService;
	}
}
