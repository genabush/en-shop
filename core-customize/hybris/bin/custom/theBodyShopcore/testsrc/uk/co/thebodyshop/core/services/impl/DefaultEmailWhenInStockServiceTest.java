/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.daos.ProductDao;
import de.hybris.platform.servicelayer.model.ModelService;

import uk.co.thebodyshop.core.daos.EmailWhenInStockDao;
import uk.co.thebodyshop.core.emailWhenInStock.ws.data.EmailWhenInStockData;
import uk.co.thebodyshop.core.emailWhenInStock.ws.data.EmailWhenInStockResultData;
import uk.co.thebodyshop.core.model.EmailWhenInStockModel;

/**
 * @author Krishna
 */
@UnitTest
public class DefaultEmailWhenInStockServiceTest
{
	private static final String PRODUCT_CODE = "p000936v";
	private static final String EMAIL_ID = "test@gmail.com";

	@InjectMocks
	private DefaultEmailWhenInStockService defaultEmailWhenInStockService;

	@Mock
	private EmailWhenInStockDao emailWhenInStockDao;

	@Mock
	private ModelService modelService;

	@Mock
	private CMSSiteService cmsSiteService;

	@Mock
	private ProductDao productDao;

	@Mock
	private EmailWhenInStockModel emailWhenInStock;

	@Mock
	private CMSSiteModel cmsSite;

	@Mock
	private ProductModel product;

	private EmailWhenInStockData emailWhenInStockData;

	private List<ProductModel> products;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		emailWhenInStockData = new EmailWhenInStockData();
		emailWhenInStockData.setEmailId(EMAIL_ID);
		emailWhenInStockData.setProductCode(PRODUCT_CODE);

		given(cmsSiteService.getCurrentSite()).willReturn(cmsSite);

		products = new ArrayList<>();
		products.add(product);
	}

	@Test
	public void testProductNotFound()
	{
		given(productDao.findProductsByCode(PRODUCT_CODE)).willReturn(null);
		final EmailWhenInStockResultData data = defaultEmailWhenInStockService.findEmailWhenInStock(emailWhenInStockData);
		assertThat(data.isSuccess()).isFalse();
	}

	@Test
	public void testRecordExists()
	{
		given(productDao.findProductsByCode(PRODUCT_CODE)).willReturn(products);
		given(emailWhenInStockDao.getEmailWhenInStock(EMAIL_ID, PRODUCT_CODE, cmsSite)).willReturn(emailWhenInStock);
		final EmailWhenInStockResultData data = defaultEmailWhenInStockService.findEmailWhenInStock(emailWhenInStockData);
		assertThat(data.isSuccess()).isTrue();

	}

	@Test
	public void testRecordDoesNotExist()
	{
		given(productDao.findProductsByCode(PRODUCT_CODE)).willReturn(products);
		given(emailWhenInStockDao.getEmailWhenInStock(EMAIL_ID, PRODUCT_CODE, cmsSite)).willReturn(null);
		given(modelService.create(EmailWhenInStockModel.class)).willReturn(emailWhenInStock);
		final EmailWhenInStockResultData data = defaultEmailWhenInStockService.findEmailWhenInStock(emailWhenInStockData);
		then(emailWhenInStock).should().setBaseSite(cmsSite);
		then(emailWhenInStock).should().setProductCode(PRODUCT_CODE);
		then(emailWhenInStock).should().setEmail(EMAIL_ID);
		then(modelService).should().save(emailWhenInStock);
		assertThat(data.isSuccess()).isTrue();
	}
}
