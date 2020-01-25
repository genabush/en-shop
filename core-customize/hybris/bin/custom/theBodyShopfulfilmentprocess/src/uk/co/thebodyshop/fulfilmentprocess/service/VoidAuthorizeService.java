/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.fulfilmentprocess.service;

import de.hybris.platform.core.model.order.OrderModel;

/**
 * @author Balakrishna
 **/
public interface VoidAuthorizeService
{
    boolean voidAuthorize(OrderModel orderModel);
}
