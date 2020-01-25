/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import uk.co.thebodyshop.core.model.TbsBaseProductModel;
import uk.co.thebodyshop.core.model.TbsProductFacetModel;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;

import java.util.ArrayList;
import java.util.List;

public class TbsProductFacetValueResolver extends AbstractValueResolver<ProductModel, Object, Object> {

    private ModelService modelService;

    public TbsProductFacetValueResolver(ModelService modelService) {
        this.modelService = modelService;
    }

    @Override
    protected void addFieldValues(InputDocument inputDocument, IndexerBatchContext indexerBatchContext, IndexedProperty indexedProperty, ProductModel product, ValueResolverContext<Object, Object> valueResolverContext) throws FieldValueProviderException {
        if(product instanceof TbsBaseProductModel) {
            addFieldValues(inputDocument, indexedProperty, (TbsBaseProductModel)product);
        }
        else if(product instanceof TbsVariantProductModel) {
            TbsVariantProductModel tbsVariantProduct = (TbsVariantProductModel)product;
            if(tbsVariantProduct.getBaseProduct() instanceof TbsBaseProductModel) {
                TbsBaseProductModel tbsBaseProduct = (TbsBaseProductModel)tbsVariantProduct.getBaseProduct();
                addFieldValues(inputDocument, indexedProperty, tbsBaseProduct);
            }
        }
    }

    private void addFieldValues(InputDocument inputDocument, IndexedProperty indexedProperty, TbsBaseProductModel tbsBaseProduct) throws FieldValueProviderException {
        Object valueObj = getModelService().getAttributeValue(tbsBaseProduct, indexedProperty.getName());
        if (valueObj instanceof TbsProductFacetModel) {
            inputDocument.addField(indexedProperty, ((TbsProductFacetModel)valueObj).getCode());
        }
        else if(valueObj instanceof List<?>) {
            addFieldValues(inputDocument, indexedProperty, (List<?>)valueObj);
        }
    }

    private void addFieldValues(InputDocument inputDocument, IndexedProperty indexedProperty, List<?> valueObjects) throws FieldValueProviderException {
        List<String> facetCodes = new ArrayList<>();
        for(Object valueObj : valueObjects) {
            if(valueObj instanceof TbsProductFacetModel) {
                TbsProductFacetModel tbsProductFacet = (TbsProductFacetModel)valueObj;
                facetCodes.add(tbsProductFacet.getCode());
            }
        }
        inputDocument.addField(indexedProperty, facetCodes);
    }

    protected ModelService getModelService() {
        return modelService;
    }

}
