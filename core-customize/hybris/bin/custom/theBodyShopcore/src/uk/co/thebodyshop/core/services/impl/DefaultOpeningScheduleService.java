/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services.impl;

import de.hybris.platform.storelocator.model.OpeningScheduleModel;
import uk.co.thebodyshop.core.daos.OpeningScheduleDao;
import uk.co.thebodyshop.core.services.OpeningScheduleService;

/**
 * @author Hemchand
 */

public class DefaultOpeningScheduleService implements OpeningScheduleService
{
    private final OpeningScheduleDao openingScheduleDao;

    @Override
    public OpeningScheduleModel getOpeningSchedule(String code)
    {
        return getOpeningScheduleDao().getOpeningSchedule(code);
    }

    public DefaultOpeningScheduleService(OpeningScheduleDao openingScheduleDao)
    {
        this.openingScheduleDao = openingScheduleDao;
    }

    protected OpeningScheduleDao getOpeningScheduleDao()
    {
        return openingScheduleDao;
    }

}
