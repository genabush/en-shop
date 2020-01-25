/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.search.solrfacetsearch.populator;

import de.hybris.platform.commerceservices.search.facetdata.BreadcrumbData;
import de.hybris.platform.commerceservices.search.facetdata.FacetData;
import de.hybris.platform.commerceservices.search.facetdata.SpellingSuggestionData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import uk.co.thebodyshop.core.search.content.data.ContentPageData;
import uk.co.thebodyshop.core.search.facetdata.ContentSearchPageData;

/**
 * @author vasanthramprakasam
 */
public class ContentSearchPagePopulator<QUERY, STATE, RESULT, ITEM extends ContentPageData>
        implements Populator<ContentSearchPageData<QUERY, RESULT>, ContentSearchPageData<STATE, ITEM>>
{
    private Converter<QUERY, STATE> searchStateConverter;
    private Converter<BreadcrumbData<QUERY>, BreadcrumbData<STATE>> breadcrumbConverter;
    private Converter<FacetData<QUERY>, FacetData<STATE>> facetConverter;
    private Converter<SpellingSuggestionData<QUERY>, SpellingSuggestionData<STATE>> spellingSuggestionConverter;
    private Converter<RESULT, ITEM> searchResultContentConverter;

    public ContentSearchPagePopulator(Converter<QUERY, STATE> searchStateConverter, Converter<BreadcrumbData<QUERY>, BreadcrumbData<STATE>> breadcrumbConverter, Converter<FacetData<QUERY>, FacetData<STATE>> facetConverter, Converter<SpellingSuggestionData<QUERY>, SpellingSuggestionData<STATE>> spellingSuggestionConverter, Converter<RESULT, ITEM> searchResultContentConverter)
    {
        this.searchStateConverter = searchStateConverter;
        this.breadcrumbConverter = breadcrumbConverter;
        this.facetConverter = facetConverter;
        this.spellingSuggestionConverter = spellingSuggestionConverter;
        this.searchResultContentConverter = searchResultContentConverter;
    }

    @Override
    public void populate(ContentSearchPageData<QUERY, RESULT> source, ContentSearchPageData<STATE, ITEM> target) throws ConversionException
    {
        target.setFreeTextSearch(source.getFreeTextSearch());

        if (source.getBreadcrumbs() != null)
        {
            target.setBreadcrumbs(Converters.convertAll(source.getBreadcrumbs(), getBreadcrumbConverter()));
        }

        target.setCurrentQuery(getSearchStateConverter().convert(source.getCurrentQuery()));

        if (source.getFacets() != null)
        {
            target.setFacets(Converters.convertAll(source.getFacets(), getFacetConverter()));
        }

        target.setPagination(source.getPagination());

        if (source.getResults() != null)
        {
            target.setResults(Converters.convertAll(source.getResults(), getSearchResultContentConverter()));
        }

        target.setSorts(source.getSorts());

        if (source.getSpellingSuggestion() != null)
        {
            target.setSpellingSuggestion(getSpellingSuggestionConverter().convert(source.getSpellingSuggestion()));
        }

        target.setKeywordRedirectUrl(source.getKeywordRedirectUrl());
    }

    protected Converter<QUERY, STATE> getSearchStateConverter()
    {
        return searchStateConverter;
    }

    protected Converter<BreadcrumbData<QUERY>, BreadcrumbData<STATE>> getBreadcrumbConverter()
    {
        return breadcrumbConverter;
    }

    protected Converter<FacetData<QUERY>, FacetData<STATE>> getFacetConverter()
    {
        return facetConverter;
    }

    protected Converter<SpellingSuggestionData<QUERY>, SpellingSuggestionData<STATE>> getSpellingSuggestionConverter()
    {
        return spellingSuggestionConverter;
    }

    protected Converter<RESULT, ITEM> getSearchResultContentConverter()
    {
        return searchResultContentConverter;
    }
}
