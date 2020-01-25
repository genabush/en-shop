/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.ws.response.handlers;

import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.servicelayer.model.ModelService;


/**
 * @author vasanthramprakasam
 */
public abstract class AbstractResponseHandler<I,O> implements ResponseHandler<I,O>
{

	protected static final Logger LOG = LoggerFactory.getLogger(AbstractResponseHandler.class);

	private Predicate<Object> retryOnErrorPredicate;

	private ModelService modelService;

	@Override
	public boolean handleResponse(I input, O output)
	{
			final boolean error = getRetryOnErrorPredicate().test(output);
			if(error)
			{
				return false;
			}else{
				handleSuccessFulResponse(input,output);
				return true;
			}
	}

	protected abstract void handleSuccessFulResponse(I input, O output);

	protected Predicate<Object> getRetryOnErrorPredicate()
	{
		return retryOnErrorPredicate;
	}

	@Required
	public void setRetryOnErrorPredicate(Predicate<Object> retryOnErrorPredicate)
	{
		this.retryOnErrorPredicate = retryOnErrorPredicate;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(ModelService modelService)
	{
		this.modelService = modelService;
	}
}
