/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services.impl;

import de.hybris.platform.commerceservices.model.storelocator.StoreLocatorFeatureModel;
import uk.co.thebodyshop.core.daos.StoreLocatorFeatureDao;
import uk.co.thebodyshop.core.services.StoreLocatorFeatureService;

/**
 * @author Hemchand
 */

public class DefaultStoreLocatorFeatureService implements StoreLocatorFeatureService
{
    private final StoreLocatorFeatureDao storeLocatorFeatureDao;
    @Override
    public StoreLocatorFeatureModel getStoreLocatorFeatureForCode(String code)
    {
        return getStoreLocatorFeatureDao().getStoreLocatorFeatureForCode(code);
    }

    public DefaultStoreLocatorFeatureService(StoreLocatorFeatureDao storeLocatorFeatureDao)
    {
        this.storeLocatorFeatureDao = storeLocatorFeatureDao;
    }

    protected StoreLocatorFeatureDao getStoreLocatorFeatureDao()
    {
        return storeLocatorFeatureDao;
    }
}
