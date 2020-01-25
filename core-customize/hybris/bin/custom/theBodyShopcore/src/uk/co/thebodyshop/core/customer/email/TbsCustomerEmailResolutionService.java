/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.customer.email;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;

import de.hybris.platform.commerceservices.customer.impl.DefaultCustomerEmailResolutionService;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.util.mail.MailUtils;

import uk.co.thebodyshop.core.customer.strategy.CustomerEmailStrategy;

/**
 * @author vasanthramprakasam
 */
public class TbsCustomerEmailResolutionService extends DefaultCustomerEmailResolutionService
{

	 private static final Logger LOG = Logger.getLogger(TbsCustomerEmailResolutionService.class);

	 private CustomerEmailStrategy customerEmailStrategy;

	 @Override
	 protected String validateAndProcessEmailForCustomer(CustomerModel customerModel)
	 {
			validateParameterNotNullStandardMessage("customerModel", customerModel);

			final String email = CustomerType.GUEST.equals(customerModel.getType()) ? StringUtils.substringAfter(
					customerModel.getUid(), "|") : getCustomerEmailStrategy().getContactEmail(customerModel);
			try
			{
				 MailUtils.validateEmailAddress(email, "customer email");
				 return email;
			}
			catch (final EmailException e)
			{
				 LOG.info("Given uid is not appropriate email. Customer PK: " + String.valueOf(customerModel.getPk()) + " Exception: "
						 + e.getClass().getName());
			}
			return null;
	 }

	 public CustomerEmailStrategy getCustomerEmailStrategy()
	 {
			return customerEmailStrategy;
	 }

	 public void setCustomerEmailStrategy(CustomerEmailStrategy customerEmailStrategy)
	 {
			this.customerEmailStrategy = customerEmailStrategy;
	 }
}
