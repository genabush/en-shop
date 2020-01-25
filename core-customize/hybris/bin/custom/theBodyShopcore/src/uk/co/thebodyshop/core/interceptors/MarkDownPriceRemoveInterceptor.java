/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.interceptors;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;

import uk.co.thebodyshop.core.markdownprice.marker.MarkDownPriceProductModificationMarker;
import uk.co.thebodyshop.core.model.MarkDownPriceRowModel;

/**
 * @author Jagadeesh
 */
public class MarkDownPriceRemoveInterceptor implements RemoveInterceptor<MarkDownPriceRowModel>
{

	MarkDownPriceProductModificationMarker markDownPriceProductModificationMarker;

	public MarkDownPriceRemoveInterceptor(final MarkDownPriceProductModificationMarker markDownPriceProductModificationMarker)
	{
		this.markDownPriceProductModificationMarker = markDownPriceProductModificationMarker;
	}

	@Override
	public void onRemove(final MarkDownPriceRowModel markDownPriceRowModel, final InterceptorContext ctx) throws InterceptorException
	{
		markDownPriceProductModificationMarker.markProductAsModifiedIfFlagSet(markDownPriceRowModel, ctx, false);
	}

	protected MarkDownPriceProductModificationMarker getMarkDownPriceProductModificationMarker()
	{
		return markDownPriceProductModificationMarker;
	}
}
