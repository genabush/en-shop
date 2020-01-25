/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.paypal.strategy;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.commerceservices.strategies.CheckoutCustomerStrategy;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.model.ModelService;

import uk.co.thebodyshop.integration.paypal.constants.TheBodyShopintegrationpaypalConstants;
import uk.co.thebodyshop.integration.paypal.model.PaypalPaymentInfoModel;

/**
 * @author vasanthramprakasam
 */
@UnitTest
public class DefaultPaypalPaymentCreationStrategyTest
{
	 public static final String AUTH_ID = "authId";
	 @InjectMocks
	 private DefaultPaypalPaymentCreationStrategy paypalPaymentCreationStrategy;
	 @Mock
	 private CheckoutCustomerStrategy checkoutCustomerStrategy;
   @Mock
	 private ModelService modelService;
	 @Mock
	 private CommerceCommonI18NService commerceCommonI18NService;
	 @Spy
	 private CartModel cartModel;
	 @Mock
	 private CustomerModel customerModel;
	 @Mock
	 private PaypalPaymentInfoModel paypalPaymentInfoModel;
	 @Spy
	 private PaymentTransactionModel paymentTransactionModel;
	 @Spy
	 private PaymentTransactionEntryModel paymentTransactionEntryModel;
	 @Mock
	 private CurrencyModel currencyModel;

	 @Before
	 public void setUp()
	 {
			MockitoAnnotations.initMocks(this);
			doNothing().when(modelService).saveAll(any(),any());
			cartModel.setCode("code");
	 }

	 @Test
	 public void testCreatePaypalPaymentInfo()
	 {
			when(checkoutCustomerStrategy.getCurrentUserForCheckout()).thenReturn(customerModel);
			when(modelService.create(PaypalPaymentInfoModel.class)).thenReturn(paypalPaymentInfoModel);
			PaypalPaymentInfoModel paypalPaymentInfoModel = paypalPaymentCreationStrategy.createPaypalPaymentInfo(AUTH_ID,cartModel);
			verify(modelService,times(1)).saveAll(any(),any());
			assertTrue(cartModel.getPaymentInfo().equals(paypalPaymentInfoModel));
	 }

	 @Test
	 public void testCreatePaymentTransaction()
	 {
			when(modelService.create(PaymentTransactionModel.class)).thenReturn(paymentTransactionModel);
			PaymentTransactionModel paymentTransactionModel = paypalPaymentCreationStrategy.createPaymentTransaction(AUTH_ID,cartModel);
			assertTrue(paymentTransactionModel.getPaymentProvider().equals(TheBodyShopintegrationpaypalConstants.PAYPAL_PAYMENT));
	 }

	 @Test
	 public void testCreateAuthorizeEntry()
	 {
	 	 when(modelService.create(PaymentTransactionEntryModel.class)).thenReturn(paymentTransactionEntryModel);
	 	 cartModel.setTotalPrice(1D);
	 	 cartModel.setTotalTax(1D);
	 	 when(cartModel.getAmountGiftCards()).thenReturn(0D);
	 	 when(commerceCommonI18NService.getCurrentCurrency()).thenReturn(currencyModel);
	 	 PaymentTransactionEntryModel paymentTransactionEntryModel = paypalPaymentCreationStrategy.createAuthorizeEntry(AUTH_ID,paymentTransactionModel,cartModel);
	 	 assertTrue(paymentTransactionEntryModel.getTransactionStatus().equals(TransactionStatus.ACCEPTED.name()));
	 	 assertTrue(paymentTransactionEntryModel.getAmount().doubleValue() == 2D);
	 	 assertTrue(paymentTransactionEntryModel.getCurrency().equals(currencyModel));
	 }

}
