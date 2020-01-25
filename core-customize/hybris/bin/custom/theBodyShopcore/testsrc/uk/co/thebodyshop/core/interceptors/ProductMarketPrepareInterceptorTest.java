/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.interceptors;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;

import uk.co.thebodyshop.core.catalog.services.TbsCatalogVersionService;

/**
 * @author Marcin
 */
@UnitTest
public class ProductMarketPrepareInterceptorTest
{
	@InjectMocks
	private ProductMarketPrepareInterceptor productMarketPrepareInterceptor;

	@Mock
	private TbsCatalogVersionService tbsCatalogVersionService;

	@Mock
	private InterceptorContext interceptorContext;

	@Mock
	private ProductModel product;

	@Mock
	private CatalogVersionModel catalogVersion;

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);

		when(product.getCatalogVersion()).thenReturn(catalogVersion);
		when(product.getApprovalStatus()).thenReturn(ArticleApprovalStatus.APPROVED);

		when(tbsCatalogVersionService.isStagedCatalog(catalogVersion)).thenReturn(true);
	}

	@Test
	public void testProductMarketPrepareInterceptorForGlobalProducts() throws Exception
	{
		given(interceptorContext.isNew(product)).willReturn(true);
		given(tbsCatalogVersionService.isMarketProductCatalog(catalogVersion)).willReturn(false);

		productMarketPrepareInterceptor.onPrepare(product, interceptorContext);

		then(product).should(times(0)).setApprovalStatus(any(ArticleApprovalStatus.class));
	}

	@Test
	public void testProductMarketPrepareInterceptorForNewMarketStagedProduct() throws Exception
	{
		given(interceptorContext.isNew(product)).willReturn(true);
		given(tbsCatalogVersionService.isMarketProductCatalog(catalogVersion)).willReturn(true);

		productMarketPrepareInterceptor.onPrepare(product, interceptorContext);

		then(product).should().setApprovalStatus(ArticleApprovalStatus.READYTOBELOCALISED);
	}

	@Test
	public void testProductMarketPrepareInterceptorForNewMarketOnlineProduct() throws Exception
	{
		given(interceptorContext.isNew(product)).willReturn(true);
		given(tbsCatalogVersionService.isMarketProductCatalog(catalogVersion)).willReturn(true);
		given(tbsCatalogVersionService.isStagedCatalog(catalogVersion)).willReturn(false);

		productMarketPrepareInterceptor.onPrepare(product, interceptorContext);

		then(product).should(times(0)).setApprovalStatus(any(ArticleApprovalStatus.class));
	}

	@Test
	public void testProductMarketPrepareInterceptorForExistingMarketProduct() throws Exception
	{
		given(interceptorContext.isNew(product)).willReturn(false);
		given(tbsCatalogVersionService.isMarketProductCatalog(catalogVersion)).willReturn(true);

		productMarketPrepareInterceptor.onPrepare(product, interceptorContext);

		then(product).should(times(0)).setApprovalStatus(any(ArticleApprovalStatus.class));
	}
}
