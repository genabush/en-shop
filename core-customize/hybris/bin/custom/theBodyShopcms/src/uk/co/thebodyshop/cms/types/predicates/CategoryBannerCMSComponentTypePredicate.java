/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.cms.types.predicates;

import java.util.function.Predicate;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.type.TypeService;

import uk.co.thebodyshop.cms.model.components.CategoryBannerCMSComponentModel;


/**
 * @author Krishna
 */
public class CategoryBannerCMSComponentTypePredicate implements Predicate<ItemModel>
{
	private TypeService typeService;

	@Override
	public boolean test(final ItemModel itemModel)
	{
		return getTypeService().isAssignableFrom(CategoryBannerCMSComponentModel._TYPECODE, itemModel.getItemtype());
	}

	public CategoryBannerCMSComponentTypePredicate(TypeService typeService)
	{
		this.typeService = typeService;
	}
	
	protected TypeService getTypeService()
	{
		return typeService;
	}
	
	

}
