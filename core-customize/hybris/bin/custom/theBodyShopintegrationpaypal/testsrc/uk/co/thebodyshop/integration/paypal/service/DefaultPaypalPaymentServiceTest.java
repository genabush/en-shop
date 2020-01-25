/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.paypal.service;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.braintreepayments.http.HttpRequest;
import com.braintreepayments.http.HttpResponse;
import com.paypal.core.PayPalHttpClient;
import com.paypal.orders.Authorization;
import com.paypal.orders.Order;
import com.paypal.orders.PaymentCollection;
import com.paypal.orders.PurchaseUnit;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.store.BaseStoreModel;

import uk.co.thebodyshop.integration.paypal.client.factory.PaypalClientFactory;
import uk.co.thebodyshop.integration.paypal.service.impl.DefaultPaypalPaymentService;

/**
 * @author vasanthramprakasam
 */
@UnitTest
public class DefaultPaypalPaymentServiceTest
{
	 private static final String CURRENCY_ISO = "GBP";
	 private static final String STORE_NAME = "theBodyShop";
	 public static final String ID = "id";
	 public static final String AUTH_ID = "TEST123";
	 public static final Integer VOID_SUCCESS_STATUS_CODE = 204;
	 public static final Integer VOID_FAILURE_STATUS_CODE = 400;
	 @InjectMocks
	 private DefaultPaypalPaymentService paypalPaymentService;
	 @Mock
	 private PaypalClientFactory paypalClientFactory;
	 @Mock
	 private PayPalHttpClient payPalHttpClient;
     @Mock
	 private CartService cartService;
     @Mock
	 private Supplier<String> storeNameSupplier;
     @Spy
	 private CartData cartData;
	 @Spy
	 private CartModel cartModel;
	 @Mock
	 private HttpResponse<Order> orderHttpResponse;
	 @Mock
	 private Order order;
	 @Mock
	 private List<PurchaseUnit> purchaseUnitList;
	 @Mock
	 private PurchaseUnit purchaseUnit;
	 @Mock
	 private PaymentCollection paymentCollection;
	 @Mock
	 private List<Authorization> authorizations;
	 @Mock
	 private Authorization authorization;
	 @Mock
	 private BaseStoreModel baseStore;
	 @Mock
	 private HttpResponse<Void> response;

   @Before
	 public void setUp()
	 {
			MockitoAnnotations.initMocks(this);
			when(cartService.getSessionCart()).thenReturn(cartModel);
			when(storeNameSupplier.get()).thenReturn(STORE_NAME);
			setUpCartData(cartData);
		    setUpAuthorization();
			when(orderHttpResponse.result()).thenReturn(order);
			when(order.id()).thenReturn(ID);
		    when(authorization.id()).thenReturn(AUTH_ID);
			when(paypalClientFactory.createClient()).thenReturn(payPalHttpClient);
	 }

	 private void setUpCartData(CartData cartData)
	 {
	 	 cartData.setCode("code");
	 	 cartData.setTotalPrice(createPriceData(ONE));
	 	 cartData.setTotalTax(createPriceData(ZERO));
	 	 cartData.setDeliveryCost(createPriceData(ZERO));
	 	 cartData.setOutstandingAmount(createPriceData(ZERO));
	 	 cartData.setTotalGiftCardsValue(createPriceData(ZERO));
	 	 setUpEntries(cartData);
	 	 setUpDeliveryAddress(cartData);
	 }

	 private void setUpEntries(CartData cartData)
	 {
			OrderEntryData orderEntryData = new OrderEntryData();
			ProductData productData = new ProductData();
			productData.setName("name");
			productData.setCategories(Collections.emptyList());
			orderEntryData.setProduct(productData);
			orderEntryData.setBasePrice(createPriceData(ONE));
			orderEntryData.setQuantity(1L);
			cartData.setEntries(Collections.singletonList(orderEntryData));
	 }

	 private void setUpDeliveryAddress(CartData cartData)
	 {
			AddressData addressData = new AddressData();
			addressData.setFirstName("first");
			addressData.setLastName("last");
			CountryData countryData = new CountryData();
			countryData.setIsocode("GB");
			addressData.setCountry(countryData);
			cartData.setDeliveryAddress(addressData);
	 }

	private void setUpAuthorization()
	{
		authorization.id(AUTH_ID);
		authorizations.add(authorization);
		paymentCollection.authorizations(authorizations);
		purchaseUnit.payments(paymentCollection);
		purchaseUnitList.add(purchaseUnit);
		order.purchaseUnits(purchaseUnitList);
	}

	 protected PriceData createPriceData(BigDecimal value)
	 {
			PriceData priceData = new PriceData();
			priceData.setCurrencyIso(CURRENCY_ISO);
			priceData.setValue(value);
			return priceData;
	 }

	 @Test
	 public void testCreateOrder() throws IOException
	 {
	 	  when(payPalHttpClient.execute(any(HttpRequest.class))).thenReturn(orderHttpResponse);
	 	  Optional<String> orderId = paypalPaymentService.createOrder(cartData);
		  verify(payPalHttpClient,times(1)).execute(any(HttpRequest.class));
		  assertTrue(orderId.get().equals(ID));
	 }

	 @Test
	 public void testAuthorizeOrder() throws IOException
	 {
	 	 when(payPalHttpClient.execute(any(HttpRequest.class))).thenReturn(orderHttpResponse);
		 Optional<String> orderId = paypalPaymentService.authorize(ID);
		 verify(payPalHttpClient,times(1)).execute(any(HttpRequest.class));
		 assertTrue(authorization.id().equals(AUTH_ID));
	 }

	@Test
	public void testVoidAuthorizeSuccess() throws IOException
	{
		when(paypalClientFactory.createClientFromBaseStore(baseStore)).thenReturn(payPalHttpClient);
		when(payPalHttpClient.execute(any(HttpRequest.class))).thenReturn(response);
		when(response.statusCode()).thenReturn(VOID_SUCCESS_STATUS_CODE);
		Optional<Integer> voidResp = paypalPaymentService.voidAuthorize(AUTH_ID, baseStore);
		verify(payPalHttpClient, times(1)).execute(any(HttpRequest.class));
		assertTrue(voidResp.get().equals(VOID_SUCCESS_STATUS_CODE));
	}

	@Test
	public void testVoidAuthorizeFailed() throws IOException
	{
		when(paypalClientFactory.createClientFromBaseStore(baseStore)).thenReturn(payPalHttpClient);
		when(payPalHttpClient.execute(any(HttpRequest.class))).thenReturn(response);
		when(response.statusCode()).thenReturn(VOID_FAILURE_STATUS_CODE);
		Optional<Integer> voidResp = paypalPaymentService.voidAuthorize(AUTH_ID, baseStore);
		verify(payPalHttpClient, times(1)).execute(any(HttpRequest.class));
		assertTrue(voidResp.get().equals(VOID_FAILURE_STATUS_CODE));
	}
}
