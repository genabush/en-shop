/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.event;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.store.BaseStoreModel;

import uk.co.thebodyshop.core.catalog.services.TbsCatalogVersionService;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;
import uk.co.thebodyshop.core.services.PhaseInPhaseOutProductService;

/**
 * Event Listener for updating the phase out to phase in product status based on feed from S/4
 *
 * @author Balakrishna
 **/
public class PhaseInOutStockEventListener extends AbstractEventListener<PhaseInOutStockEvent>
{
	private static final Logger LOG = LoggerFactory.getLogger(PhaseInOutStockEventListener.class);

	private final PhaseInPhaseOutProductService phaseInPhaseOutProductService;

	private final TbsCatalogVersionService catalogVersionService;

	private final ProductService productService;

	public PhaseInOutStockEventListener(final PhaseInPhaseOutProductService phaseInPhaseOutProductService, final TbsCatalogVersionService catalogVersionService, final ProductService productService)
	{
		this.phaseInPhaseOutProductService = phaseInPhaseOutProductService;
		this.catalogVersionService = catalogVersionService;
		this.productService = productService;
	}

	@Override
	protected void onEvent(final PhaseInOutStockEvent phaseInOutStockEvent)
	{
		if (StringUtils.isEmpty(phaseInOutStockEvent.getProductCode()) || Objects.isNull(phaseInOutStockEvent.getWarehouseModel()))
		{
			LOG.error("Product code or Warehouse should not be null or empty.");
			return;
		}

		final WarehouseModel warehouseModel = phaseInOutStockEvent.getWarehouseModel();
		final Collection<BaseStoreModel> baseStores = warehouseModel.getBaseStores();
		if (CollectionUtils.isNotEmpty(baseStores))
		{
			final BaseStoreModel baseStore = baseStores.stream().findFirst().get();
			final Optional<CatalogModel> productCatalog = baseStore.getCatalogs().stream().filter(catalogModel -> !(catalogModel instanceof ContentCatalogModel)).findFirst();
			if (productCatalog.isPresent())
			{
				// updating both staged and online phaseIn and PhaseOut Products
				productCatalog.get().getCatalogVersions().stream().filter(catalogVersionModel -> getCatalogVersionService().isMarketProductCatalog(catalogVersionModel)).forEach(catalogVersionModel -> {
					final ProductModel product = getProductService().getProductForCode(catalogVersionModel, phaseInOutStockEvent.getProductCode());

					if (product instanceof TbsVariantProductModel)
					{
						final TbsVariantProductModel phaseOutProduct = (TbsVariantProductModel) product;

						final boolean success = getPhaseInPhaseOutProductService().changePhaseInOutProductStatus(phaseOutProduct);

						if (success)
						{
							final CMSSiteModel cmsSite = (CMSSiteModel) baseStores.stream().findFirst().get().getCmsSites().stream().findFirst().get();
							if (!getCatalogVersionService().isStagedCatalog(catalogVersionModel) && Objects.nonNull(cmsSite))
							{
								getPhaseInPhaseOutProductService().emailStockNotificationForPhaseInOutProduct(phaseOutProduct, cmsSite);
								getPhaseInPhaseOutProductService().replacePhaseOutWithPhaseInProductsInWishList(phaseOutProduct);
							}
						}
					}
				});
			}
		}
	}

	protected PhaseInPhaseOutProductService getPhaseInPhaseOutProductService()
	{
		return phaseInPhaseOutProductService;
	}

	protected TbsCatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	protected ProductService getProductService()
	{
		return productService;
	}
}
