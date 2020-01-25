/**
 *
 */
package uk.co.thebodyshop.backoffice.validators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.backoffice.excel.template.header.ExcelHeaderService;
import com.hybris.backoffice.excel.template.sheet.ExcelSheetService;
import com.hybris.backoffice.excel.validators.WorkbookValidator;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;


/**
 * @author Lakshmi
 *
 */
public class ExcelImportExcludeAttributeValidator implements WorkbookValidator
{
	private static final String EXCEL_IMPORT_VALIDATION_INCORRECT_FILE_READONLY_ATTRIBUTE_IMPORT_MESSAGE = "excel.import.validation.incorrect.file.exclude.attribute.description";
	private static final String EXCEL_IMPORT_VALIDATION_INCORRECT_FILE_HEADER_MESSAGE = "excel.import.validation.incorrect.file.header";

	private final ExcelHeaderService excelHeaderService;
	private final ExcelSheetService excelSheetService;
	private final List<String> listOfExcludeHeaders;

	@Override
	public List<ExcelValidationResult> validate(final Workbook workbook)
	{
		final List<ExcelValidationResult> validationResults = new ArrayList<ExcelValidationResult>();
		final Collection<Sheet> sheets = getExcelSheetService().getSheets(workbook);
		for (final Sheet sheet : sheets)
		{
			final Collection<String> headerNames = getExcelHeaderService().getHeaderDisplayNames(sheet);
			if (CollectionUtils.isNotEmpty(headerNames) && CollectionUtils.isNotEmpty(listOfExcludeHeaders))
			{

				final List<String> results = headerNames.stream().filter(predicate -> listOfExcludeHeaders.contains(predicate))
						.collect(Collectors.toList());

				if (CollectionUtils.isNotEmpty(results))
				{
					final ExcelValidationResult incorrectFileValidationResult = new ExcelValidationResult(new ValidationMessage(
							EXCEL_IMPORT_VALIDATION_INCORRECT_FILE_READONLY_ATTRIBUTE_IMPORT_MESSAGE, results.toString()));
					incorrectFileValidationResult
							.setHeader(new ValidationMessage(EXCEL_IMPORT_VALIDATION_INCORRECT_FILE_HEADER_MESSAGE));
					incorrectFileValidationResult.setWorkbookValidationResult(true);
					return Collections.singletonList(incorrectFileValidationResult);
				}
			}
		}
		return validationResults;
	}

	@Autowired
	public ExcelImportExcludeAttributeValidator(final ExcelHeaderService excelHeaderService,
			final ExcelSheetService excelSheetService, final List<String> listOfExcludeHeaders)
	{
		this.excelHeaderService = excelHeaderService;
		this.excelSheetService = excelSheetService;
		this.listOfExcludeHeaders = listOfExcludeHeaders;
	}

	protected ExcelHeaderService getExcelHeaderService()
	{
		return excelHeaderService;
	}

	protected ExcelSheetService getExcelSheetService()
	{
		return excelSheetService;
	}

	protected List<String> getListOfExcludeHeaders()
	{
		return listOfExcludeHeaders;
	}

}
