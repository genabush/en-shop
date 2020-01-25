/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.search.solrfacetsearch.provider.impl;

import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;

import uk.co.thebodyshop.core.catalog.services.TbsCatalogVersionService;
import uk.co.thebodyshop.core.model.TbsBaseProductModel;

public class ProductPriceAvailableValueProvider extends AbstractValueResolver<ItemModel, Object, Object>
{

	private TbsCatalogVersionService tbsCatalogVersionService;

	@Override
	protected void addFieldValues(final InputDocument document, final IndexerBatchContext batchContext, final IndexedProperty indexedProperty, final ItemModel model, final ValueResolverContext<Object, Object> resolverContext)
			throws FieldValueProviderException
	{
		if (model instanceof ProductModel)
		{
			final ProductModel product = (ProductModel) model;
			final boolean isProductPriceAvailable = getPropertyValue(product, indexedProperty);
			document.addField(indexedProperty, isProductPriceAvailable, resolverContext.getFieldQualifier());
		}

	}


	protected boolean getPropertyValue(final ProductModel product, final IndexedProperty indexedProperty)
	{
		return isActivePriceAvailable(product);
	}

	private boolean isActivePriceAvailable(final ProductModel product)
	{
		if (getTbsCatalogVersionService().isGlobalProductCatalog(product.getCatalogVersion()))
		{
			return true;
		}
		else if(product instanceof TbsBaseProductModel && CollectionUtils.isNotEmpty(product.getVariants()))
		{
			final int totalVariantListWithoutPrice = product.getVariants().stream().filter((variant) -> CollectionUtils.isEmpty(variant.getEurope1Prices())).collect(Collectors.toList()).size();
			return totalVariantListWithoutPrice != product.getVariants().size();
		}
		return CollectionUtils.isNotEmpty(product.getEurope1Prices());
	}

	protected TbsCatalogVersionService getTbsCatalogVersionService()
	{
		return tbsCatalogVersionService;
	}

	public void setTbsCatalogVersionService(final TbsCatalogVersionService tbsCatalogVersionService)
	{
		this.tbsCatalogVersionService = tbsCatalogVersionService;
	}

}