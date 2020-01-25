/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;

/**
 * @author Jagadeesh
 */
public interface UserPriceGroupService
{
	/**
	 * This method sets the User price group to the session from site
	 *
	 * @param baseSite
	 */
	public void setUserPriceGroupForSite(final BaseSiteModel baseSite);
}
