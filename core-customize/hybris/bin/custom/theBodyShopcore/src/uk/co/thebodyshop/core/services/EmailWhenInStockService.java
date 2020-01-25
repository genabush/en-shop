/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services;

import java.util.Collection;

import de.hybris.platform.cms2.model.site.CMSSiteModel;

import uk.co.thebodyshop.core.emailWhenInStock.ws.data.EmailWhenInStockData;
import uk.co.thebodyshop.core.emailWhenInStock.ws.data.EmailWhenInStockResultData;
import uk.co.thebodyshop.core.model.EmailWhenInStockModel;

/**
 * @author Kirshna
 */
public interface EmailWhenInStockService
{
	public EmailWhenInStockResultData findEmailWhenInStock(EmailWhenInStockData emailWhenInStockData);

	Collection<EmailWhenInStockModel> findEmailWhenInStockRecords(final String productCode, final CMSSiteModel baseSite);

}
