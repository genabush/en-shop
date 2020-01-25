/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.gateways;

import javax.xml.bind.JAXBElement;

import com.mercury.ws.v1.placeorder.PlaceOrder;
import com.mercury.ws.v1.placeorder.PlaceOrderResponse;


/**
 * @author vasanthramprakasam
 */
public interface PlaceOrderGateway
{
	JAXBElement<PlaceOrderResponse> placeOrder(JAXBElement<PlaceOrder> placeOrder);
}
