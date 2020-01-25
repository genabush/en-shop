/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.ws.predicates;

import de.hybris.bootstrap.annotations.UnitTest;

import javax.xml.bind.JAXBElement;

import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mercury.ws.v1.placeorder.HybrisResponse;
import com.mercury.ws.v1.placeorder.PlaceOrderResponse;

import uk.co.thebodyshop.integration.constants.TheBodyShopintegrationlegacyConstants;


/**
 * @author vasanthramprakasam
 */
@UnitTest
public class RetryOnErrorResponsePredicateTest
{
	public static final String SUCCESS = "SUCCESS";
	private RetryOnErrorResponsePredicate predicate;
	@Mock
	private JAXBElement<PlaceOrderResponse> placeOrderResponseJAXBElement;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		predicate = new RetryOnErrorResponsePredicate();
	}

	@Test
	public void testShouldNotRetry()
	{
		setUpObject(SUCCESS);
		Assert.assertFalse(predicate.test(placeOrderResponseJAXBElement));
	}

	@Test
	public void testShouldRetry()
	{
		setUpObject(TheBodyShopintegrationlegacyConstants.SERVICE_RESPONSE_STATUS.FAILED);
		Assert.assertTrue(predicate.test(placeOrderResponseJAXBElement));
		setUpObject(TheBodyShopintegrationlegacyConstants.SERVICE_RESPONSE_STATUS.LOCKED);
		Assert.assertTrue(predicate.test(placeOrderResponseJAXBElement));
	}

	private void setUpObject(String status)
	{
		PlaceOrderResponse placeOrderResponse = new PlaceOrderResponse();
		HybrisResponse hybrisResponse = new HybrisResponse();
		hybrisResponse.setStatus(status);
		placeOrderResponse.setReturn(hybrisResponse);
		when(placeOrderResponseJAXBElement.getValue()).thenReturn(placeOrderResponse);
	}
}
