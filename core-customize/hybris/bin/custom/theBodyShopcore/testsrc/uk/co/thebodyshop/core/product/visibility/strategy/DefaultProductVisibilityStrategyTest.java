/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.product.visibility.strategy;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.product.ProductModel;

import uk.co.thebodyshop.core.product.visibility.context.ProductVisibilityContext;
import uk.co.thebodyshop.core.product.visibility.data.ProductVisibilityData;
import uk.co.thebodyshop.core.product.visibility.functions.PredicatedFunction;

/**
 * @author vasanthramprakasam
 */
@UnitTest
public class DefaultProductVisibilityStrategyTest
{
	 private DefaultProductVisibilityStrategy productVisibilityStrategy;
	 @Mock
	 private PredicatedFunction<ProductVisibilityContext, ProductVisibilityData> predicatedFunction;
	 @Mock
	 private ProductVisibilityContext visibilityContext;
	 @Mock
	 private ProductVisibilityData productVisibilityData;
	 @Spy
	 private ProductModel productModel;

	 @Before
	 public void setUp()
	 {
	 	 MockitoAnnotations.initMocks(this);
	 	 productVisibilityStrategy = new DefaultProductVisibilityStrategy(Collections.singletonList(predicatedFunction));
	 	 productModel.setCode("code");
	 	 when(visibilityContext.getProduct()).thenReturn(productModel);
	 }

	 @Test
	 public void testProductNotVisible()
	 {
			when(predicatedFunction.applyIfValid(visibilityContext)).thenReturn(productVisibilityData);
			ProductVisibilityData visibilityData = productVisibilityStrategy.getProductVisibility(visibilityContext);
			assertEquals(visibilityData,productVisibilityData);
	 }

	 @Test
	 public void testProductVisible()
	 {
	 	 when(predicatedFunction.applyIfValid(visibilityContext)).thenReturn(null);
	 	 ProductVisibilityData visibilityData = productVisibilityStrategy.getProductVisibility(visibilityContext);
	 	 assertEquals(visibilityData.isVisible(),true);
	 }
}
