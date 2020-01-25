/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.markdownprice.marker.impl;

import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.util.Config;

import uk.co.thebodyshop.core.markdownprice.marker.MarkDownPriceProductModificationMarker;
import uk.co.thebodyshop.core.model.MarkDownPriceRowModel;

/**
 * @author Jagadeesh
 */
public class DefaultMarkDownPriceProductModificationMarker implements MarkDownPriceProductModificationMarker
{

	public static final String MARK_PRODUCT_MODIFIED = "markdownprice.product.modified";

	@Override
	public void markProductAsModifiedIfFlagSet(final MarkDownPriceRowModel markDownPriceModel, final InterceptorContext ctx)
	{
		markProductAsModifiedIfFlagSet(markDownPriceModel, ctx, false);
	}

	@Override
	public void markProductAsModifiedIfFlagSet(final MarkDownPriceRowModel markDownPriceModel, final InterceptorContext ctx, final boolean isRemove)
	{
		final boolean markProductModified = Config.getBoolean(MARK_PRODUCT_MODIFIED, false);
		if (markProductModified)
		{
			markProductModifiedIfNeeded(markDownPriceModel, ctx, isRemove);
		}
	}

	private static void markProductModifiedIfNeeded(final MarkDownPriceRowModel markDownPriceModel, final InterceptorContext ctx, final boolean isRemove)
	{
		final ProductModel product = markDownPriceModel.getProduct();
		if (product == null)
		{
			return;
		}

		final boolean markDownPriceModelChanged = ctx.isNew(markDownPriceModel) || ctx.isRemoved(markDownPriceModel) || isRemove || isPdtRowModelChanged(markDownPriceModel);

		if (markDownPriceModelChanged)
		{
			product.setModifiedtime(new Date());
			if (!ctx.contains(product, PersistenceOperation.SAVE))
			{
				ctx.registerElementFor(product, PersistenceOperation.SAVE);
			}
		}
	}

	private static boolean isPdtRowModelChanged(final MarkDownPriceRowModel markDownPriceModel)
	{
		return isAnyDirtyAttributeChanged(markDownPriceModel) || isAnyDirtyLocalizedAttributeChanged(markDownPriceModel);
	}

	private static boolean isAnyDirtyLocalizedAttributeChanged(final MarkDownPriceRowModel markDownPriceModel)
	{
		final ItemModelContext itemModelContext = markDownPriceModel.getItemModelContext();
		final Map<Locale, Set<String>> dirtyLocalizedAttributes = itemModelContext.getDirtyLocalizedAttributes();
		if (dirtyLocalizedAttributes != null)
		{
			for (final Map.Entry<Locale, Set<String>> localeSetEntry : dirtyLocalizedAttributes.entrySet())
			{
				final Locale locale = localeSetEntry.getKey();
				final Set<String> attributes = localeSetEntry.getValue();
				final boolean anyLocalizedAttributeChanged = attributes.stream().anyMatch(s -> {
					final Object originalValue = itemModelContext.getOriginalValue(s, locale);
					return !Objects.equals(originalValue, markDownPriceModel.getProperty(s, locale));
				});

				if (anyLocalizedAttributeChanged)
				{
					return true;
				}
			}
		}
		return false;
	}

	private static boolean isAnyDirtyAttributeChanged(final MarkDownPriceRowModel markDownPriceModel)
	{
		final ItemModelContext itemModelContext = markDownPriceModel.getItemModelContext();
		final Set<String> dirtyAttributes = itemModelContext.getDirtyAttributes();

		return dirtyAttributes != null && dirtyAttributes.stream().anyMatch(s -> {
			final Object originalValue = itemModelContext.getOriginalValue(s);
			return !Objects.equals(originalValue, markDownPriceModel.getProperty(s));
		});
	}
}
