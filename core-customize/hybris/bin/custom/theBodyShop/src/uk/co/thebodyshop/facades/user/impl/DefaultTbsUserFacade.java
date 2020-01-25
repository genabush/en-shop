/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.facades.user.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.impl.DefaultUserFacade;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import org.apache.commons.collections.CollectionUtils;
import uk.co.thebodyshop.core.customer.TbsCustomerAccountService;
import uk.co.thebodyshop.facades.user.TbsUserFacade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author prateek.goel
 */
public class DefaultTbsUserFacade extends DefaultUserFacade implements TbsUserFacade
{
	private TbsCustomerAccountService tbsCustomerAccountService;

	@Override
	public AddressData getDefaultAddress()
	{
		final CustomerModel currentCustomer = (CustomerModel) getUserService().getCurrentUser();
		AddressData defaultAddressData = null;

		final AddressModel defaultAddress = getCustomerAccountService().getDefaultAddress(currentCustomer);
		if (defaultAddress != null)
		{
			defaultAddressData = getAddressConverter().convert(defaultAddress);
		}
		return defaultAddressData;
	}

	@Override
	public void addAddress(final AddressData addressData)
	{
		validateParameterNotNullStandardMessage("addressData", addressData);

		final CustomerModel currentCustomer = getCurrentUserForCheckout();

		// Create the new address model
		final AddressModel newAddress = getModelService().create(AddressModel.class);
		getAddressReversePopulator().populate(addressData, newAddress);

		// Store the address against the user
		getCustomerAccountService().saveAddressEntry(currentCustomer, newAddress);

		// Update the address ID in the newly created address
		addressData.setId(newAddress.getPk().toString());

		if (addressData.isDefaultAddress())
		{
			getCustomerAccountService().setDefaultAddressEntry(currentCustomer, newAddress);
		}
	}

	@Override
	public List<AddressData> getAddressBookByAddressType(String type) {
		// Get the current customer's addresses
		final CustomerModel currentUser = (CustomerModel) getUserService().getCurrentUser();
		final Collection<AddressModel> addresses = getTbsCustomerAccountService().getAddressBookEntriesByAddressType(currentUser,type);

		if (CollectionUtils.isNotEmpty(addresses))
		{
			final List<AddressData> result = new ArrayList<AddressData>();
			final AddressData defaultAddress = getDefaultAddress();

			for (final AddressModel address : addresses)
			{
				final AddressData addressData = getAddressConverter().convert(address);

				if (defaultAddress != null && defaultAddress.getId() != null && defaultAddress.getId().equals(addressData.getId()))
				{
					addressData.setDefaultAddress(true);
					result.add(0, addressData);
				}
				else
				{
					result.add(addressData);
				}
			}
			return result;
		}
		return Collections.emptyList();
	}

	public DefaultTbsUserFacade(TbsCustomerAccountService tbsCustomerAccountService) {
		this.tbsCustomerAccountService = tbsCustomerAccountService;
	}

	protected TbsCustomerAccountService getTbsCustomerAccountService() {
		return tbsCustomerAccountService;
	}
}
