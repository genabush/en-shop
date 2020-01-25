/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.populators;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commerceservices.search.solrfacetsearch.populators.SearchSolrQueryPopulator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;

/**
 * @author vasanthramprakasam
 */
public class TbsContentSearchSolrQueryPopulator<INDEXED_PROPERTY_TYPE, INDEXED_TYPE_SORT_TYPE> extends SearchSolrQueryPopulator<INDEXED_PROPERTY_TYPE, INDEXED_TYPE_SORT_TYPE>
{

    private static final Logger LOG = Logger.getLogger(TbsContentSearchSolrQueryPopulator.class);

    @Override
    protected IndexedType getIndexedType(final FacetSearchConfig config)
    {
        Optional<IndexedType> contentType = config.getIndexConfig().getIndexedTypes().values().stream()
                                            .filter(indexedType -> indexedType.getComposedType().getCode().equals(ContentPageModel._TYPECODE)).findAny();
        if (contentType.isPresent())
        {
            return contentType.get();
        }else {
            LOG.error("No index type found for content search");
            throw new ConversionException("No valid index type found for content search");
        }
    }

    @Override
    protected Collection<CatalogVersionModel> getSessionProductCatalogVersions()
    {
        final Collection<CatalogVersionModel> sessionCatalogVersions = getCatalogVersionService().getSessionCatalogVersions();

        return sessionCatalogVersions.stream().filter(catalogVersionModel -> catalogVersionModel.getCatalog() instanceof ContentCatalogModel).collect(Collectors.toList());
    }
}
