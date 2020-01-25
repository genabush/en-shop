/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.s4.setup;

import java.util.ArrayList;
import java.util.List;

import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;

import uk.co.thebodyshop.integration.s4.constants.TheBodyShops4integrationConstants;

@SystemSetup(extension = TheBodyShops4integrationConstants.EXTENSIONNAME)
public class TheBodyShops4integrationSystemSetup extends AbstractSystemSetup
{

	@Override
	public List<SystemSetupParameter> getInitializationOptions()
	{
		final List<SystemSetupParameter> params = new ArrayList<>();
		return params;
	}

	@SystemSetup(type = Type.PROJECT, process = Process.ALL)
	public void createProjectData(final SystemSetupContext context)
	{
		importImpexFile(context, "/theBodyShops4integration/import/product/integrationservices.impex");
	}
}
