/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.search.solrfacetsearch.display.name.provider;

import org.apache.commons.lang.StringUtils;

import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractFacetValueDisplayNameProvider;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

public class RatingRangeDisplayNameProvider extends AbstractFacetValueDisplayNameProvider {

    @Override
    public String getDisplayName(final SearchQuery searchQuery, final IndexedProperty indexedProperty, final String value) {
        return StringUtils.replace(value, "plus", "+");
    }

}
