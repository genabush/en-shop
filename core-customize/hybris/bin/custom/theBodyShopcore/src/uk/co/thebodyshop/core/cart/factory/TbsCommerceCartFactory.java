/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.cart.factory;

import de.hybris.platform.commerceservices.order.impl.CommerceCartFactory;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.BaseStoreModel;

/**
 * @author Balakrishna
 */
public class TbsCommerceCartFactory extends CommerceCartFactory
{
	private static final String DEFAULT_CART_SOURCE = "tbs.order.source.code";
	private static final String DEFAULT_CART_ENV = "x";
	private static final String CART_ENV = "tbs.order.env";
	private static final String STORE_CODE = "tbs.store.%s.code";

	private final ModelService modelService;
	private final ConfigurationService configurationService;

	public TbsCommerceCartFactory(final ModelService modelService, final ConfigurationService configurationService)
	{
		this.modelService = modelService;
		this.configurationService = configurationService;
	}

	@Override
	public CartModel createCart()
	{
		final CartModel cart = createCartInternal();
		final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();

		if (currentBaseStore != null)
		{
			final StringBuilder tbsCartCode = new StringBuilder();
			tbsCartCode.append(getConfigurationService().getConfiguration().getString(String.format(STORE_CODE, currentBaseStore.getUid())));
			tbsCartCode.append(getConfigurationService().getConfiguration().getString(DEFAULT_CART_SOURCE));
			tbsCartCode.append(getConfigurationService().getConfiguration().getString(CART_ENV, DEFAULT_CART_ENV));
			tbsCartCode.append(cart.getCode());

			cart.setCode(tbsCartCode.toString());
			getModelService().save(cart);
		}

		return cart;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}
}
