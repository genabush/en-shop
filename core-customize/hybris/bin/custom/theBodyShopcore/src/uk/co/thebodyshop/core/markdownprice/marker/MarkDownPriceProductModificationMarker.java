/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.markdownprice.marker;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;

import uk.co.thebodyshop.core.model.MarkDownPriceRowModel;

/**
 * @author Jagadeesh
 */
public interface MarkDownPriceProductModificationMarker
{
	public void markProductAsModifiedIfFlagSet(final MarkDownPriceRowModel markDownPriceModel, final InterceptorContext ctx);

	public void markProductAsModifiedIfFlagSet(final MarkDownPriceRowModel markDownPriceModel, final InterceptorContext ctx, final boolean isRemove);
}
