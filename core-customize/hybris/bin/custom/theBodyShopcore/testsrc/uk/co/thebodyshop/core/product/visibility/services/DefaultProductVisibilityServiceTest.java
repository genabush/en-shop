
/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.product.visibility.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;

import uk.co.thebodyshop.core.product.visibility.context.ProductVisibilityContext;
import uk.co.thebodyshop.core.product.visibility.context.ProductVisibilityContextExtractor;
import uk.co.thebodyshop.core.product.visibility.data.ProductVisibilityData;
import uk.co.thebodyshop.core.product.visibility.strategy.ProductVisibilityStrategy;
import uk.co.thebodyshop.core.product.visibility.strategy.factory.ProductVisibilityStrategyFactory;

/**
 * @author Marcin
 *
 */
@UnitTest
public class DefaultProductVisibilityServiceTest {

	@InjectMocks
	private DefaultProductVisibilityService defaultProductVisibilityService;

	@Mock
	private ProductVisibilityContextExtractor productVisibilityContextExtractor;

	@Mock
	private ProductVisibilityStrategyFactory productVisibilityStrategyFactory;

	@Mock
	private CatalogVersionModel catalogVersionModel;

	@Mock
	private ProductVisibilityContext visibilityContext;

	@Mock
	private ProductVisibilityStrategy productVisibilityStrategy;

	@Spy
	private ProductVisibilityData productVisibilityData;

	@Before
	public void setUp()
	{
		 MockitoAnnotations.initMocks(this);
		 when(productVisibilityContextExtractor.extractContext("code",catalogVersionModel)).thenReturn(visibilityContext);
		 when(productVisibilityStrategyFactory.createStrategy(visibilityContext)).thenReturn(productVisibilityStrategy);
		 when(productVisibilityStrategy.canApply(visibilityContext)).thenReturn(true);
		 when(productVisibilityStrategy.getProductVisibility(visibilityContext)).thenReturn(productVisibilityData);
	}

	@Test
	public void testGetVisibility()
	{
		 ProductVisibilityData visibiltyInfo = defaultProductVisibilityService.getVisibiltyInfo("code",catalogVersionModel);
		 assertEquals(visibiltyInfo,productVisibilityData);
	}

}
