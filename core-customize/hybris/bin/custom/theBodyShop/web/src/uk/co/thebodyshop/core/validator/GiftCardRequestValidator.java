/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.validator;


import org.apache.solr.common.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import uk.co.thebodyshop.integrations.svs.dto.GiftCardRequestWsDTO;

/**
 * @author Marcin
 */
public class GiftCardRequestValidator implements Validator
{
	private static final String ONLY_DIGITS_REGEX = "\\d*";
	private static final String GIFT_CARD_PIN = "giftCardPin";
	private static final String GIFT_CARD_NUMBER = "giftCardNumber";
	private static final String FIELD_REQUIRED_MESSAGE_ID = "field.required";
	private static final String GIFT_CARD_FIELD_LENGTH_INVALID = "gift.card.field.length.invalid";
	private static final String GIFT_CARD_NUMBER_NUMERIC = "gift.card.number.numeric";
	private static final int GIFT_CARD_NUMBER_LENGHT = 19;
	private static final int GIFT_CARD_PIN_LENGHT = 4;

	@Override
	public boolean supports(final Class clazz)
	{
		return GiftCardRequestWsDTO.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors)
	{
		final GiftCardRequestWsDTO giftCardRequest = (GiftCardRequestWsDTO) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, GIFT_CARD_NUMBER, FIELD_REQUIRED_MESSAGE_ID);
		validateFieldLength(GIFT_CARD_NUMBER, giftCardRequest.getGiftCardNumber(), GIFT_CARD_NUMBER_LENGHT, errors);
		validateIsFieldNumeric(GIFT_CARD_NUMBER, giftCardRequest.getGiftCardNumber(), errors);
		if (giftCardRequest.isPinRequired())
		{
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, GIFT_CARD_PIN, FIELD_REQUIRED_MESSAGE_ID);
			validateFieldLength(GIFT_CARD_PIN, giftCardRequest.getGiftCardPin(), GIFT_CARD_PIN_LENGHT, errors);
		}

	}

	private void validateFieldLength(final String field, final String fieldValue, final int expectedSize, final Errors errors)
	{
		if (!StringUtils.isEmpty(fieldValue) && fieldValue.length() != expectedSize)
		{
			errors.rejectValue(field, GIFT_CARD_FIELD_LENGTH_INVALID, new String[]
					{ String.valueOf(expectedSize) }, null);
		}
	}

	private void validateIsFieldNumeric(final String field, final String fieldValue, final Errors errors)
	{
		if (!fieldValue.matches(ONLY_DIGITS_REGEX))
		{
			errors.rejectValue(field, GIFT_CARD_NUMBER_NUMERIC);
		}
	}
}
