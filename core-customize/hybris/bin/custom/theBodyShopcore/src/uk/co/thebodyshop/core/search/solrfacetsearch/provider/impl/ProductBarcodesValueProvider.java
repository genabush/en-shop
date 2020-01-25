/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.search.solrfacetsearch.provider.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import de.hybris.platform.variants.model.VariantProductModel;

import uk.co.thebodyshop.core.model.TbsVariantProductModel;


public class ProductBarcodesValueProvider extends AbstractValueResolver<ItemModel, Object, Object>
{
	private SearchRestrictionService searchRestrictionService;

	@Override
	protected void addFieldValues(final InputDocument document, final IndexerBatchContext batchContext, final IndexedProperty indexedProperty, final ItemModel model, final ValueResolverContext<Object, Object> resolverContext)
			throws FieldValueProviderException
	{

		if (model instanceof ProductModel)
		{
			final ProductModel product = (ProductModel) model;

			final List<String> barcodes = getPropertyValue(product, indexedProperty);

			if (CollectionUtils.isNotEmpty(barcodes))
			{
				document.addField(indexedProperty, barcodes, resolverContext.getFieldQualifier());
			}
		}

	}

	private Collection<String> collectProductBarcodes(final ProductModel product)
	{
		if (product.getApprovalStatus() == ArticleApprovalStatus.APPROVED || isPhasedOut(product))
		{
			return product.getBarcodelist();
		}
		return Collections.emptyList();
	}
	private boolean isPhasedOut(final ProductModel product)
	{
		if (ArticleApprovalStatus.DISCONTINUED.equals(product.getApprovalStatus()) && product instanceof TbsVariantProductModel)
		{
			final TbsVariantProductModel tbsVariant = (TbsVariantProductModel) product;
			return tbsVariant.getPhaseInProduct() != null;
		}
		return false;
	}
	protected List<String> getPropertyValue(final ProductModel product, final IndexedProperty indexedProperty)
	{
		final List<String> barcodeList = new ArrayList<>();
		final Collection<String> productBarcodes = collectProductBarcodes(product);
		if (CollectionUtils.isNotEmpty(productBarcodes))
		{
			barcodeList.addAll(productBarcodes);
		}
		getSearchRestrictionService().disableSearchRestrictions();
		if (CollectionUtils.isNotEmpty(product.getVariants()))
		{
			for (final VariantProductModel variant : product.getVariants())
			{
				final Collection<String> variantProductBarcodes = collectProductBarcodes(variant);
				if (CollectionUtils.isNotEmpty(variantProductBarcodes))
				{
					barcodeList.addAll(variantProductBarcodes);
				}
			}
		}
		getSearchRestrictionService().enableSearchRestrictions();
		return barcodeList;
	}

	public SearchRestrictionService getSearchRestrictionService() {
		return searchRestrictionService;
	}

	public void setSearchRestrictionService(final SearchRestrictionService searchRestrictionService) {
		this.searchRestrictionService = searchRestrictionService;
	}
}
