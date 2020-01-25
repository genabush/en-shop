/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.search.solrfacetsearch.populator;

import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import uk.co.thebodyshop.core.search.content.data.ContentPageData;

/**
 * @author vasanthramprakasam
 */
public class SearchResultContentPopulator implements Populator<SearchResultValueData, ContentPageData>
{
    @Override
    public void populate(SearchResultValueData source, ContentPageData target) throws ConversionException
    {
        target.setTitle(getValue(source,"title"));
        target.setSummary(getValue(source,"summary"));
        target.setImageUrl(getValue(source,"imageUrl"));
        target.setUrl(getValue(source,"url"));
    }

    protected <T> T getValue(final SearchResultValueData source, final String propertyName)
    {
        if (source.getValues() == null)
        {
            return null;
        }

        // DO NOT REMOVE the cast (T) below, while it should be unnecessary it is required by the javac compiler
        return (T) source.getValues().get(propertyName);
    }
}
