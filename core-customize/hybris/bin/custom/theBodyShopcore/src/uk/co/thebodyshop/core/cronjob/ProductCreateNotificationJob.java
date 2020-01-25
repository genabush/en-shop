/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.cronjob;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobHistoryModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.CronJobHistoryService;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.store.BaseStoreModel;

import uk.co.thebodyshop.core.catalog.services.TbsCatalogVersionService;
import uk.co.thebodyshop.core.model.ProductCreateNotificationCronjobModel;
import uk.co.thebodyshop.core.model.ProductCreateNotificationProcessModel;
import uk.co.thebodyshop.core.product.services.TbsProductService;

/**
 * @author prateek.goel
 */
public class ProductCreateNotificationJob extends AbstractJobPerformable<ProductCreateNotificationCronjobModel>
{

	private static final Logger LOG = Logger.getLogger(ProductCreateNotificationJob.class);

	private static final String PROCESS_NAME = "productCreateNotificationProcess";
	private static final String GLOBAL_CATALOG_NAME = "tbsGlobalProductCatalog";

	private TbsProductService tbsProductService;

	private BusinessProcessService businessProcessService;

	private TimeService timeService;

	private TbsCatalogVersionService tbsCatalogVersionService;

	private CronJobHistoryService cronJobHistoryService;

	@Override
	public PerformResult perform(final ProductCreateNotificationCronjobModel cronjob)
	{
		final Collection<BaseSiteModel> baseSites = cronjob.getSites();
		boolean isExecutionSuccessful = true;
		if (CollectionUtils.isNotEmpty(baseSites))
		{
			for (final BaseSiteModel site : baseSites)
			{
				if (clearAbortRequestedIfNeeded(cronjob))
				{
					return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.ABORTED);
				}
				if (CollectionUtils.isNotEmpty(site.getStores()))
				{
					final BaseStoreModel store = site.getStores().iterator().next();
					try
					{
						//global catalog: variants only
						final String marketCatalogId = ((CMSSiteModel) site).getDefaultCatalog().getId();
						if(marketCatalogId.contains(GLOBAL_CATALOG_NAME)){
							final CatalogVersionModel globalCatalogVersion = getTbsCatalogVersionService().getStagedGlobalProductCatalog();
							final List<String> variantProductCodes = getTbsProductService().fetchRecentlyCreatedVariantProductCodes(globalCatalogVersion, getLatestSuccessfulTime(cronjob.getCode()));
							if (CollectionUtils.isNotEmpty(variantProductCodes) && CollectionUtils.isNotEmpty(store.getProductCreateNotifyEmailList()))
							{
								isExecutionSuccessful = createAndStartProductCreateNotificationProcess(variantProductCodes, store, site, marketCatalogId);
							}
						}
						else {
							//Market catalog: Both Base & Variant
							final CatalogVersionModel catalogVersion = getTbsCatalogVersionService().getStagedMarketProductCatalog(marketCatalogId);
							final List<String> baseProductCodes = getTbsProductService().fetchRecentlyCreatedBaseProductCodes(catalogVersion, getLatestSuccessfulTime(cronjob.getCode()));
							final List<String> variantProductCodes = getTbsProductService().fetchRecentlyCreatedVariantProductCodes(catalogVersion, getLatestSuccessfulTime(cronjob.getCode()));
							final List<String> listOfVariantAndBaseProductCodes = Stream.of(baseProductCodes, variantProductCodes).flatMap(codes -> codes.stream()).collect(Collectors.toList());
							if (CollectionUtils.isNotEmpty(listOfVariantAndBaseProductCodes) && CollectionUtils.isNotEmpty(store.getProductCreateNotifyEmailList()))
							{
								isExecutionSuccessful = createAndStartProductCreateNotificationProcess(listOfVariantAndBaseProductCodes, store, site, marketCatalogId);
							}
						}
					}
					catch(final Exception e)
					{
						isExecutionSuccessful = false;
						LOG.error(e.getMessage());
					}
				}
			}
		}
		if (isExecutionSuccessful)
		{
			return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		}
		return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
	}

	protected boolean createAndStartProductCreateNotificationProcess(final List<String> codes, final BaseStoreModel store, final BaseSiteModel site, final String marketCatalogId)
	{
		final ProductCreateNotificationProcessModel process = businessProcessService.createProcess(String.format("%s-%s-%s", PROCESS_NAME, store.getUid(), timeService.getCurrentTime().getTime()), PROCESS_NAME);
		if (process != null)
		{
			process.setProductCodes(codes);
			process.setSite(site);
			process.setStore(store);
			process.setLanguage(site.getDefaultLanguage());
			process.setCatalogId(marketCatalogId);
			process.setEmailList(store.getProductCreateNotifyEmailList());
			modelService.save(process);
			businessProcessService.startProcess(process);
			return true;
		}
		return false;
	}

	/**
	 * This method is to get the last cronjob successful execution time
	 *
	 * @param cronjobCode
	 * @return
	 */
	private Date getLatestSuccessfulTime(final String cronjobCode)
	{
		Date suggestedTime = new Date();
		final List<CronJobHistoryModel> cronjobHistoryList = cronJobHistoryService.getCronJobHistoryBy(cronjobCode);
		if (CollectionUtils.isNotEmpty(cronjobHistoryList))
		{
			final List<CronJobHistoryModel> tobeSortedFeedJobHistoryList = new ArrayList<>(cronjobHistoryList);
			Collections.sort(tobeSortedFeedJobHistoryList, new Comparator<CronJobHistoryModel>()
			{
				public int compare(final CronJobHistoryModel jobHistory1, final CronJobHistoryModel jobHistory2)
				{
					return jobHistory1.getStartTime().compareTo(jobHistory2.getStartTime());
				}
			});
			Collections.reverse(tobeSortedFeedJobHistoryList);
			final Optional<CronJobHistoryModel> lastSuccessfulCronjobHistory = tobeSortedFeedJobHistoryList.stream().filter((stockJob) -> null != stockJob.getResult() && stockJob.getResult() == CronJobResult.SUCCESS).findFirst();
			if (lastSuccessfulCronjobHistory.isPresent())
			{
				suggestedTime = lastSuccessfulCronjobHistory.get().getStartTime();
			}
		}
		return suggestedTime;
	}

	@Override
	public boolean isAbortable()
	{
		return true;
	}

	public void setTbsProductService(final TbsProductService tbsProductService)
	{
		this.tbsProductService = tbsProductService;
	}

	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}

	public void setTimeService(final TimeService timeService)
	{
		this.timeService = timeService;
	}

	public void setTbsCatalogVersionService(final TbsCatalogVersionService tbsCatalogVersionService)
	{
		this.tbsCatalogVersionService = tbsCatalogVersionService;
	}

	protected static String getProcessName()
	{
		return PROCESS_NAME;
	}

	protected TbsProductService getTbsProductService()
	{
		return tbsProductService;
	}

	protected BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	protected TimeService getTimeService()
	{
		return timeService;
	}

	protected TbsCatalogVersionService getTbsCatalogVersionService()
	{
		return tbsCatalogVersionService;
	}

	protected CronJobHistoryService getCronJobHistoryService()
	{
		return cronJobHistoryService;
	}

	public void setCronJobHistoryService(final CronJobHistoryService cronJobHistoryService)
	{
		this.cronJobHistoryService = cronJobHistoryService;
	}

}
