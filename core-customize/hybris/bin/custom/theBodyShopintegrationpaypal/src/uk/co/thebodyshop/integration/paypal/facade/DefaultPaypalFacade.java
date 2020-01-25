/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.paypal.facade;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.model.ModelService;

import uk.co.thebodyshop.integration.paypal.service.PaypalPaymentService;
import uk.co.thebodyshop.integration.paypal.strategy.PaypalPaymentCreationStrategy;
import uk.co.thebodyshop.paypal.data.PaypalCreateOrderResponse;

/**
 * @author vasanthramprakasam
 */
public class DefaultPaypalFacade implements PaypalFacade
{

	 private static final Logger LOG = LoggerFactory.getLogger(DefaultPaypalFacade.class);

	 private final PaypalPaymentService paypalPaymentService;

	 private final CartService cartService;

	 private final CheckoutFacade checkoutFacade;

	 private final PaypalPaymentCreationStrategy paypalPaymentCreationStrategy;

	 private final ModelService modelService;

	 public DefaultPaypalFacade(PaypalPaymentService paypalPaymentService, CartService cartService, CheckoutFacade checkoutFacade, PaypalPaymentCreationStrategy paypalPaymentCreationStrategy, ModelService modelService)
	 {
			this.paypalPaymentService = paypalPaymentService;
			this.cartService = cartService;
			this.checkoutFacade = checkoutFacade;
			this.paypalPaymentCreationStrategy = paypalPaymentCreationStrategy;
			this.modelService = modelService;
	 }

	 @Override
	 public PaypalCreateOrderResponse createOrder(CartData cartData)
	 {
	 	 final PaypalCreateOrderResponse paypalCreateOrderResponse = new PaypalCreateOrderResponse();
		 final Optional<String> paypalOrderIdOptional = getPaypalPaymentService().createOrder(cartData);
		 if(paypalOrderIdOptional.isEmpty())
		 {
		 	 LOG.error("order not created on paypal");
		 	 return paypalCreateOrderResponse;
		 }
		 final String paypalOrderId = paypalOrderIdOptional.get();
		 paypalCreateOrderResponse.setOrderId(paypalOrderId);
	 	 final CartModel cartModel = getCartService().getSessionCart();
	 	 if(cartModel != null)
	 	 {
	 	 	 cartModel.setPaypalOrderCode(paypalOrderId);
	 	 	 getModelService().save(cartModel);
	 	 }
	 	 return paypalCreateOrderResponse;
	 }

	 @Override
	 public Optional<OrderData> authorizePayment(String orderId)
	 {
			try
			{
				 final CartModel cartModel = getCartService().getSessionCart();
				 String paypalOrderIdOnCart = cartModel.getPaypalOrderCode();
				 Assert.notNull(paypalOrderIdOnCart,"Paypal order Id cannot be null");
				 Assert.isTrue(paypalOrderIdOnCart.equals(orderId), "Order Id on cart not same as order Id passed");
				 final Optional<String> authIdOptional = getPaypalPaymentService().authorize(paypalOrderIdOnCart);
				 if(authIdOptional.isEmpty())
				 {
				 	 LOG.error("order [{}] not authorised on paypal",orderId);
				 	 return Optional.empty();
				 }
				 final String authId = authIdOptional.get();
				 if(LOG.isDebugEnabled())
				 {
						LOG.debug("Received Authid [{}] for cart [{}] with paypal order id [{}]", authId,cartModel.getCode(),orderId);
				 }
				 getPaypalPaymentCreationStrategy().createPaypalPaymentInfo(authId, cartModel);
				 final PaymentTransactionModel paymentTransactionModel = getPaypalPaymentCreationStrategy().createPaymentTransaction(authId, cartModel);
				 getPaypalPaymentCreationStrategy().createAuthorizeEntry(authId, paymentTransactionModel,cartModel);
				 final OrderData orderData = getCheckoutFacade().placeOrder();
				 return Optional.of(orderData);
			}
			catch (InvalidCartException ice)
			{
				 LOG.error("Invalid cart",ice);
			}
			return Optional.empty();
	 }

	 protected PaypalPaymentService getPaypalPaymentService()
	 {
			return paypalPaymentService;
	 }

	 protected CartService getCartService()
	 {
			return cartService;
	 }

	 protected CheckoutFacade getCheckoutFacade()
	 {
			return checkoutFacade;
	 }

	 protected PaypalPaymentCreationStrategy getPaypalPaymentCreationStrategy()
	 {
			return paypalPaymentCreationStrategy;
	 }

	 protected ModelService getModelService()
	 {
			return modelService;
	 }
}
