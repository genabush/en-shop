/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.backoffice.excel.imp.validators;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.hybris.backoffice.excel.data.SelectedAttribute;
import com.hybris.backoffice.excel.validators.WorkbookMandatoryColumnsValidator;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;

import uk.co.thebodyshop.backoffice.excel.export.helpers.ExcelHelper;

/**
 * @author Marcin
 */
public class TbsWorkbookMandatoryColumnsValidator extends WorkbookMandatoryColumnsValidator
{
	private UserService userService;

	private ExcelHelper excelHelper;

	@Override
	public List<ExcelValidationResult> validate(final Workbook workbook)
	{
		final List<ExcelValidationResult> validationResults = new ArrayList<>();
		final Sheet typeSystemSheet = getExcelWorkbookService().getMetaInformationSheet(workbook);
		getExcelSheetService().getSheets(workbook).stream().map(sheet -> this.validateSheet(typeSystemSheet, sheet)).filter(Optional::isPresent).map(Optional::get).forEach(validationResults::add);
		return validationResults;
	}

	@Override
	protected Optional<ExcelValidationResult> validateSheet(final Sheet typeSystemSheet, final Sheet sheet)
	{
		final List<ValidationMessage> messages = new ArrayList<>();
		final String typeCode = getExcelSheetService().findTypeCodeForSheetName(sheet.getWorkbook(), sheet.getSheetName());
		final Set<AttributeDescriptorModel> mandatoryFields = findAllMandatoryFields(typeCode);
		List<String> excludedAttributes = new ArrayList<>();
		final UserModel userModel = getUserService().getCurrentUser();
		final boolean isFullExportEnabled = getExcelHelper().getFullExportFlagForCurrentUser(userModel);
		if (!isFullExportEnabled)
		{
			excludedAttributes = getExcelHelper().getExcAttributeForCurrentUser(userModel, excludedAttributes, typeCode);
		}

		for (final AttributeDescriptorModel mandatoryField : mandatoryFields)
		{
			if (getExcelHelper().isAllowedAttribute(mandatoryField, excludedAttributes))
			{
				final SelectedAttribute selectedAttribute = prepareSelectedAttribute(mandatoryField);
				final int columnIndex = getExcelSheetService().findColumnIndex(typeSystemSheet, sheet, prepareExcelAttribute(selectedAttribute.getAttributeDescriptor(), selectedAttribute.getIsoCode()));
				if (columnIndex == -1)
				{
					messages.add(new ValidationMessage(VALIDATION_MESSAGE_DESCRIPTION, getAttributeDisplayedName(mandatoryField), sheet.getSheetName()));
				}
			}
		}

		if (messages.isEmpty())
		{
			return Optional.empty();
		}

		final ValidationMessage header = new ValidationMessage(VALIDATION_MESSAGE_HEADER, sheet.getSheetName());
		return Optional.of(new ExcelValidationResult(header, messages));
	}

	/**
	 * @return the excelHelper
	 */
	public ExcelHelper getExcelHelper()
	{
		return excelHelper;
	}

	/**
	 * @param excelHelper
	 *          the excelHelper to set
	 */
	public void setExcelHelper(ExcelHelper excelHelper)
	{
		this.excelHelper = excelHelper;
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *          the userService to set
	 */
	public void setUserService(UserService userService)
	{
		this.userService = userService;
	}

}
