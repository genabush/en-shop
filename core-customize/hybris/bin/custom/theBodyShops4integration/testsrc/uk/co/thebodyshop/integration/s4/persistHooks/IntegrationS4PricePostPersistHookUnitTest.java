/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.s4.persistHooks;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import uk.co.thebodyshop.core.catalog.services.TbsCatalogVersionService;
import uk.co.thebodyshop.core.model.TbsBaseProductModel;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;
import uk.co.thebodyshop.integration.s4.model.IntegrationS4PriceModel;

/**
 * @author prateek.goel
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class IntegrationS4PricePostPersistHookUnitTest
{

	@Mock
	private ModelService modelService;
	@Mock
	private TbsCatalogVersionService tbsCatalogVersionService;
	@Mock
	private ProductService productService;
	@Mock
	private SearchRestrictionService searchRestrictionService;
	@Mock
	private BaseStoreService baseStoreService;

	@Mock
	private TbsVariantProductModel tbsVariantProductModel;

	@Mock
	private TbsBaseProductModel tbsBaseProductModel;

	@Mock
	private CatalogVersionModel catalogVersion;

	@Mock
	private BaseStoreModel baseStoreModel;

	@Mock
	private Map<String, String> countryCatalogIdMap;

	@Mock
	private Map<String, String> countryBaseStoreIdMap;

	@InjectMocks
	private IntegrationS4PricePostPersistHook integrationS4PricePostPersistHook;

	@Mock
	private IntegrationS4PriceModel integrationS4PriceModel;

	@Mock
	private Converter<IntegrationS4PriceModel, PriceRowModel> integrationPriceConverter;

	@Mock
	private UserPriceGroup priceGroup;

	private final PriceRowModel priceRow = new PriceRowModel();

	@Before
	public void setUp()
	{
		when(tbsCatalogVersionService.getStagedMarketProductCatalog(Mockito.anyString())).thenReturn(catalogVersion);
		when(baseStoreService.getBaseStoreForUid(Mockito.anyString())).thenReturn(baseStoreModel);
		when(modelService.create(PriceRowModel.class)).thenReturn(priceRow);
		when(integrationS4PriceModel.getCountry()).thenReturn("UK");
		when(tbsVariantProductModel.getBaseProduct()).thenReturn(tbsBaseProductModel);
		when(priceGroup.getCode()).thenReturn("priceGroup");
	}

	@Test
	public void testProductVariantNotPresent()
	{
		integrationS4PricePostPersistHook.execute(integrationS4PriceModel);

	}

	@Test
	public void testProductVariantIsPresent()
	{
		when(productService.getProductForCode(Mockito.anyObject(), Mockito.anyString())).thenReturn(tbsVariantProductModel);
		integrationS4PricePostPersistHook.execute(integrationS4PriceModel);
		Assert.assertTrue(priceRow.getMinqtd() == Long.valueOf(1));
		final UserPriceGroup usp;
	}

	@Test
	public void testPriceRowAlreadyAvailable()
	{
		priceRow.setUg(priceGroup);
		when(productService.getProductForCode(Mockito.anyObject(), Mockito.anyString())).thenReturn(tbsVariantProductModel);
		final List<PriceRowModel> priceRows = new ArrayList<>();
		priceRows.add(priceRow);
		when(tbsVariantProductModel.getEurope1Prices()).thenReturn(priceRows);
		when(baseStoreModel.getBaseStoreUserPriceGroup()).thenReturn(priceGroup);
		integrationS4PricePostPersistHook.execute(integrationS4PriceModel);
		Assert.assertNull(priceRow.getMinqtd());
	}
}
