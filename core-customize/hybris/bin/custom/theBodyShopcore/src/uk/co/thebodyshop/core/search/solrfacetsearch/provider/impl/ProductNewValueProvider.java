/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.search.solrfacetsearch.provider.impl;

import java.util.Date;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import de.hybris.platform.store.BaseStoreModel;

import uk.co.thebodyshop.core.model.TbsBaseProductModel;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;


/**
 * @author Krishna
 */
public class ProductNewValueProvider extends AbstractValueResolver<ItemModel, Object, Object> 
{

	@Override
	protected void addFieldValues(InputDocument document, IndexerBatchContext batchContext, IndexedProperty indexedProperty, ItemModel model, ValueResolverContext<Object, Object> resolverContext) throws FieldValueProviderException
	{
		IndexConfig indexConfig = batchContext.getFacetSearchConfig().getIndexConfig();
		BaseSiteModel baseSiteModel = indexConfig.getBaseSite();
		if (baseSiteModel != null && baseSiteModel.getStores() != null && !baseSiteModel.getStores().isEmpty())
		{
			final BaseStoreModel baseStore = baseSiteModel.getStores().get(0);
			if (Objects.nonNull(baseStore))
			{
				if (model instanceof TbsVariantProductModel)
				{
					boolean productNew = checkProductIsNew(baseStore,(ProductModel)model);
					document.addField(indexedProperty, productNew, resolverContext.getFieldQualifier());
				}
				if (model instanceof TbsBaseProductModel)
				{
					boolean productNew = checkBaseProductIsNew(baseStore,model);
					document.addField(indexedProperty, productNew, resolverContext.getFieldQualifier());
				}
			}
		}
	}
	
	/**
	 * Checks Base Product is new or not
	 */
	private boolean checkBaseProductIsNew(BaseStoreModel baseStore,ItemModel model)
	{
		Boolean productNew = true;
		boolean isVariantNew = false;
		ProductModel baseProduct = (ProductModel)model;
		if(CollectionUtils.isNotEmpty(baseProduct.getVariants()))
		{
			for (ProductModel varaint : baseProduct.getVariants())
			{
				if(Objects.nonNull( varaint.getOnlineDate()))
				{
					isVariantNew = checkProductIsNew(baseStore , varaint);
				}
				if(isVariantNew)
				{
					break;
				}
			}
		}
		if(checkProductIsNew(baseStore , baseProduct) || isVariantNew)
		{
			productNew = Boolean.TRUE;
		}
		else
		{
			productNew = Boolean.FALSE;
		}
		return productNew;
	}
	
	private boolean checkProductIsNew(BaseStoreModel baseStore,ProductModel product)
	{
		Date todayDate = new Date();
		Integer newInThreshold = baseStore.getNewInThreshold();
		Boolean productNew = product.getProductNew();
		if(Objects.nonNull(product.getOnlineDate()))
		{
			Date onlineFromDate = product.getOnlineDate();
			int isNew = DateUtils.addDays(onlineFromDate, newInThreshold).compareTo(todayDate);
			if(isNew<0)
			{
				productNew = Boolean.FALSE;
			}
		}
		return productNew;
	}
}
