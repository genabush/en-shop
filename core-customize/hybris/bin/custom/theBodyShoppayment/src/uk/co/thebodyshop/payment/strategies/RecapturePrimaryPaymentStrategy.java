/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.payment.strategies;

import de.hybris.platform.core.model.order.OrderModel;

/**
 * @author Marcin
 */
public interface RecapturePrimaryPaymentStrategy
{
	boolean capturePrimaryPayment(final OrderModel order);

}
