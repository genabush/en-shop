/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.payment.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.model.ModelService;

import uk.co.thebodyshop.payment.daos.PaymentRedirectInfoDao;
import uk.co.thebodyshop.payment.model.PaymentRedirectInfoModel;
import uk.co.thebodyshop.payment.services.impl.DefaultPaymentRedirectInfoService;

/**
 * @author Marcin
 */
@UnitTest
public class DefaultPaymentRedirectInfoServiceTest
{
	private static final String ANOTHER_MD = "anotherMD";

	private static final String ANOTHER_PA_REQ = "anotherPaReq";

	private static final String MD = "MD";

	private static final String PA_REQ = "paReq";

	private static final String USER_ID = "userId";

	private static final String CART_ID = "cartId";

	private DefaultPaymentRedirectInfoService paymentRedirectInfoService;

	@Mock
	private PaymentRedirectInfoDao paymentRedirectInfoDao;

	@Mock
	private ModelService modelService;

	private List<PaymentRedirectInfoModel> paymentRedirectionInfos;

	private PaymentRedirectInfoModel paymentRedirectInfoModel;

	private PaymentRedirectInfoModel anotherPaymentRedirectInfoModel;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		paymentRedirectInfoService = new DefaultPaymentRedirectInfoService(paymentRedirectInfoDao, modelService);

		paymentRedirectInfoModel = new PaymentRedirectInfoModel();
		paymentRedirectInfoModel.setCartId(CART_ID);
		paymentRedirectInfoModel.setUserId(USER_ID);
		paymentRedirectInfoModel.setAdyenPaReq(PA_REQ);
		paymentRedirectInfoModel.setAdyenPaymentMD(MD);

		anotherPaymentRedirectInfoModel = new PaymentRedirectInfoModel();
		anotherPaymentRedirectInfoModel.setCartId(CART_ID);
		anotherPaymentRedirectInfoModel.setUserId(USER_ID);
		anotherPaymentRedirectInfoModel.setAdyenPaReq(ANOTHER_PA_REQ);
		anotherPaymentRedirectInfoModel.setAdyenPaymentMD(ANOTHER_MD);
	}

	@Test
	public void testGetPaymentRedirectionInfoForCartIdAndUserId()
	{
		when(paymentRedirectInfoDao.findPaymentRedirectInfoForUserAndCart(USER_ID, CART_ID)).thenReturn(paymentRedirectInfoModel);
		final PaymentRedirectInfoModel paymentredirectInfo = paymentRedirectInfoService.getPaymentRedirectInfoForUserAndCart(USER_ID, CART_ID);
		assertEquals(paymentredirectInfo.getUserId(), USER_ID);
		assertEquals(paymentredirectInfo.getCartId(), CART_ID);
		assertEquals(paymentredirectInfo.getAdyenPaReq(), PA_REQ);
		assertEquals(paymentredirectInfo.getAdyenPaymentMD(), MD);
	}

	@Test
	public void testRemoveAllPaymentRedirectionInfoForCartIdAndUserId()
	{
		paymentRedirectionInfos = new ArrayList<>();
		paymentRedirectionInfos.add(paymentRedirectInfoModel);
		paymentRedirectionInfos.add(anotherPaymentRedirectInfoModel);
		when(paymentRedirectInfoDao.findAllPaymentRedirectInfosForUserAndCart(USER_ID, CART_ID)).thenReturn(paymentRedirectionInfos);
		paymentRedirectInfoService.clearAllPaymentRedirectInfoForUserAndCart(USER_ID, CART_ID);
		verify(modelService, times(1)).removeAll(paymentRedirectionInfos);
	}

	@Test
	public void testNoRemovalAllPaymentRedirectionInfoForCartIdAndUserId()
	{
		when(paymentRedirectInfoDao.findAllPaymentRedirectInfosForUserAndCart(USER_ID, CART_ID)).thenReturn(null);
		paymentRedirectInfoService.clearAllPaymentRedirectInfoForUserAndCart(USER_ID, CART_ID);
		verify(modelService, never()).removeAll(paymentRedirectionInfos);
	}
}
