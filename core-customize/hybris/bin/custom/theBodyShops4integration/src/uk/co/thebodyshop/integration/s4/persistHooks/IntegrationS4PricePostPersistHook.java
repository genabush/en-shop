/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.s4.persistHooks;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.odata2services.odata.persistence.hook.PostPersistHook;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import uk.co.thebodyshop.core.catalog.services.TbsCatalogVersionService;
import uk.co.thebodyshop.core.model.TbsBaseProductModel;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;
import uk.co.thebodyshop.integration.s4.model.IntegrationS4PriceModel;

/**
 * @author prateek.goel
 */
public class IntegrationS4PricePostPersistHook implements PostPersistHook
{
	private ModelService modelService;
	private TbsCatalogVersionService tbsCatalogVersionService;
	private ProductService productService;
	private SearchRestrictionService searchRestrictionService;
	private BaseStoreService baseStoreService;
	private Map<String, String> countryCatalogIdMap;
	private Map<String, String> countryBaseStoreIdMap;
	private Converter<IntegrationS4PriceModel, PriceRowModel> integrationPriceConverter;

	@Override
	public void execute(final ItemModel item)
	{
		if (item instanceof IntegrationS4PriceModel)
		{
			final IntegrationS4PriceModel model = (IntegrationS4PriceModel) item;
			final String marketCatalogId = getCountryCatalogIdMap().get(model.getCountry().toUpperCase());
			final BaseStoreModel baseStore = getBaseStoreService().getBaseStoreForUid(getCountryBaseStoreIdMap().get(model.getCountry().toUpperCase()));
			getSearchRestrictionService().disableSearchRestrictions();
			addorUpdatePriceRow(model, getTbsCatalogVersionService().getStagedMarketProductCatalog(marketCatalogId), baseStore);
			getSearchRestrictionService().enableSearchRestrictions();
			this.getModelService().remove(model);
		}
	}

	private void addorUpdatePriceRow(final IntegrationS4PriceModel model, final CatalogVersionModel marketProductCatalogVersion, final BaseStoreModel baseStore)
	{
		final ProductModel product = getProductService().getProductForCode(marketProductCatalogVersion, model.getProductCode());
		if (Objects.nonNull(product))
		{
			final PriceRowModel priceRow = retrievePriceRow(model, marketProductCatalogVersion, baseStore);
			getIntegrationPriceConverter().convert(model, priceRow);
			this.getModelService().save(priceRow);
		}
	}

	private PriceRowModel retrievePriceRow(final IntegrationS4PriceModel model, final CatalogVersionModel stagedMarketProductCatalog, final BaseStoreModel baseStore)
	{
		TbsVariantProductModel variant = null;
		variant = (TbsVariantProductModel) getProductService().getProductForCode(stagedMarketProductCatalog, model.getProductCode());
		final TbsBaseProductModel product = (TbsBaseProductModel) variant.getBaseProduct();
		product.setModifiedtime(new Date());
		getModelService().save(product);
		if (null == variant.getEurope1PriceFactory_PTG())
		{
			variant.setEurope1PriceFactory_PTG(baseStore.getDefaultProductTaxGroup());
		}
		PriceRowModel priceRow = getCurrentBasePrice(variant.getEurope1Prices(), baseStore);
		if (null == priceRow)
		{
			priceRow = getModelService().create(PriceRowModel.class);
			priceRow.setProductId(variant.getCode());
			priceRow.setUnit(variant.getUnit());
			priceRow.setMinqtd(Long.valueOf(1));
			priceRow.setUnitFactor(Integer.valueOf(1));
			priceRow.setNet(Boolean.valueOf(baseStore.isNet()));
			priceRow.setUg(baseStore.getBaseStoreUserPriceGroup());
		}
		return priceRow;
	}

	private PriceRowModel getCurrentBasePrice(final Collection<PriceRowModel> variantPriceRows, final BaseStoreModel baseStore)
	{
		if (CollectionUtils.isNotEmpty(variantPriceRows))
		{
			for (final PriceRowModel priceRow : variantPriceRows) {
				if (null != priceRow.getUg() && priceRow.getUg().equals(baseStore.getBaseStoreUserPriceGroup()))
				{
					return priceRow;
				}
			}
		}
		return null;
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

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	protected Map<String, String> getCountryCatalogIdMap()
	{
		return countryCatalogIdMap;
	}

	public void setCountryCatalogIdMap(final Map<String, String> countryCatalogIdMap)
	{
		this.countryCatalogIdMap = countryCatalogIdMap;
	}

	protected Map<String, String> getCountryBaseStoreIdMap()
	{
		return countryBaseStoreIdMap;
	}

	public void setCountryBaseStoreIdMap(final Map<String, String> countryBaseStoreIdMap)
	{
		this.countryBaseStoreIdMap = countryBaseStoreIdMap;
	}

	protected Converter<IntegrationS4PriceModel, PriceRowModel> getIntegrationPriceConverter()
	{
		return integrationPriceConverter;
	}

	public void setIntegrationPriceConverter(final Converter<IntegrationS4PriceModel, PriceRowModel> integrationPriceConverter)
	{
		this.integrationPriceConverter = integrationPriceConverter;
	}
}