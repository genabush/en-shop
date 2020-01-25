/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.cms.types.predicates;

import java.util.function.Predicate;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.type.TypeService;

import uk.co.thebodyshop.cms.model.components.GiftWrapBannerCMSComponentModel;

/**
 * @author Jagadeesh
 */
public class GiftWrapBannerCMSComponentTypePredicate implements Predicate<ItemModel>
{
	private final TypeService typeService;

	@Override
	public boolean test(final ItemModel itemModel)
	{
		return getTypeService().isAssignableFrom(GiftWrapBannerCMSComponentModel._TYPECODE, itemModel.getItemtype());
	}

	public GiftWrapBannerCMSComponentTypePredicate(final TypeService typeService)
	{
		this.typeService = typeService;
	}

	protected TypeService getTypeService()
	{
		return typeService;
	}
}
