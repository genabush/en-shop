/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.product.services.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.product.impl.DefaultProductService;

import uk.co.thebodyshop.core.model.TbsBaseProductModel;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;
import uk.co.thebodyshop.core.product.dao.TbsProductDao;
import uk.co.thebodyshop.core.product.services.TbsProductService;

/**
 * @author prateek.goel
 */
public class DefaultTbsProductService extends DefaultProductService implements TbsProductService
{

	private final TbsProductDao tbsProductDao;

	@Override
	public List<String> fetchRecentlyCreatedVariantProductCodes(final CatalogVersionModel catalogVersion, final Date lastSuccessfulTime)
	{
		final List<TbsVariantProductModel> variants = this.tbsProductDao.fetchRecentlyCreatedVariantProducts(catalogVersion, lastSuccessfulTime);
		if (CollectionUtils.isNotEmpty(variants))
		{
			return variants.stream().map(TbsVariantProductModel::getCode).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@Override
	public List<String> fetchRecentlyCreatedBaseProductCodes(CatalogVersionModel catalogVersion, Date lastSuccessfulTime) {
		final List<TbsBaseProductModel> variants = this.tbsProductDao.fetchRecentlyCreatedBaseProducts(catalogVersion, lastSuccessfulTime);
		if (CollectionUtils.isNotEmpty(variants))
		{
			return variants.stream().map(TbsBaseProductModel::getCode).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}


	public DefaultTbsProductService(final TbsProductDao tbsProductDao)
	{
		super();
		this.tbsProductDao = tbsProductDao;
	}

	protected TbsProductDao getTbsProductDao()
	{
		return tbsProductDao;
	}

}
