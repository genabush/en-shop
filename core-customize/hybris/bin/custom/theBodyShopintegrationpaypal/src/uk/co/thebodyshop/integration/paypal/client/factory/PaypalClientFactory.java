/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.paypal.client.factory;

import com.paypal.core.PayPalHttpClient;

import de.hybris.platform.store.BaseStoreModel;

/**
 * @author vasanthramprakasam
 */
public interface PaypalClientFactory
{
	 PayPalHttpClient createClient();

	 PayPalHttpClient createClientFromBaseStore(BaseStoreModel baseStoreModel);
}
