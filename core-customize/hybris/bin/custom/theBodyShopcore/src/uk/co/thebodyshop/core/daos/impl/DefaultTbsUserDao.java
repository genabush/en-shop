/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.daos.impl;

import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.user.daos.impl.DefaultUserDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import uk.co.thebodyshop.core.daos.TbsUserDao;
import uk.co.thebodyshop.loyalty.model.LoyaltyCardModel;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultTbsUserDao extends DefaultUserDao implements TbsUserDao
{
    private static final String USER_BY_UID_AND_SITE_SQL = "SELECT {pk} from {Customer} where {baseSite} = ?session.currentSite and {uid} = ?uid";

    private static final String CUSTOMER_BY_CUSTOMER_ID_SQL = "SELECT {pk} from {Customer} where {customerID} = ?customerId";

    private static final String CUSTOMER_BY_CUSTOMERID_AND_SITE_SQL = "SELECT {pk} from {Customer} where {baseSite} = ?cmsSiteModel and {customerID} = ?customerId";

    private static final String CUSTOMER_BY_NATIONALID_SQL = "SELECT {pk} from {Customer} where {baseSite} = ?cmsSiteModel and {nationalID} = ?nationalID";

    private static final String CUSTOMER_BY_LYBCARD_SQL = "SELECT {pk} from {LoyaltyCard} where {cardNumber} = ?loyaltyCardNumber";

    @Resource(name = "flexibleSearchService")
    private FlexibleSearchService flexibleSearchService;

    @Override
    public CustomerModel findUserByUidAndSite(String uid)
    {
        final Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        final SearchResult<CustomerModel> result = flexibleSearchService.search(USER_BY_UID_AND_SITE_SQL, params);
        final List<CustomerModel> customers = result.getResult();
        if (customers.size() > 1)
        {
            throw new AmbiguousIdentifierException("Found " + customers.size() + " customer with the unique uid '" + uid + "'");
        }
        return customers.isEmpty() ? null : customers.get(0);
    }

    @Override
    public CustomerModel findCustomerByCustomerIdAndSite(String customerID, CMSSiteModel cmsSiteModel)
    {
        final Map<String, Object> params = new HashMap<>();
        params.put("cmsSiteModel", cmsSiteModel);
        params.put("customerId", customerID);
        final SearchResult<CustomerModel> result = flexibleSearchService.search(CUSTOMER_BY_CUSTOMERID_AND_SITE_SQL, params);
        final List<CustomerModel> customers = result.getResult();
        if (customers.size() > 1)
        {
            throw new AmbiguousIdentifierException("Found " + customers.size() + " customer with the customerId '" + customerID + "'");
        }
        return customers.isEmpty() ? null : customers.get(0);
    }

    @Override
    public CustomerModel findCustomerByNationalID(String nationalID, CMSSiteModel cmsSiteModel)
    {
        final Map<String, Object> params = new HashMap<>();
        params.put("cmsSiteModel", cmsSiteModel);
        params.put("nationalID", nationalID);
        final SearchResult<CustomerModel> result = flexibleSearchService.search(CUSTOMER_BY_NATIONALID_SQL, params);
        final List<CustomerModel> customers = result.getResult();
        if (customers.size() > 1)
        {
            throw new AmbiguousIdentifierException("Found " + customers.size() + " customer with the nationalID '" + nationalID + "'");
        }
        return customers.isEmpty() ? null : customers.get(0);
    }

    @Override
    public CustomerModel findCustomerByLoyaltyCardNumber(String loyaltyCardNumber)
    {
        final Map<String, Object> params = new HashMap<>();
        params.put("loyaltyCardNumber", loyaltyCardNumber);
        final SearchResult<LoyaltyCardModel> result = flexibleSearchService.search(CUSTOMER_BY_LYBCARD_SQL, params);

        final List<LoyaltyCardModel> loyaltyCardModels = result.getResult();
        if (loyaltyCardModels.size() > 1)
        {
            throw new AmbiguousIdentifierException("Found " + loyaltyCardModels.size() + " customer with the loyaltyCardNumber '" + loyaltyCardNumber + "'");
        }
        return loyaltyCardModels.isEmpty() ? null : loyaltyCardModels.get(0).getCustomer();
    }

    @Override
    public UserModel findCustomerByCustomerId(String customerId)
    {
        if (StringUtils.isBlank(customerId))
        {
            throw new IllegalArgumentException("Provided customer id is blank");
        }

        final Map<String, Object> params = new HashMap<>();
        params.put("customerId", customerId);
        final SearchResult<CustomerModel> result = flexibleSearchService.search(CUSTOMER_BY_CUSTOMER_ID_SQL, params);
        final List<CustomerModel> customerModels = result.getResult();

        if (CollectionUtils.isEmpty(customerModels))
        {
            throw new UnknownIdentifierException("No customer with customerId [" + customerId + "] found");
        }

        if (customerModels.size() > 1)
        {
            throw new AmbiguousIdentifierException("Found " + customerModels.size() + " customer with the customerId '" + customerId + "'");
        }

        return customerModels.isEmpty() ? null : customerModels.get(0);
    }

    public DefaultTbsUserDao(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }

    protected FlexibleSearchService getFlexibleSearchService()
    {
        return flexibleSearchService;
    }
}

