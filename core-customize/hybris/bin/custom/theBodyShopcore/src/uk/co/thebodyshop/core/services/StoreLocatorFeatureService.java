/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services;

import de.hybris.platform.commerceservices.model.storelocator.StoreLocatorFeatureModel;

/**
 * @author Hemchand
 */

public interface StoreLocatorFeatureService
{
    StoreLocatorFeatureModel getStoreLocatorFeatureForCode(String code);
}
