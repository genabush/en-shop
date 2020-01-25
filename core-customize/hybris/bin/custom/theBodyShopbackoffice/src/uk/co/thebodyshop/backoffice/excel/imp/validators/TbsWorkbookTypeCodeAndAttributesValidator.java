/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.backoffice.excel.imp.validators;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Lists;

import com.hybris.backoffice.excel.validators.WorkbookTypeCodeAndAttributesValidator;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;

import de.hybris.platform.catalog.model.ProductFeatureModel;

import uk.co.thebodyshop.backoffice.excel.export.helpers.ExcelHelper;

/**
 * @author Marcin
 */
public class TbsWorkbookTypeCodeAndAttributesValidator extends WorkbookTypeCodeAndAttributesValidator
{
	private ExcelHelper excelHelper;

	@Override
	protected List<ValidationMessage> validatePermissionsToTypes()
	{
		final List<ValidationMessage> messages = new ArrayList<>();

		final List<String> readWriteTypes = Lists.newArrayList(ProductFeatureModel._TYPECODE);
		readWriteTypes.forEach(type -> {

			if (hasCustomAccessForType(type) || (!getPermissionCRUDService().canReadType(type) || !getPermissionCRUDService().canChangeType(type) || !getPermissionCRUDService().canCreateTypeInstance(type)))
			{
				messages.add(new ValidationMessage(INSUFFICIENT_PERMISSIONS_TO_TYPE, type));
			}
		});

		return messages;
	}

	@Override
	protected boolean hasPermissionsToTypeCode(final String typeCode)
	{
		if (hasCustomAccessForType(typeCode))
		{
			return true;
		}
		return getPermissionCRUDService().canReadType(typeCode) && getPermissionCRUDService().canChangeType(typeCode) && getPermissionCRUDService().canCreateTypeInstance(typeCode);
	}

	private boolean hasCustomAccessForType(final String typeCode)
	{
		if (getExcelHelper().hasCustomPermissionsForType(typeCode))
		{
			return getPermissionCRUDService().canReadType(typeCode) && getPermissionCRUDService().canChangeType(typeCode);
		}
		return false;
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
	public void setExcelHelper(final ExcelHelper excelHelper)
	{
		this.excelHelper = excelHelper;
	}
}
