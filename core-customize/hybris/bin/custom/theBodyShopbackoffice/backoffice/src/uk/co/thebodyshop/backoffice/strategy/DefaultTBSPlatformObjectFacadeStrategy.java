/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.backoffice.strategy;

import java.util.Collection;

import com.hybris.backoffice.cockpitng.dataaccess.facades.object.DefaultPlatformObjectFacadeStrategy;
import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacadeOperationResult;


public class DefaultTBSPlatformObjectFacadeStrategy extends DefaultPlatformObjectFacadeStrategy
{
	@Override
	public <T> ObjectFacadeOperationResult<T> delete(final Collection<T> objects, final Context ctx)
	{
		final ObjectFacadeOperationResult<T> result = super.delete(objects, ctx);
		return result;
	}

}
