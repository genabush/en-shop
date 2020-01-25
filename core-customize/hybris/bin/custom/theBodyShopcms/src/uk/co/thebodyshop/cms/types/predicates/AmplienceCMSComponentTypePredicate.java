/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.cms.types.predicates;

import java.util.function.Predicate;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.type.TypeService;

import uk.co.thebodyshop.cms.model.components.AmplienceCMSComponentModel;

/**
 * @author Marcin
 */
public class AmplienceCMSComponentTypePredicate implements Predicate<ItemModel>
{
	private final TypeService typeService;

	public boolean test(final ItemModel itemModel)
	{
		return getTypeService().isAssignableFrom(AmplienceCMSComponentModel._TYPECODE, itemModel.getItemtype());
	}

	public AmplienceCMSComponentTypePredicate(final TypeService typeService)
	{
		this.typeService = typeService;
	}

	protected TypeService getTypeService()
	{
		return this.typeService;
	}
}
