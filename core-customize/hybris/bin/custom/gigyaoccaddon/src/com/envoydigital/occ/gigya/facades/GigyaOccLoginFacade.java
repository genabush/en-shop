/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package com.envoydigital.occ.gigya.facades;

import de.hybris.platform.gigya.gigyafacades.login.GigyaLoginFacade;
import de.hybris.platform.gigya.gigyaservices.model.GigyaConfigModel;

/**
 * @author vasanthramprakasam
 */
public interface GigyaOccLoginFacade extends GigyaLoginFacade
{
	 void logOutUserFromGigya(String userId,GigyaConfigModel gigyaConfig);
}
