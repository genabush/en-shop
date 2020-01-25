/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package uk.co.thebodyshop.cms.types.predicates;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author Marcin
 */
public class NavigationComponentTypePredicate implements Predicate<ItemModel>
{
	private static final String NAVIGATION_COMPONENT = "NavigationComponent";

	private final TypeService typeService;

	public boolean test(final ItemModel itemModel)
	{
		return this.getTypeService().isAssignableFrom(NAVIGATION_COMPONENT, itemModel.getItemtype());
	}

	protected TypeService getTypeService()
	{
		return this.typeService;
	}

	@Autowired
	public NavigationComponentTypePredicate(final TypeService typeService)
	{
		this.typeService = typeService;
	}
}
