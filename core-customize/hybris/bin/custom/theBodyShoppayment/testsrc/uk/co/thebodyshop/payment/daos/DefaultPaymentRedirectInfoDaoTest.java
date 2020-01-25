/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.payment.daos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import uk.co.thebodyshop.payment.daos.impl.DefaultPaymentRedirectInfoDao;
import uk.co.thebodyshop.payment.model.PaymentRedirectInfoModel;

/**
 * @author Marcin
 */
@UnitTest
public class DefaultPaymentRedirectInfoDaoTest
{
	private static final String ANOTHER_MD = "anotherMD";

	private static final String ANOTHER_PA_REQ = "anotherPaReq";

	private static final String MD = "MD";

	private static final String PA_REQ = "paReq";

	private static final String USER_ID = "userId";

	private static final String CART_ID = "cartId";

	private static final String EXPECTED_FIND_PAYMENT_REDIRECT_INFO_QUERY = "SELECT {" + PaymentRedirectInfoModel.PK + "} " + "FROM {" + PaymentRedirectInfoModel._TYPECODE + "} " + "WHERE {" + PaymentRedirectInfoModel.USERID + "} = ?userId " + "AND {"
			+ PaymentRedirectInfoModel.CARTID + "} = ?cartId " + "ORDER BY {" + PaymentRedirectInfoModel.CREATIONTIME + "} DESC";

	private static final String FIND_ALL_PAYMENT_REDIRECT_INFOS_QUERY = "SELECT {" + PaymentRedirectInfoModel.PK + "} " + "FROM {" + PaymentRedirectInfoModel._TYPECODE + "} " + "WHERE {" + PaymentRedirectInfoModel.USERID + "} = ?userId " + "AND {"
			+ PaymentRedirectInfoModel.CARTID + "} = ?cartId ";

	private DefaultPaymentRedirectInfoDao defaultPaymentRedirectInfoDao;

	@Mock
	private FlexibleSearchService flexibleSearchService;

	@Mock
	private SearchResult<Object> searchResult;

	private List<Object> paymentRedirectionInfos;

	private PaymentRedirectInfoModel paymentRedirectInfoModel;

	private PaymentRedirectInfoModel anotherPaymentRedirectInfoModel;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		defaultPaymentRedirectInfoDao = new DefaultPaymentRedirectInfoDao(flexibleSearchService);

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
	public void testFindPaymentRedirectionInfoForCartIdAndUserId()
	{
		paymentRedirectionInfos = new ArrayList<>();
		paymentRedirectionInfos.add(paymentRedirectInfoModel);
		final Map<String, Object> params = new HashMap<>();
		params.put(USER_ID, USER_ID);
		params.put(CART_ID, CART_ID);
		when(flexibleSearchService.search(EXPECTED_FIND_PAYMENT_REDIRECT_INFO_QUERY, params)).thenReturn(searchResult);
		when(searchResult.getResult()).thenReturn(paymentRedirectionInfos);
		final PaymentRedirectInfoModel paymentredirectInfo = defaultPaymentRedirectInfoDao.findPaymentRedirectInfoForUserAndCart(USER_ID, CART_ID);
		assertEquals(paymentredirectInfo.getUserId(), USER_ID);
		assertEquals(paymentredirectInfo.getCartId(), CART_ID);
		assertEquals(paymentredirectInfo.getAdyenPaReq(), PA_REQ);
		assertEquals(paymentredirectInfo.getAdyenPaymentMD(), MD);
	}

	@Test
	public void testNoPaymentRedirectionInfoForCartIdAndUserId()
	{
		paymentRedirectionInfos = new ArrayList<>();
		final Map<String, Object> params = new HashMap<>();
		params.put(USER_ID, USER_ID);
		params.put(CART_ID, CART_ID);
		when(flexibleSearchService.search(EXPECTED_FIND_PAYMENT_REDIRECT_INFO_QUERY, params)).thenReturn(searchResult);
		when(searchResult.getResult()).thenReturn(paymentRedirectionInfos);
		final PaymentRedirectInfoModel paymentredirectInfo = defaultPaymentRedirectInfoDao.findPaymentRedirectInfoForUserAndCart(USER_ID, CART_ID);
		assertNull(paymentredirectInfo);
	}

	@Test
	public void testfindAllPaymentRedirectionInfoForCartIdAndUserId()
	{
		paymentRedirectionInfos = new ArrayList<>();
		paymentRedirectionInfos.add(paymentRedirectInfoModel);
		paymentRedirectionInfos.add(anotherPaymentRedirectInfoModel);
		final Map<String, Object> params = new HashMap<>();
		params.put(USER_ID, USER_ID);
		params.put(CART_ID, CART_ID);
		when(flexibleSearchService.search(FIND_ALL_PAYMENT_REDIRECT_INFOS_QUERY, params)).thenReturn(searchResult);
		when(searchResult.getResult()).thenReturn(paymentRedirectionInfos);
		final List<PaymentRedirectInfoModel> paymentredirectInfos = defaultPaymentRedirectInfoDao.findAllPaymentRedirectInfosForUserAndCart(USER_ID, CART_ID);
		assertEquals(paymentredirectInfos.size(), 2);
		assertEquals(paymentredirectInfos.get(0).getUserId(), USER_ID);
		assertEquals(paymentredirectInfos.get(0).getCartId(), CART_ID);
		assertEquals(paymentredirectInfos.get(0).getAdyenPaReq(), PA_REQ);
		assertEquals(paymentredirectInfos.get(0).getAdyenPaymentMD(), MD);
		assertEquals(paymentredirectInfos.get(1).getUserId(), USER_ID);
		assertEquals(paymentredirectInfos.get(1).getCartId(), CART_ID);
		assertEquals(paymentredirectInfos.get(1).getAdyenPaReq(), ANOTHER_PA_REQ);
		assertEquals(paymentredirectInfos.get(1).getAdyenPaymentMD(), ANOTHER_MD);
	}
}