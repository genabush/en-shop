/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.daos;

import de.hybris.platform.storelocator.model.OpeningScheduleModel;

/**
 * @author Hemchand
 */
public interface OpeningScheduleDao
{
    OpeningScheduleModel getOpeningSchedule(String code);
}
