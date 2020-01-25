/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.s4.populators;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.product.UnitService;
import de.hybris.platform.servicelayer.model.ModelService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;
import uk.co.thebodyshop.integration.s4.model.IntegrationS4ProductModel;
import uk.co.thebodyshop.integration.s4.model.ProductBarCodeModel;

/**
 * @author prateek.goel
 */
public class IntegrationVariantProductPopulator implements Populator<IntegrationS4ProductModel, TbsVariantProductModel>
{
	private Logger LOG= LoggerFactory.getLogger(IntegrationVariantProductPopulator.class);
	private UnitService unitService;
	private ModelService modelService;
	private final String DEFAULT_ONLINE_DATE_VALUE= "01-01-2019 06:30:00";
	private final String ONLINE_DATE_FORMAT= "dd-MM-yyyy HH:mm:ss";

	@Override
	public void populate(final IntegrationS4ProductModel source, final TbsVariantProductModel target)
	{
		if (getModelService().isNew(target))
		{
			target.setCode(source.getCode());
			target.setName(source.getInternalName(), Locale.ENGLISH);

			if (source.getMeasurement() != null)
			{
				target.setSize(String.valueOf(source.getMeasurement().doubleValue()));
			}

			if (StringUtils.isNotBlank(source.getUnitOfMeasure()))
			{
				target.setUnit(getUnitService().getUnitForCode(source.getUnitOfMeasure()));
			}

			SimpleDateFormat sdf = new SimpleDateFormat(ONLINE_DATE_FORMAT);
			try {
				target.setOnlineDate(sdf.parse(DEFAULT_ONLINE_DATE_VALUE));
			} catch (ParseException e) {
				LOG.error("Error while parsing onlineDate");
			}
		}

		target.setErpCollection(source.getCollection());
		target.setErpColour(source.getColour());
		target.setErpFlavour(source.getFlavour());
		target.setErpHazardous(source.getHazardous());
		target.setErpInternalName(source.getInternalName());
		target.setErpMeasurement(source.getMeasurement());
		target.setErpMllVariant(source.getMllVariant());
		target.setErpUnitOfMeasure(source.getUnitOfMeasure());
		target.setErpSeason(source.getSeason());
		target.setErpSeasonYear(source.getSeasonYear());
		target.setErpType(source.getType());

		if (CollectionUtils.isNotEmpty(source.getBarcodes()))
		{
			List<String> barcodes = new ArrayList<>();
			barcodes = source.getBarcodes().stream().map(ProductBarCodeModel::getValue).collect(Collectors.toList());
			target.setBarcodelist(barcodes);
		}
	}

	protected UnitService getUnitService()
	{
		return unitService;
	}

	public void setUnitService(final UnitService unitService)
	{
		this.unitService = unitService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}
