/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.job;

import java.util.List;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import org.apache.log4j.Logger;

import uk.co.thebodyshop.core.model.AmplienceSyncCronJobModel;
import uk.co.thebodyshop.core.services.AmplienceImageSyncService;

public class AmplienceSyncJobPerformable extends AbstractJobPerformable<AmplienceSyncCronJobModel> {

	private static final Logger LOG = Logger.getLogger(AmplienceSyncJobPerformable.class);

	private final ProductService productService;
	private final AmplienceImageSyncService amplienceImageSyncService;

	public AmplienceSyncJobPerformable(final ProductService productService, final AmplienceImageSyncService amplienceImageSyncService) {
		this.productService = productService;
		this.amplienceImageSyncService = amplienceImageSyncService;
	}

	@Override
	public PerformResult perform(final AmplienceSyncCronJobModel amplienceSyncCronJob) {
		final StringBuilder syncMessage = new StringBuilder();
		final List<ProductModel> products = productService.getAllProductsForCatalogVersion(amplienceSyncCronJob.getCatalogVersion());

		for (final ProductModel product : products)
		{
			if (clearAbortRequestedIfNeeded(amplienceSyncCronJob))
			{
				return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.ABORTED);
			}
			try {
				getAmplienceImageSyncService().syncImagesForProduct(product, syncMessage);
			} catch (final Exception e) {
				LOG.error("Sync Error Message::"+syncMessage.toString(),e);
				return new PerformResult(CronJobResult.ERROR, CronJobStatus.FINISHED);
			}
		}

		LOG.info("SynchMessage::" + syncMessage);
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	@Override
	public boolean isAbortable()
	{
		return true;
	}

	protected ProductService getProductService() {
		return productService;
	}

	protected AmplienceImageSyncService getAmplienceImageSyncService() {
		return amplienceImageSyncService;
	}
}
