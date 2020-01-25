/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.storefinder.impl;

import java.util.ArrayList;
import java.util.List;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.storefinder.data.StoreFinderStockSearchPageData;
import de.hybris.platform.commercefacades.storefinder.impl.DefaultStoreFinderStockFacade;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceStockData;
import de.hybris.platform.commercefacades.storelocator.data.StoreStockHolder;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.store.data.GeoPoint;
import de.hybris.platform.commerceservices.storefinder.data.PointOfServiceDistanceData;
import de.hybris.platform.commerceservices.storefinder.data.StoreFinderSearchPageData;
import de.hybris.platform.store.BaseStoreModel;

/**
 * @author Jagadeesh
 */
public class DefaultTbsStoreFinderStockFacade extends DefaultStoreFinderStockFacade<PointOfServiceStockData>
{

	@Override
	protected StoreFinderStockSearchPageData<PointOfServiceStockData> getResultForPOSData(final StoreFinderSearchPageData<PointOfServiceDistanceData> storeFinderSearchPageData, final ProductData productData)
	{
		final List<PointOfServiceStockData> result = new ArrayList<>(storeFinderSearchPageData.getResults().size());
		for (final PointOfServiceDistanceData distanceData : storeFinderSearchPageData.getResults())
		{
			final StoreStockHolder storeStockHolder = createStoreStockHolder();
			storeStockHolder.setPointOfService(distanceData.getPointOfService());
			storeStockHolder.setProduct(getProductService().getProductForCode(productData.getCode()));
			final PointOfServiceStockData posStockData = getStoreStockConverter().convert(storeStockHolder);
			final PointOfServiceData posData = getPointOfServiceDistanceDataConverter().convert(distanceData);
			posStockData.setFormattedDistance(posData.getFormattedDistance());
			posStockData.setDistance(posData.getDistance());
			posStockData.setDistanceUnit(posData.getDistanceUnit());
			posStockData.setAvailable(posData.isAvailable());
			result.add(posStockData);
		}
		return createSearchResult(result, storeFinderSearchPageData.getPagination(), productData);
	}

	@Override
	public StoreFinderStockSearchPageData<PointOfServiceStockData> productSearch(final GeoPoint geoPoint, final ProductData productData, final PageableData pageableData)
	{
		final BaseStoreModel basetoreModel = getBaseStoreService().getCurrentBaseStore();
		final StoreFinderSearchPageData<PointOfServiceDistanceData> storeFinderSearchPageData = getStoreFinderService().positionSearch(basetoreModel, geoPoint, pageableData, basetoreModel.getMaxRadiusForPoSSearch());
		return getResultForPOSData(storeFinderSearchPageData, productData);
	}
}
