/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.daos;

import de.hybris.platform.core.model.order.OrderModel;

/**
 * @author vasanthramprakasam
 */
public interface TbsOrderDao
{
	OrderModel findOrderByCode(String code);
}
