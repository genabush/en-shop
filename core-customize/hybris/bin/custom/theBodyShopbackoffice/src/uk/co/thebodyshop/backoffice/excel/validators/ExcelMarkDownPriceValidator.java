/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.backoffice.excel.validators;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.util.ExcelDateUtils;
import com.hybris.backoffice.excel.validators.ExcelValidator;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.CollectionTypeModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.i18n.I18NService;

import uk.co.thebodyshop.core.helpers.MarkDownPriceValidationHelper;
import uk.co.thebodyshop.core.model.MarkDownPriceRowModel;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;

public class ExcelMarkDownPriceValidator implements ExcelValidator
{

	private static final Logger LOG = LoggerFactory.getLogger(ExcelMarkDownPriceValidator.class);

	public static final Pattern PATTERN_DATE_RANGE = Pattern.compile("(.+)\\s*to\\s*(.+)");

	public static final String VALIDATION_EMPTY_PRICE = "excel.import.validation.markdownprice.empty";
	public static final String VALIDATION_PRICE_GREATER_BASE = "excel.import.validation.markdownprice.base.greater";
	public static final String VALIDATION_PRICE_GREATER_ZERO = "excel.import.validation.markdownprice.zero.greater";
	public static final String VALIDATION_EMPTY_PRODUCT = "excel.import.validation.markdownprice.product.empty";
	public static final String VALIDATION_EMPTY_CATALOG = "excel.import.validation.markdownprice.catalog.empty";
	public static final String VALIDATION_EMPTY_CATALOG_VERSION = "excel.import.validation.markdownprice.catalog.version.empty";
	public static final String VALIDATION_EMPTY_DATE = "excel.import.validation.markdownprice.date.empty";
	public static final String VALIDATION_INCORRECT_DATE_RANGE = "excel.import.validation.markdownprice.date.incorrect.format";
	public static final String VALIDATION_START_DATE_AFTER_END_DATE = "excel.import.validation.markdownprice.date.start.after.end";
	public static final String VALIDATION_START_DATE = "excel.import.validation.markdownprice.date.start";
	public static final String VALIDATION_START_DATE_END_DATE_EMPTY = "excel.import.validation.markdownprice.date.start.end.empty";

	private final ExcelDateUtils excelDateUtils;

	private final ProductService productService;

	private final CatalogVersionService catalogVersionService;

	protected I18NService i18nService;

	private final MarkDownPriceValidationHelper markDownPriceValidationHelper;

	public ExcelMarkDownPriceValidator(final ExcelDateUtils excelDateUtils, final ProductService productService, final CatalogVersionService catalogVersionService, final I18NService i18nService,
			final MarkDownPriceValidationHelper markDownPriceValidationHelper)
	{
		super();
		this.excelDateUtils = excelDateUtils;
		this.productService = productService;
		this.catalogVersionService = catalogVersionService;
		this.i18nService = i18nService;
		this.markDownPriceValidationHelper = markDownPriceValidationHelper;
	}

	@Override
	public ExcelValidationResult validate(final ImportParameters importParameters, final AttributeDescriptorModel attributeDescriptor, final Map<String, Object> context)
	{
		final List<ValidationMessage> validationMessages = new ArrayList<>();
		final List<Date> startTmeList = new ArrayList<>();
		for (final Map<String, String> parameters : importParameters.getMultiValueParameters())
		{
			validationMessages.addAll(validateSingleValue(context, parameters, startTmeList));
		}
		return validationMessages.isEmpty() ? ExcelValidationResult.SUCCESS : new ExcelValidationResult(validationMessages);
	}

	protected List<ValidationMessage> validateSingleValue(final Map<String, Object> ctx, final Map<String, String> parameters, final List<Date> startTimeList)
	{
		final List<ValidationMessage> validations = new ArrayList<>();
		final String code = parameters.get(ProductModel._TYPECODE);
		final String catalogId = parameters.get(CatalogVersionModel.CATALOG);
		final String catalogVersionName = parameters.get(CatalogVersionModel.VERSION);
		validateProductWithCatalogVersion(code, catalogId, catalogVersionName).ifPresent(validations::addAll);
		if (validations.isEmpty())
		{
			final CatalogVersionModel version = getCatalogVersionService().getCatalogVersion(catalogId, catalogVersionName);
			if (null != version && null != version.getCatalog() && CollectionUtils.isNotEmpty(version.getCatalog().getBaseStores()))
			{
				final CurrencyModel currency = version.getCatalog().getBaseStores().iterator().next().getDefaultCurrency();
				if (null != currency)
				{
					i18nService.setCurrentCurrency(currency);
				}
			}
			final ProductModel model = productService.getProductForCode(version, code);
			if (null != model && model instanceof TbsVariantProductModel)
			{
				final TbsVariantProductModel variantModel = (TbsVariantProductModel) model;
				validateMarkDownPriceRow(parameters.get(MarkDownPriceRowModel.PRICE), variantModel).ifPresent(validations::addAll);
				validateDate(parameters.get(excelDateUtils.getDateRangeParamKey()), variantModel, startTimeList).ifPresent(validations::add);
			}
		}
		return validations;
	}

	@Override
	public boolean canHandle(final ImportParameters importParameters, final AttributeDescriptorModel attributeDescriptor)
	{
		return importParameters.isCellValueNotBlank() && attributeDescriptor.getAttributeType() instanceof CollectionTypeModel
				&& MarkDownPriceRowModel._TYPECODE.equals(((CollectionTypeModel) attributeDescriptor.getAttributeType()).getElementType().getCode());
	}

	private Optional<List<ValidationMessage>> validateProductWithCatalogVersion(final String code, final String catalogId, final String version)
	{
		final List<ValidationMessage> validationMessages = new ArrayList<>();
		if (StringUtils.isEmpty(code))
		{
			validationMessages.add(new ValidationMessage(VALIDATION_EMPTY_PRODUCT, code));
		}
		if (StringUtils.isEmpty(catalogId))
		{
			validationMessages.add(new ValidationMessage(VALIDATION_EMPTY_CATALOG, catalogId));
		}
		if (StringUtils.isEmpty(version))
		{
			validationMessages.add(new ValidationMessage(VALIDATION_EMPTY_CATALOG_VERSION, version));
		}
		if (!CollectionUtils.isEmpty(validationMessages))
		{
			return Optional.of(validationMessages);
		}
		return Optional.empty();
	}

	private Optional<ValidationMessage> validateDate(final String value, final TbsVariantProductModel variantModel, final List<Date> startTimeList)
	{
		if (StringUtils.isEmpty(value))
		{
			return Optional.of(new ValidationMessage(VALIDATION_EMPTY_DATE, value));
		}
		final Matcher matcher = PATTERN_DATE_RANGE.matcher(value);

		if (matcher.matches())
		{
			final Date from = parseDate(matcher.group(1));
			final Date to = parseDate(matcher.group(2));
			if (from != null && to != null)
			{
				if (startTimeList.contains(from))
				{
					return Optional.of(new ValidationMessage(VALIDATION_START_DATE, matcher.group(1), matcher.group(2)));
				}
				if (CollectionUtils.isEmpty(startTimeList) || !startTimeList.contains(from))
				{
					startTimeList.add(from);
				}
				if (from.after(to))
				{
					return Optional.of(new ValidationMessage(VALIDATION_START_DATE_AFTER_END_DATE, matcher.group(1)));
				}
				return Optional.empty();
			}
			else
			{
				return Optional.of(new ValidationMessage(VALIDATION_START_DATE_END_DATE_EMPTY));
			}
		}

		return Optional.of(new ValidationMessage(VALIDATION_INCORRECT_DATE_RANGE, value));
	}

	private Optional<List<ValidationMessage>> validateMarkDownPriceRow(final String value, final TbsVariantProductModel variantModel)
	{
		if (StringUtils.isEmpty(value))
		{
			return Optional.of(Lists.newArrayList(new ValidationMessage(VALIDATION_EMPTY_PRICE, value)));
		}
		final Double price = new Double(value);
		if (!(price > 0))
		{
			return Optional.of(Lists.newArrayList(new ValidationMessage(VALIDATION_PRICE_GREATER_ZERO, value)));
		}
		final Double currentPrice = getMarkDownPriceValidationHelper().getCurrentBasePrice(variantModel);
		if (null != currentPrice)
		{
			if (price == 0 || price > currentPrice.doubleValue())
			{
				return Optional.of(Lists.newArrayList(new ValidationMessage(VALIDATION_PRICE_GREATER_BASE, value)));
			}
		}
		return Optional.empty();
	}

	protected Date parseDate(final String date)
	{
		try
		{
			return excelDateUtils.convertToImportedDate(date);
		}
		catch (final DateTimeParseException e)
		{
			LOG.debug("Wrong date format " + date, e);
			return null;
		}
	}

	protected ExcelDateUtils getExcelDateUtils()
	{
		return excelDateUtils;
	}

	protected ProductService getProductService()
	{
		return productService;
	}

	protected CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	protected I18NService getI18nService()
	{
		return i18nService;
	}

	/**
	 * @return the markDownPriceValidationHelper
	 */
	protected MarkDownPriceValidationHelper getMarkDownPriceValidationHelper()
	{
		return markDownPriceValidationHelper;
	}
}
