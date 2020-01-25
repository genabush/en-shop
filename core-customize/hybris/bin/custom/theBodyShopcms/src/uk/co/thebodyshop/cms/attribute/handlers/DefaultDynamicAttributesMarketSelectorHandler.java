/**
 *
 */
package uk.co.thebodyshop.cms.attribute.handlers;

import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import uk.co.thebodyshop.cms.data.MarketSelectorData;
import uk.co.thebodyshop.cms.model.components.MarketSelectorCMSComponentModel;
import uk.co.thebodyshop.core.model.MarketSelectorSiteModel;
import uk.co.thebodyshop.core.services.MarketSelectorService;


/**
 * @author Marcin
 *
 */
public class DefaultDynamicAttributesMarketSelectorHandler
		implements DynamicAttributeHandler<Map<String, MarketSelectorData>, MarketSelectorCMSComponentModel>
{
	private final MarketSelectorService tbsMarketSelectorService;

	@Override
	public Map<String, MarketSelectorData> get(final MarketSelectorCMSComponentModel marketSelectorCMSComponentModel)
	{
		final List<MarketSelectorSiteModel> marketsSelectorSites = tbsMarketSelectorService.getAllEnabledSites();
		final Map<String, MarketSelectorData> marketSelectorsMap = new LinkedHashMap<>();
		if (CollectionUtils.isNotEmpty(marketsSelectorSites))
		{
			for (final MarketSelectorSiteModel marketSelectorSiteModel : marketsSelectorSites)
			{
				marketSelectorsMap.put(marketSelectorSiteModel.getUid(), createMarketSelectorData(marketSelectorSiteModel));
			}
		}

		final List<Map.Entry<String, MarketSelectorData>> marketSelectors = new LinkedList<>(marketSelectorsMap.entrySet());
		Collections.sort(marketSelectors, new Comparator<Map.Entry<String, MarketSelectorData>>()
		{
			public int compare(final Map.Entry<String, MarketSelectorData> o1, final Map.Entry<String, MarketSelectorData> o2)
			{
				return (o1.getValue().getName()).compareToIgnoreCase(o2.getValue().getName());
			}
		});

		marketSelectorsMap.clear();
		for (final Entry<String, MarketSelectorData> market : marketSelectors)
		{
			marketSelectorsMap.put(market.getKey(), market.getValue());
		}

		return marketSelectorsMap;
	}

	private MarketSelectorData createMarketSelectorData(final MarketSelectorSiteModel marketSelectorSiteModel)
	{
		final MarketSelectorData marketSelectorData = new MarketSelectorData();
		marketSelectorData.setName(marketSelectorSiteModel.getName());
		marketSelectorData.setUrl(marketSelectorSiteModel.getUrl());
		return marketSelectorData;
	}

	@Override
	public void set(final MarketSelectorCMSComponentModel marketSelectorCMSComponentModel,
			final Map<String, MarketSelectorData> marketSites)
	{
		throw new UnsupportedOperationException();
	}

	@Autowired
	public DefaultDynamicAttributesMarketSelectorHandler(final MarketSelectorService tbsMarketSelectorService)
	{
		this.tbsMarketSelectorService = tbsMarketSelectorService;
	}

	/**
	 * @return the tbsMarketSelectorService
	 */
	protected MarketSelectorService getTbsMarketSelectorService()
	{
		return tbsMarketSelectorService;
	}
}
