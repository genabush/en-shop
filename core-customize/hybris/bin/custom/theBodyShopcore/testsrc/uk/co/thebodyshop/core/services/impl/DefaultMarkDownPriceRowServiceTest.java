/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import de.hybris.bootstrap.annotations.UnitTest;

import uk.co.thebodyshop.core.model.MarkDownPriceRowModel;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;

/**
 * @author Balakrishna
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultMarkDownPriceRowServiceTest
{
	@InjectMocks
	private DefaultMarkDownPriceRowService defaultMarkDownPriceRowService;

	@Mock
	private TbsVariantProductModel productModel;

	@Mock
	private MarkDownPriceRowModel markDownPriceMock;

	@Mock
	private Set<MarkDownPriceRowModel> markDownPricesList;

	private Set<MarkDownPriceRowModel> markDownPrices;

	private MarkDownPriceRowModel markDownPrice;

	private TbsVariantProductModel product;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		product = new TbsVariantProductModel();
		markDownPrice = new MarkDownPriceRowModel();
		markDownPrice.setProduct(productModel);
		markDownPrice.setPrice(new Double(3));
		final Date date = new Date();
		final Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(date);
		startCalendar.add(Calendar.DATE, -3);
		final Date yesterday = startCalendar.getTime();
		final Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(new Date());
		endCalendar.add(Calendar.DATE, +5);
		final Date tomorrow = endCalendar.getTime();
		markDownPrice.setStartTime(yesterday);
		markDownPrice.setEndTime(tomorrow);
		markDownPrices = new HashSet<>();
		markDownPrices.add(markDownPrice);
		product.setMarkDownPrices(markDownPrices);
	}

	@Test
	public void testGetActiveMarkDownPriceEmptyResult()
	{
		final TbsVariantProductModel variantproductModel = new TbsVariantProductModel();
		final MarkDownPriceRowModel markDownPriceRow = defaultMarkDownPriceRowService.getActiveMarkDownPrice(variantproductModel);
		assertThat(markDownPriceRow == null).isTrue();
	}

	@Test
	public void testGetActiveMarkDownPriceNonEmptyResult()
	{
		when(markDownPriceMock.getProduct()).thenReturn(product);
		when(productModel.getMarkDownPrices()).thenReturn(markDownPrices);
		final MarkDownPriceRowModel markDownPriceRow = defaultMarkDownPriceRowService.getActiveMarkDownPrice(productModel);
		assertThat(markDownPriceRow != null).isTrue();
	}
}
