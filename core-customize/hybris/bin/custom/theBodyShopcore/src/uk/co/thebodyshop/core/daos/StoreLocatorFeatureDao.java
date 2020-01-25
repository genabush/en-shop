/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.daos;

import de.hybris.platform.commerceservices.model.storelocator.StoreLocatorFeatureModel;

/**
 * @author Hemchand
 */

public interface StoreLocatorFeatureDao
{
    StoreLocatorFeatureModel getStoreLocatorFeatureForCode(String code);
}
