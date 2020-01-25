/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.cms.types.predicates;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.type.TypeService;

import uk.co.thebodyshop.cms.model.components.AmplienceCMSComponentModel;

/**
 * @author j.wong
 */
@UnitTest
public class AmplienceCMSComponentTypePredicateTest
{
	@InjectMocks
	private AmplienceCMSComponentTypePredicate amplienceCMSComponentTypePredicate;

	@Mock
	private TypeService typeService;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

	}

	@Test
	public void testSuccessForGivenAmplienceCMSComponentModel()
	{
		given(typeService.isAssignableFrom(AmplienceCMSComponentModel._TYPECODE, AmplienceCMSComponentModel._TYPECODE)).willReturn(true);
		assertTrue(amplienceCMSComponentTypePredicate.test(new AmplienceCMSComponentModel()));
	}

	@Test
	public void testFailureForNonAmplienceCMSComponentModel()
	{
		given(typeService.isAssignableFrom(AmplienceCMSComponentModel._TYPECODE, ProductModel._TYPECODE)).willReturn(false);
		assertFalse(amplienceCMSComponentTypePredicate.test(new ProductModel()));
	}
}
