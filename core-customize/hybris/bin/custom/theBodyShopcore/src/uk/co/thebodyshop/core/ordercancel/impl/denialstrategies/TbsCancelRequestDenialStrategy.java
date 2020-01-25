/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.ordercancel.impl.denialstrategies;

import org.springframework.beans.factory.annotation.Autowired;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.ordercancel.OrderCancelDenialReason;
import de.hybris.platform.ordercancel.OrderCancelDenialStrategy;
import de.hybris.platform.ordercancel.impl.denialstrategies.AbstractCancelDenialStrategy;
import de.hybris.platform.ordercancel.model.OrderCancelConfigModel;

import uk.co.thebodyshop.core.strategies.OrderHoldStrategy;

/**
 * @author Jagadeesh
 */
public class TbsCancelRequestDenialStrategy extends AbstractCancelDenialStrategy implements OrderCancelDenialStrategy
{

	private final OrderHoldStrategy orderHoldStrategy;

	@Autowired
	public TbsCancelRequestDenialStrategy(final OrderHoldStrategy orderHoldStrategy)
	{
		super();
		this.orderHoldStrategy = orderHoldStrategy;
	}

	@Override
	public OrderCancelDenialReason getCancelDenialReason(final OrderCancelConfigModel orderCancelConfigModel, final OrderModel orderModel, final PrincipalModel principalModel, final boolean isPartialCancel, final boolean isPartialEntryCancel)
	{
		if (orderHoldStrategy.shouldOrderBeHeld(orderModel) && !OrderStatus.CANCELLED.equals(orderModel.getStatus()))
		{
			return null;
		}
		return this.getReason();
	}

	protected OrderHoldStrategy getOrderHoldStrategy()
	{
		return orderHoldStrategy;
	}
}
