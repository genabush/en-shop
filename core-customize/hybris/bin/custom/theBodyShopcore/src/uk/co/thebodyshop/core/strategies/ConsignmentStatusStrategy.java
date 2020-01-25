/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.strategies;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;

import uk.co.thebodyshop.core.enums.ConsignmentEntryStatus;

/**
 * @author vasanthramprakasam
 */
public interface ConsignmentStatusStrategy
{
	ConsignmentStatus calculateConsignmentStatus(ConsignmentModel consignmentModel);

	ConsignmentEntryStatus calculateConsignmentEntryStatus(ConsignmentEntryModel consignmentEntryModel);
}
