/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.order.populator;

import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.storelocator.data.OpeningScheduleData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commercefacades.storelocator.data.WeekdayOpeningDayData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commerceservices.store.data.GeoPoint;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import uk.co.thebodyshop.core.collection.CollectionPointData;
import uk.co.thebodyshop.core.model.CollectionOpeningDayModel;
import uk.co.thebodyshop.core.model.CollectionPointModel;
import uk.co.thebodyshop.core.model.ServiceProductModel;
import uk.co.thebodyshop.core.model.StoreDetailModel;
import uk.co.thebodyshop.core.services.store.pickup.PickupPOSConsolidationStrategy;
import uk.co.thebodyshop.core.storelocator.pos.TbsPointOfServiceService;

/**
 * @author prateek.goel
 */
public class OrderCollectionPopulator implements Populator<AbstractOrderModel, AbstractOrderData>
{

	private EnumerationService enumerationService;

	private Converter<CollectionOpeningDayModel, WeekdayOpeningDayData> collectionOpeningDayConverter;

	private Converter<PointOfServiceModel, PointOfServiceData> pointOfServiceConverter;

	private PickupPOSConsolidationStrategy PickupPOSConsolidationStrategy;

	private TbsPointOfServiceService tbsPointOfServiceService;

	@Override
	public void populate(final AbstractOrderModel source, final AbstractOrderData target) throws ConversionException
	{
		final PointOfServiceModel pointOfService = source.getDeliveryPointOfService();
		final BaseStoreModel baseStore = source.getStore();
		String serviceType = StringUtils.EMPTY;
		serviceType = null != baseStore.getCollectionPoint() ? baseStore.getCollectionPoint().getCode() : null;
		target.setEligibleForCollectionPoint(StringUtils.isNotBlank(serviceType));
		target.setEligibleForCollectInStore(isCisEnabledForBaseStore(baseStore) && isCartContainGiftMessage(source) && isCartContainGiftProduct(source));
		if (null != pointOfService)
		{
			final PointOfServiceData pointofServiceData = getPointOfServiceConverter().convert(pointOfService);
			pointofServiceData.setAvailable(getTbsPointOfServiceService().isAvailableForCollectInStore(pointOfService));
			addStoreDetails(source, pointofServiceData, baseStore);
			target.setDeliveryPointOfService(pointofServiceData);
		}
		final CollectionPointModel collectionPoint = source.getCollectionPoint();
		if (null != collectionPoint)
		{
			final CollectionPointData collectionPointData = new CollectionPointData();
			addDetails(collectionPoint, collectionPointData, baseStore, serviceType);
			addAddressDetails(source, collectionPointData);
			addOpeningSchedule(collectionPoint, collectionPointData);
			target.setCollectionPoint(collectionPointData);
		}
		if (null != source.getFulfillmentMethod())
		{
			target.setFulfillmentMethod(source.getFulfillmentMethod().getCode());
		}

	}

	private void addStoreDetails(final AbstractOrderModel source, final PointOfServiceData pointofServiceData, final BaseStoreModel baseStore)
	{
		final StoreDetailModel extendedStoreDtl = source.getExtendedStoreDtl();
		if (null != extendedStoreDtl)
		{
			pointofServiceData.setDistanceUnit(getDistanceUnit(baseStore));
			final String distance = extendedStoreDtl.getDistance();
			if (StringUtils.isNoneBlank(distance))
			{
				pointofServiceData.setDistance(Double.valueOf(distance));
			}
			pointofServiceData.setSourceLatitude(extendedStoreDtl.getSourceLatitude());
			pointofServiceData.setSourceLongitude(extendedStoreDtl.getSourceLongitude());

		}
		final AddressData address = pointofServiceData.getAddress();
		final AddressModel deliveryAddress = source.getDeliveryAddress();
		if (null != address && null != deliveryAddress)
		{
			address.setFirstName(deliveryAddress.getFirstname());
			address.setLastName(deliveryAddress.getLastname());
			address.setCellphone(deliveryAddress.getCellphone());
		}
		if (CollectionUtils.isNotEmpty(source.getEntries()) && pointofServiceData.isAvailable())
		{
			pointofServiceData.setAvailable(getPickupPOSConsolidationStrategy().checkAllStockAvailableAtPointOfService(source.getEntries(), source.getDeliveryPointOfService()));
		}
	}

	private void addOpeningSchedule(final CollectionPointModel collectionPoint, final CollectionPointData collectionPointData)
	{
		final OpeningScheduleData openingSchedule = new OpeningScheduleData();
		openingSchedule.setWeekDayOpeningList(collectionOpeningDayConverter.convertAll(collectionPoint.getCollectionOpeningDays()));
		collectionPointData.setOpeningHours(openingSchedule);
	}

	private void addAddressDetails(final AbstractOrderModel source, final CollectionPointData collectionPointData)
	{
		final AddressModel collectionPointAddress = source.getDeliveryAddress();
		final AddressData collectionPointAddressData = new AddressData();
		collectionPointAddressData.setCellphone(collectionPointAddress.getCellphone());
		collectionPointAddressData.setCompanyName(collectionPointAddress.getCompany());
		final CountryData country = new CountryData();
		country.setIsocode(collectionPointAddress.getCountry().getIsocode());
		country.setName(collectionPointAddress.getCountry().getName());
		collectionPointAddressData.setCountry(country);
		collectionPointAddressData.setFirstName(collectionPointAddress.getFirstname());
		collectionPointAddressData.setLastName(collectionPointAddress.getLastname());
		collectionPointAddressData.setLine1(collectionPointAddress.getLine1());
		collectionPointAddressData.setLine2(collectionPointAddress.getLine2());
		collectionPointAddressData.setPhone(collectionPointAddress.getPhone1());
		collectionPointAddressData.setPostalCode(collectionPointAddress.getPostalcode());
		collectionPointAddressData.setTown(collectionPointAddress.getTown());
		collectionPointData.setAddress(collectionPointAddressData);
	}

	private void addDetails(final CollectionPointModel collectionPoint, final CollectionPointData collectionPointData, final BaseStoreModel baseStore, final String serviceType)
	{
		collectionPointData.setDistance(Double.valueOf(collectionPoint.getDistance()));
		collectionPointData.setServiceType(serviceType);
		collectionPointData.setDistanceUnit(getDistanceUnit(baseStore));
		final GeoPoint geopoint = new GeoPoint();
		geopoint.setLatitude(collectionPoint.getLatitude().doubleValue());
		geopoint.setLongitude(collectionPoint.getLongitude().doubleValue());
		collectionPointData.setGeoPoint(geopoint);
		collectionPointData.setSourceLatitude(collectionPoint.getSourceLatitude().doubleValue());
		collectionPointData.setSourceLongitude(collectionPoint.getSourceLongitude().doubleValue());
	}

	private String getDistanceUnit(final BaseStoreModel baseStore)
	{
		return getEnumerationService().getEnumerationName(baseStore.getStorelocatorDistanceUnit());
	}

	private boolean isCisEnabledForBaseStore(final BaseStoreModel baseStore)
	{
		return BooleanUtils.isTrue(baseStore.getEligibleForCollectInStore()) && null != baseStore.getCisDeliveryMode();
	}

	private boolean isCartContainGiftMessage(final AbstractOrderModel abstractOrderModel)
	{
		return StringUtils.isEmpty(abstractOrderModel.getGiftMessageName()) && StringUtils.isEmpty(abstractOrderModel.getGiftMessage()) && StringUtils.isEmpty(abstractOrderModel.getGiftMessageSenderName());
	}

	private boolean isCartContainGiftProduct(final AbstractOrderModel abstractOrderModel)
	{
		return !(Objects.nonNull(abstractOrderModel.getGiftWrapProduct()) && abstractOrderModel.getGiftWrapProduct() instanceof ServiceProductModel);
	}

	protected EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}

	protected Converter<CollectionOpeningDayModel, WeekdayOpeningDayData> getCollectionOpeningDayConverter()
	{
		return collectionOpeningDayConverter;
	}

	public void setCollectionOpeningDayConverter(final Converter<CollectionOpeningDayModel, WeekdayOpeningDayData> collectionOpeningDayConverter)
	{
		this.collectionOpeningDayConverter = collectionOpeningDayConverter;
	}

	protected Converter<PointOfServiceModel, PointOfServiceData> getPointOfServiceConverter()
	{
		return pointOfServiceConverter;
	}

	public void setPointOfServiceConverter(final Converter<PointOfServiceModel, PointOfServiceData> pointOfServiceConverter)
	{
		this.pointOfServiceConverter = pointOfServiceConverter;
	}

	protected PickupPOSConsolidationStrategy getPickupPOSConsolidationStrategy()
	{
		return PickupPOSConsolidationStrategy;
	}

	public void setPickupPOSConsolidationStrategy(final PickupPOSConsolidationStrategy pickupPOSConsolidationStrategy)
	{
		PickupPOSConsolidationStrategy = pickupPOSConsolidationStrategy;
	}

	protected TbsPointOfServiceService getTbsPointOfServiceService()
	{
		return tbsPointOfServiceService;
	}

	public void setTbsPointOfServiceService(final TbsPointOfServiceService tbsPointOfServiceService)
	{
		this.tbsPointOfServiceService = tbsPointOfServiceService;
	}

}
