/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services.impl;

import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.store.BaseStoreModel;

import uk.co.thebodyshop.core.services.UserPriceGroupService;

/**
 * @author Jagadeesh
 */
public class DefaultUserPriceGroupService implements UserPriceGroupService
{

	private final SessionService sessionService;

	public DefaultUserPriceGroupService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	public void setUserPriceGroupForSite(final BaseSiteModel baseSite)
	{
		final List<BaseStoreModel> baseStores = baseSite.getStores();
		if (CollectionUtils.isNotEmpty(baseStores))
		{
			final Optional<BaseStoreModel> optionalBaseStoreModel = baseStores.stream().findFirst();
			if (optionalBaseStoreModel.isPresent())
			{
				final BaseStoreModel baseStoreModel = optionalBaseStoreModel.get();
				getSessionService().setAttribute(Europe1Constants.PARAMS.UPG, baseStoreModel.getBaseStoreUserPriceGroup());
			}
		}
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}
}
