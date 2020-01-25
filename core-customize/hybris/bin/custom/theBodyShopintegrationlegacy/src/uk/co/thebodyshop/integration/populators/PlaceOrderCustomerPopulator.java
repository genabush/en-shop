/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Optional;

import org.springframework.util.Assert;

import uk.co.thebodyshop.integration.jaxb.order.Order;


/**
 * @author vasanthramprakasam
 */
public class PlaceOrderCustomerPopulator implements Populator<OrderModel, Order.Customer>
{

	@Override
	public void populate(OrderModel orderModel, Order.Customer customer) throws ConversionException
	{
		//TODO populate all other attributes
		Assert.notNull(orderModel.getUser(),"User cannot be null");
		final UserModel user = orderModel.getUser();
		Optional<CustomerModel> customerModel = Optional.ofNullable(user).filter(CustomerModel.class::isInstance).map(CustomerModel.class::cast);
		if(customerModel.isPresent())
		{
			customer.setCustomerID(customerModel.get().getCustomerID());
			customer.setEmailAddress(customerModel.get().getContactEmail());
		}
	}
}
