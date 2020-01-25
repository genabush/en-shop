/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.strategies.impl;

import java.util.Objects;
import java.util.function.Predicate;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;

import uk.co.thebodyshop.core.enums.ConsignmentEntryStatus;
import uk.co.thebodyshop.core.strategies.ConsignmentStatusStrategy;

/**
 * @author vasanthramprakasam
 */
public class DefaultConsignmentStatusStrategy implements ConsignmentStatusStrategy
{

	private final Predicate<ConsignmentModel> clickAndCollectConsignmentPredicate;

	public DefaultConsignmentStatusStrategy(final Predicate<ConsignmentModel> clickAndCollectConsignmentPredicate)
	{
		this.clickAndCollectConsignmentPredicate = clickAndCollectConsignmentPredicate;
	}

	@Override
	public ConsignmentStatus calculateConsignmentStatus(final ConsignmentModel consignmentModel)
	{
		final boolean clickAndCollect = getClickAndCollectConsignmentPredicate().test(consignmentModel);

		final boolean allShipped = consignmentModel.getConsignmentEntries().stream()
				.allMatch(entry -> Objects.nonNull(entry.getOrderEntry()) && (entry.getStatus().equals(ConsignmentEntryStatus.SHIPPED) || entry.getStatus().equals(ConsignmentEntryStatus.PICKUP_COMPLETE)));
		if (allShipped)
		{
			return clickAndCollect ? ConsignmentStatus.PICKUP_COMPLETE : ConsignmentStatus.SHIPPED;
		}

		final boolean noneShipped = consignmentModel.getConsignmentEntries().stream()
				.allMatch(entry -> Objects.nonNull(entry.getOrderEntry()) && (entry.getStatus().equals(ConsignmentEntryStatus.NOT_SHIPPED) || entry.getStatus().equals(ConsignmentEntryStatus.NOT_PICKED)));
		if (noneShipped)
		{
			return clickAndCollect ? ConsignmentStatus.NOT_PICKED : ConsignmentStatus.NOT_SHIPPED;
		}

		final boolean partShipped = consignmentModel.getConsignmentEntries().stream()
				.anyMatch(entry -> Objects.nonNull(entry.getOrderEntry()) && (entry.getStatus().equals(ConsignmentEntryStatus.PART_SHIPPED) || entry.getStatus().equals(ConsignmentEntryStatus.SHIPPED)
						|| entry.getStatus().equals(ConsignmentEntryStatus.PART_PICKED) || entry.getStatus().equals(ConsignmentEntryStatus.PICKUP_COMPLETE)));
		if (partShipped)
		{
			return clickAndCollect ? ConsignmentStatus.PART_PICKED : ConsignmentStatus.PART_SHIPPED;
		}

		return consignmentModel.getStatus();
	}

	@Override
	public ConsignmentEntryStatus calculateConsignmentEntryStatus(final ConsignmentEntryModel consignmentEntryModel)
	{
		final boolean clickAndCollect = getClickAndCollectConsignmentPredicate().test(consignmentEntryModel.getConsignment());

		final Long quantity = consignmentEntryModel.getQuantity();
		final Long shippedQuantity = consignmentEntryModel.getShippedQuantity();

		if (quantity == shippedQuantity)
		{
			return clickAndCollect ? ConsignmentEntryStatus.PICKUP_COMPLETE : ConsignmentEntryStatus.SHIPPED;
		}

		if (shippedQuantity > 0 && shippedQuantity < quantity)
		{
			return clickAndCollect ? ConsignmentEntryStatus.PART_PICKED : ConsignmentEntryStatus.PART_SHIPPED;
		}

		if (quantity > 0 && shippedQuantity == 0)
		{
			return clickAndCollect ? ConsignmentEntryStatus.NOT_PICKED : ConsignmentEntryStatus.NOT_SHIPPED;
		}

		return ConsignmentEntryStatus.READY;
	}

	protected Predicate<ConsignmentModel> getClickAndCollectConsignmentPredicate()
	{
		return clickAndCollectConsignmentPredicate;
	}
}
