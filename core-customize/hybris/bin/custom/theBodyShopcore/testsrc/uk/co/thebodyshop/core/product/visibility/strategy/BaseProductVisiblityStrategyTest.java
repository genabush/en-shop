/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.product.visibility.strategy;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
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
import de.hybris.platform.variants.model.VariantProductModel;

import uk.co.thebodyshop.core.model.TbsBaseProductModel;
import uk.co.thebodyshop.core.product.visibility.context.ProductVisibilityContext;
import uk.co.thebodyshop.core.product.visibility.data.ProductVisibilityData;

/**
 * @author vasanthramprakasam
 */
@UnitTest
public class BaseProductVisiblityStrategyTest
{
	 @InjectMocks
	 private BaseProductVisiblityStrategy baseProductVisiblityStrategy;
	 @Mock
	 private ProductVisibilityStrategy productVisibilityStrategy;
	 @Mock
	 private Function<ProductVisibilityContext, ProductVisibilityData> categoryRedirectFunction;

	 private ProductVisibilityContext visibilityContext;
	 @Spy
	 private TbsBaseProductModel baseProductModel;
	 @Spy
	 private VariantProductModel variantProductModel;
	 @Mock
	 private UserModel userModel;
	 @Mock
	 private CatalogVersionModel catalogVersionModel;
	 @Spy
	 private ProductVisibilityData productVisibilityData;

	 @Before
	 public void setUp()
	 {
			MockitoAnnotations.initMocks(this);
			visibilityContext = new ProductVisibilityContext(catalogVersionModel,baseProductModel,userModel,"baseProduct");
			baseProductModel.setVariants(Collections.singletonList(variantProductModel));
			baseProductModel.setCode("baseProduct");
			when(productVisibilityStrategy.getProductVisibility(any(ProductVisibilityContext.class))).thenReturn(productVisibilityData);
	 }

	 @Test
	 public void testGetVisibilityBaseProductFail()
	 {
	 	 productVisibilityData.setVisible(false);
	 	 when(categoryRedirectFunction.apply(visibilityContext)).thenReturn(productVisibilityData);
	 	 baseProductVisiblityStrategy.getProductVisibility(visibilityContext);
	 	 verify(categoryRedirectFunction,times(1)).apply(visibilityContext);
	 }

	 @Test
	 public void testGetVisibilityBaseProductSuccess()
	 {
			productVisibilityData.setVisible(true);
			when(categoryRedirectFunction.apply(visibilityContext)).thenReturn(productVisibilityData);
			baseProductVisiblityStrategy.getProductVisibility(visibilityContext);
			verify(categoryRedirectFunction,never()).apply(visibilityContext);
	 }
}
