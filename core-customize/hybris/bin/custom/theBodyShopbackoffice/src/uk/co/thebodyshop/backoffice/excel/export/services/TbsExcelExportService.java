/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.backoffice.excel.export.services;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.hybris.backoffice.excel.data.SelectedAttribute;
import com.hybris.backoffice.excel.exporting.DefaultExcelExportService;

import de.hybris.platform.core.model.ItemModel;

import uk.co.thebodyshop.backoffice.excel.export.helpers.ExcelHelper;

/**
 * @author Marcin
 */
public class TbsExcelExportService extends DefaultExcelExportService
{
	private static final String TBS_VARIANT_TYPE_CODE = "TbsVariantProduct";

	private ExcelHelper excelHelper;

	@Override
	protected Workbook exportData(final Map<String, Set<ItemModel>> itemsByType, final List<SelectedAttribute> selectedAttributes)
	{
		final Map<String, Set<ItemModel>> itemsByTypeFiltered = applyTypePredicates(itemsByType);
		final Collection<SelectedAttribute> selectedAttributesFiltered = applyAttributePredicates(selectedAttributes);

		final Map<String, Set<SelectedAttribute>> attributesByType = getExcelExportDivider().groupAttributesByType(itemsByTypeFiltered.keySet(), selectedAttributesFiltered);
		getExcelHelper().filterMarketUserAttributes(attributesByType, TBS_VARIANT_TYPE_CODE);
		final Workbook workbook = getExcelWorkbookService().createWorkbook(loadExcelTemplate());

		attributesByType.forEach((typeCode, attributes) -> {
			final Sheet sheet = getExcelSheetService().createTypeSheet(workbook, typeCode);

			addHeader(sheet, attributes);
			addValues(itemsByTypeFiltered, typeCode, attributes, sheet);
		});
		signWorkbookFile(workbook);
		return workbook;
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
}
