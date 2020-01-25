/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package uk.co.thebodyshop.smartedit.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import uk.co.thebodyshop.smartedit.constants.TheBodyShopsmarteditConstants;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class TheBodyShopsmarteditManager extends GeneratedTheBodyShopsmarteditManager
{
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger( TheBodyShopsmarteditManager.class.getName() );
	
	public static final TheBodyShopsmarteditManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (TheBodyShopsmarteditManager) em.getExtension(TheBodyShopsmarteditConstants.EXTENSIONNAME);
	}
	
}
