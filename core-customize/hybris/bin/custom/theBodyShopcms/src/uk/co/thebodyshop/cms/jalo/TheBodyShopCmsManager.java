/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.cms.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;

import org.apache.log4j.Logger;

import uk.co.thebodyshop.cms.constants.TheBodyShopCmsConstants;


public class TheBodyShopCmsManager extends GeneratedTheBodyShopCmsManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( TheBodyShopCmsManager.class.getName() );

	public static final TheBodyShopCmsManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (TheBodyShopCmsManager) em.getExtension(TheBodyShopCmsConstants.EXTENSIONNAME);
	}

}
