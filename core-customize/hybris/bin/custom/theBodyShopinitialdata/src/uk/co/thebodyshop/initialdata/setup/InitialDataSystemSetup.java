/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package uk.co.thebodyshop.initialdata.setup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemCronJobModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.catalog.synchronization.CatalogSynchronizationService;
import de.hybris.platform.commerceservices.dataimport.impl.CoreDataImportService;
import de.hybris.platform.commerceservices.dataimport.impl.SampleDataImportService;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.commerceservices.setup.events.CoreDataImportedEvent;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.util.Config;

import uk.co.thebodyshop.initialdata.constants.TheBodyShopInitialDataConstants;

/**
 * This class provides hooks into the system's initialization and update processes.
 */
@SystemSetup(extension = TheBodyShopInitialDataConstants.EXTENSIONNAME)
public class InitialDataSystemSetup extends AbstractSystemSetup
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(InitialDataSystemSetup.class);

	private static final String IMPORT_CORE_DATA = "importCoreData";
	private static final String IMPORT_SAMPLE_DATA = "importSampleData";
	private static final String ACTIVATE_SOLR_CRON_JOBS = "activateSolrCronJobs";

	private static final String STORE = "Store";
	private static final String STAGED_CATALOG_VERSION = "Staged";
	private static final String TBS_GLOBAL_PRODUCT_CATALOG = "tbsGlobalProductCatalog";
	private static final String GLOBAL_STORE_CONSTANT = "tbsGlobal";
	private static final String GLOBAL_SITE_ID = "thebodyshop-global";
	private static final String AU_STORE_CONSTANT = "tbsAU";
	private static final String AU_SITE_ID = "thebodyshop-au";
	private static final String UK_STORE_CONSTANT = "tbsUK";
	private static final String UK_SITE_ID = "thebodyshop-uk";
	private static final String CA_STORE_CONSTANT = "tbsCA";
	private static final String CA_SITE_ID = "thebodyshop-ca";
	private static final String DE_STORE_CONSTANT = "tbsDE";
	private static final String DE_SITE_ID = "thebodyshop-de";
	private static final String DK_STORE_CONSTANT = "tbsDK";
	private static final String DK_SITE_ID = "thebodyshop-dk";
	private static final String ES_STORE_CONSTANT = "tbsES";
	private static final String ES_SITE_ID = "thebodyshop-es";
	private static final String FR_STORE_CONSTANT = "tbsFR";
	private static final String FR_SITE_ID = "thebodyshop-fr";
	private static final String NL_STORE_CONSTANT = "tbsNL";
	private static final String NL_SITE_ID = "thebodyshop-nl";
	private static final String SE_STORE_CONSTANT = "tbsSE";
	private static final String SE_SITE_ID = "thebodyshop-se";
	private static final String US_STORE_CONSTANT = "tbsUS";
	private static final String US_SITE_ID = "thebodyshop-us";
	private static final String AT_STORE_CONSTANT = "tbsAT";
	private static final String AT_SITE_ID = "thebodyshop-at";
	private static final String HK_STORE_CONSTANT = "tbsHK";
	private static final String HK_SITE_ID = "thebodyshop-hk";
	private static final String SG_STORE_CONSTANT = "tbsSG";
	private static final String SG_SITE_ID = "thebodyshop-sg";

	private CoreDataImportService coreDataImportService;
	private SampleDataImportService sampleDataImportService;
	private CatalogSynchronizationService catalogSynchronizationService;
	private CronJobService cronJobService;

	/**
	 * Generates the Dropdown and Multi-select boxes for the project data import
	 */
	@Override
	@SystemSetupParameterMethod
	public List<SystemSetupParameter> getInitializationOptions()
	{
		final List<SystemSetupParameter> params = new ArrayList<>();

		params.add(createBooleanSystemSetupParameter(IMPORT_CORE_DATA, "Import Core Data", true));
		params.add(createBooleanSystemSetupParameter(IMPORT_SAMPLE_DATA, "Import Sample Data", true));
		params.add(createBooleanSystemSetupParameter(ACTIVATE_SOLR_CRON_JOBS, "Activate Solr Cron Jobs", true));
		// Add more Parameters here as you require

		return params;
	}

	/**
	 * Implement this method to create initial objects. This method will be called by system creator during initialization and system update. Be sure that this method can be called repeatedly.
	 *
	 * @param context
	 *          the context provides the selected parameters and values
	 */
	@SystemSetup(type = Type.ESSENTIAL, process = Process.ALL)
	public void createEssentialData(final SystemSetupContext context)
	{
	}

	@SystemSetup(type = Type.ESSENTIAL, process = Process.UPDATE)
	public void createEssentialDataOnUpdate(final SystemSetupContext context)
	{
		// configure the storefront whitelist urls used in SmartEdit
		importImpexFile(context, "/theBodyShopinitialdata/import/coredata/common/configure-smartedit.impex");
	}

	/**
	 * Implement this method to create data that is used in your project. This method will be called during the system initialization. <br>
	 * Add import data for each site you have configured
	 *
	 * <pre>
	 * final List<ImportData> importData = new ArrayList<ImportData>();
	 *
	 * final ImportData sampleImportData = new ImportData();
	 * sampleImportData.setProductCatalogName(SAMPLE_PRODUCT_CATALOG_NAME);
	 * sampleImportData.setContentCatalogNames(Arrays.asList(SAMPLE_CONTENT_CATALOG_NAME));
	 * sampleImportData.setStoreNames(Arrays.asList(SAMPLE_STORE_NAME));
	 * importData.add(sampleImportData);
	 *
	 * getCoreDataImportService().execute(this, context, importData);
	 * getEventService().publishEvent(new CoreDataImportedEvent(context, importData));
	 *
	 * getSampleDataImportService().execute(this, context, importData);
	 * getEventService().publishEvent(new SampleDataImportedEvent(context, importData));
	 * </pre>
	 *
	 * @param context
	 *          the context provides the selected parameters and values
	 */
	@SystemSetup(type = Type.PROJECT, process = Process.ALL)
	public void createProjectData(final SystemSetupContext context)
	{
		// import common essential data
		importImpexFile(context, "/theBodyShopinitialdata/import/common/essential-data.impex");

		// import default user groups
		importImpexFile(context, "/theBodyShopinitialdata/import/sampledata/users/user-groups.impex");

		// import TBS store data
		importProjectData(context, GLOBAL_STORE_CONSTANT, GLOBAL_SITE_ID);
		importProjectData(context, AU_STORE_CONSTANT, AU_SITE_ID);
		importProjectData(context, UK_STORE_CONSTANT, UK_SITE_ID);
		importProjectData(context, CA_STORE_CONSTANT, CA_SITE_ID);
		importProjectData(context, DE_STORE_CONSTANT, DE_SITE_ID);
		importProjectData(context, DK_STORE_CONSTANT, DK_SITE_ID);
		importProjectData(context, ES_STORE_CONSTANT, ES_SITE_ID);
		importProjectData(context, FR_STORE_CONSTANT, FR_SITE_ID);
		importProjectData(context, NL_STORE_CONSTANT, NL_SITE_ID);
		importProjectData(context, SE_STORE_CONSTANT, SE_SITE_ID);
		importProjectData(context, US_STORE_CONSTANT, US_SITE_ID);
		importProjectData(context, AT_STORE_CONSTANT, AT_SITE_ID);
		importProjectData(context, HK_STORE_CONSTANT, HK_SITE_ID);
		importProjectData(context, SG_STORE_CONSTANT, SG_SITE_ID);

		// import user group permissions
		importImpexFile(context, "/theBodyShopinitialdata/import/sampledata/users/user-group-permissions.impex");

		// import cronjob data having all the sites
		importImpexFile(context, "/theBodyShopinitialdata/import/common/cronjobs.impex");

		// import market selector sites
		importImpexFile(context, "/theBodyShopinitialdata/import/common/market-selector-sites.impex");

		// import the CMS preview urls used in SmartEdit
		importImpexFile(context, "/theBodyShopinitialdata/import/common/set-up-cms-preview-urls.impex");

		// configure the storefront whitelist urls used in SmartEdit
		importImpexFile(context, "/theBodyShopinitialdata/import/common/configure-smartedit.impex");

		// import common cms impex
		importImpexFile(context, "/theBodyShopinitialdata/import/common/common-cms.impex");

		syncContentCatalogForId(GLOBAL_STORE_CONSTANT + "ContentCatalog");
	}

	private void importProjectData(final SystemSetupContext context, final String STORE_CONSTANT, final String SITE_UID)
	{
		final List<ImportData> importData = new ArrayList<>();

		final ImportData sampleImportData = new ImportData();
		sampleImportData.setProductCatalogName(STORE_CONSTANT);
		sampleImportData.setContentCatalogNames(Arrays.asList(STORE_CONSTANT));
		sampleImportData.setStoreNames(Arrays.asList(STORE_CONSTANT + STORE));
		importData.add(sampleImportData);

		final List<ImportData> eventImportData = new ArrayList<>();
		final ImportData sampleEventImportData = new ImportData();
		sampleEventImportData.setProductCatalogName(STORE_CONSTANT);
		sampleEventImportData.setContentCatalogNames(Arrays.asList(STORE_CONSTANT));
		sampleEventImportData.setStoreNames(Arrays.asList(SITE_UID));
		eventImportData.add(sampleEventImportData);

		getCoreDataImportService().execute(this, context, importData);

		if (!STORE_CONSTANT.contentEquals(GLOBAL_STORE_CONSTANT))
		{
			executeGlobalProductCatalogSyncJob(STORE_CONSTANT + "ProductCatalog");
		}

		getSampleDataImportService().execute(this, context, importData);

		if (Config.getBoolean("gigya.import.data", false))
		{
			LOG.info("Importing addon data");
			getEventService().publishEvent(new CoreDataImportedEvent(context, eventImportData));
		}
	}

	public PerformResult executeGlobalProductCatalogSyncJob(final String targetCatalogId)
	{
		final SyncItemJobModel catalogSyncJob = getSyncJobForProductCatalogs(TBS_GLOBAL_PRODUCT_CATALOG, targetCatalogId);
		if (catalogSyncJob == null)
		{
			LOG.error("Couldn't find 'SyncItemJob' for source catalog [tbsGlobalProductCatalog:Staged] and targetCatalog [" + targetCatalogId + ":Staged]", null);
			return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.UNKNOWN);
		}
		else
		{
			if (CollectionUtils.isNotEmpty(catalogSyncJob.getCronJobs()))
			{
				final SyncItemCronJobModel syncJob = (SyncItemCronJobModel) catalogSyncJob.getCronJobs().iterator().next();

				LOG.info("Starting synchronization, this may take a while ...");

				getCronJobService().performCronJob(syncJob, true);

				LOG.info("Synchronization complete for source catalog [tbsGlobalProductCatalog:Staged] and targetCatalog [" + targetCatalogId + ":Staged]");

				final CronJobResult result = syncJob.getResult();
				final CronJobStatus status = syncJob.getStatus();

				return new PerformResult(result, status);
			}
			else
			{
				return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.UNKNOWN);
			}
		}
	}

	protected SyncItemJobModel getSyncJobForProductCatalogs(final String sourceCatalogId, final String targetCatalogId)
	{
		try
		{
			final CatalogVersionModel source = getCatalogVersionService().getCatalogVersion(sourceCatalogId, STAGED_CATALOG_VERSION);
			final CatalogVersionModel target = getCatalogVersionService().getCatalogVersion(targetCatalogId, STAGED_CATALOG_VERSION);

			return getCatalogSynchronizationService().getSyncJob(source, target, null);
		}
		catch (UnknownIdentifierException | AmbiguousIdentifierException e)
		{
			LOG.error(String.format("CatalogVersions[sourceCatalogId=%s, targetCatalogVersion=%$] cannot be found", sourceCatalogId, targetCatalogId), e);
		}
		return null;
	}

	private void syncContentCatalogForId(final String catalogId)
	{
		getSetupSyncJobService().executeCatalogSyncJob(catalogId);
	}

	public CoreDataImportService getCoreDataImportService()
	{
		return coreDataImportService;
	}

	@Required
	public void setCoreDataImportService(final CoreDataImportService coreDataImportService)
	{
		this.coreDataImportService = coreDataImportService;
	}

	public SampleDataImportService getSampleDataImportService()
	{
		return sampleDataImportService;
	}

	@Required
	public void setSampleDataImportService(final SampleDataImportService sampleDataImportService)
	{
		this.sampleDataImportService = sampleDataImportService;
	}

	/**
	 * @return the catalogSynchronizationService
	 */
	public CatalogSynchronizationService getCatalogSynchronizationService()
	{
		return catalogSynchronizationService;
	}

	/**
	 * @param catalogSynchronizationService
	 *          the catalogSynchronizationService to set
	 */
	public void setCatalogSynchronizationService(final CatalogSynchronizationService catalogSynchronizationService)
	{
		this.catalogSynchronizationService = catalogSynchronizationService;
	}

	/**
	 * @return the cronjobService
	 */
	public CronJobService getCronJobService()
	{
		return cronJobService;
	}

	/**
	 * @param cronjobService
	 *          the cronjobService to set
	 */
	public void setCronJobService(final CronJobService cronJobService)
	{
		this.cronJobService = cronJobService;
	}
}
