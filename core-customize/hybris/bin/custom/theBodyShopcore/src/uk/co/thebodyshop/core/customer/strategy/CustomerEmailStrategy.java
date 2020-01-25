/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.customer.strategy;

import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.user.CustomerModel;

/**
 * @author vasanthramprakasam
 */
public interface CustomerEmailStrategy
{
	 String getEmail(String emailId, CMSSiteModel market);

	 String getContactEmail(CustomerModel customerModel);
}
