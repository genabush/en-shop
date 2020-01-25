/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package uk.co.thebodyshop.integration.adyen.factory;

import static com.adyen.v6.constants.Adyenv6coreConstants.KLARNA;
import static com.adyen.v6.constants.Adyenv6coreConstants.PAYMENT_METHOD_BOLETO;
import static com.adyen.v6.constants.Adyenv6coreConstants.PAYMENT_METHOD_BOLETO_SANTANDER;
import static com.adyen.v6.constants.Adyenv6coreConstants.PAYMENT_METHOD_CC;
import static com.adyen.v6.constants.Adyenv6coreConstants.PAYMENT_METHOD_FACILPAY_PREFIX;
import static com.adyen.v6.constants.Adyenv6coreConstants.PAYMENT_METHOD_IDEAL;
import static com.adyen.v6.constants.Adyenv6coreConstants.PAYMENT_METHOD_ONECLICK;
import static uk.co.thebodyshop.integration.adyen.constants.TheBodyShopintegrationadyenConstants.PAYMENT_METHOD_GIROPAY;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.util.TaxValue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.adyen.Util.Util;
import com.adyen.enums.VatCategory;
import com.adyen.model.Address;
import com.adyen.model.Amount;
import com.adyen.model.Installments;
import com.adyen.model.Name;
import com.adyen.model.additionalData.InvoiceLine;
import com.adyen.model.applicationinfo.ApplicationInfo;
import com.adyen.model.checkout.DefaultPaymentMethodDetails;
import com.adyen.model.checkout.PaymentMethodDetails;
import com.adyen.model.checkout.PaymentsDetailsRequest;
import com.adyen.model.checkout.PaymentsRequest;
import com.adyen.model.modification.CaptureRequest;
import com.adyen.model.recurring.Recurring;
import com.adyen.v6.enums.AdyenCardTypeEnum;
import com.adyen.v6.enums.RecurringContractMode;
import com.adyen.v6.factory.AdyenRequestFactory;
import com.adyen.v6.model.RequestInfo;
import com.adyen.v6.repository.OrderRepository;

import uk.co.thebodyshop.integration.adyen.model.checkout.TbsPaymentMethodDetails;


public class TbsAdyenRequestFactory extends AdyenRequestFactory
{
	private static final Logger LOG = Logger.getLogger(TbsAdyenRequestFactory.class);

	private ConfigurationService configurationService;

	private static final String PLATFORM_NAME = "Hybris";
	private static final String PLATFORM_VERSION_PROPERTY = "build.version.api";
	private static final String IS_3DS2_ALLOWED_PROPERTY = "is3DS2allowed";
	private static final String ALLOW_3DS2_PROPERTY = "allow3DS2";

	private OrderRepository orderRepository;


	@Override
	public PaymentsDetailsRequest create3DPaymentsRequest(final String paymentData, final String md, final String paRes)
	{

		final PaymentsDetailsRequest paymentsDetailsRequest = new PaymentsDetailsRequest();
		paymentsDetailsRequest.set3DRequestData(md, paRes, paymentData);
		return paymentsDetailsRequest;
	}

	@Override
	public PaymentsDetailsRequest create3DS2PaymentsRequest(final String paymentData, final String token, final String type)
	{

		final PaymentsDetailsRequest paymentsDetailsRequest = new PaymentsDetailsRequest();
		if (type.equals("fingerprint"))
		{
			paymentsDetailsRequest.setFingerPrint(token, paymentData);

		}
		else if (type.equals("challenge"))
		{
			paymentsDetailsRequest.setChallengeResult(token, paymentData);
		}
		return paymentsDetailsRequest;
	}

	@Override
	public PaymentsRequest createPaymentsRequest(final String merchantAccount, final CartData cartData,
			final RequestInfo requestInfo, final CustomerModel customerModel, final RecurringContractMode recurringContractMode)
	{
		PaymentsRequest paymentsRequest = new PaymentsRequest();
		paymentsRequest.setReturnUrl(cartData.getAdyenReturnUrl());
		final String adyenPaymentMethod = cartData.getAdyenPaymentMethod();

		if (adyenPaymentMethod == null)
		{
			throw new IllegalArgumentException("Payment method is null");
		}
		//Update payment request for generic information for all payment method types

		updatePaymentRequest(merchantAccount, cartData, requestInfo, customerModel, paymentsRequest);
		final Boolean is3DS2allowed = is3DS2Allowed();

		//For credit cards
		if (PAYMENT_METHOD_CC.equals(adyenPaymentMethod))
		{
			updatePaymentRequestForCC(paymentsRequest, cartData, recurringContractMode);
			if (is3DS2allowed)
			{
				paymentsRequest = enhanceForThreeDS2(paymentsRequest, cartData);
			}
		}
		//For one click
		else if (adyenPaymentMethod.indexOf(PAYMENT_METHOD_ONECLICK) == 0)
		{
			final String selectedReference = cartData.getAdyenSelectedReference();
			if (selectedReference != null && !selectedReference.isEmpty())
			{
				paymentsRequest.addOneClickData(selectedReference, cartData.getAdyenEncryptedSecurityCode());
				final String cardBrand = cartData.getAdyenCardBrand();
				if (cardBrand != null && cardBrand.equals(AdyenCardTypeEnum.BCMC.getCode()))
				{
					final DefaultPaymentMethodDetails paymentMethodDetails = (DefaultPaymentMethodDetails) (paymentsRequest
							.getPaymentMethod());
					paymentMethodDetails.setType(cardBrand);
					paymentsRequest.setPaymentMethod(paymentMethodDetails);
				}
			}
			if (is3DS2allowed)
			{
				paymentsRequest = enhanceForThreeDS2(paymentsRequest, cartData);
			}
		}
		//Set Boleto parameters
		else if (cartData.getAdyenPaymentMethod().indexOf(PAYMENT_METHOD_BOLETO) == 0)
		{
			setBoletoData(paymentsRequest, cartData);
		}
		//For alternate payment methods like iDeal, Paypal etc.
		else
		{
			updatePaymentRequestForAlternateMethod(paymentsRequest, cartData, customerModel);
		}

		final ApplicationInfo applicationInfo = updateApplicationInfo(paymentsRequest.getApplicationInfo());
		paymentsRequest.setApplicationInfo(applicationInfo);
		return paymentsRequest;
	}

	private void updatePaymentRequestForCC(final PaymentsRequest paymentsRequest, final CartData cartData,
			final RecurringContractMode recurringContractMode)
	{
		final Recurring recurringContract = getRecurringContractType(recurringContractMode);
		Recurring.ContractEnum contractEnum = null;
		if (recurringContract != null)
		{
			contractEnum = recurringContract.getContract();
		}

		paymentsRequest.setEnableRecurring(false);
		paymentsRequest.setEnableOneClick(false);

		final String encryptedCardNumber = cartData.getAdyenEncryptedCardNumber();
		final String encryptedExpiryMonth = cartData.getAdyenEncryptedExpiryMonth();
		final String encryptedExpiryYear = cartData.getAdyenEncryptedExpiryYear();
		if (cartData.getAdyenInstallments() != null)
		{
			final Installments installmentObj = new Installments();
			installmentObj.setValue(cartData.getAdyenInstallments());
			paymentsRequest.setInstallments(installmentObj);
		}

		if (!StringUtils.isEmpty(encryptedCardNumber) && !StringUtils.isEmpty(encryptedExpiryMonth)
				&& !StringUtils.isEmpty(encryptedExpiryYear))
		{

			paymentsRequest.addEncryptedCardData(encryptedCardNumber, encryptedExpiryMonth, encryptedExpiryYear,
					cartData.getAdyenEncryptedSecurityCode(), cartData.getAdyenCardHolder());
		}
		if (Recurring.ContractEnum.ONECLICK_RECURRING == contractEnum)
		{
			paymentsRequest.setEnableRecurring(true);
			paymentsRequest.setEnableOneClick(true);
		}
		else if (Recurring.ContractEnum.ONECLICK == contractEnum)
		{
			paymentsRequest.setEnableOneClick(true);
		}
		else if (Recurring.ContractEnum.RECURRING == contractEnum)
		{
			paymentsRequest.setEnableRecurring(true);
		}

		// Set storeDetails parameter when shopper selected to have his card details stored
		if (cartData.getAdyenRememberTheseDetails())
		{
			final DefaultPaymentMethodDetails paymentMethodDetails = (DefaultPaymentMethodDetails) paymentsRequest
					.getPaymentMethod();
			paymentMethodDetails.setStoreDetails(true);
		}
	}

	private void updatePaymentRequest(final String merchantAccount, final CartData cartData, final RequestInfo requestInfo,
			final CustomerModel customerModel, final PaymentsRequest paymentsRequest)
	{

		//Get details from CartData to set in PaymentRequest.
		final String amount = String.valueOf(cartData.getOutstandingAmount().getValue());
		final String currency = cartData.getTotalPrice().getCurrencyIso();
		final String reference = cartData.getCode();

		final AddressData billingAddress = cartData.getPaymentInfo().getBillingAddress();
		final AddressData deliveryAddress = cartData.getDeliveryAddress();

		//Get details from HttpServletRequest to set in PaymentRequest.
		final String userAgent = requestInfo.getUserAgent();
		final String acceptHeader = requestInfo.getAcceptHeader();
		final String shopperIP = requestInfo.getShopperIp();
		final String origin = requestInfo.getOrigin();
		final String shopperLocale = requestInfo.getShopperLocale();

		paymentsRequest.setAmountData(amount, currency).reference(reference).merchantAccount(merchantAccount)
				.addBrowserInfoData(userAgent, acceptHeader).shopperIP(shopperIP).origin(origin).shopperLocale(shopperLocale)
				.setCountryCode(getCountryCode(cartData));

		// set shopper details from CustomerModel.
		if (customerModel != null)
		{
			paymentsRequest.setShopperReference(customerModel.getCustomerID());
			paymentsRequest.setShopperEmail(customerModel.getContactEmail());
		}

		// if address details are provided, set it to the PaymentRequest
		if (deliveryAddress != null)
		{
			paymentsRequest.setDeliveryAddress(setAddressData(deliveryAddress));
		}

		if (billingAddress != null)
		{
			paymentsRequest.setBillingAddress(setAddressData(billingAddress));
			// set PhoneNumber if it is provided
			final String phone = billingAddress.getPhone();
			if (phone != null && !phone.isEmpty())
			{
				paymentsRequest.setTelephoneNumber(phone);
			}
		}
	}

	private void updatePaymentRequestForAlternateMethod(final PaymentsRequest paymentsRequest, final CartData cartData,
			final CustomerModel customerModel)
	{
		final String adyenPaymentMethod = cartData.getAdyenPaymentMethod();
		final PaymentMethodDetails paymentMethod = getPaymentMethodDetailsForPaymentMethod(adyenPaymentMethod);
		paymentsRequest.setPaymentMethod(paymentMethod);
		paymentMethod.setType(adyenPaymentMethod);
		if (adyenPaymentMethod.equals(PAYMENT_METHOD_IDEAL))
		{
			if (paymentMethod instanceof DefaultPaymentMethodDetails)
			{
				((DefaultPaymentMethodDetails) paymentMethod).setIdealIssuer(cartData.getAdyenIssuerId());
			}
		}
		else if (adyenPaymentMethod.equals(PAYMENT_METHOD_GIROPAY))
		{
			if (paymentMethod instanceof TbsPaymentMethodDetails)
			{
				((TbsPaymentMethodDetails) paymentMethod).setBic(cartData.getAdyenIssuerId());
			}
		}
		else if (adyenPaymentMethod.startsWith(KLARNA) || adyenPaymentMethod.startsWith(PAYMENT_METHOD_FACILPAY_PREFIX))
		{
			setOpenInvoiceData(paymentsRequest, cartData, customerModel);
		}
	}

	/**
	 * Set Boleto payment request data
	 */
	private void setBoletoData(final PaymentsRequest paymentsRequest, final CartData cartData)
	{
		DefaultPaymentMethodDetails paymentMethodDetails = (DefaultPaymentMethodDetails) (paymentsRequest.getPaymentMethod());
		if (paymentMethodDetails == null)
		{
			paymentMethodDetails = new DefaultPaymentMethodDetails();
		}
		paymentMethodDetails.setType(PAYMENT_METHOD_BOLETO_SANTANDER);
		paymentsRequest.setPaymentMethod(paymentMethodDetails);
		paymentsRequest.setSocialSecurityNumber(cartData.getAdyenSocialSecurityNumber());

		final Name shopperName = new Name();
		shopperName.setFirstName(cartData.getAdyenFirstName());
		shopperName.setLastName(cartData.getAdyenLastName());
		paymentsRequest.setShopperName(shopperName);

		if (paymentsRequest.getBillingAddress() != null)
		{
			final String stateOrProvinceBilling = paymentsRequest.getBillingAddress().getStateOrProvince();
			if (!StringUtils.isEmpty(stateOrProvinceBilling) && stateOrProvinceBilling.length() > 2)
			{
				final String shortStateOrProvince = stateOrProvinceBilling.substring(stateOrProvinceBilling.length() - 2);
				paymentsRequest.getBillingAddress().setStateOrProvince(shortStateOrProvince);
			}
		}
		if (paymentsRequest.getDeliveryAddress() != null)
		{
			final String stateOrProvinceDelivery = paymentsRequest.getDeliveryAddress().getStateOrProvince();
			if (!StringUtils.isEmpty(stateOrProvinceDelivery) && stateOrProvinceDelivery.length() > 2)
			{
				final String shortStateOrProvince = stateOrProvinceDelivery.substring(stateOrProvinceDelivery.length() - 2);
				paymentsRequest.getDeliveryAddress().setStateOrProvince(shortStateOrProvince);
			}
		}
	}

	private PaymentMethodDetails getPaymentMethodDetailsForPaymentMethod(final String adyenPaymentMethod)
	{
		PaymentMethodDetails paymentMethodDetails = null;
		if (adyenPaymentMethod.equals(PAYMENT_METHOD_GIROPAY))
		{
			paymentMethodDetails = new TbsPaymentMethodDetails();
		}
		else
		{
			paymentMethodDetails = new DefaultPaymentMethodDetails();
		}
		return paymentMethodDetails;
	}

	private Boolean is3DS2Allowed()
	{

		final Configuration configuration = getConfigurationService().getConfiguration();
		boolean is3DS2AllowedValue = false;
		if (configuration.containsKey(IS_3DS2_ALLOWED_PROPERTY))
		{
			is3DS2AllowedValue = configuration.getBoolean(IS_3DS2_ALLOWED_PROPERTY);
		}
		return is3DS2AllowedValue;
	}

	private Recurring getRecurringContractType(final RecurringContractMode recurringContractMode)
	{
		final Recurring recurringContract = new Recurring();

		//If recurring contract is disabled, return null
		if (recurringContractMode == null || RecurringContractMode.NONE.equals(recurringContractMode))
		{
			return null;
		}

		final String recurringMode = recurringContractMode.getCode();
		final Recurring.ContractEnum contractEnum = Recurring.ContractEnum.valueOf(recurringMode);

		recurringContract.contract(contractEnum);

		return recurringContract;
	}

	private String getCountryCode(final CartData cartData)
	{
		//Identify country code based on shopper's delivery address
		String countryCode = "";
		final AddressData billingAddressData = cartData.getPaymentInfo().getBillingAddress();
		if (billingAddressData != null)
		{
			final CountryData billingCountry = billingAddressData.getCountry();
			if (billingCountry != null)
			{
				countryCode = billingCountry.getIsocode();
			}
		}
		else
		{
			final AddressData deliveryAddressData = cartData.getDeliveryAddress();
			if (deliveryAddressData != null)
			{
				final CountryData deliveryCountry = deliveryAddressData.getCountry();
				if (deliveryCountry != null)
				{
					countryCode = deliveryCountry.getIsocode();
				}
			}
		}
		return countryCode;
	}

	/**
	 * Set Address Data into API
	 */
	private Address setAddressData(final AddressData addressData)
	{

		final Address address = new Address();

		// set defaults because all fields are required into the API
		address.setCity("NA");
		address.setCountry("NA");
		address.setHouseNumberOrName("NA");
		address.setPostalCode("NA");
		address.setStateOrProvince("NA");
		address.setStreet("NA");

		// set the actual values if they are available
		if (addressData.getTown() != null && !addressData.getTown().isEmpty())
		{
			address.setCity(addressData.getTown());
		}

		if (addressData.getCountry() != null && !addressData.getCountry().getIsocode().isEmpty())
		{
			address.setCountry(addressData.getCountry().getIsocode());
		}

		if (addressData.getLine1() != null && !addressData.getLine1().isEmpty())
		{
			address.setStreet(addressData.getLine1());
		}

		if (addressData.getLine2() != null && !addressData.getLine2().isEmpty())
		{
			address.setHouseNumberOrName(addressData.getLine2());
		}

		if (addressData.getPostalCode() != null && !address.getPostalCode().isEmpty())
		{
			address.setPostalCode(addressData.getPostalCode());
		}

		if (addressData.getRegion() != null && !addressData.getRegion().getIsocode().isEmpty())
		{
			//State value will be updated later for boleto in boleto specific method.
			address.setStateOrProvince(addressData.getRegion().getIsocode());
		}

		return address;
	}

	@Override
	public CaptureRequest createCaptureRequest(final String merchantAccount, final BigDecimal amount, final Currency currency,
			final String authReference, final String merchantReference)
	{
		final CaptureRequest request = super.createCaptureRequest(merchantAccount, amount, currency, authReference,
				merchantReference);
		updateKlarnaLineItems(request, merchantReference);
		return request;
	}

	private void updateKlarnaLineItems(final CaptureRequest request, final String merchantReference)
	{
		final OrderModel order = getOrderRepository().getOrderModel(merchantReference);
		final PaymentInfoModel paymentInfo = order.getPaymentInfo();

		if (null != paymentInfo && paymentInfo.getAdyenPaymentMethod().startsWith("klarna"))
		{
			final List<InvoiceLine> invoiceLines = new ArrayList<>();
			final String currency = order.getCurrency().getIsocode();
			if (isInvoiceDataRequired(order.getConsignments()))
			{
				for (final ConsignmentModel consignment : order.getConsignments())
				{
					for (final ConsignmentEntryModel consignmentEntry : consignment.getConsignmentEntries())
					{
						if (consignmentEntry.getQuantity() == 0L)
						{
							continue;
						}
						final AbstractOrderEntryModel entry = consignmentEntry.getOrderEntry();
						setupOrderEntryInvoiceLine(invoiceLines, currency, consignmentEntry, entry);
					}
				}
				// Add delivery costs
				if (order.getDeliveryCost() != null)
				{
					setupAdditionalInvoiceLine(order, invoiceLines, currency, "Delivery Costs", order.getDeliveryCost().toString());
				}
				// Add gift cards value
				if (null != order.getAmountGiftCards()
						&& BigDecimal.valueOf(order.getAmountGiftCards()).compareTo(BigDecimal.ZERO) != 0)
				{
					setupAdditionalInvoiceLine(order, invoiceLines, currency, "Gift Card",
							"-" + order.getAmountGiftCards().toString());
				}
				request.setInvoiceLines(invoiceLines);
			}
		}
	}

	private boolean isInvoiceDataRequired(final Set<ConsignmentModel> consignments)
	{
		return !consignments.stream()
				.allMatch(consignment -> consignment.getStatus().equals(ConsignmentStatus.SHIPPED)
						|| consignment.getStatus().equals(ConsignmentStatus.PICKUP_COMPLETE));
	}

	private void setupOrderEntryInvoiceLine(final List<InvoiceLine> invoiceLines, final String currency,
			final ConsignmentEntryModel consignmentEntry, final AbstractOrderEntryModel entry)
	{
		if (null != entry)
		{
			final BigDecimal pricePerItem = BigDecimal.valueOf(entry.getTotalPrice())
					.divide(BigDecimal.valueOf(entry.getQuantity()));
			String description = "NA";
			if (null != entry.getProduct() && null != entry.getProduct().getName()
					&& StringUtils.isNotEmpty(entry.getProduct().getName()))
			{
				description = entry.getProduct().getName();
			}
			// Tax of total price (included quantity)
			Double tax = entry.getTaxValues().stream().map(TaxValue::getAppliedValue).reduce(0.0, (x, y) -> x + y);
			// Calculate Tax per quantity
			if (tax > 0)
			{
				tax = tax / entry.getQuantity().intValue();
			}
			// Calculate price without tax
			final Amount itemAmountWithoutTax = Util.createAmount(pricePerItem.subtract(new BigDecimal(tax)), currency);
			final Double percentage = entry.getTaxValues().stream().map(TaxValue::getValue).reduce(0.0, (x, y) -> x + y) * 100;
			final InvoiceLine invoiceLine = new InvoiceLine();
			invoiceLine.setDescription(description);
			invoiceLine.setCurrencyCode(currency);
			//The price for one item in the invoice line, represented in minor units. The due amount for the item, VAT excluded.
			invoiceLine.setItemAmount(itemAmountWithoutTax.getValue());
			// The VAT due for one item in the invoice line, represented in minor units.
			invoiceLine.setItemVATAmount(Util.createAmount(BigDecimal.valueOf(tax), currency).getValue());
			// The VAT percentage for one item in the invoice line, represented in minor units.
			invoiceLine.setItemVatPercentage(percentage.longValue());
			// The country-specific VAT category a product falls under.  Allowed values: (High,Low,None)
			invoiceLine.setVatCategory(VatCategory.NONE);
			invoiceLine.setNumberOfItems(consignmentEntry.getShippedQuantity().intValue());
			if (entry.getProduct() != null && !entry.getProduct().getCode().isEmpty())
			{
				invoiceLine.setItemId(entry.getProduct().getCode());
			}
			LOG.debug("InvoiceLine Product:" + invoiceLine.toString());
			invoiceLines.add(invoiceLine);
		}
	}

	private void setupAdditionalInvoiceLine(final OrderModel order, final List<InvoiceLine> invoiceLines, final String currency,
			final String itemDescription, final String itemAmount)
	{
		final InvoiceLine invoiceLine = new InvoiceLine();
		invoiceLine.setDescription(itemDescription);
		invoiceLine.setCurrencyCode(currency);
		final Amount itemAmountValue = Util.createAmount(itemAmount, currency);
		invoiceLine.setItemAmount(itemAmountValue.getValue());
		invoiceLine.setItemVATAmount(new Long("0"));
		invoiceLine.setItemVatPercentage(new Long("0"));
		invoiceLine.setVatCategory(VatCategory.NONE);
		invoiceLine.setNumberOfItems(1);
		invoiceLines.add(invoiceLine);
	}

	/**
	 * @return the configurationService
	 */
	@Override
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	@Override
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	/**
	 * @return the orderRepository
	 */
	public OrderRepository getOrderRepository()
	{
		return orderRepository;
	}

	/**
	 * @param orderRepository
	 *           the orderRepository to set
	 */
	public void setOrderRepository(final OrderRepository orderRepository)
	{
		this.orderRepository = orderRepository;
	}
}