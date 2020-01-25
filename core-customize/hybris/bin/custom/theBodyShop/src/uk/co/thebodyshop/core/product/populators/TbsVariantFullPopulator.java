/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.product.populators;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commercefacades.product.converters.populator.VariantFullPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.variants.model.VariantProductModel;

import uk.co.thebodyshop.core.model.TbsBaseProductModel;
import uk.co.thebodyshop.core.product.visibility.services.ProductVisibilityService;
import uk.co.thebodyshop.core.strategies.impl.VariantProductsSortStrategy;

/**
 * @author Marcin
 */
public class TbsVariantFullPopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends VariantFullPopulator<SOURCE, TARGET>
{
	private final VariantProductsSortStrategy variantProductsSortStrategy;

	private final ProductVisibilityService productVisibilityService;

	private final Supplier<Optional<CatalogVersionModel>> activeProductCatalogVersionSupplier;

	@Override
	public void populate(final SOURCE productModel, final TARGET productData) throws ConversionException
	{
		final ProductModel baseProduct = getBaseProduct(productModel);
		if (Objects.nonNull(baseProduct))
		{
			final String currentVariantCode = (!productModel.getCode().equals(baseProduct.getCode())) ? productModel.getCode() : StringUtils.EMPTY;
			// Populate the list of available child variant options
			if (baseProduct.getVariantType() != null && CollectionUtils.isNotEmpty(baseProduct.getVariants()))
			{
				final List<VariantOptionData> variantOptions = new ArrayList<>();
				final List<VariantProductModel> visibleVariants = baseProduct.getVariants().stream().filter(this::isVariantVisible).collect(Collectors.toList());
				for (final VariantProductModel variantProductModel : visibleVariants)
				{
					final VariantOptionData variantOptionData = getVariantOptionDataConverter().convert(variantProductModel);
					variantOptionData.setSelectedFlag(getSelectedFlag(currentVariantCode, variantOptionData.getCode()));
					variantOptions.add(variantOptionData);
				}
				if (CollectionUtils.isNotEmpty(variantOptions))
				{
					getVariantProductsSortStrategy().sortVariantProductsInProductPage(variantOptions, (TbsBaseProductModel) baseProduct);
				}
				productData.setVariantOptions(variantOptions);
				if (CollectionUtils.isNotEmpty(visibleVariants) && visibleVariants.size() >= 2)
				{
					productData.setMultiVariant(Boolean.TRUE);
				}
				if (CollectionUtils.isNotEmpty(visibleVariants))
				{
					productData.setVariants(String.join(", ", visibleVariants.stream().map(variant -> variant.getCode()).collect(Collectors.toList())));
				}
			}
		}
	}

	private boolean isVariantVisible(final VariantProductModel variantProductModel)
	{
		return getProductVisibilityService().getVisibiltyInfo(variantProductModel.getCode(), getActiveProductCatalogVersionSupplier().get().orElse(null)).isVisible();
	}

	private boolean getSelectedFlag(final String currentVariantCode, final String varintOptionCode)
	{
		if (StringUtils.isNotEmpty(currentVariantCode) && currentVariantCode.contentEquals(varintOptionCode))
		{
			return true;
		}
		return false;
	}

	private ProductModel getBaseProduct(final ProductModel currentProduct)
	{
		if (currentProduct instanceof VariantProductModel)
		{
			return ((VariantProductModel) currentProduct).getBaseProduct();
		}
		return currentProduct;
	}

	public TbsVariantFullPopulator(final VariantProductsSortStrategy variantProductsSortStrategy, final ProductVisibilityService productVisibilityService, final Supplier<Optional<CatalogVersionModel>> activeProductCatalogVersionSupplier)
	{
		this.variantProductsSortStrategy = variantProductsSortStrategy;
		this.productVisibilityService = productVisibilityService;
		this.activeProductCatalogVersionSupplier = activeProductCatalogVersionSupplier;
	}

	protected VariantProductsSortStrategy getVariantProductsSortStrategy()
	{
		return variantProductsSortStrategy;
	}

	protected ProductVisibilityService getProductVisibilityService()
	{
		return productVisibilityService;
	}

	protected Supplier<Optional<CatalogVersionModel>> getActiveProductCatalogVersionSupplier()
	{
		return activeProductCatalogVersionSupplier;
	}
}
