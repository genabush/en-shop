/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.payment.facade.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.adyen.model.checkout.PaymentMethod;
import com.adyen.service.exception.ApiException;
import com.adyen.v6.factory.AdyenPaymentServiceFactory;
import com.adyen.v6.service.AdyenPaymentService;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.PaymentModeModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.webservicescommons.mapping.DataMapper;

import uk.co.thebodyshop.payment.data.PaymentMethodWSDTO;
import uk.co.thebodyshop.payment.data.PaymentMethodsResponse;
import uk.co.thebodyshop.payment.facade.TbsPaymentFacade;


/**
 * @author mahesh
 */
public class DefaultTbsPaymentFacade implements TbsPaymentFacade
{

	private AdyenPaymentServiceFactory adyenPaymentServiceFactory;
	private BaseStoreService baseStoreService;
	private CartService cartService;
	private ConfigurationService configurationService;
	@Resource(name = "dataMapper")
	private DataMapper dataMapper;
	public static final Logger LOG = Logger.getLogger(DefaultTbsPaymentFacade.class);

	private static final String CREDIT_CARD_MODE = "creditcard";
	private static final String ADYEN_ORIGIN_KEY = "theBodyShop.adyen.originkey";

	@Override
	public PaymentMethodsResponse getSupportedPaymentMethods()
	{
		final PaymentMethodsResponse response = new PaymentMethodsResponse();
		if (getCartService().hasSessionCart())
		{
			final CartModel cart = getCartService().getSessionCart();
			final BaseStoreModel baseStore = getBaseStoreService().getCurrentBaseStore();
			final String currencyIso = baseStore.getDefaultCurrency() != null ? baseStore.getDefaultCurrency().getIsocode() : "GBP";
			final String countryCode = baseStore.getDeliveryCountries().iterator().next().getIsocode();
			final String adyenOriginKey = baseStore.getAdyenOriginKey();
			final Collection<PaymentModeModel> baseStorePaymentModes = baseStore.getSupportedPaymentModes();
			try
			{
				final List<PaymentMethod> adyenResponse = getAdyenPaymentService().getPaymentMethods(
					BigDecimal.valueOf(10.0),
						currencyIso, countryCode,
						StringUtils.EMPTY, StringUtils.EMPTY);

				final List<String> supportedAdyenPaymentTypes = getSupportedAdyenPaymentTypes(baseStorePaymentModes);

				final List<PaymentMethodWSDTO> paymentMethods = adyenResponse.stream()
						.filter(resp -> supportedAdyenPaymentTypes.contains(resp.getType()))
					.map(resp -> dataMapper.map(resp, PaymentMethodWSDTO.class)).collect(Collectors.toList());
				//adyenResponse.stream().map(i -> convert(i)).collect(Collectors.toList());
			response.setPaymentMethods(paymentMethods);

			}
			catch (IOException | ApiException e)
			{
				LOG.error(ExceptionUtils.getStackTrace(e));
			}

			if (CollectionUtils.isNotEmpty(baseStorePaymentModes))
			{
				baseStorePaymentModes.stream().forEach(mode -> {
					if (CREDIT_CARD_MODE.equalsIgnoreCase(mode.getCode()))
					{
						response.setCreditCardPayment(true);
					}

				});
			}

			if (StringUtils.isNotEmpty(adyenOriginKey))
			{
				response.setAdyenOriginKey(adyenOriginKey);
			}
			else
			{
				response.setAdyenOriginKey(getConfigurationService().getConfiguration().getString(ADYEN_ORIGIN_KEY));
			}
		}


		return response;
	}

	private List<String> getSupportedAdyenPaymentTypes(final Collection<PaymentModeModel> paymentModes)
	{
		final List<String> supportedAdyenPaymentTypes = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(paymentModes))
		{
			paymentModes.stream().filter(paymentMode -> StringUtils.isNotEmpty(paymentMode.getAdyenPaymentType()))
				.forEach(paymentMode -> supportedAdyenPaymentTypes.add(paymentMode.getAdyenPaymentType()));
		}
		return supportedAdyenPaymentTypes;
	}


	private AdyenPaymentService getAdyenPaymentService()
	{
		return adyenPaymentServiceFactory.createFromBaseStore(baseStoreService.getCurrentBaseStore());
	}

	/**
	 * @return the cartService
	 */
	public CartService getCartService()
	{
		return cartService;
	}

	/**
	 * @param cartService
	 *           the cartService to set
	 */
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	/**
	 * @return the adyenPaymentServiceFactory
	 */
	public AdyenPaymentServiceFactory getAdyenPaymentServiceFactory()
	{
		return adyenPaymentServiceFactory;
	}

	/**
	 * @param adyenPaymentServiceFactory
	 *           the adyenPaymentServiceFactory to set
	 */
	public void setAdyenPaymentServiceFactory(final AdyenPaymentServiceFactory adyenPaymentServiceFactory)
	{
		this.adyenPaymentServiceFactory = adyenPaymentServiceFactory;
	}

	/**
	 * @return the baseStoreService
	 */
	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	/**
	 * @param baseStoreService
	 *           the baseStoreService to set
	 */
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	/**
	 * @return the configurationService
	 */
	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}
}
