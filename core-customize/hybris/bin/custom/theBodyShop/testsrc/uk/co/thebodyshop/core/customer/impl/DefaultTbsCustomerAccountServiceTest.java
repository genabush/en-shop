/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.customer.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.customer.dao.CustomerAccountDao;
import de.hybris.platform.commerceservices.enums.CountryType;
import de.hybris.platform.commerceservices.order.CommerceCheckoutService;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;

/**
 * @author Balakrishna
 **/
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultTbsCustomerAccountServiceTest
{
	@InjectMocks
	private DefaultTbsCustomerAccountService defaultTbsCustomerAccountService;

	@Mock
	private CommerceCheckoutService commerceCheckoutService;

	@Mock
	private CustomerAccountDao customerAccountDao;

	@Mock
	private CustomerModel customerModel;

	@Mock
	private CountryModel shippingCountryModel;
	@Mock
	private CountryModel deliveryCountryModel;

	private List<CountryModel> shippingCountryModelList;

	private List<CountryModel> deliveryCountryModelList;

	@Mock
	private AddressModel shippingAddressModel;

	@Mock
	private AddressModel deliveryAddressModel;

	private List<AddressModel> addressModelList;

	@Before
	public void setup()
	{
		addressModelList=new ArrayList<>();
		shippingCountryModelList=new ArrayList<>();
		deliveryCountryModelList=new ArrayList<>();

		Mockito.when(shippingCountryModel.getName()).thenReturn("UK");
		Mockito.when(deliveryCountryModel.getName()).thenReturn("US");

		Mockito.when(shippingAddressModel.getCountry()).thenReturn(shippingCountryModel);
		Mockito.when(deliveryAddressModel.getCountry()).thenReturn(deliveryCountryModel);
		shippingCountryModelList.add(shippingCountryModel);

	}

	@Test
	public void getAddressBookEntriesByBillingAddress()
	{
		deliveryCountryModelList.add(shippingCountryModel);
		deliveryCountryModelList.add(deliveryCountryModel);
		addressModelList.add(shippingAddressModel);
		addressModelList.add(deliveryAddressModel);
		Mockito.when(commerceCheckoutService.getCountries(CountryType.BILLING)).thenReturn(deliveryCountryModelList);
		Mockito.when(customerAccountDao.findAddressBookDeliveryEntriesForCustomer(customerModel,deliveryCountryModelList)).thenReturn(addressModelList);
		final List<AddressModel> result = defaultTbsCustomerAccountService.getAddressBookEntriesByAddressType(customerModel, CountryType.BILLING.getCode());
		Assert.assertTrue(result.size()==2);
	}

	@Test
	public void getAddressBookEntriesByShippingAddress()
	{
		deliveryCountryModelList.add(shippingCountryModel);
		addressModelList.add(shippingAddressModel);
		Mockito.when(commerceCheckoutService.getCountries(CountryType.SHIPPING)).thenReturn(shippingCountryModelList);
		Mockito.when(customerAccountDao.findAddressBookDeliveryEntriesForCustomer(customerModel, shippingCountryModelList)).thenReturn(addressModelList);
		final List<AddressModel> result = defaultTbsCustomerAccountService.getAddressBookEntriesByAddressType(customerModel, CountryType.SHIPPING.getCode());
		Assert.assertTrue(result.size() == 1);
	}

}
