/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.storelocator.pos.impl;

import java.util.Date;

import org.apache.commons.lang3.BooleanUtils;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.storelocator.pos.impl.DefaultPointOfServiceService;

import uk.co.thebodyshop.core.storelocator.pos.TbsPointOfServiceService;

/**
 * @author prateek.goel
 */
public class DefaultTbsPointOfServiceService extends DefaultPointOfServiceService implements TbsPointOfServiceService
{

	private ConfigurationService configurationService;

	private TimeService timeService;

	@Override
	public boolean isAvailableForCollectInStore(final PointOfServiceModel pointofService)
	{
		if (BooleanUtils.isTrue(pointofService.getEnabledForCis()))
		{
			final Integer collectInStoreOrderThreshold = pointofService.getMaxCapacity();
			final Integer cisOpenOrders = pointofService.getCisOpenOrders();
			return !isStoreTemporarilyClosed(pointofService) && (!storeCapacityCheckEnabled() || null == collectInStoreOrderThreshold || (null != cisOpenOrders && cisOpenOrders.intValue() < collectInStoreOrderThreshold.intValue()));
		}
		return false;
	}

	private boolean isStoreTemporarilyClosed(final PointOfServiceModel pointOfService)
	{
		final Date currentDate = getTimeService().getCurrentTime();
		final Date fromDate = pointOfService.getTemporaryClosedFromDate();
		final Date toDate = pointOfService.getTemporaryClosedToDate();
		return null != fromDate && currentDate.after(fromDate) && currentDate.before(toDate);
	}

	private boolean storeCapacityCheckEnabled()
	{
		return getConfigurationService().getConfiguration().getBoolean("store.stock.pos.capacity.check");
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	protected TimeService getTimeService()
	{
		return timeService;
	}

	public void setTimeService(final TimeService timeService)
	{
		this.timeService = timeService;
	}

}
