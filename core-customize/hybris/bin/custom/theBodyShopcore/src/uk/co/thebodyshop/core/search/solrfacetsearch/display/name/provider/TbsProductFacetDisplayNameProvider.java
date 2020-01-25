/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.search.solrfacetsearch.display.name.provider;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractFacetValueDisplayNameProvider;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import uk.co.thebodyshop.core.model.TbsProductFacetModel;
import uk.co.thebodyshop.core.services.ProductFacetService;

public class TbsProductFacetDisplayNameProvider extends AbstractFacetValueDisplayNameProvider
{
	private final ProductFacetService tbsProductFacetService;

	private final CommonI18NService commonI18NService;

	public TbsProductFacetDisplayNameProvider(final ProductFacetService tbsProductFacetService, final CommonI18NService commonI18NService)
	{
		this.tbsProductFacetService = tbsProductFacetService;
		this.commonI18NService = commonI18NService;
	}

	@Override
	public String getDisplayName(final SearchQuery searchQuery, final IndexedProperty indexedProperty, final String facetCode)
	{
		String displayName = StringUtils.EMPTY;
		final String typeForFacet = StringUtils.capitalize(indexedProperty.getName()) + "Facet";
		final TbsProductFacetModel tbsProductFacet = getTbsProductFacetService().getFacetByTypeAndCode(typeForFacet, facetCode);
		final LanguageModel queryLanguage = getCommonI18NService().getLanguage(searchQuery.getLanguage());
		if (tbsProductFacet != null && queryLanguage != null)
		{
			displayName = tbsProductFacet.getName(getCommonI18NService().getLocaleForLanguage(queryLanguage));
			if (StringUtils.isBlank(displayName))
			{
				displayName = getDisplayNameForFallbackLanguage(tbsProductFacet, queryLanguage);
			}
		}
		return displayName;
	}

	private String getDisplayNameForFallbackLanguage(final TbsProductFacetModel tbsProductFacet, final LanguageModel queryLanguage)
	{
		String displayName = StringUtils.EMPTY;
		final List<LanguageModel> fallbackLanguages = queryLanguage.getFallbackLanguages();
		if (fallbackLanguages != null && CollectionUtils.isNotEmpty(fallbackLanguages))
		{
			final LanguageModel fallbackLanguage = fallbackLanguages.get(0);
			displayName = tbsProductFacet.getName(getCommonI18NService().getLocaleForLanguage(fallbackLanguage));
		}
		return displayName;
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	protected ProductFacetService getTbsProductFacetService()
	{
		return tbsProductFacetService;
	}
}
