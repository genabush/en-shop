/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.storelocator.pos;

import de.hybris.platform.storelocator.model.PointOfServiceModel;

/**
 * @author prateek.goel
 */
public interface TbsPointOfServiceService
{
	boolean isAvailableForCollectInStore(PointOfServiceModel pointofService);
}
