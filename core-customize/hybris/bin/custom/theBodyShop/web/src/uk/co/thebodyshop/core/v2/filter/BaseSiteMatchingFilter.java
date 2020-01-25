/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package uk.co.thebodyshop.core.v2.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.webservicescommons.util.YSanitizer;

import uk.co.thebodyshop.core.exceptions.InvalidResourceException;
import uk.co.thebodyshop.core.services.UserPriceGroupService;

/**
 * Filter that resolves base site id from the requested url and activates it.
 */
public class BaseSiteMatchingFilter extends AbstractUrlMatchingFilter
{
	private static final Logger LOG = Logger.getLogger(BaseSiteMatchingFilter.class);

	private String regexp;
	private BaseSiteService baseSiteService;

	private UserPriceGroupService userPriceGroupService;

	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException
	{
		final String baseSiteID = getBaseSiteValue(request, regexp);

		if (baseSiteID != null)
		{
			final BaseSiteModel requestedBaseSite = getBaseSiteService().getBaseSiteForUID(baseSiteID);
			if (requestedBaseSite != null)
			{
				final BaseSiteModel currentBaseSite = getBaseSiteService().getCurrentBaseSite();

				if (!requestedBaseSite.equals(currentBaseSite))
				{
					getBaseSiteService().setCurrentBaseSite(requestedBaseSite, true);
					userPriceGroupService.setUserPriceGroupForSite(requestedBaseSite);
				}
			}
			else
			{
				final InvalidResourceException ex = new InvalidResourceException(YSanitizer.sanitize(baseSiteID));
				LOG.debug(ex.getMessage());
				throw ex;
			}
		}

		filterChain.doFilter(request, response);
	}

	protected String getRegexp()
	{
		return regexp;
	}

	@Required
	public void setRegexp(final String regexp)
	{
		this.regexp = regexp;
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	protected UserPriceGroupService getUserPriceGroupService()
	{
		return userPriceGroupService;
	}

	public void setUserPriceGroupService(final UserPriceGroupService userPriceGroupService)
	{
		this.userPriceGroupService = userPriceGroupService;
	}
}
