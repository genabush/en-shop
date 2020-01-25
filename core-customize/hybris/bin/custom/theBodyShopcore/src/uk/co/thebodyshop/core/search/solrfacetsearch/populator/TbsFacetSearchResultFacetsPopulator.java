/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.search.solrfacetsearch.populator;

import de.hybris.platform.solrfacetsearch.config.FacetSortProvider;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.ValueRange;
import de.hybris.platform.solrfacetsearch.search.FacetValue;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.impl.populators.FacetSearchResultFacetsPopulator;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TbsFacetSearchResultFacetsPopulator extends FacetSearchResultFacetsPopulator {

    protected void sortFacetValues(FacetSearchResultFacetsPopulator.FacetInfo facetInfo, SearchQuery searchQuery, List<FacetValue> facetValues) {
        FacetSortProvider sortProvider = this.resolveFacetValuesSortProvider(facetInfo);
        if (sortProvider != null) {
            Comparator<FacetValue> comparator = sortProvider.getComparatorForTypeAndProperty(searchQuery.getIndexedType(), facetInfo.getIndexedProperty());
            Collections.sort(facetValues, comparator);
        } else if (facetInfo.isRanged()) {
            IndexedProperty indexedProperty = facetInfo.getIndexedProperty();

            List<ValueRange> valueRanges = this.resolveFacetValueRanges(indexedProperty, getValueRangesQualifier(indexedProperty, searchQuery));
            Map<String, FacetValue> facetValuesMap = (Map)facetValues.stream().collect(Collectors.toMap(FacetValue::getName, Function.identity()));
            facetValues.clear();
            Iterator var9 = valueRanges.iterator();

            while(var9.hasNext()) {
                ValueRange valueRange = (ValueRange)var9.next();
                FacetValue facetValue = (FacetValue)facetValuesMap.get(valueRange.getName());
                if (facetValue != null) {
                    facetValues.add(facetValue);
                }
            }
        }

    }

    private String getValueRangesQualifier(IndexedProperty indexedProperty, SearchQuery searchQuery) {
        if(indexedProperty.isCurrency()) {
            return searchQuery.getCurrency();
        }
        else if(indexedProperty.isLocalized()) {
            return searchQuery.getLanguage();
        }
        else {
            return StringUtils.EMPTY;
        }
    }

}
