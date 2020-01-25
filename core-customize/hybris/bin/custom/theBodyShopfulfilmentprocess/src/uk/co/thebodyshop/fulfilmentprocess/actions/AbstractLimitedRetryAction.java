/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.fulfilmentprocess.actions;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.processengine.action.AbstractAction;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.model.ProcessTaskModel;
import de.hybris.platform.task.RetryLaterException;


/**
 * @author vasanthramprakasam
 */
public abstract class AbstractLimitedRetryAction<T extends BusinessProcessModel> extends AbstractAction<T>
{

	private static final Logger LOG = LoggerFactory.getLogger(AbstractLimitedRetryAction.class);

	private int maxRetries;

	private boolean failAfterAllRetries;

	private Long retryDelay;

	private RetryLaterException.Method retryMethod;

	public final String execute(T process) throws RetryLaterException, Exception
	{
		try
		{
			executeAction(process);
		}catch (RetryLaterException rle) {
			final boolean canRetry = canRetry(process);
			if(canRetry)
			{
				rle.setDelay(getRetryDelay());
				rle.setMethod(getRetryMethod());
				throw rle;
			}
			if(isFailAfterAllRetries())
			{
				LOG.error("All retries consumed on process {}, will not retry",process.getCode());
				return Transition.ERROR.toString();
			}
		}
		return Transition.OK.toString();
	}

	protected boolean canRetry(T process)
	{
		final Optional<ProcessTaskModel> currentTask = process.getCurrentTasks().stream().findFirst();
		if(currentTask.isEmpty())
		{
			LOG.error("No task found on the process");
			return true;
		}
		return Optional.ofNullable(currentTask.get().getRetry()).orElse(0) + 1 <= getMaxRetries();
	}

	@Override
	public Set<String> getTransitions()
	{
		return Transition.getStringValues();
	}

	public abstract void executeAction(T process) throws RetryLaterException, Exception;

	public enum Transition
	{
		OK,ERROR;

		public static Set<String> getStringValues()
		{
			final Set<String> res = new HashSet<>();
			for (final Transition transition : Transition.values())
			{
				res.add(transition.toString());
			}
			return res;
		}
	}

	public int getMaxRetries()
	{
		return maxRetries;
	}

	@Required
	public void setMaxRetries(int maxRetries)
	{
		this.maxRetries = maxRetries;
	}

	public boolean isFailAfterAllRetries()
	{
		return failAfterAllRetries;
	}

	@Required
	public void setFailAfterAllRetries(boolean failAfterAllRetries)
	{
		this.failAfterAllRetries = failAfterAllRetries;
	}

	public Long getRetryDelay()
	{
		return retryDelay;
	}

	@Required
	public void setRetryDelay(Long retryDelay)
	{
		this.retryDelay = retryDelay;
	}

	public RetryLaterException.Method getRetryMethod()
	{
		return retryMethod;
	}

	@Required
	public void setRetryMethod(RetryLaterException.Method retryMethod)
	{
		this.retryMethod = retryMethod;
	}
}
