/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package uk.co.thebodyshop.core.queues.cronjob;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.Date;

import uk.co.thebodyshop.core.model.ProductExpressUpdateCleanerCronJobModel;
import uk.co.thebodyshop.core.queues.impl.ProductExpressUpdateQueue;


/**
 * A Cron Job for cleaning up {@link ProductExpressUpdateQueue}.
 */
public class ProductExpressUpdateCleanerJob extends AbstractJobPerformable<ProductExpressUpdateCleanerCronJobModel>
{
	private ProductExpressUpdateQueue productExpressUpdateQueue;

	@Override
	public PerformResult perform(final ProductExpressUpdateCleanerCronJobModel cronJob)
	{
		long result=(cronJob.getQueueTimeLimit().intValue() * 60 * 1000);
		final Date timestamp = new Date(System.currentTimeMillis() - result);
		productExpressUpdateQueue.removeItems(timestamp);
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	public void setProductExpressUpdateQueue(final ProductExpressUpdateQueue productExpressUpdateQueue)
	{
		this.productExpressUpdateQueue = productExpressUpdateQueue;
	}
}
