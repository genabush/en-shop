/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.customer;

import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.List;

/**
 * @author Balakrishna
 **/
public interface TbsCustomerAccountService extends CustomerAccountService
{
    /**
     * it will return list of addressModels by its type
     * @param customerModel
     * @param type
     * @return List of Address Models
     */
     List<AddressModel> getAddressBookEntriesByAddressType(final CustomerModel customerModel,final String type);
}
