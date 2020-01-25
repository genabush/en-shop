/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.product.populators;

import java.math.BigDecimal;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.Assert;

import com.microsoft.sqlserver.jdbc.StringUtils;

import de.hybris.platform.commercefacades.product.converters.populator.VariantOptionDataPopulator;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.variants.model.VariantProductModel;

import uk.co.thebodyshop.core.helpers.VariantDataHelper;
import uk.co.thebodyshop.core.model.TbsBaseProductModel;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;

/**
 * @author Marcin
 */
public class TbsVariantOptionDataPopulator extends VariantOptionDataPopulator
{

	private VariantDataHelper variantDataHelper;

	private BaseStoreService baseStoreService;

	private Populator<ProductModel, VariantOptionData> tbsVariantOptionGalleryImagesPopulator;

	@Override
	public void populate(final VariantProductModel source, final VariantOptionData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (source.getBaseProduct() != null)
		{
			target.setName(source.getName());
			target.setCode(source.getCode());
			target.setUrl(getProductModelUrlResolver().resolve(source));
			target.setStock(getStockConverter().convert(source));

			if (source instanceof TbsVariantProductModel)
			{
				final BaseStoreModel baseStore = getBaseStoreService().getCurrentBaseStore();

				final TbsVariantProductModel tbsVariant = ((TbsVariantProductModel) source);

				target.setSize(getVariantDataHelper().getFormattedSizeValue(tbsVariant));
				target.setSizeForSort(getVariantDataHelper().getSizeForSort(tbsVariant));
				target.setVariantType(getVariantType(tbsVariant));
				target.setTechnicalIngredients(tbsVariant.getTechnicalIngredients());
				target.setColourPosition(tbsVariant.getColourPosition());

				final Integer productMaxOrderQuantity = tbsVariant.getMaxOrderQuantity();
				target.setMaxOrderProductQuantity((!Objects.isNull(productMaxOrderQuantity) && productMaxOrderQuantity > 0) ? productMaxOrderQuantity : baseStore.getMaxProductOrderQuantity());

				if (null != tbsVariant.getEmailMeWhenInStockToggle())
				{
					target.setEmailMeWhenInStockToggle(tbsVariant.getEmailMeWhenInStockToggle());
				}

				populatePriceData(tbsVariant, target, baseStore);

				populateVariantColourAttributes(target, tbsVariant);

				getTbsVariantOptionGalleryImagesPopulator().populate(source, target);
			}
		}
	}

	private void populatePriceData(final TbsVariantProductModel source, final VariantOptionData target, final BaseStoreModel baseStore)
	{
		final PriceDataType priceType;
		final PriceInformation info;
		if (CollectionUtils.isEmpty(source.getVariants()))
		{
			priceType = PriceDataType.BUY;
			info = getCommercePriceService().getWebPriceForProduct(source);
		}
		else
		{
			priceType = PriceDataType.FROM;
			info = getCommercePriceService().getFromPriceForProduct(source);
		}

		if (info != null)
		{

			final PriceData priceData = getPriceDataFactory().create(priceType, BigDecimal.valueOf(info.getPriceValue().getValue()), info.getPriceValue().getCurrencyIso());
			priceData.setPricePerMetric(getVariantDataHelper().getPricePerMetric(source, baseStore, priceData.getValue()));
			priceData.setLoyaltyPoints(getVariantDataHelper().getLoyaltyPoints(source, baseStore, priceData.getValue()));
			target.setPriceData(priceData);
		}
	}

	private void populateVariantColourAttributes(final VariantOptionData target, final TbsVariantProductModel variant)
	{
		if (Objects.nonNull(variant.getColour()))
		{
			target.setColour(variant.getColour().getHexCode());
			target.setColourName(variant.getColour().getName());
		}
	}

	private String getVariantType(final TbsVariantProductModel variant)
	{
		if (Objects.nonNull(((TbsBaseProductModel) variant.getBaseProduct()).getType()))
		{
			return ((TbsBaseProductModel) variant.getBaseProduct()).getType().getCode();
		}
		return StringUtils.EMPTY;
	}

	/**
	 * @return the variantDataHelper
	 */
	protected VariantDataHelper getVariantDataHelper()
	{
		return variantDataHelper;
	}

	/**
	 * @param variantDataHelper
	 *          the variantDataHelper to set
	 */
	public void setVariantDataHelper(final VariantDataHelper variantDataHelper)
	{
		this.variantDataHelper = variantDataHelper;
	}

	/**
	 * @return the baseStoreService
	 */
	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	/**
	 * @param baseStoreService
	 *          the baseStoreService to set
	 */
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	protected Populator<ProductModel, VariantOptionData> getTbsVariantOptionGalleryImagesPopulator()
	{
		return tbsVariantOptionGalleryImagesPopulator;
	}

	public void setTbsVariantOptionGalleryImagesPopulator(final Populator<ProductModel, VariantOptionData> tbsVariantOptionGalleryImagesPopulator)
	{
		this.tbsVariantOptionGalleryImagesPopulator = tbsVariantOptionGalleryImagesPopulator;
	}
}
