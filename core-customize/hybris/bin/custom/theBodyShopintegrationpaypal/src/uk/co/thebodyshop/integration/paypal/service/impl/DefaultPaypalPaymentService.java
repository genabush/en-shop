/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.paypal.service.impl;

import static uk.co.thebodyshop.integration.paypal.constants.TheBodyShopintegrationpaypalConstants.PayPalIntent.AUTHORIZE;
import static uk.co.thebodyshop.integration.paypal.constants.TheBodyShopintegrationpaypalConstants.PaypalLandingPage.LOGIN;
import static uk.co.thebodyshop.integration.paypal.constants.TheBodyShopintegrationpaypalConstants.ShippingPreference.SET_PROVIDED_ADDRESS;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.braintreepayments.http.HttpResponse;
import com.google.gson.Gson;
import com.paypal.core.PayPalHttpClient;
import com.paypal.orders.AddressPortable;
import com.paypal.orders.AmountBreakdown;
import com.paypal.orders.AmountWithBreakdown;
import com.paypal.orders.ApplicationContext;
import com.paypal.orders.Authorization;
import com.paypal.orders.Item;
import com.paypal.orders.Money;
import com.paypal.orders.Name;
import com.paypal.orders.Order;
import com.paypal.orders.OrderRequest;
import com.paypal.orders.OrdersAuthorizeRequest;
import com.paypal.orders.OrdersCreateRequest;
import com.paypal.orders.PurchaseUnit;
import com.paypal.orders.PurchaseUnitRequest;
import com.paypal.orders.ShippingDetail;
import com.paypal.payments.AuthorizationsVoidRequest;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.store.BaseStoreModel;

import uk.co.thebodyshop.integration.paypal.client.factory.PaypalClientFactory;
import uk.co.thebodyshop.integration.paypal.service.PaypalPaymentService;

/**
 * @author vasanthramprakasam
 */
public class DefaultPaypalPaymentService implements PaypalPaymentService
{

	private static final Logger LOG = LoggerFactory.getLogger(DefaultPaypalPaymentService.class);

	private static final int MAX_LENGTH = 126;

	private final PaypalClientFactory paypalClientFactory;

	private final Supplier<String> storeNameSupplier;

	public DefaultPaypalPaymentService(final PaypalClientFactory paypalClientFactory, final Supplier<String> storeNameSupplier)
	{
		this.paypalClientFactory = paypalClientFactory;
		this.storeNameSupplier = storeNameSupplier;
	}

	@Override
	public Optional<String> createOrder(final CartData cartData)
	{
		final OrdersCreateRequest request = new OrdersCreateRequest();
		request.header("prefer", "return=representation");
		request.requestBody(buildCompleteRequestBody(cartData));
		try
		{
			final PayPalHttpClient payPalHttpClient = getPaypalClientFactory().createClient();
			final HttpResponse<Order> response = payPalHttpClient.execute(request);
			final String orderId = response.result().id();

			if (LOG.isDebugEnabled())
			{
				LOG.debug("response recieved from paypal for create order is \n [{}]", new Gson().toJson(response.result()));
				LOG.debug("order [{}] created at paypal", orderId);
			}
			return Optional.of(orderId);
		}
		catch (final IOException io)
		{
			LOG.error("error", io);
		}

		return Optional.empty();
	}

	 @Override
	 public Optional<String> authorize(final String orderId)
	 {
	 	 try
		 {
				final OrdersAuthorizeRequest request = new OrdersAuthorizeRequest(orderId);
				request.requestBody(new OrderRequest());
				final PayPalHttpClient payPalHttpClient = getPaypalClientFactory().createClient();
				final HttpResponse<Order> response = payPalHttpClient.execute(request);
				 String authId = null;
			     for (final PurchaseUnit purchaseUnit : response.result().purchaseUnits())
				 {
					   authId=purchaseUnit.payments().authorizations().stream().map(Authorization::id).findAny().get();

				 }
				if(LOG.isDebugEnabled())
				{
					 LOG.debug("response recieved from paypal for authorize is \n [{}]",new Gson().toJson(response.result()));
					 LOG.debug("order [{}] has auth id [{}] at paypal",orderId,authId);
				}
				return authId==null?Optional.empty():Optional.of(authId);
		 }
	 	 catch (final IOException io)
		 {
		 	 LOG.error("error",io);
		 }
	 	 return Optional.empty();
	 }
	@Override
	public Optional<Integer> voidAuthorize(final String authorizationId, final BaseStoreModel baseStore)
	{
		try
		{
			final AuthorizationsVoidRequest authorizationsVoidRequest=new AuthorizationsVoidRequest(authorizationId);

			final PayPalHttpClient payPalHttpClient = getPaypalClientFactory().createClientFromBaseStore(baseStore);
			final HttpResponse<Void> response = payPalHttpClient.execute(authorizationsVoidRequest);
			final int statusCode=response.statusCode();
			if(LOG.isDebugEnabled())
			{
				LOG.debug("response code recieved from paypal for void authorize is \n [{}]",statusCode);
			}
			return Optional.of(statusCode);
		}
		catch (final IOException io)
		{
			LOG.error("error",io);
		}
		return Optional.empty();
	}


	/**
	 * Method to create complete order body with <b>AUTHORIZE</b> intent
	 *
	 * @return OrderRequest with created order request
	 */
	protected OrderRequest buildCompleteRequestBody(final CartData cartData)
	{
		final OrderRequest orderRequest = new OrderRequest();
		orderRequest.checkoutPaymentIntent(AUTHORIZE);

		final String brandName = getStoreNameSupplier().get();
		final ApplicationContext applicationContext = new ApplicationContext().brandName(brandName).landingPage(LOGIN).shippingPreference(SET_PROVIDED_ADDRESS);
		orderRequest.applicationContext(applicationContext);

		final List<PurchaseUnitRequest> purchaseUnitRequests = new ArrayList<>();
		final String isocode = cartData.getTotalPrice().getCurrencyIso();
		final BigDecimal totalAmount = cartData.getOutstandingAmount().getValue().add(cartData.getTotalTax().getValue());
		final BigDecimal totalWithoutShipping = cartData.getTotalPrice().getValue().subtract(cartData.getDeliveryCost().getValue());
		final PurchaseUnitRequest purchaseUnitRequest = new PurchaseUnitRequest().referenceId(cartData.getCode()).description(brandName).customId(brandName).softDescriptor(brandName)
				.amountWithBreakdown(new AmountWithBreakdown().currencyCode(isocode).value(totalAmount.toString())
						.amountBreakdown(new AmountBreakdown().itemTotal(new Money().currencyCode(isocode).value(totalWithoutShipping.toString())).taxTotal(new Money().currencyCode(isocode).value(cartData.getTotalTax().getValue().toString()))
								.handling(new Money().currencyCode(isocode).value("0.00")).shipping(new Money().currencyCode(isocode).value(cartData.getDeliveryCost().getValue().toString())).shippingDiscount(new Money().currencyCode(isocode).value("0.00"))
								.discount(new Money().currencyCode(isocode).value(cartData.getTotalGiftCardsValue().getValue().toString()))// Add the gift card as discount for now as the total amount needs to match
																																																														// the amount breakdown, TODO see where to do this properly
						));

		final List<Item> items = cartData.getEntries().stream().map(entry -> {
			final ProductData product = entry.getProduct();
			return new Item().name(product.getName()).description(Optional.ofNullable(product.getDescription()).filter(StringUtils::isNotBlank).map(desc -> StringUtils.substring(desc, 0, MAX_LENGTH)).orElse("description")).sku(product.getCode())
					.unitAmount(new Money().currencyCode(isocode).value(entry.getBasePrice().getValue().toString())).tax(new Money().currencyCode(isocode).value("0.00")).quantity(entry.getQuantity().toString())
					.category(product.getCategories().stream().findFirst().map(CategoryData::getName).orElse(null));
		}).collect(Collectors.toList());

		purchaseUnitRequest.items(items);

		final AddressData deliveryAddress = cartData.getDeliveryAddress();
		purchaseUnitRequest.shippingDetail(new ShippingDetail().name(new Name().fullName(deliveryAddress.getFirstName() + " " + deliveryAddress.getLastName())).addressPortable(new AddressPortable().addressLine1(deliveryAddress.getLine1())
				.addressLine2(deliveryAddress.getLine2()).adminArea2(deliveryAddress.getTown()).adminArea1(deliveryAddress.getDistrict()).postalCode(deliveryAddress.getPostalCode()).countryCode(deliveryAddress.getCountry().getIsocode())));
		purchaseUnitRequests.add(purchaseUnitRequest);
		orderRequest.purchaseUnits(purchaseUnitRequests);
		if (LOG.isDebugEnabled())
		{
			LOG.debug("request \n {}", new Gson().toJson(orderRequest));
		}
		return orderRequest;
	}

	public PaypalClientFactory getPaypalClientFactory()
	{
		return paypalClientFactory;
	}

	public Supplier<String> getStoreNameSupplier()
	{
		return storeNameSupplier;
	}
}
