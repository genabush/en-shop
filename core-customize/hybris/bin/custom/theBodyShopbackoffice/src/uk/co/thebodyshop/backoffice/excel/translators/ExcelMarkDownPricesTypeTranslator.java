/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.backoffice.excel.translators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Joiner;
import com.hybris.backoffice.excel.data.ImpexHeaderValue;
import com.hybris.backoffice.excel.data.ImpexValue;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.template.filter.ExcelFilter;
import com.hybris.backoffice.excel.translators.AbstractCatalogVersionAwareTranslator;
import com.hybris.backoffice.excel.util.ExcelDateUtils;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.CollectionTypeModel;

import uk.co.thebodyshop.core.model.MarkDownPriceMessageModel;
import uk.co.thebodyshop.core.model.MarkDownPriceRowModel;

public class ExcelMarkDownPricesTypeTranslator extends AbstractCatalogVersionAwareTranslator<Collection<MarkDownPriceRowModel>>
{
	private static final String PATTERN = "%s:%s";
	private static final String PATTERN_PRICE = "%s:%s:%s:%s";
	private ExcelDateUtils excelDateUtils;
	private ExcelFilter<AttributeDescriptorModel> excelUniqueFilter;
	private ExcelFilter<AttributeDescriptorModel> mandatoryFilter;

	@Override
	public boolean canHandle(final AttributeDescriptorModel attributeDescriptor)
	{
		return attributeDescriptor.getAttributeType() instanceof CollectionTypeModel && MarkDownPriceRowModel._TYPECODE.equals(((CollectionTypeModel) attributeDescriptor.getAttributeType()).getElementType().getCode());
	}

	@Override
	public Optional<Object> exportData(final Collection<MarkDownPriceRowModel> objectToExport)
	{
		return CollectionUtils.emptyIfNull(objectToExport).stream().map(this::exportPriceRow).reduce(Joiner.on(',')::join).map(Object.class::cast);
	}

	protected String exportPriceRow(final MarkDownPriceRowModel objectToExport)
	{
		if (objectToExport != null && objectToExport.getProduct() != null && objectToExport.getStartTime() != null && objectToExport.getPrice() > 0)
		{
			final CatalogVersionModel catalogVersion = objectToExport.getProduct().getCatalogVersion();
			final String productString = String.format(PATTERN, objectToExport.getProduct().getCode(), exportCatalogVersionData(catalogVersion));
			return String.format(PATTERN_PRICE, productString, objectToExport.getPrice(), excelDateUtils.exportDateRange(objectToExport.getStartTime(), objectToExport.getEndTime()),
					objectToExport.getMarkDownMessage() != null ? objectToExport.getMarkDownMessage().getCode() : "");
		}
		return null;
	}

	@Override
	public String referenceFormat(final AttributeDescriptorModel attributeDescriptor)
	{
		final String productString = String.format(PATTERN, ProductModel._TYPECODE, referenceCatalogVersionFormat());
		return String.format(PATTERN_PRICE, productString, MarkDownPriceRowModel.PRICE, excelDateUtils.getDateRangePattern(), MarkDownPriceMessageModel._TYPECODE);
	}

	@Override
	public ImpexValue importValue(final AttributeDescriptorModel attributeDescriptor, final ImportParameters importParameters)
	{
		final List<String> formattedPrices = new ArrayList<>();
		for (final Map<String, String> params : importParameters.getMultiValueParameters())
		{
			formattedPrices.add(buildSinglePriceImpexValue(params));
		}
		return new ImpexValue(String.join(", ", formattedPrices), new ImpexHeaderValue.Builder(attributeDescriptor.getQualifier()).withDateFormat(excelDateUtils.getDateTimeFormat()).withTranslator(MarkDownPriceTranslator.class.getName())
				.withUnique(excelUniqueFilter.test(attributeDescriptor)).withMandatory(getMandatoryFilter().test(attributeDescriptor)).withLang(importParameters.getIsoCode()).withQualifier(attributeDescriptor.getQualifier()).build());
	}

	protected String buildSinglePriceImpexValue(final Map<String, String> params)
	{
		final StringBuilder sb = new StringBuilder();
		sb.append(params.get(ProductModel._TYPECODE) != null ? params.get(ProductModel._TYPECODE) : "").append(":");
		sb.append(params.get(CatalogVersionModel.CATALOG) != null ? params.get(CatalogVersionModel.CATALOG) : "").append(":");
		sb.append(params.get(CatalogVersionModel.VERSION) != null ? params.get(CatalogVersionModel.VERSION) : "").append(";");
		sb.append(params.get(MarkDownPriceRowModel.PRICE) != null ? params.get(MarkDownPriceRowModel.PRICE) : "").append(";");
		sb.append(getImpexDateRange(params.get(excelDateUtils.getDateRangeParamKey()) != null ? params.get(excelDateUtils.getDateRangeParamKey()) : "")).append(";");
		sb.append(params.get(MarkDownPriceMessageModel._TYPECODE) != null ? params.get(MarkDownPriceMessageModel._TYPECODE) : "");
		return sb.toString();
	}

	protected String getImpexDateRange(final String dateRange)
	{
		if (StringUtils.isNotEmpty(dateRange))
		{
			final Pair<String, String> range = excelDateUtils.extractDateRange(dateRange);
			if (range != null)
			{
				return String.format("[%s%s%s]", excelDateUtils.importDate(range.getLeft()), "to", excelDateUtils.importDate(range.getRight()));
			}
		}
		return null;
	}

	public ExcelDateUtils getExcelDateUtils()
	{
		return excelDateUtils;
	}

	public void setExcelDateUtils(final ExcelDateUtils excelDateUtils)
	{
		this.excelDateUtils = excelDateUtils;
	}

	public ExcelFilter<AttributeDescriptorModel> getExcelUniqueFilter()
	{
		return excelUniqueFilter;
	}

	public void setExcelUniqueFilter(final ExcelFilter<AttributeDescriptorModel> excelUniqueFilter)
	{
		this.excelUniqueFilter = excelUniqueFilter;
	}

	public ExcelFilter<AttributeDescriptorModel> getMandatoryFilter()
	{
		return mandatoryFilter;
	}

	public void setMandatoryFilter(final ExcelFilter<AttributeDescriptorModel> mandatoryFilter)
	{
		this.mandatoryFilter = mandatoryFilter;
	}

}
