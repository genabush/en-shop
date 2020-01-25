/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.daos;

import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.daos.UserDao;

public interface TbsUserDao extends UserDao
{
     CustomerModel findUserByUidAndSite(String uid);

     CustomerModel findCustomerByCustomerIdAndSite(final String customerID, final CMSSiteModel cmsSiteModel);

     CustomerModel findCustomerByNationalID(final String nationalID, final CMSSiteModel cmsSiteModel);

     CustomerModel findCustomerByLoyaltyCardNumber(final String loyaltyCardNumber);

     UserModel findCustomerByCustomerId(String customerId);
}
