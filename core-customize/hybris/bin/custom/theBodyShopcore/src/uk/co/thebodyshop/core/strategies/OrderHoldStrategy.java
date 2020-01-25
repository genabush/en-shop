/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.strategies;

import de.hybris.platform.core.model.order.OrderModel;


/**
 * @author vasanthramprakasam
 */
public interface OrderHoldStrategy
{

	Integer getOrderHoldDuration(OrderModel orderModel);

	boolean shouldOrderBeHeld(OrderModel orderModel);

	void holdOrder(OrderModel orderModel);

	void releaseOrder(OrderModel orderModel);

	void triggerOrderReleasedEvent(OrderModel orderModel);
}
