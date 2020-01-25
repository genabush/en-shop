/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.validator;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import uk.co.thebodyshop.core.constants.TheBodyShopCoreConstants;

/**
 * @author prateek.goel
 */
public class POBoxAddressValidator implements Validator
{

	private String fieldPath;

	private BaseStoreService baseStoreService;

	private ConfigurationService configurationService;

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return true;
	}

	@Override
	public void validate(final Object target, final Errors errors)
	{
		Assert.notNull(errors, "Errors object must not be null");
		final String fieldValue = (String) errors.getFieldValue(getFieldPath());
		if (StringUtils.isNotBlank(fieldValue) && hasPoxBoxAddress(fieldValue))
		{
			errors.rejectValue(getFieldPath(), "po.box.ship.error", null, "We cannot ship to P.O. Boxes");
		}
	}

	public boolean hasPoxBoxAddress(String address)
	{
		if (StringUtils.isNotEmpty(address))
		{
			address = address.trim();
			final BaseStoreModel baseStore = getBaseStoreService().getCurrentBaseStore();

			final boolean poBoxValidationEnabled = null != baseStore && BooleanUtils.isTrue(baseStore.getPoBoxToggle());
			if (poBoxValidationEnabled)
			{
				final String restrictedWords = getConfigurationService().getConfiguration().getString(TheBodyShopCoreConstants.PO_VALIDATION_TEXT + baseStore.getUid(), "");
				if (StringUtils.isNotEmpty(restrictedWords))
				{
					final String[] poBoxList = restrictedWords.split(",");
					return hasRestrictedText(address, poBoxList);

				}

			}

		}
		return false;
	}

	private String getAlphabetic(String address)
	{
		final StringBuffer onlyAlphabetic = new StringBuffer();
		address = address.trim();

		if (StringUtils.isNotEmpty(address))
		{
			for (final char c : address.toCharArray())
			{
				if (Character.isLetter(c))
				{
					onlyAlphabetic.append(c);
				}
			}

		}

		return onlyAlphabetic.toString();
	}

	private boolean hasRestrictedText(String lineAddress, String[] restrictedWords)
	{
		final String alphabeticUserAddress = getAlphabetic(lineAddress);

		if (StringUtils.isNotEmpty(alphabeticUserAddress.toString()))
		{
			for (String restrictedWord : restrictedWords)
			{
				restrictedWord = getAlphabetic(restrictedWord);
				if (StringUtils.containsIgnoreCase(alphabeticUserAddress, restrictedWord))
				{
					return true;
				}
			}
		}

		return false;
	}

	protected String getFieldPath()
	{
		return fieldPath;
	}

	public void setFieldPath(String fieldPath)
	{
		this.fieldPath = fieldPath;
	}

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	public void setBaseStoreService(BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	public void setConfigurationService(ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

}
