/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services;

import de.hybris.platform.storelocator.model.OpeningScheduleModel;

/**
 * @author Hemchand
 */

public interface OpeningScheduleService
{
    OpeningScheduleModel getOpeningSchedule(String code);
}
