/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.ws.predicates;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Predicate;

import javax.xml.bind.JAXBElement;

import org.apache.log4j.Logger;

import uk.co.thebodyshop.integration.constants.TheBodyShopintegrationlegacyConstants;


/**
 * @author vasanthramprakasam
 */
public class RetryOnErrorResponsePredicate implements Predicate<Object>
{

	private static final Logger LOG = Logger.getLogger(RetryOnErrorResponsePredicate.class);

	@Override
	public boolean test(Object response)
	{
		try
		{
			Object responseObject = response;
			if (response instanceof JAXBElement)
			{
				responseObject = ((JAXBElement) response).getValue();
			}

			final Method methodGetReturn = responseObject.getClass().getMethod("getReturn");
			final Object returnResponse = methodGetReturn.invoke(responseObject);
			if (returnResponse != null)
			{
				//TODO add check for LoyaltyPointsTransactionResponse as in SuccessfulWSCallTask in legacy tbsdataprocessing extension
				final Method methodGetStatus = returnResponse.getClass().getMethod("getStatus");
				final Object responseStatus = methodGetStatus.invoke(returnResponse);

				if (TheBodyShopintegrationlegacyConstants.SERVICE_RESPONSE_STATUS.FAILED.equals(responseStatus) || TheBodyShopintegrationlegacyConstants.SERVICE_RESPONSE_STATUS.LOCKED.equals(responseStatus))
				{
					return true;
				}
			}
		}
		catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			LOG.error(e);
		}
		return false;
	}
}
