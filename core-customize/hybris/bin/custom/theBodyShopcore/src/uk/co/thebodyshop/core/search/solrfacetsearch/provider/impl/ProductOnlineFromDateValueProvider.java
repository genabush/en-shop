/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.search.solrfacetsearch.provider.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import de.hybris.platform.variants.model.VariantProductModel;

import uk.co.thebodyshop.core.model.TbsBaseProductModel;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;

/**
 * @author Krishna
 */
public class ProductOnlineFromDateValueProvider extends AbstractValueResolver<ItemModel, Object, Object>
{
	@Override
	protected void addFieldValues(InputDocument document, IndexerBatchContext batchContext, IndexedProperty indexedProperty, ItemModel model, ValueResolverContext<Object, Object> resolverContext) throws FieldValueProviderException
	{
		Date onlineFromDate = getOnlineFromDate(model);
		if(null != onlineFromDate)
		{
			document.addField(indexedProperty, onlineFromDate, resolverContext.getFieldQualifier());
		}
	}
	
	/**
	 * Return Online Date of Base Product or Variant Product
	 */
	private Date getOnlineFromDate(ItemModel model)
	{
		Date onlineFromDate = null;
		
		if(model instanceof TbsVariantProductModel) 
		{
			TbsVariantProductModel variant = (TbsVariantProductModel) model;
			if(Objects.nonNull( variant.getOnlineDate()))
			{
				onlineFromDate = variant.getOnlineDate();
			}
		}
		if(model instanceof TbsBaseProductModel) 
		{
			TbsBaseProductModel baseProduct = (TbsBaseProductModel) model;
			Date recentOnlineDate = getRecentOnlineFromDate(baseProduct);
			if(Objects.nonNull(recentOnlineDate))
			{
				onlineFromDate = recentOnlineDate;
			}
		}
		return onlineFromDate;
	}
	
	private Date getRecentOnlineFromDate(TbsBaseProductModel baseProduct)
	{
		List<Date> dates  = new ArrayList<Date>();
		
		if (CollectionUtils.isNotEmpty(baseProduct.getVariants()))
		{
			Collection<VariantProductModel> variants = baseProduct.getVariants();
			for (VariantProductModel variantProduct : variants)
			{
				if(Objects.nonNull(variantProduct.getOnlineDate()))
				{
					dates.add(variantProduct.getOnlineDate());
				}	
			}
		}
		if(Objects.nonNull(baseProduct.getOnlineDate()))
		{
			dates.add(baseProduct.getOnlineDate());
		}
		
		if (CollectionUtils.isNotEmpty(dates))
		{
			Collections.sort(dates, new Comparator<Date>() 
			{
				public int compare(Date date1, Date date2) 
				{
					if (date1 == null || date2 == null)
						return 0;     
					return date2.compareTo(date1);
        }
      });
		}
		if(CollectionUtils.isNotEmpty(dates))
		{
			return dates.iterator().next();
		}
		return null;
	}	
}
