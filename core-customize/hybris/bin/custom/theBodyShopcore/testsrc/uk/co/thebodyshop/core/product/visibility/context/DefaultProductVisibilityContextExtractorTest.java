/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.product.visibility.context;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.product.daos.ProductDao;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

/**
 * @author vasanthramprakasam
 */
@UnitTest
public class DefaultProductVisibilityContextExtractorTest
{
	 @InjectMocks
	 private DefaultProductVisibilityContextExtractor contextExtractor;
	 @Mock
	 private  SessionService sessionService;
   @Mock
	 private  UserService userService;
	 @Mock
	 private  SearchRestrictionService searchRestrictionService;
	 @Mock
	 private  ProductDao productDao;
	 @Mock
	 private CatalogVersionModel catalogVersionModel;
   @Spy
	 private ProductModel productModel;
   @Spy
	 private UserModel userModel;

	 @Before
	 public void setUp()
	 {
			MockitoAnnotations.initMocks(this);
		  when(productDao.findProductsByCode(catalogVersionModel,"code")).thenReturn(Collections.singletonList(productModel));
		  when(sessionService.executeInLocalView(any(SessionExecutionBody.class))).thenReturn(productModel);
		  when(userService.getCurrentUser()).thenReturn(userModel);
	 }

	 @Test
	 public void testExtractContext()
	 {
	 	 final ProductVisibilityContext visibilityContext = contextExtractor.extractContext("code",catalogVersionModel);
	 	 assertEquals(visibilityContext.getCatalogVersion(),catalogVersionModel);
		 assertEquals(visibilityContext.getProduct(),productModel);
		 assertEquals(visibilityContext.getUser(),userModel);
	 }

}
