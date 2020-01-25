/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.search.solrfacetsearch.provider.impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import uk.co.thebodyshop.cms.model.SearchKeywordModel;

/**
 * @author vasanthramprakasam
 */
public class TbsSearchKeywordsValueProvider extends AbstractValueResolver<ContentPageModel, Collection<SearchKeywordModel>,Object>
{

    @Override
    protected void addFieldValues(InputDocument document, IndexerBatchContext batchContext, IndexedProperty indexedProperty, ContentPageModel contentPageModel, ValueResolverContext<Collection<SearchKeywordModel>, Object> resolverContext) throws FieldValueProviderException
    {
        Collection<SearchKeywordModel> searchKeywordModels = contentPageModel.getSearchKeywords();

        if(CollectionUtils.isNotEmpty(searchKeywordModels))
        {
            List<String> keyWords = searchKeywordModels.stream().map(SearchKeywordModel::getName).collect(Collectors.toList());
            for(String keyWord : keyWords)
            {
                filterAndAddFieldValues(document, batchContext, indexedProperty, keyWord,
                        resolverContext.getFieldQualifier());
            }
        }

    }

}
