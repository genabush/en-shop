/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.customer.impl;

import de.hybris.platform.commerceservices.customer.impl.DefaultCustomerAccountService;
import de.hybris.platform.commerceservices.enums.CountryType;
import de.hybris.platform.commerceservices.order.CommerceCheckoutService;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import org.apache.commons.lang3.StringUtils;
import uk.co.thebodyshop.core.customer.TbsCustomerAccountService;

import java.util.List;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

/**
 * @author Balakrishna
 **/
public class DefaultTbsCustomerAccountService extends DefaultCustomerAccountService implements TbsCustomerAccountService
{
    private CommerceCheckoutService commerceCheckoutService;

    @Override
    public List<AddressModel> getAddressBookEntriesByAddressType(CustomerModel customerModel, String type)
    {
        validateParameterNotNull(customerModel, "Customer model cannot be null");
        validateParameterNotNull(type, "Address Type cannot be null");
        return getCustomerAccountDao().findAddressBookDeliveryEntriesForCustomer(customerModel,
                getCommerceCheckoutService().getCountries(StringUtils.isNotBlank(type) ? CountryType.valueOf(type) : null));
    }

    public void setCommerceCheckoutService(CommerceCheckoutService commerceCheckoutService) {
        this.commerceCheckoutService = commerceCheckoutService;
    }

    protected CommerceCheckoutService getCommerceCheckoutService()
    {
        return commerceCheckoutService;
    }
}
