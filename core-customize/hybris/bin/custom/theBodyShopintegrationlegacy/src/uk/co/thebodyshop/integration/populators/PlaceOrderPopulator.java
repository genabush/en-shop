/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.populators;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import uk.co.thebodyshop.integration.jaxb.order.AddressType;
import uk.co.thebodyshop.integration.jaxb.order.ObjectFactory;
import uk.co.thebodyshop.integration.jaxb.order.Order;
import uk.co.thebodyshop.integration.jaxb.order.Order.Entries;
import uk.co.thebodyshop.integration.jaxb.order.ShippingAddressType;

/**
 * @author vasanthramprakasam
 */
public class PlaceOrderPopulator implements Populator<OrderModel, Order>
{
	private final Map<String, String> internalExternalChannelTypeMapping;

	private final Converter<OrderModel, Order.Customer> placeOrderCustomerConverter;

	private final Converter<AddressModel, AddressType> placeOrderAddressConverter;

	private final Converter<OrderModel, Entries> placeOrderEntryConverter;

	@Autowired
	public PlaceOrderPopulator(final Map<String, String> internalExternalChannelTypeMapping,
			final Converter<OrderModel, Order.Customer> placeOrderCustomerConverter,
			final Converter<AddressModel, AddressType> placeOrderAddressConverter, final Converter<OrderModel, Entries> placeOrderEntryConverter)
	{
		this.internalExternalChannelTypeMapping = internalExternalChannelTypeMapping;
		this.placeOrderCustomerConverter = placeOrderCustomerConverter;
		this.placeOrderAddressConverter = placeOrderAddressConverter;
		this.placeOrderEntryConverter = placeOrderEntryConverter;
	}

	@Override
	public void populate(final OrderModel source, final Order target) throws ConversionException
	{
		//TODO populate all missing attributes
		Assert.notNull(target, "target cannot be null");
		target.setCode(source.getCode());
		target.setStatus(source.getStatus().getCode());
		target.setCurrency(source.getCurrency().getIsocode());
		target.setEntries(getPlaceOrderEntryConverter().convert(source));
		final SalesApplication salesApplication = source.getSalesApplication();
		if (salesApplication != null)
		{
			if (getInternalExternalChannelTypeMapping().containsKey(salesApplication.getCode()))
			{
				target.setChannel(getInternalExternalChannelTypeMapping().get(salesApplication.getCode()));
			}
			else
			{
				target.setChannel(salesApplication.getCode());
			}
		}
		populateOrderTotal(source, target);
		target.setCustomer(getPlaceOrderCustomerConverter().convert(source));
		final AddressModel paymentAddress = source.getPaymentAddress();
		if(paymentAddress != null)
		{
			target.setBillingAddress(getPlaceOrderAddressConverter().convert(paymentAddress));
		}

		final AddressModel deliveryAddress = source.getDeliveryAddress();
		if(deliveryAddress != null)
		{
			final ShippingAddressType shippingAddressType = new ObjectFactory().createShippingAddressType();
			getPlaceOrderAddressConverter().convert(deliveryAddress,shippingAddressType);
			target.setShippingAddress(shippingAddressType);
		}
	}

	protected void populateOrderTotal(final OrderModel orderModel, final Order order)
	{
		final Double value = orderModel.getTotalPrice();

		if (orderModel.getNet())
		{
			order.setTotalNet(value);
		}
		else
		{
			order.setTotalGross(value);
		}
	}

	protected Map<String, String> getInternalExternalChannelTypeMapping()
	{
		return internalExternalChannelTypeMapping;
	}

	protected Converter<OrderModel, Order.Customer> getPlaceOrderCustomerConverter()
	{
		return placeOrderCustomerConverter;
	}

	protected Converter<AddressModel, AddressType> getPlaceOrderAddressConverter()
	{
		return placeOrderAddressConverter;
	}

	protected Converter<OrderModel, Entries> getPlaceOrderEntryConverter()
	{
		return placeOrderEntryConverter;
	}

}
