/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.payment.services.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import de.hybris.platform.servicelayer.model.ModelService;

import uk.co.thebodyshop.payment.daos.PaymentRedirectInfoDao;
import uk.co.thebodyshop.payment.model.PaymentRedirectInfoModel;
import uk.co.thebodyshop.payment.services.PaymentRedirectInfoService;

/**
 * @author Marcin
 */
public class DefaultPaymentRedirectInfoService implements PaymentRedirectInfoService
{
	private final PaymentRedirectInfoDao paymentRedirectInfoDao;

	private final ModelService modelService;

	@Override
	public PaymentRedirectInfoModel getPaymentRedirectInfoForUserAndCart(final String userId, final String cartId)
	{
		return getPaymentRedirectInfoDao().findPaymentRedirectInfoForUserAndCart(userId, cartId);
	}

	@Override
	public void clearAllPaymentRedirectInfoForUserAndCart(final String userId, final String cartId)
	{
		final List<PaymentRedirectInfoModel> paymentRedirectInfos = getPaymentRedirectInfoDao().findAllPaymentRedirectInfosForUserAndCart(userId, cartId);
		if (CollectionUtils.isNotEmpty(paymentRedirectInfos))
		{
			getModelService().removeAll(paymentRedirectInfos);
		}
	}

	public DefaultPaymentRedirectInfoService(final PaymentRedirectInfoDao paymentRedirectInfoDao, final ModelService modelService)
	{
		this.paymentRedirectInfoDao = paymentRedirectInfoDao;
		this.modelService = modelService;
	}

	/**
	 * @return the paymentRedirectInfoDao
	 */
	protected PaymentRedirectInfoDao getPaymentRedirectInfoDao()
	{
		return paymentRedirectInfoDao;
	}

	/**
	 * @return the modelService
	 */
	protected ModelService getModelService()
	{
		return modelService;
	}
}
