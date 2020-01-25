/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package uk.co.thebodyshop.smarteditwebservices.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;
import uk.co.thebodyshop.smarteditwebservices.constants.TheBodyShopsewebservicesConstants;

public class TheBodyShopsewebservicesManager extends GeneratedTheBodyShopsewebservicesManager
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger( TheBodyShopsewebservicesManager.class.getName() );
	
	public static final TheBodyShopsewebservicesManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (TheBodyShopsewebservicesManager) em.getExtension(TheBodyShopsewebservicesConstants.EXTENSIONNAME);
	}
	
}
