/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.daos;

import java.util.Collection;

import de.hybris.platform.cms2.model.site.CMSSiteModel;

import uk.co.thebodyshop.core.model.EmailWhenInStockModel;

/**
 * @author Krishna
 */
public interface EmailWhenInStockDao
{
	public EmailWhenInStockModel getEmailWhenInStock(String email, String productCode, CMSSiteModel site);

	public Collection<EmailWhenInStockModel> getEmailWhenInStockRecordsByProductAndSite(final String productCode, final CMSSiteModel site);
}
