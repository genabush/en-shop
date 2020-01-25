/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.s4.persistHooks;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;

import uk.co.thebodyshop.core.catalog.services.TbsCatalogVersionService;
import uk.co.thebodyshop.core.model.TbsBaseProductModel;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;
import uk.co.thebodyshop.integration.s4.model.IntegrationS4ProductModel;

/**
 * @author prateek.goel
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class IntegrationS4ProductPostPersistHookUnitTest
{
	@InjectMocks
	private IntegrationS4ProductPostPersistHook integrationS4ProductPostPersistHook;

	@Mock
	private ModelService modelService;

	@Mock
	private TbsCatalogVersionService tbsCatalogVersionService;

	@Mock
	private ProductService productService;

	@Mock
	private SearchRestrictionService searchRestrictionService;

	@Mock
	private ConfigurationService configurationService;

	@Mock
	private TbsBaseProductModel tbsBaseProductModel;

	@Mock
	private CatalogVersionModel catalogVersion;

	@Mock
	private Configuration configuration;

	@Mock
	private Converter<IntegrationS4ProductModel, TbsVariantProductModel> integrationVariantProductConverter;

	@Mock
	private IntegrationS4ProductModel integrationS4ProductModel;

	@Mock
	private TbsVariantProductModel tbsVariantProductModel;

	@Before
	public void setUp()
	{
		when(tbsCatalogVersionService.getStagedGlobalProductCatalog()).thenReturn(catalogVersion);
		when(configurationService.getConfiguration()).thenReturn(configuration);
		when(integrationS4ProductModel.getCode()).thenReturn("p001234v");
	}

	@Test
	public void testVariantProductNotAvailable()
	{
		when(productService.getProductForCode(catalogVersion, integrationS4ProductModel.getCode())).thenThrow(Exception.class);
		when(modelService.create(TbsVariantProductModel.class)).thenReturn(tbsVariantProductModel);
		integrationS4ProductPostPersistHook.execute(integrationS4ProductModel);
		verify(tbsVariantProductModel).setCatalogVersion(catalogVersion);
	}

	@Test
	public void testVariantProductAvailable()
	{
		when(productService.getProductForCode(catalogVersion, integrationS4ProductModel.getCode())).thenReturn(tbsVariantProductModel);
		integrationS4ProductPostPersistHook.execute(integrationS4ProductModel);
		verify(tbsVariantProductModel, times(0)).setCode("p001234v");
	}
}
