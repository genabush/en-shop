/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package uk.co.thebodyshop.core.validator;

import org.apache.solr.common.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.adyen.v6.constants.Adyenv6coreConstants;

import de.hybris.platform.commercewebservicescommons.dto.order.PaymentDetailsWsDTO;

import uk.co.thebodyshop.integration.adyen.constants.TheBodyShopintegrationadyenConstants;

/**
 * Validates instances of {@link PaymentDetailsWsDTO}.
 *
 */
public class PaymentDetailsDTOValidator implements Validator
{
	private static final String FIELD_REQUIRED_MESSAGE_ID = "field.required";
	private Validator paymentAddressValidator;

	@Override
	public boolean supports(final Class clazz)
	{
		return PaymentDetailsWsDTO.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors)
	{
		final PaymentDetailsWsDTO paymentDetails = (PaymentDetailsWsDTO) target;

		// if (StringUtils.isNotBlank(paymentDetails.getStartMonth()) && StringUtils.isNotBlank(paymentDetails.getStartYear())
		// && StringUtils.isNotBlank(paymentDetails.getExpiryMonth()) && StringUtils.isNotBlank(paymentDetails.getExpiryYear()))
		// {
		// final Calendar start = Calendar.getInstance();
		// start.set(Calendar.DAY_OF_MONTH, 0);
		// start.set(Calendar.MONTH, Integer.parseInt(paymentDetails.getStartMonth()) - 1);
		// start.set(Calendar.YEAR, Integer.parseInt(paymentDetails.getStartYear()) - 1);
		//
		// final Calendar expiration = Calendar.getInstance();
		// expiration.set(Calendar.DAY_OF_MONTH, 0);
		// expiration.set(Calendar.MONTH, Integer.parseInt(paymentDetails.getExpiryMonth()) - 1);
		// expiration.set(Calendar.YEAR, Integer.parseInt(paymentDetails.getExpiryYear()) - 1);
		//
		// if (start.after(expiration))
		// {
		// errors.rejectValue("startMonth", "payment.startDate.invalid");
		// }
		// }
		if (paymentDetails.isCardPaymentRequired())
		{
			if (!StringUtils.isEmpty(paymentDetails.getAdyenPaymentMethod()))
			{
				if (Adyenv6coreConstants.PAYMENT_METHOD_ONECLICK.equals(paymentDetails.getAdyenPaymentMethod()))
				{
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "encryptedSecurityCode", FIELD_REQUIRED_MESSAGE_ID);
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "adyenSelectedReference", FIELD_REQUIRED_MESSAGE_ID);
				}
				else
				{
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "accountHolderName", FIELD_REQUIRED_MESSAGE_ID);
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "encryptedSecurityCode", FIELD_REQUIRED_MESSAGE_ID);
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "encryptedCardNumber", FIELD_REQUIRED_MESSAGE_ID);
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "encryptedExpiryMonth", FIELD_REQUIRED_MESSAGE_ID);
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "encryptedExpiryYear", FIELD_REQUIRED_MESSAGE_ID);
				}
			}
			else
			{
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "adyenPaymentMethod", FIELD_REQUIRED_MESSAGE_ID);
			}
		}
		else
		{
			if (!StringUtils.isEmpty(paymentDetails.getAdyenPaymentMethod()))
			{
				if (Adyenv6coreConstants.PAYMENT_METHOD_IDEAL.equals(paymentDetails.getAdyenPaymentMethod()) || TheBodyShopintegrationadyenConstants.PAYMENT_METHOD_GIROPAY.equals(paymentDetails.getAdyenPaymentMethod()))
				{
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "issueNumber", FIELD_REQUIRED_MESSAGE_ID);
				}
			}
		}

		if (isBillingAddressValidationRequired(paymentDetails))
		{
			paymentAddressValidator.validate(paymentDetails, errors);
		}
	}

	private boolean isBillingAddressValidationRequired(final PaymentDetailsWsDTO paymentDetails)
	{
		if (paymentDetails.isCardPaymentRequired())
		{
			if (!StringUtils.isEmpty(paymentDetails.getAdyenPaymentMethod()))
			{
				if (Adyenv6coreConstants.PAYMENT_METHOD_ONECLICK.equals(paymentDetails.getAdyenPaymentMethod()))
				{
					return false;
				}
			}
		}
		return true;
	}

	public Validator getPaymentAddressValidator()
	{
		return paymentAddressValidator;
	}

	@Required
	public void setPaymentAddressValidator(final Validator paymentAddressValidator)
	{
		this.paymentAddressValidator = paymentAddressValidator;
	}
}
