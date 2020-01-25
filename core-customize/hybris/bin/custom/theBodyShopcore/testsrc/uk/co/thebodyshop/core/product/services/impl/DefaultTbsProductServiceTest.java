/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.product.services.impl;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;

import uk.co.thebodyshop.core.model.TbsBaseProductModel;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;
import uk.co.thebodyshop.core.product.dao.TbsProductDao;

/**
 * @author prateek.goel
 */

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultTbsProductServiceTest
{

	@Mock
	private TbsProductDao tbsProductDao;

	@Mock
	private TbsVariantProductModel tbsVariantProductModel;

	@Mock
	private TbsBaseProductModel tbsBaseProductModel;

	@Mock
	private CatalogVersionModel catalogVersion;

	@InjectMocks
	private DefaultTbsProductService defaultTbsProductService;

	private final List<TbsVariantProductModel> variants = new ArrayList<>();

	private final List<TbsBaseProductModel> baseProductModels = new ArrayList<>();

	@Before
	public void setUp()
	{
		variants.add(tbsVariantProductModel);
		baseProductModels.add(tbsBaseProductModel);
		when(tbsVariantProductModel.getCode()).thenReturn("p1123v");
		when(tbsBaseProductModel.getCode()).thenReturn("p1123");
	}

	@Test
	public void testVariantAvailable()
	{
		when(tbsProductDao.fetchRecentlyCreatedVariantProducts(Mockito.anyObject(), Mockito.anyObject())).thenReturn(variants);
		final List<String> codes = defaultTbsProductService.fetchRecentlyCreatedVariantProductCodes(catalogVersion, null);
		Assert.assertTrue(CollectionUtils.isNotEmpty(codes));
	}

	@Test
	public void testVariantNotAvailable()
	{
		when(tbsProductDao.fetchRecentlyCreatedVariantProducts(Mockito.anyObject(), Mockito.anyObject())).thenReturn(null);
		final List<String> codes = defaultTbsProductService.fetchRecentlyCreatedVariantProductCodes(catalogVersion, null);
		Assert.assertTrue(CollectionUtils.isEmpty(codes));
	}

	@Test
	public void testBaseProductsAvailable()
	{
		when(tbsProductDao.fetchRecentlyCreatedBaseProducts(Mockito.anyObject(), Mockito.anyObject())).thenReturn(baseProductModels);
		final List<String> codes = defaultTbsProductService.fetchRecentlyCreatedBaseProductCodes(catalogVersion, null);
		Assert.assertTrue(CollectionUtils.isNotEmpty(codes));
	}

	@Test
	public void testBaseProductsNotAvailable()
	{
		when(tbsProductDao.fetchRecentlyCreatedBaseProducts(Mockito.anyObject(), Mockito.anyObject())).thenReturn(null);
		final List<String> codes = defaultTbsProductService.fetchRecentlyCreatedBaseProductCodes(catalogVersion, null);
		Assert.assertTrue(CollectionUtils.isEmpty(codes));
	}

}
