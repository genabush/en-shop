/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.paypal.client.factory;

import org.springframework.util.Assert;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;

import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import uk.co.thebodyshop.integration.paypal.model.PaypalClientConfigurationModel;

/**
 * @author vasanthramprakasam
 */
public class DefaultPaypalClientFactory implements PaypalClientFactory
{
	 private final BaseStoreService baseStoreService;

	 public DefaultPaypalClientFactory(BaseStoreService baseStoreService)
	 {
			this.baseStoreService = baseStoreService;
	 }

	 @Override
	 public PayPalHttpClient createClient()
	 {
			final BaseStoreModel baseStoreModel = getBaseStoreService().getCurrentBaseStore();
			return createClientFromBaseStore(baseStoreModel);
	 }

	 @Override
	 public PayPalHttpClient createClientFromBaseStore(BaseStoreModel baseStoreModel)
	 {
			Assert.notNull(baseStoreModel,"Base store cannot be null");
			final PaypalClientConfigurationModel paypalClientConfiguration = baseStoreModel.getPaypalClientConfiguration();
			Assert.notNull(paypalClientConfiguration,"Paypal config cannot be null");
			final String clientId = paypalClientConfiguration.getClientId();
			final String clientSecret = paypalClientConfiguration.getClientSecret();
			final boolean sandBox = paypalClientConfiguration.getSandBox();
			Assert.notNull(clientId,"Client Id cannot be null");
			Assert.notNull(clientSecret,"Client secret cannot be null");
			Assert.notNull(sandBox,"sand box cannot be null");
			if(sandBox)
			{
				 return new PayPalHttpClient(new PayPalEnvironment.Sandbox(clientId,clientSecret));
			}
			else
			{
				 return new PayPalHttpClient(new PayPalEnvironment.Live(clientId,clientSecret));
			}
	 }

	 protected BaseStoreService getBaseStoreService()
	 {
			return baseStoreService;
	 }
}
