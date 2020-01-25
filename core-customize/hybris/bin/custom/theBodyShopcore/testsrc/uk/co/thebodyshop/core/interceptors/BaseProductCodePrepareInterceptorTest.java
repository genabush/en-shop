/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.interceptors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;

import uk.co.thebodyshop.core.catalog.services.TbsCatalogVersionService;
import uk.co.thebodyshop.core.model.TbsBaseProductModel;

@UnitTest
public class BaseProductCodePrepareInterceptorTest
{

	private static final String GENERATED_PRODUCT_CODE = "testProductCode";
	private static final String GENERATED_PRODUCT_CODE_LOWERCASE = "testproductcode";

	@Mock
	private PersistentKeyGenerator persistentKeyGenerator;
	@Mock
	private TbsCatalogVersionService tbsCatalogVersionService;
	@Mock
	private InterceptorContext interceptorContext;
	@Mock
	private ModelService modelService;

	@InjectMocks
	private BaseProductCodePrepareInterceptor baseProductCodePrepareInterceptor;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		baseProductCodePrepareInterceptor = new BaseProductCodePrepareInterceptor(persistentKeyGenerator, tbsCatalogVersionService);
		when(persistentKeyGenerator.generate()).thenReturn(GENERATED_PRODUCT_CODE);
		when(interceptorContext.getModelService()).thenReturn(modelService);
	}

	@Test
	public void shouldGenerateCodeForTbsBaseProduct()
	{
		final TbsBaseProductModel tbsBaseProduct = new TbsBaseProductModel();
		setCatalogVersion(tbsBaseProduct);
		when(modelService.isNew(any(TbsBaseProductModel.class))).thenReturn(Boolean.TRUE);
		when(tbsCatalogVersionService.isStagedGlobalProductCatalog(any(CatalogVersionModel.class))).thenReturn(Boolean.TRUE);
		invokeOnPrepareOnInterceptor(tbsBaseProduct, interceptorContext);
		assertEquals(GENERATED_PRODUCT_CODE_LOWERCASE, tbsBaseProduct.getCode());
	}

	@Test
	public void shouldNotGenerateCodeForNonTbsBaseProduct()
	{
		final TbsBaseProductModel tbsBaseProduct = new TbsBaseProductModel();
		setCatalogVersion(tbsBaseProduct);
		when(modelService.isNew(any(TbsBaseProductModel.class))).thenReturn(Boolean.FALSE);
		when(tbsCatalogVersionService.isStagedGlobalProductCatalog(any(CatalogVersionModel.class))).thenReturn(Boolean.TRUE);
		invokeOnPrepareOnInterceptor(tbsBaseProduct, interceptorContext);
		assertNotEquals(GENERATED_PRODUCT_CODE_LOWERCASE, tbsBaseProduct.getCode());
	}

	@Test
	public void shouldNotGenerateCodeForNotNewBaseProduct()
	{
		final TbsBaseProductModel tbsBaseProduct = new TbsBaseProductModel();
		setCatalogVersion(tbsBaseProduct);
		when(modelService.isNew(any(TbsBaseProductModel.class))).thenReturn(Boolean.FALSE);
		when(tbsCatalogVersionService.isStagedGlobalProductCatalog(any(CatalogVersionModel.class))).thenReturn(Boolean.TRUE);
		invokeOnPrepareOnInterceptor(tbsBaseProduct, interceptorContext);
		assertNotEquals(GENERATED_PRODUCT_CODE_LOWERCASE, tbsBaseProduct.getCode());
	}

	@Test
	public void shouldNotGenerateCodeIfNotStagedGlobalCatalog()
	{
		final TbsBaseProductModel tbsBaseProduct = new TbsBaseProductModel();
		setCatalogVersion(tbsBaseProduct);
		when(modelService.isNew(any(TbsBaseProductModel.class))).thenReturn(Boolean.TRUE);
		when(tbsCatalogVersionService.isStagedGlobalProductCatalog(any(CatalogVersionModel.class))).thenReturn(Boolean.FALSE);
		invokeOnPrepareOnInterceptor(tbsBaseProduct, interceptorContext);
		assertNotEquals(GENERATED_PRODUCT_CODE_LOWERCASE, tbsBaseProduct.getCode());
	}

	private void setCatalogVersion(final TbsBaseProductModel tbsBaseProductModel)
	{
		final CatalogVersionModel catalogVersion = new CatalogVersionModel();
		catalogVersion.setVersion("Staged");
		tbsBaseProductModel.setCatalogVersion(catalogVersion);
	}

	private void invokeOnPrepareOnInterceptor(final TbsBaseProductModel tbsBaseProduct, final InterceptorContext interceptorContext)
	{
		try
		{
			baseProductCodePrepareInterceptor.onPrepare(tbsBaseProduct, interceptorContext);
		}
		catch (final InterceptorException ex)
		{
			fail();
		}
	}

}
