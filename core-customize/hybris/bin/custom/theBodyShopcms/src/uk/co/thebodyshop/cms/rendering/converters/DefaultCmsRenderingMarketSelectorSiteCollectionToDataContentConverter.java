/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package uk.co.thebodyshop.cms.rendering.converters;

import de.hybris.platform.cms2.common.functions.Converter;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.MapUtils;

import com.google.gson.Gson;
import com.microsoft.sqlserver.jdbc.StringUtils;

import uk.co.thebodyshop.cms.data.MarketSelectorData;


/**
 * @author Marcin
 */
public class DefaultCmsRenderingMarketSelectorSiteCollectionToDataContentConverter
		implements Converter<Map<String, Object>, String>
{

	@Override
	public String convert(final Map<String, Object> marketSelectorSiteMap)
	{
		StringBuilder marketSelectorAttribute = null;
		if (MapUtils.isNotEmpty(marketSelectorSiteMap))
		{
			for (final Entry<String, Object> entry : marketSelectorSiteMap.entrySet())
			{
				if (entry.getValue() instanceof MarketSelectorData)
				{
					if (null == marketSelectorAttribute)
					{
						marketSelectorAttribute = new StringBuilder("[");
					}
					else
					{
						marketSelectorAttribute.append(",");
					}
					final Gson gson = new Gson();
					marketSelectorAttribute.append(gson.toJson((MarketSelectorData) entry.getValue()).replaceAll("\"", "'"));
				}
			}
		}
		if (null != marketSelectorAttribute)
		{
			marketSelectorAttribute.append("]");
			return marketSelectorAttribute.toString();
		}
		return StringUtils.EMPTY;
	}

}
