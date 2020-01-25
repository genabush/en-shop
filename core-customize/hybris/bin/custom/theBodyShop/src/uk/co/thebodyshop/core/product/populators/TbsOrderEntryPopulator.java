/*
 * Copyright (c)
 * 2019. THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.product.populators;

import de.hybris.platform.commercefacades.order.converters.populator.OrderEntryPopulator;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.store.services.BaseStoreService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;


/**
 * @author Balakrishna
 */
public class TbsOrderEntryPopulator extends OrderEntryPopulator {
    private BaseStoreService baseStoreService;

    @Autowired
    public TbsOrderEntryPopulator(BaseStoreService baseStoreService) {
        this.baseStoreService = baseStoreService;
    }

    @Override
    public void populate(final AbstractOrderEntryModel source, final OrderEntryData target) {
        super.populate(source, target);
        Integer productMaxOrderQuantity = source.getProduct().getMaxOrderQuantity();
        target.setMaxProductOrderQuantity(Objects.isNull(productMaxOrderQuantity) ? getBaseStoreService().getCurrentBaseStore().getMaxProductOrderQuantity() : productMaxOrderQuantity);
    }

    protected BaseStoreService getBaseStoreService() {
        return baseStoreService;
    }
}
