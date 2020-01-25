/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.data.processing;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;

import de.hybris.platform.acceleratorservices.dataimport.batch.util.BatchDirectoryUtils;
import de.hybris.platform.basecommerce.enums.PointOfServiceTypeEnum;
import de.hybris.platform.commerceservices.model.storelocator.StoreLocatorFeatureModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.storelocator.pos.PointOfServiceService;

import uk.co.thebodyshop.core.services.StoreLocatorFeatureService;

/**
 * @author Hemchand
 */
public class PointOfServiceFileProcessor
{
	private static final Logger LOG = LoggerFactory.getLogger(PointOfServiceFileProcessor.class);

	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final String DATE_VALIDATION = "^[12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])";
	private static final String STORE_FEATURE_CIS ="buyOnlinePickupInStore";
	private final ModelService modelService;
	private final PointOfServiceService pointOfServiceService;
	private final CommonI18NService commonI18NService;
	private final StoreLocatorFeatureService storeLocatorFeatureService;

	@ServiceActivator
	public void uploadPOSFile(final File pointOfServiceFile) throws IOException
	{
		final String fileName = pointOfServiceFile.getName();

		LOG.info("Processing the file [{}] for importing store data", fileName);

		try (final FileReader reader = new FileReader(pointOfServiceFile))
		{
      final CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(reader);
			for (final CSVRecord record : parser.getRecords())
			{
				if (validateRecord(record))
				{
					processRecord(record);
				}
			}
		}
		catch (final Exception e)
		{
			LOG.error("Exception importing / processing the store import file", e);
		}

		moveFileToArchiveFolder(pointOfServiceFile);

		LOG.info("Successfully finished processing the file [{}]", fileName);
	}

	/**
	 * @param record
	 * @return
	 */
	private boolean validateRecord(final CSVRecord record)
	{
		if (record.get("storeID").isBlank() && record.get("market").isBlank())
		{
			return false;
		}

		if (findBaseStore(record.get("market")) == null)
		{
			return false;
		}

		return true;
	}

	/*
	 * cells.get(1) not required as it is the same as cells.get(2)
	 */
	private void processRecord(final CSVRecord records) throws ParseException
	{
		final String storeId = records.get("storeID");
		final String displayName = records.get("displayName");
		final String addressLine1 = records.get("addressLine1");
		final String addressLine2 = records.get("addressLine2");
		final String addressLine3 = records.get("addressLine3");
		final String addressLine4 = records.get("addressLine4");
		final String town = records.get("town");
		final String region = records.get("region");
		final String postalCode = records.get("postalCode");
		final String country = records.get("country");
		final String phoneNumber = records.get("phoneNumber");
		final String longitute = records.get("longitude");
		final String latitude = records.get("latitude");
		final Boolean cis = Boolean.valueOf(records.get("cis"));
		final String market = records.get("market");
		final String maxOpenOrders = records.get("maxOpenOrders");
		final Boolean permanentlyClosed = Boolean.valueOf(records.get("permanentlyClosed"));
		final String closingPeriodStart = records.get("closingPeriodStart");
		final String closingPeriodEnd = records.get("closingPeriodEnd");

		PointOfServiceModel pointOfServiceModel = getPointOfServiceService().getPointOfServiceForName(storeId);

		if (pointOfServiceModel == null)
		{
			pointOfServiceModel = getModelService().create(PointOfServiceModel.class);
			pointOfServiceModel.setName(storeId);
		}

		updateBasicDetails(pointOfServiceModel, displayName, cis, market, maxOpenOrders, permanentlyClosed);

		updateStoreFeature(pointOfServiceModel, cis);

		updateLocation(pointOfServiceModel, latitude, longitute);

		updateClosingPeriods(pointOfServiceModel, closingPeriodStart, closingPeriodEnd);

		updateAddress(pointOfServiceModel, addressLine1, addressLine2, addressLine3, addressLine4, town, region, postalCode, country, phoneNumber);

		getModelService().saveAll();
	}

	private void updateBasicDetails(final PointOfServiceModel pointOfServiceModel, final String displayName, final Boolean cis, final String market, final String maxOpenOrders, final Boolean permanentlyClosed)
	{
		pointOfServiceModel.setType(PointOfServiceTypeEnum.STORE);
		pointOfServiceModel.setDisplayName(displayName);
		pointOfServiceModel.setEnabledForCis(cis);
		pointOfServiceModel.setPermanentlyClosed(permanentlyClosed);
		try
		{
			if (!maxOpenOrders.isBlank())
			{
				pointOfServiceModel.setMaxCapacity(Integer.valueOf(maxOpenOrders));
			}
			else
			{
				pointOfServiceModel.setMaxCapacity(0);
			}
		}
		catch (final Exception e)
		{
			LOG.warn("Could not parse maxOpenOrders [{}] for the store [{}]", maxOpenOrders, pointOfServiceModel.getName());
			pointOfServiceModel.setMaxCapacity(0);
		}

		final BaseStoreModel store = findBaseStore(market);

		if (store != null)
		{
			pointOfServiceModel.setBaseStore(store);
		}
		else
		{
			LOG.warn("No BaseStore for the market [{}] was found", market);
		}
	}

	private void updateStoreFeature(final PointOfServiceModel pointOfServiceModel, final Boolean cis)
	{
		final Set<StoreLocatorFeatureModel> updatedStoreLocator = new HashSet<>();
		if (CollectionUtils.isNotEmpty(pointOfServiceModel.getFeatures()))
		{
			updatedStoreLocator.addAll(getStoreLocatorFeaturesExcludingCis(pointOfServiceModel.getFeatures()));
		}
		if (cis)
		{
			updatedStoreLocator.add(getStoreLocatorFeatureService().getStoreLocatorFeatureForCode(STORE_FEATURE_CIS));
		}
		pointOfServiceModel.setFeatures(updatedStoreLocator);
	}

	private Set<StoreLocatorFeatureModel> getStoreLocatorFeaturesExcludingCis(final Set<StoreLocatorFeatureModel> existedStoreLocatorFeature)
	{
		return existedStoreLocatorFeature.stream().filter(feature -> !feature.getCode().equals(STORE_FEATURE_CIS)).collect(Collectors.toSet());
	}

	private BaseStoreModel findBaseStore(final String market)
	{
		try
		{
			final CountryModel marketCountry = getCommonI18NService().getCountry(market);

			if (marketCountry != null && marketCountry.getIsocode().equals(market))
			{
				return marketCountry.getBaseStores().iterator().next();
			}
		}
		catch (final UnknownIdentifierException e)
		{
			LOG.warn("The given country ISO code [{}] was not found when looking up the BaseStore", market);
		}

		return null;
	}

	private void updateLocation(final PointOfServiceModel pointOfServiceModel, final String latitude, final String longitute)
	{
		if (isDouble(latitude) && isDouble(longitute))
		{
			pointOfServiceModel.setLatitude(Double.parseDouble(latitude));
			pointOfServiceModel.setLongitude(Double.parseDouble(longitute));
			pointOfServiceModel.setGeocodeTimestamp(new Date());
		}
		else
		{
			LOG.warn("Latitude [{}] and Longitude [{}] for Store [{}] is in wrong format", latitude, longitute, pointOfServiceModel.getName());
		}
	}

	private void updateClosingPeriods(final PointOfServiceModel pointOfServiceModel, final String closingPeriodStart, final String closingPeriodEnd) throws ParseException
	{
		final Date closingPeriodStartDate = getDate(closingPeriodStart);
		final Date closingPeriodEndDate = getDate(closingPeriodEnd);

		if (closingPeriodStartDate != null && closingPeriodEndDate != null)
		{
			pointOfServiceModel.setTemporaryClosedFromDate(closingPeriodStartDate);
			pointOfServiceModel.setTemporaryClosedToDate(closingPeriodEndDate);
		}
		else
		{
			pointOfServiceModel.setTemporaryClosedFromDate(null);
			pointOfServiceModel.setTemporaryClosedToDate(null);
		}
	}

	private void updateAddress(final PointOfServiceModel pointOfServiceModel, final String addressLine1, final String addressLine2, final String addressLine3, final String addressLine4, final String town, final String region, final String postalCode,
			final String country, final String phoneNumber)
	{
		AddressModel addressModel = pointOfServiceModel.getAddress();

		if (addressModel == null)
		{
			addressModel = getModelService().create(AddressModel.class);
			pointOfServiceModel.setAddress(addressModel);
			addressModel.setOwner(pointOfServiceModel);
		}

		addressModel.setStreetname(addressLine1);
		addressModel.setStreetnumber(addressLine2);
		addressModel.setLine1(addressLine1);
		addressModel.setLine2(addressLine2);
		addressModel.setLine3(addressLine3);
		addressModel.setLine4(addressLine4);
		addressModel.setTown(town);
		addressModel.setPostalcode(postalCode);
		addressModel.setPhone1(phoneNumber);

		try
		{
			final CountryModel countryModel = getCommonI18NService().getCountry(country);

			if (countryModel != null)
			{
				addressModel.setCountry(countryModel);

				if (!region.isBlank())
				{
					try
					{
						final RegionModel regionModel = getCommonI18NService().getRegion(countryModel, region);
						addressModel.setRegion(regionModel);
					}
					catch (final UnknownIdentifierException e)
					{
						LOG.warn("The given region [{}] is not found in the country [{}]", region, country);
					}
				}
			}
		}
		catch (final UnknownIdentifierException e)
		{
			LOG.warn("The given country ISO code [{}] was not found when creating the address", country);
		}
	}

	private void moveFileToArchiveFolder(final File pointOfServiceFile)
	{
		try
		{
			final File archiveDir = new File(BatchDirectoryUtils.getRelativeArchiveDirectory(pointOfServiceFile));
			FileUtils.moveFileToDirectory(pointOfServiceFile, archiveDir, false);
		}
		catch (final IOException e)
		{
			LOG.error("Could not move the pointOfService file [{}] to the archive directory", pointOfServiceFile.getName());
			LOG.error("Exception occured moving the pointOfService file to the archive directory", e);
		}
	}

	public boolean isDouble(final String str)
	{
		try
		{
			Double.parseDouble(str);
			return true;
		}
		catch (final Exception e)
		{
			return false;
		}
	}

	public Date getDate(final String date) throws ParseException
	{
		Date parsedDate = null;

		if (!date.isBlank())
		{
			if (Pattern.matches(DATE_VALIDATION, date))
			{
				final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
				final Date tempDate = dateFormat.parse(date);
				final Date currentDate = new Date();
				if (tempDate.compareTo(currentDate) > 0)
				{
					parsedDate = tempDate;
				}
			}
			else
			{
				LOG.warn("Given date [{}] is not in the expected format [{}]", date, DATE_FORMAT);
			}
		}

		return parsedDate;
	}

	public PointOfServiceFileProcessor(final ModelService modelService, final PointOfServiceService pointOfServiceService, final CommonI18NService commonI18NService, final StoreLocatorFeatureService storeLocatorFeatureService)
	{
		this.pointOfServiceService = pointOfServiceService;
		this.modelService = modelService;
		this.commonI18NService = commonI18NService;
		this.storeLocatorFeatureService = storeLocatorFeatureService;
	}

	protected PointOfServiceService getPointOfServiceService()
	{
		return pointOfServiceService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	protected StoreLocatorFeatureService getStoreLocatorFeatureService()
	{
		return storeLocatorFeatureService;
	}
}
