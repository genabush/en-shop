/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.product.dao.impl;

import static org.mockito.Mockito.when;

import java.util.Date;
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
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import uk.co.thebodyshop.core.model.TbsBaseProductModel;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;

/**
 * @author prateek.goel
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultTbsProductDaoTest
{

	@InjectMocks
	private DefaultTbsProductDao defaultTbsProductDao;

	@Mock
	private CatalogVersionModel catalogVersion;

	@Mock
	private FlexibleSearchService flexibleSearchService;

	@Mock
	private SearchResult<TbsVariantProductModel> searchResult;

	@Mock
	private SearchResult<TbsBaseProductModel> baseSearchResult;

	@Mock
	private List<TbsVariantProductModel> variants;

	@Mock
	private List<TbsBaseProductModel> baseProductModels;

	@Before
	public void setUp()
	{
		defaultTbsProductDao = new DefaultTbsProductDao(null);
		defaultTbsProductDao.setFlexibleSearchService(flexibleSearchService);
		when(flexibleSearchService.search(Mockito.anyString(), Mockito.anyMap())).thenReturn(searchResult);
		when(variants.size()).thenReturn(1);
		when(baseProductModels.size()).thenReturn(1);
	}

	@Test
	public void testVariantDataAvailable()
	{
		when(searchResult.getResult()).thenReturn(variants);
		final List<TbsVariantProductModel> variants = defaultTbsProductDao.fetchRecentlyCreatedVariantProducts(catalogVersion, new Date());
		Assert.assertEquals(variants.size(), 1);
	}

	@Test
	public void testVariantDataNotAvailable()
	{
		when(searchResult.getResult()).thenReturn(null);
		final List<TbsVariantProductModel> variants = defaultTbsProductDao.fetchRecentlyCreatedVariantProducts(catalogVersion, new Date());
		Assert.assertFalse(CollectionUtils.isNotEmpty(variants));
	}

	@Test
	public void testBaseProductDataAvailable()
	{
		when(baseSearchResult.getResult()).thenReturn(baseProductModels);
		when(flexibleSearchService.search(Mockito.anyString(), Mockito.anyMap())).thenReturn(baseSearchResult);
		final List<TbsBaseProductModel> baseProductModels = defaultTbsProductDao.fetchRecentlyCreatedBaseProducts(catalogVersion, new Date());
		Assert.assertEquals(baseProductModels.size(), 1);
	}

	@Test
	public void testBaseProductDataNotAvailable()
	{
		when(baseSearchResult.getResult()).thenReturn(null);
		final List<TbsBaseProductModel> baseProductModels = defaultTbsProductDao.fetchRecentlyCreatedBaseProducts(catalogVersion, new Date());
		Assert.assertFalse(CollectionUtils.isNotEmpty(baseProductModels));
	}

}
