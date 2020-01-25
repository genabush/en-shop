/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.loyalty.event;

import de.hybris.platform.acceleratorservices.site.AbstractAcceleratorSiteEventListener;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.commerceservices.event.RegisterEvent;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import uk.co.thebodyshop.loyalty.service.LoyaltyService;

/**
 * @author Krishna
 */
public class LoyaltyRegistrationEventListener extends AbstractAcceleratorSiteEventListener<RegisterEvent>
{
	private final LoyaltyService loyaltyService;

	@Override
	protected void onSiteEvent(final RegisterEvent registerEvent)
	{
		getLoyaltyService().registerForLybc(registerEvent.getCustomer(), registerEvent.getSite());

	}

	@Override
	protected SiteChannel getSiteChannelForEvent(final RegisterEvent event)
	{
		final BaseSiteModel site = event.getSite();
		ServicesUtil.validateParameterNotNullStandardMessage("event.order.site", site);
		return site.getChannel();
	}

	public LoyaltyRegistrationEventListener(final LoyaltyService loyaltyService)
	{
		this.loyaltyService = loyaltyService;
	}

	protected LoyaltyService getLoyaltyService()
	{
		return loyaltyService;
	}

}
