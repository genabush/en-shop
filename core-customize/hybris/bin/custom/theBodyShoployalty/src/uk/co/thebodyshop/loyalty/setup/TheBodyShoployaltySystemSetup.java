/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package uk.co.thebodyshop.loyalty.setup;

import de.hybris.platform.core.initialization.SystemSetup;

import uk.co.thebodyshop.loyalty.constants.TheBodyShoployaltyConstants;

@SystemSetup(extension = TheBodyShoployaltyConstants.EXTENSIONNAME)
public class TheBodyShoployaltySystemSetup
{
	@SystemSetup(process = SystemSetup.Process.INIT, type = SystemSetup.Type.ESSENTIAL)
	public void createEssentialData()
	{

	}
}
