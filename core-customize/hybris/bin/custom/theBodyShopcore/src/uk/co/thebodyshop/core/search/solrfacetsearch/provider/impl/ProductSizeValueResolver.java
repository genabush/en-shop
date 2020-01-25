/*
 * Copyright (c) 2019
 * THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 *
 */

package uk.co.thebodyshop.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;

import uk.co.thebodyshop.core.enums.TbsBaseType;
import uk.co.thebodyshop.core.model.TbsBaseProductModel;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author Lakshmi
 **/
public class ProductSizeValueResolver extends AbstractValueResolver<ProductModel, Object, Object> {

    @Override
    protected void addFieldValues(final InputDocument inputDocument, final IndexerBatchContext indexerBatchContext, final IndexedProperty indexedProperty, final ProductModel productModel, final ValueResolverContext<Object, Object> valueResolverContext)
            throws FieldValueProviderException
    {
        if (productModel instanceof TbsVariantProductModel)
        {
            final TbsVariantProductModel tbsVariantProduct = (TbsVariantProductModel) productModel;
            if (TbsBaseType.SIZE.equals(((TbsBaseProductModel)tbsVariantProduct.getBaseProduct()).getType()) && Objects.nonNull(tbsVariantProduct.getSize()))
            {
                StringBuilder sizeValues= new StringBuilder();
                sizeValues.append(tbsVariantProduct.getSize().split(Pattern.quote("."))[0]);
                sizeValues.append(tbsVariantProduct.getUnit().getCode());
                inputDocument.addField(indexedProperty, sizeValues);
            }
        }
    }
}
