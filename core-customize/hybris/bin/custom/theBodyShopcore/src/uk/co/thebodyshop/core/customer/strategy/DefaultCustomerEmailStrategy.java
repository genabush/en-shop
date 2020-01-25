/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.customer.strategy;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.user.CustomerModel;

/**
 * @author vasanthramprakasam
 */
public class DefaultCustomerEmailStrategy implements CustomerEmailStrategy
{
	 private static final String SEPARATOR = "|";

	 @Override
	 public String getEmail(String emailId, CMSSiteModel market)
	 {
			Assert.notNull(emailId,"Email cannot be null");
			Assert.notNull(market,"Market cannot be null");
			return emailId.concat(SEPARATOR).concat(market.getUid());
	 }

	 @Override
	 public String getContactEmail(CustomerModel customerModel)
	 {
			return StringUtils.substringBefore(customerModel.getUid(),SEPARATOR);
	 }
}
