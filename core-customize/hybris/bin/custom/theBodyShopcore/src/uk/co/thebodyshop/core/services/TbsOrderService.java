/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.services;

import de.hybris.platform.core.model.order.OrderModel;


/**
 * @author vasanthramprakasam
 */
public interface TbsOrderService
{
	OrderModel getOrderForCode(String code);
}
