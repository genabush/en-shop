/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.populators;

import java.util.Map;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import uk.co.thebodyshop.integration.jaxb.order.AddressType;
import uk.co.thebodyshop.integration.jaxb.order.Order;
import uk.co.thebodyshop.integration.jaxb.order.Order.Customer;
import uk.co.thebodyshop.integration.jaxb.order.Order.Entries;

/**
 * @author Marcin
 */
public class OrderSendPaymentPopulator extends PlaceOrderPopulator
{

	public OrderSendPaymentPopulator(final Map<String, String> internalExternalChannelTypeMapping, final Converter<OrderModel, Customer> placeOrderCustomerConverter, final Converter<AddressModel, AddressType> placeOrderAddressConverter,
			final Converter<OrderModel, Entries> placeOrderEntryConverter)
	{
		super(internalExternalChannelTypeMapping, placeOrderCustomerConverter, placeOrderAddressConverter,placeOrderEntryConverter);
	}

	@Override
	protected void populateOrderTotal(final OrderModel orderModel, final Order order)
	{
		final Double value = orderModel.getDeliveredOrderTotal();
		if (orderModel.getNet())
		{
			order.setTotalNet(value);
		}
		else
		{
			order.setTotalGross(value);
		}
	}
}
