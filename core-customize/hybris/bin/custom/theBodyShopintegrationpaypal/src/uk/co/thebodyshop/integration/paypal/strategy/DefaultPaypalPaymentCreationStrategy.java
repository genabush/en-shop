/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.paypal.strategy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;

import org.apache.commons.collections.CollectionUtils;

import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.commerceservices.strategies.CheckoutCustomerStrategy;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.model.ModelService;

import uk.co.thebodyshop.integration.paypal.constants.TheBodyShopintegrationpaypalConstants;
import uk.co.thebodyshop.integration.paypal.model.PaypalPaymentInfoModel;

/**
 * @author vasanthramprakasam
 */
public class DefaultPaypalPaymentCreationStrategy implements PaypalPaymentCreationStrategy
{

	 private final CheckoutCustomerStrategy checkoutCustomerStrategy;

	 private final ModelService modelService;

	 private final CommerceCommonI18NService commerceCommonI18NService;

	 public DefaultPaypalPaymentCreationStrategy(CheckoutCustomerStrategy checkoutCustomerStrategy, ModelService modelService, CommerceCommonI18NService commerceCommonI18NService)
	 {
			this.checkoutCustomerStrategy = checkoutCustomerStrategy;
			this.modelService = modelService;
			this.commerceCommonI18NService = commerceCommonI18NService;
	 }

	 @Override
	 public PaypalPaymentInfoModel createPaypalPaymentInfo(String authId, AbstractOrderModel orderModel)
	 {
			final CustomerModel customerModel = getCheckoutCustomerStrategy().getCurrentUserForCheckout();
			PaypalPaymentInfoModel paypalPaymentInfoModel = getModelService().create(PaypalPaymentInfoModel.class);
			paypalPaymentInfoModel.setCode(orderModel.getCode()+"_"+authId);
			paypalPaymentInfoModel.setToken(authId);
			paypalPaymentInfoModel.setBillingAddress(orderModel.getDeliveryAddress());
			paypalPaymentInfoModel.setUser(customerModel);
			paypalPaymentInfoModel.setOwner(orderModel);
			orderModel.setPaymentInfo(paypalPaymentInfoModel);
			getModelService().saveAll(paypalPaymentInfoModel,orderModel);
			return paypalPaymentInfoModel;
	 }

	 @Override
	 public PaymentTransactionModel createPaymentTransaction(String authId, AbstractOrderModel orderModel)
	 {
	 	  final PaymentTransactionModel paymentTransactionModel = getModelService().create(PaymentTransactionModel.class);
	 	  paymentTransactionModel.setCode(orderModel.getCode()+"_"+authId);
	 	  paymentTransactionModel.setOrder(orderModel);
	 	  paymentTransactionModel.setInfo(orderModel.getPaymentInfo());
	 	  paymentTransactionModel.setRequestId(authId);
	 	  paymentTransactionModel.setRequestToken(authId);
	 	  paymentTransactionModel.setPaymentProvider(TheBodyShopintegrationpaypalConstants.PAYPAL_PAYMENT);
	 	  getModelService().save(paymentTransactionModel);
			return paymentTransactionModel;
	 }

	 @Override
	 public PaymentTransactionEntryModel createAuthorizeEntry(String authId, PaymentTransactionModel paymentTransactionModel,AbstractOrderModel orderModel)
	 {
	 	  final PaymentTransactionEntryModel paymentTransactionEntryModel = getModelService().create(PaymentTransactionEntryModel.class);
	 	  final String code = paymentTransactionModel.getCode()+"_"+ (CollectionUtils.isEmpty(paymentTransactionModel.getEntries()) ? "0" : paymentTransactionModel.getEntries().size()+1);
	 	  paymentTransactionEntryModel.setCode(code);
			paymentTransactionEntryModel.setPaymentTransaction(paymentTransactionModel);
			paymentTransactionEntryModel.setRequestToken(authId);
			paymentTransactionEntryModel.setRequestId(authId);
			paymentTransactionEntryModel.setType(PaymentTransactionType.AUTHORIZATION);
			paymentTransactionEntryModel.setAmount(new BigDecimal(orderModel.getTotalPrice()+orderModel.getTotalTax()-orderModel.getAmountGiftCards()));
			paymentTransactionEntryModel.setCurrency(getCommerceCommonI18NService().getCurrentCurrency());
			paymentTransactionEntryModel.setTime(new Date());
			paymentTransactionEntryModel.setTransactionStatus(TransactionStatus.ACCEPTED.name());
			paymentTransactionModel.setEntries(Collections.singletonList(paymentTransactionEntryModel));
			getModelService().saveAll(paymentTransactionEntryModel,paymentTransactionModel);
			return paymentTransactionEntryModel;
	 }

	 protected CheckoutCustomerStrategy getCheckoutCustomerStrategy()
	 {
			return checkoutCustomerStrategy;
	 }

	 protected ModelService getModelService()
	 {
			return modelService;
	 }

	 public CommerceCommonI18NService getCommerceCommonI18NService()
	 {
			return commerceCommonI18NService;
	 }
}
