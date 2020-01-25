/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package uk.co.thebodyshop.integration.adyen.populator;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;


public class TbsAdyenCartPopulator implements Populator<CartModel, CartData>
{

	private ConfigurationService configurationService;
	private CommonI18NService commonI18NService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void populate(final CartModel source, final CartData target) throws ConversionException
	{
		target.setAdyenDfValue(source.getAdyenDfValue());

		final PaymentInfoModel paymentInfo = source.getPaymentInfo();
		if (paymentInfo != null && isAdyenPaymentInfo(paymentInfo))
		{
			target.setAdyenPaymentMethod(paymentInfo.getAdyenPaymentMethod());
			target.setAdyenIssuerId(paymentInfo.getAdyenIssuerId());
			target.setAdyenRememberTheseDetails(paymentInfo.getAdyenRememberTheseDetails());
			target.setAdyenSelectedReference(paymentInfo.getAdyenSelectedReference());
			target.setAdyenDob(paymentInfo.getAdyenDob());
			target.setAdyenSocialSecurityNumber(paymentInfo.getAdyenSocialSecurityNumber());
			target.setAdyenFirstName(paymentInfo.getAdyenFirstName());
			target.setAdyenLastName(paymentInfo.getAdyenLastName());
			target.setAdyenPaymentToken(paymentInfo.getAdyenPaypalEcsToken());
			target.setAdyenCardHolder(paymentInfo.getAdyenCardHolder());
			target.setAdyenCardBrand(paymentInfo.getCardBrand());
			target.setAdyenEncryptedCardNumber(paymentInfo.getEncryptedCardNumber());
			target.setAdyenEncryptedExpiryMonth(paymentInfo.getEncryptedExpiryMonth());
			target.setAdyenEncryptedExpiryYear(paymentInfo.getEncryptedExpiryYear());
			target.setAdyenEncryptedSecurityCode(paymentInfo.getEncryptedSecurityCode());
			target.setAdyenInstallments(paymentInfo.getAdyenInstallments());
			target.setAdyenBrowserInfo(paymentInfo.getAdyenBrowserInfo());
			final String adyenReturnUrl = getReturnUrl(target);
			target.setAdyenReturnUrl(adyenReturnUrl);
		}
	}

	protected boolean isAdyenPaymentInfo(final PaymentInfoModel paymentInfo)
	{
		return !(paymentInfo instanceof CreditCardPaymentInfoModel);
	}

	private String getReturnUrl(final CartData cartData)
	{
		final StringBuilder returnUrl = new StringBuilder(
				getConfigurationService().getConfiguration().getString("adyen.payment.redirect.return.url.domain"));
		returnUrl.append(cartData.getSite());
		returnUrl.append("/userId/" + cartData.getUser().getUid());
		returnUrl.append("/cart/" + cartData.getCode());
		returnUrl.append("/language/" + getCommonI18NService().getCurrentLanguage().getIsocode());
		returnUrl.append("/currency/" + getCommonI18NService().getCurrentCurrency().getIsocode());
		returnUrl.append("/paymentRedirectDetails");
		return returnUrl.toString();
	}

	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}
}