/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.predicates;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;

import java.util.function.Predicate;


/**
 * @author vasanthramprakasam
 */
public class ClickAndCollectConsignmentPredicate implements Predicate<ConsignmentModel>
{
	@Override
	public boolean test(ConsignmentModel consignmentModel)
	{
		return consignmentModel.getDeliveryPointOfService() != null;
	}
}
