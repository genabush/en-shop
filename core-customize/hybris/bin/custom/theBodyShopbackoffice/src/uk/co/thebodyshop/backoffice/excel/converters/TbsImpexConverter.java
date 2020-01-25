/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.backoffice.excel.converters;

import com.hybris.backoffice.excel.data.ImpexForType;
import com.hybris.backoffice.excel.importing.DefaultImpexConverter;

import uk.co.thebodyshop.backoffice.excel.export.helpers.ExcelHelper;

/**
 * @author Marcin
 */
public class TbsImpexConverter extends DefaultImpexConverter
{
	public static final String IMPEX_UPDATE_OPERATION_TYPE = "UPDATE ";

	private final ExcelHelper excelHelper;

	@Override
	protected String prepareImpexHeader(final ImpexForType impexForType)
	{
		final StringBuilder sb = new StringBuilder();
		sb.append(getImpexOperationTypeForCurrentUser(impexForType.getTypeCode())).append(impexForType.getTypeCode()).append(DEFAULT_FIELD_SEPARATOR);
		impexForType.getImpexTable().columnKeySet().forEach(attr -> sb.append(prepareHeaderAttribute(attr)).append(DEFAULT_FIELD_SEPARATOR));
		sb.append(DEFAULT_LINE_SEPARATOR);
		return sb.toString();
	}

	private String getImpexOperationTypeForCurrentUser(final String typeCode)
	{
		if (getExcelHelper().hasCustomPermissionsForType(typeCode)) {
			return IMPEX_UPDATE_OPERATION_TYPE;
		} else {
			return IMPEX_OPERATION_TYPE;
		}
	}

	public TbsImpexConverter(final ExcelHelper excelHelper)
	{
		this.excelHelper = excelHelper;
	}

	/**
	 * @return the excelHelper
	 */
	protected ExcelHelper getExcelHelper()
	{
		return excelHelper;
	}
}
