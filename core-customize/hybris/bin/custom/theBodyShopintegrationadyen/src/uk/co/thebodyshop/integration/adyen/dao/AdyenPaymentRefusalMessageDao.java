/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.adyen.dao;

import java.util.List;

import uk.co.thebodyshop.integration.adyen.model.AdyenPaymentRefusalMessageModel;


/**
 * @author Krishna
 */
public interface AdyenPaymentRefusalMessageDao
{
	public List<AdyenPaymentRefusalMessageModel> getRefusalMessages();
}
