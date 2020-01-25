/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package uk.co.thebodyshop.fulfilmentprocess.jobs;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.model.ProcessTaskModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import uk.co.thebodyshop.fulfilmentprocess.constants.TheBodyShopFulfilmentProcessConstants;


/**
 * CronJob periodically send CleanUpEvent for <b>order-process</b> processes which are in action <b>waitForCleanUp</b>
 */
public class CleanUpFraudOrderJob extends AbstractJobPerformable<CronJobModel>
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(CleanUpFraudOrderJob.class);
	private BusinessProcessService businessProcessService;

	protected BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	@Required
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}

	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		final String processDefinitionName = TheBodyShopFulfilmentProcessConstants.ORDER_PROCESS_NAME;
		final String processCurrentAction = "waitForCleanUp";
		final List<BusinessProcessModel> processes = getAllProcessByDefinitionAndCurrentAction(processDefinitionName,
				processCurrentAction);

		final String eventNameSuffix = "_CleanUpEvent";
		for (final BusinessProcessModel bpm : processes)
		{
			//${process.code}_CleanUpEvent
			final String eventName = bpm.getCode() + eventNameSuffix;
			businessProcessService.triggerEvent(eventName);
		}
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	protected List<BusinessProcessModel> getAllProcessByDefinitionAndCurrentAction(final String processDefinitionName,
			final String processCurrentAction)
	{
		final String query = "select {bp:" + BusinessProcessModel.PK + "} FROM {" + BusinessProcessModel._TYPECODE + " AS bp JOIN " + ProcessTaskModel._TYPECODE + " AS pt ON {bp:" + BusinessProcessModel.PK + "} = {pt:" + ProcessTaskModel.PROCESS + "} } "
				+ "WHERE {bp:" + BusinessProcessModel.PROCESSDEFINITIONNAME + "} = ?processDefinitionName and {pt:" + ProcessTaskModel.ACTION + "} = ?processCurrentAction";

		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query);
		searchQuery.addQueryParameter("processDefinitionName", processDefinitionName);
		searchQuery.addQueryParameter("processCurrentAction", processCurrentAction);
		final SearchResult<BusinessProcessModel> processes = flexibleSearchService.search(searchQuery);
		return processes.getResult();
	}
}
