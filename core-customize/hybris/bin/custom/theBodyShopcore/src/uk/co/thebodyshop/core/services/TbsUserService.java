/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services;

import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;

public interface TbsUserService extends UserService
{
     CustomerModel getUserByUidAndSite(String uid);

     CustomerModel getCustomerByCustomerIdAndSite(final String customerID, final CMSSiteModel cmsSiteModel);

     CustomerModel getCustomerByNationalID(final String nationalID, final CMSSiteModel cmsSiteModel);

     CustomerModel getCustomerByLoyaltyCardNumber(final String loyaltyCardNumber);

     UserModel getCustomerByCustomerId(String customerId);
}
