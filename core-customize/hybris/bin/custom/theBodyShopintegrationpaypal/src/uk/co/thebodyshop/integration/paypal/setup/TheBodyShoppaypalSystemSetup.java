/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.paypal.setup;

import java.util.ArrayList;
import java.util.List;

import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;

import uk.co.thebodyshop.integration.paypal.constants.TheBodyShopintegrationpaypalConstants;


@SystemSetup(extension = TheBodyShopintegrationpaypalConstants.EXTENSIONNAME)
public class TheBodyShoppaypalSystemSetup extends AbstractSystemSetup
{
	@SystemSetup(process = SystemSetup.Process.INIT, type = SystemSetup.Type.ESSENTIAL)
	public void createEssentialData()
	{
		//Nothing
	}

	 @SystemSetup(type = SystemSetup.Type.PROJECT, process = SystemSetup.Process.ALL)
	 public void createProjectData(final SystemSetupContext context)
	 {
			importImpexFile(context, "/theBodyShopintegrationpaypal/import/paypal-client.impex");
	 }

	 @Override
	 public List<SystemSetupParameter> getInitializationOptions()
	 {
			final List<SystemSetupParameter> params = new ArrayList<>();
			return params;
	 }

}
