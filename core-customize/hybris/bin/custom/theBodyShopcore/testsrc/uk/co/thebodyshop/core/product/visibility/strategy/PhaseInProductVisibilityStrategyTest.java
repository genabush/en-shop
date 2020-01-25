/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.product.visibility.strategy;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.user.UserModel;

import uk.co.thebodyshop.core.model.TbsVariantProductModel;
import uk.co.thebodyshop.core.product.visibility.context.ProductVisibilityContext;
import uk.co.thebodyshop.core.product.visibility.data.ProductVisibilityData;

/**
 * @author vasanthramprakasam
 */
@UnitTest
public class PhaseInProductVisibilityStrategyTest
{
	 @InjectMocks
	 private PhaseInProductVisibilityStrategy phaseInProductVisibilityStrategy;

   @Mock
	 private ProductVisibilityStrategy productVisibilityStrategy;

	 private ProductVisibilityContext visibilityContext;
	 @Spy
	 private TbsVariantProductModel variantProductModel;
	 @Mock
	 private CatalogVersionModel catalogVersionModel;
	 @Mock
	 private UserModel userModel;
	 @Spy
	 private ProductVisibilityData productVisibilityData;
	 @Mock
	 private Function<ProductVisibilityContext, ProductVisibilityData> productRedirectFunction;

	 @Before
	 public void setUp()
	 {
			MockitoAnnotations.initMocks(this);
			visibilityContext = new ProductVisibilityContext(catalogVersionModel,variantProductModel,userModel,"code");
			variantProductModel.setPhaseInProduct(variantProductModel);
		  when(productVisibilityStrategy.getProductVisibility(any(ProductVisibilityContext.class))).thenReturn(productVisibilityData);
	 }

	 @Test
	 public void testPhaseInVisibility()
	 {
	 	 //enable phase in product
	 	 productVisibilityData.setVisible(true);
	 	 when(productRedirectFunction.apply(any(ProductVisibilityContext.class))).thenReturn(productVisibilityData);
	 	 phaseInProductVisibilityStrategy.getProductVisibility(visibilityContext);
		 verify(productVisibilityStrategy,times(1)).getProductVisibility(any(ProductVisibilityContext.class));
		 verify(productRedirectFunction,times(1)).apply(any(ProductVisibilityContext.class));
	 }
}
