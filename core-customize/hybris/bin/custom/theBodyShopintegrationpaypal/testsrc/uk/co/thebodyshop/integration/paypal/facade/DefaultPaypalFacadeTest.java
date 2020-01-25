/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.paypal.facade;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.model.ModelService;

import uk.co.thebodyshop.integration.paypal.model.PaypalPaymentInfoModel;
import uk.co.thebodyshop.integration.paypal.service.PaypalPaymentService;
import uk.co.thebodyshop.integration.paypal.strategy.PaypalPaymentCreationStrategy;
import uk.co.thebodyshop.paypal.data.PaypalCreateOrderResponse;

/**
 * @author vasanthramprakasam
 */
@UnitTest
public class DefaultPaypalFacadeTest
{
	 private static final String PAYPAL_ORDER_ID = "paypalOrderId";
	 private static final String PAYPAL_AUTH_ID = "paypalAuthId";
	 @InjectMocks
	 private DefaultPaypalFacade paypalFacade;
	 @Mock
	 private PaypalPaymentService paypalPaymentService;
	 @Mock
	 private CartService cartService;
	 @Mock
	 private CheckoutFacade checkoutFacade;
	 @Mock
	 private PaypalPaymentCreationStrategy paypalPaymentCreationStrategy;
	 @Mock
	 private ModelService modelService;
	 @Spy
	 private CartData cartData;
	 @Spy
	 private CartModel cartModel;
	 @Mock
	 private PaypalPaymentInfoModel paypalPaymentInfoModel;
	 @Mock
	 private PaymentTransactionModel paymentTransactionModel;
	 @Mock
	 private PaymentTransactionEntryModel paymentTransactionEntryModel;
	 @Mock
	 private OrderData orderData;

	 @Before
	 public void setUp()
	 {
			MockitoAnnotations.initMocks(this);
			when(cartService.getSessionCart()).thenReturn(cartModel);
			doNothing().when(modelService).save(any());
	 }

	 @Test
	 public void testCreateOrder()
	 {
			when(paypalPaymentService.createOrder(cartData)).thenReturn(Optional.of(PAYPAL_ORDER_ID));
			PaypalCreateOrderResponse paypalCreateOrderResponse = paypalFacade.createOrder(cartData);
			verify(cartService,times(1)).getSessionCart();
			verify(modelService,times(1)).save(cartModel);
			assertTrue(paypalCreateOrderResponse.getOrderId().equals(PAYPAL_ORDER_ID));
	 }

	 @Test
	 public void testAuthorizeOrder() throws InvalidCartException
	 {
	 	 cartModel.setPaypalOrderCode(PAYPAL_ORDER_ID);
	 	 when(paypalPaymentService.authorize(PAYPAL_ORDER_ID)).thenReturn(Optional.of(PAYPAL_AUTH_ID));
		 when(paypalPaymentCreationStrategy.createPaypalPaymentInfo(PAYPAL_AUTH_ID,cartModel)).thenReturn(paypalPaymentInfoModel);
		 when(paypalPaymentCreationStrategy.createPaymentTransaction(PAYPAL_AUTH_ID,cartModel)).thenReturn(paymentTransactionModel);
		 when(paypalPaymentCreationStrategy.createAuthorizeEntry(PAYPAL_AUTH_ID,paymentTransactionModel,cartModel)).thenReturn(paymentTransactionEntryModel);
		 when(checkoutFacade.placeOrder()).thenReturn(orderData);
		 paypalFacade.authorizePayment(PAYPAL_ORDER_ID);
		 verify(cartService,times(1)).getSessionCart();
		 verify(paypalPaymentService,times(1)).authorize(PAYPAL_ORDER_ID);
		 verify(paypalPaymentCreationStrategy,times(1)).createPaypalPaymentInfo(PAYPAL_AUTH_ID,cartModel);
		 verify(paypalPaymentCreationStrategy,times(1)).createPaymentTransaction(PAYPAL_AUTH_ID,cartModel);
		 verify(paypalPaymentCreationStrategy,times(1)).createAuthorizeEntry(PAYPAL_AUTH_ID,paymentTransactionModel,cartModel);
		 verify(checkoutFacade,times(1)).placeOrder();
	 }

	 @Test(expected = IllegalArgumentException.class)
	 public void testAuthorizeOrderOnInvalidCart() throws InvalidCartException
	 {
			cartModel.setPaypalOrderCode("invalidId");
			when(paypalPaymentService.authorize(PAYPAL_ORDER_ID)).thenReturn(Optional.of(PAYPAL_AUTH_ID));
			paypalFacade.authorizePayment(PAYPAL_ORDER_ID);
			verify(cartService,times(1)).getSessionCart();
			verify(paypalPaymentService,never()).authorize(PAYPAL_ORDER_ID);
			verify(paypalPaymentCreationStrategy,never()).createPaypalPaymentInfo(PAYPAL_AUTH_ID,cartModel);
			verify(paypalPaymentCreationStrategy,never()).createPaymentTransaction(PAYPAL_AUTH_ID,cartModel);
			verify(paypalPaymentCreationStrategy,never()).createAuthorizeEntry(PAYPAL_AUTH_ID,paymentTransactionModel,cartModel);
			verify(checkoutFacade,never()).placeOrder();
	 }
}
