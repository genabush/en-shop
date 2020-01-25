/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.s4.persistHooks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.odata2services.odata.persistence.hook.PostPersistHook;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;

import uk.co.thebodyshop.core.catalog.services.TbsCatalogVersionService;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;
import uk.co.thebodyshop.core.retention.impl.DefaultCustomerCleanupRelatedObjectsAction;
import uk.co.thebodyshop.integration.s4.model.IntegrationS4ProductModel;

/**
 * @author prateek.goel
 */
public class IntegrationS4ProductPostPersistHook implements PostPersistHook
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultCustomerCleanupRelatedObjectsAction.class);

	private ModelService modelService;
	private TbsCatalogVersionService tbsCatalogVersionService;
	private ProductService productService;
	private SearchRestrictionService searchRestrictionService;
	private ConfigurationService configurationService;
	private Converter<IntegrationS4ProductModel, TbsVariantProductModel> integrationVariantProductConverter;

	private static final String DD_MM_YYYY = "dd-MM-yyyy";
	private static final String DEFAULT_ONLINE_DATE = "01-01-2019";
	private static final String DEFAULT_EAN = "defaultEAN";
	private static final String DUMMY_BASE_PRODUCT_CODE = "tbs.dummy.baseproduct.code";

	@Override
	public void execute(final ItemModel item)
	{
		if (item instanceof IntegrationS4ProductModel)
		{
			final IntegrationS4ProductModel s4Product = (IntegrationS4ProductModel) item;

			if (LOG.isInfoEnabled())
			{
				LOG.info("Creating/updating variant product [{}] from S/4", s4Product.getCode());
			}

			createOrUpdateProduct(s4Product);
		}
	}

	private void createOrUpdateProduct(final IntegrationS4ProductModel s4Product)
	{
		getSearchRestrictionService().disableSearchRestrictions();
		final TbsVariantProductModel variant = retrieveVariant(s4Product);
		getIntegrationVariantProductConverter().convert(s4Product, variant);
		getModelService().save(variant);
		getModelService().remove(s4Product);
		getSearchRestrictionService().enableSearchRestrictions();
	}

	private TbsVariantProductModel retrieveVariant(final IntegrationS4ProductModel s4Product)
	{
		final CatalogVersionModel globalStagedCatalog = getTbsCatalogVersionService().getStagedGlobalProductCatalog();

		TbsVariantProductModel variant = null;
		try
		{
			variant = (TbsVariantProductModel) getProductService().getProductForCode(globalStagedCatalog, s4Product.getCode());
		}
		catch (final Exception e)
		{
			if (LOG.isInfoEnabled())
			{
				LOG.info("Creating a brand new variant product [{}] from S/4", s4Product.getCode());
			}

			variant = getModelService().create(TbsVariantProductModel.class);
			variant.setCatalogVersion(globalStagedCatalog);

			setDummyDataforMandatoryFields(variant, globalStagedCatalog);
		}
		return variant;
	}

	private void setDummyDataforMandatoryFields(final TbsVariantProductModel variant, final CatalogVersionModel catalogVersion)
	{
		try
		{
			final SimpleDateFormat sdf = new SimpleDateFormat(DD_MM_YYYY);
			final Date onlineDate = sdf.parse(DEFAULT_ONLINE_DATE);
			variant.setOnlineDate(onlineDate);
		}
		catch (final ParseException e1)
		{
			LOG.error("Error in parsing the date [{}] using the format [{}]", DEFAULT_ONLINE_DATE, DD_MM_YYYY);
		}

		final String dummyBaseProductCode = getConfigurationService().getConfiguration().getString(DUMMY_BASE_PRODUCT_CODE);
		final ProductModel baseProduct = getProductService().getProductForCode(catalogVersion, dummyBaseProductCode);

		variant.setBaseProduct(baseProduct);
		variant.setApprovalStatus(ArticleApprovalStatus.READYTOBELOCALISED);
		variant.setMainEan(DEFAULT_EAN);
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected TbsCatalogVersionService getTbsCatalogVersionService()
	{
		return tbsCatalogVersionService;
	}

	public void setTbsCatalogVersionService(final TbsCatalogVersionService tbsCatalogVersionService)
	{
		this.tbsCatalogVersionService = tbsCatalogVersionService;
	}

	protected ProductService getProductService()
	{
		return productService;
	}

	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	protected SearchRestrictionService getSearchRestrictionService()
	{
		return searchRestrictionService;
	}

	public void setSearchRestrictionService(final SearchRestrictionService searchRestrictionService)
	{
		this.searchRestrictionService = searchRestrictionService;
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	protected Converter<IntegrationS4ProductModel, TbsVariantProductModel> getIntegrationVariantProductConverter()
	{
		return integrationVariantProductConverter;
	}

	public void setIntegrationVariantProductConverter(final Converter<IntegrationS4ProductModel, TbsVariantProductModel> integrationVariantProductConverter)
	{
		this.integrationVariantProductConverter = integrationVariantProductConverter;
	}
}
