/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.cms.types.predicates;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.type.TypeService;
import uk.co.thebodyshop.cms.model.components.TbsStoreFinderComponentModel;

import java.util.function.Predicate;

/**
 * @author Balakrishna
 **/
public class TbsStoreFinderComponentTypePredicate implements Predicate<ItemModel> {
    private TypeService typeService;

    @Override
    public boolean test(final ItemModel itemModel)
    {
        return getTypeService().isAssignableFrom(TbsStoreFinderComponentModel._TYPECODE, itemModel.getItemtype());
    }

    public TbsStoreFinderComponentTypePredicate(TypeService typeService)
    {
        this.typeService = typeService;
    }

    protected TypeService getTypeService()
    {
        return typeService;
    }

}
