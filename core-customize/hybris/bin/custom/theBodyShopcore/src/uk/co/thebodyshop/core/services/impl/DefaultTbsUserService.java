/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services.impl;

import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.impl.DefaultUserService;
import uk.co.thebodyshop.core.daos.TbsUserDao;
import uk.co.thebodyshop.core.services.TbsUserService;

public class DefaultTbsUserService extends DefaultUserService implements TbsUserService
{
    private final TbsUserDao tbsUserDao;

    @Override
    public CustomerModel getUserByUidAndSite(String uid)
    {
        return getTbsUserDao().findUserByUidAndSite(uid);
    }

    @Override
    public CustomerModel getCustomerByCustomerIdAndSite(String customerID, CMSSiteModel cmsSiteModel)
    {
        return getTbsUserDao().findCustomerByCustomerIdAndSite(customerID,cmsSiteModel);
    }

    @Override
    public CustomerModel getCustomerByNationalID(String nationalID, CMSSiteModel cmsSiteModel)
    {
        return getTbsUserDao().findCustomerByNationalID(nationalID,cmsSiteModel);
    }

    @Override
    public CustomerModel getCustomerByLoyaltyCardNumber(String loyaltyCardNumber)
    {
        return getTbsUserDao().findCustomerByLoyaltyCardNumber(loyaltyCardNumber);
    }

    @Override
    public UserModel getCustomerByCustomerId(String customerId)
    {
        return getTbsUserDao().findCustomerByCustomerId(customerId);
    }


    public DefaultTbsUserService(TbsUserDao tbsUserDao)
    {
        this.tbsUserDao = tbsUserDao;
    }

    protected TbsUserDao getTbsUserDao()
    {
        return tbsUserDao;
    }
}
