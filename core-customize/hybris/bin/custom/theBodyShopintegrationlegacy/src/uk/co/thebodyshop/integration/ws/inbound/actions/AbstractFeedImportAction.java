/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.ws.inbound.actions;

import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author vasanthramprakasam
 */
public abstract class AbstractFeedImportAction<T> implements FeedImportAction<T>
{

	protected static final Logger LOG = LoggerFactory.getLogger(AbstractFeedImportAction.class);

	private ModelService modelService;

	private EnumerationService enumerationService;

	public ModelService getModelService()
	{
		return modelService;
	}

	public void setModelService(ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Override
	public T execute(T feed)
	{
		try
		{
			Transaction.current().execute(new TransactionBody()
			{
				@Override
				public Boolean execute() throws Exception
				{
					executeInternal(feed);
					return Boolean.TRUE;
				}
			});
		}
		catch (Exception e)
		{
			LOG.error("Error while trying to import feed of type {}",feed.getClass().getSimpleName());
		}
		return feed;
	}

	protected abstract void executeInternal(T feed) throws Exception;

	public EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	public void setEnumerationService(EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}
}
