/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.backoffice.excel.validators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.ExcelValidator;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;

/**
 * @author Marcin
 */
public class ExcelProductConstraintsValidator implements ExcelValidator
{
	private static final String EXCEL_IMPORT_VALIDATION_MAX_CONSTRAINT_VALIDATION = "excel.import.validation.max.constraint.validation";
	private static final String EXCEL_IMPORT_VALIDATION_MIN_CONSTRAINT_VALIDATION = "excel.import.validation.min.constraint.validation";
	private static final String MAX = "max";
	private static final String MIN = "min";
	private final Map<String, Map<String, String>> constraintAttributes;

	@Override
	public ExcelValidationResult validate(final ImportParameters importParameters, final AttributeDescriptorModel attributeDescriptor, final Map<String, Object> context)
	{
		final Map<String, String> validationRules = constraintAttributes.get(attributeDescriptor.getQualifier() + importParameters.getTypeCode());
		final List<ValidationMessage> validationMessages = new ArrayList<>();
		if (MapUtils.isNotEmpty(validationRules))
		{
			validateMinSizeConstraint(importParameters, validationRules, validationMessages);
			validateMaxSizeConstraint(importParameters, validationRules, validationMessages);
		}
		return validationMessages.isEmpty() ? ExcelValidationResult.SUCCESS : new ExcelValidationResult(validationMessages);
	}

	private void validateMaxSizeConstraint(final ImportParameters importParameters, final Map<String, String> validationRules, final List<ValidationMessage> validationMessages)
	{
		if (validationRules.containsKey(MAX))
		{
			final int maxLength = Integer.valueOf(validationRules.get(MAX));
			if (importParameters.getCellValue().toString().length() > maxLength)
			{
				validationMessages.add(new ValidationMessage(EXCEL_IMPORT_VALIDATION_MAX_CONSTRAINT_VALIDATION, importParameters.getCellValue().toString()));
			}
		}
	}

	private void validateMinSizeConstraint(final ImportParameters importParameters, final Map<String, String> validationRules, final List<ValidationMessage> validationMessages)
	{
		if (validationRules.containsKey(MIN))
		{
			final int minLength = Integer.valueOf(validationRules.get(MIN));
			if (importParameters.getCellValue().toString().length() < minLength)
			{
				validationMessages.add(new ValidationMessage(EXCEL_IMPORT_VALIDATION_MIN_CONSTRAINT_VALIDATION, importParameters.getCellValue().toString()));
			}
		}
	}

	@Override
	public boolean canHandle(final ImportParameters importParameters, final AttributeDescriptorModel attributeDescriptor)
	{
		if (constraintAttributes.containsKey(attributeDescriptor.getQualifier() + importParameters.getTypeCode()))
		{
			return true;
		}
		return false;
	}

	public ExcelProductConstraintsValidator(final Map<String, Map<String, String>> constraintAttributes)
	{
		this.constraintAttributes = constraintAttributes;
	}

	/**
	 * @return the constraintAttributes
	 */
	protected Map<String, Map<String, String>> getConstraintAttributes()
	{
		return constraintAttributes;
	}
}
