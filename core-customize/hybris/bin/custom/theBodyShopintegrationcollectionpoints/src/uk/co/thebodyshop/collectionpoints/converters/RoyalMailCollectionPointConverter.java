/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.collectionpoints.converters;

import de.hybris.platform.commercefacades.storelocator.data.TimeData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.I18NService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import uk.co.thebodyshop.collectionpoints.response.data.Address;
import uk.co.thebodyshop.collectionpoints.response.data.CollectionPoint;
import uk.co.thebodyshop.collectionpoints.response.data.OperatingHours;
import uk.co.thebodyshop.collectionpoints.royalmail.jaxb.lcdeliverylocations.Location;
import uk.co.thebodyshop.collectionpoints.royalmail.jaxb.lcdeliverylocations.OpeningDay;

/**
 * Created by alexjollands on 27/09/2016.
 */
public class RoyalMailCollectionPointConverter implements Converter<Location, CollectionPoint>
{

    private static final String COUNTRY_CODE = "GB";

    private static final String DISTANCE_CODE = "MI";

    @Resource(name = "i18NService") private I18NService i18NService;

    @Resource(name = "timeDataConverter")
    private Converter<Date, TimeData> timeDataConverter;

    @Override public CollectionPoint convert(Location location) throws ConversionException
    {
        return convert(location, new CollectionPoint());
    }

    @Override
    public CollectionPoint convert(final Location source, final CollectionPoint target) throws ConversionException
    {
        if (source != null)
        {
            populateIdDetails(source, target);
            populateAddress(source, target);
            populateDistance(source, target);
            populateGeocode(source, target);
            populateOpeningHours(source, target);
        }
        return target;
    }

    private void populateOpeningHours(Location source, CollectionPoint target)
    {
        List<OperatingHours> operatingHours = new ArrayList<OperatingHours>(7);
        for (OpeningDay day : source.getOpeningDay())
        {
            operatingHours.add(createOperatingHours(day));
        }
        target.setOperatingHours(operatingHours);
    }

    private OperatingHours createOperatingHours(OpeningDay day)
    {
        OperatingHours operatingHours = new OperatingHours();
        operatingHours.setDay(getDayCode(day.getDayOfWeekType().getDayOfWeekCode().getName()));

        final Calendar date = Calendar.getInstance(i18NService.getCurrentLocale());

        int hour = day.getOpeningTime().getHour();
        int minute = day.getOpeningTime().getMinute();
        date.set(Calendar.HOUR_OF_DAY, hour);
        date.set(Calendar.MINUTE, minute);
        operatingHours.setOpenHours(timeDataConverter.convert(date.getTime()).getFormattedHour());

        hour = day.getClosingTime().getHour();
        minute = day.getClosingTime().getMinute();
        date.set(Calendar.HOUR_OF_DAY, hour);
        date.set(Calendar.MINUTE, minute);
        operatingHours.setCloseHours(timeDataConverter.convert(date.getTime()).getFormattedHour());

        return operatingHours;
    }

    private String getDayCode(String name)
    {
        String dayCode = "0";
        switch (name)
        {
        case "Sunday":
            dayCode = "1";
            break;
        case "Monday":
            dayCode = "2";
            break;
        case "Tuesday":
            dayCode = "3";
            break;
        case "Wednesday":
            dayCode = "4";
            break;
        case "Thursday":
            dayCode = "5";
            break;
        case "Friday":
            dayCode = "6";
            break;
        case "Saturday":
            dayCode = "7";
            break;
        }
        return dayCode;
    }

    private void populateGeocode(Location source, CollectionPoint target)
    {
        if (source.getLocationPosition() != null)
        {
            if (source.getLocationPosition().getLatitude() != null && source.getLocationPosition().getLongitude() != null)
            {
                target.setLatitude(String.valueOf(source.getLocationPosition().getLatitude()));
                target.setLongitude(String.valueOf(source.getLocationPosition().getLongitude()));
            }
        }
    }

    private void populateDistance(Location source, CollectionPoint target)
    {
        target.setDistance(String.valueOf(source.getSearchDistance()));
        target.setDistanceUnit(DISTANCE_CODE);
    }

    private void populateAddress(Location source, CollectionPoint target)
    {
        Address targetAddress = new Address();
        if (source.getAddress() != null)
        {
            targetAddress.setConsigneeName(source.getOrganisationName());
            targetAddress.setAddressLine(source.getAddress().getAddressLine1());
            targetAddress.setAddressLine1(source.getAddress().getAddressLine2());
            targetAddress.setCity(source.getAddress().getAddressLine4());
            targetAddress.setCounty(source.getAddress().getCounty().getCountyCode().getName());
            targetAddress.setCountryCode(COUNTRY_CODE);
            targetAddress.setPostcode(source.getAddress().getPostcode());
        }
        target.setAddress(targetAddress);
    }

    private void populateIdDetails(Location source, CollectionPoint target)
    {
        target.setCollectionPointId(source.getLocationName());
    }

}
