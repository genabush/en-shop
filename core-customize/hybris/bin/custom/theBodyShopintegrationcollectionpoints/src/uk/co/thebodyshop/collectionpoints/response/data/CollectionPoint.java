/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.collectionpoints.response.data;

import java.util.List;

public class CollectionPoint
{

    private String name;

    private String collectionPointId;

    private String decription;

    private Address address;

    private String distance;

    private String distanceUnit;

    private String latitude;

    private String longitude;

    private List<OperatingHours> operatingHours;

    private String phoneNumber;

    private String standardHoursOfOperation;

    private String status;

    private String imageUrl;

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getCollectionPointId()
    {
        return collectionPointId;
    }

    public void setCollectionPointId(final String collectionPointId)
    {
        this.collectionPointId = collectionPointId;
    }

    public String getDecription()
    {
        return decription;
    }

    public void setDecription(final String decription)
    {
        this.decription = decription;
    }

    public Address getAddress()
    {
        return address;
    }

    public void setAddress(final Address address)
    {
        this.address = address;
    }

    public String getDistance()
    {
        return distance;
    }

    public void setDistance(final String distance)
    {
        this.distance = distance;
    }

    public String getDistanceUnit()
    {
        return distanceUnit;
    }

    public void setDistanceUnit(final String distanceUnit)
    {
        this.distanceUnit = distanceUnit;
    }

    public String getLatitude()
    {
        return latitude;
    }

    public void setLatitude(final String latitude)
    {
        this.latitude = latitude;
    }

    public String getLongitude()
    {
        return longitude;
    }

    public void setLongitude(final String longitude)
    {
        this.longitude = longitude;
    }

    public List<OperatingHours> getOperatingHours()
    {
        return operatingHours;
    }

    public void setOperatingHours(final List<OperatingHours> operatingHours)
    {
        this.operatingHours = operatingHours;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getStandardHoursOfOperation()
    {
        return standardHoursOfOperation;
    }

    public void setStandardHoursOfOperation(final String standardHoursOfOperation)
    {
        this.standardHoursOfOperation = standardHoursOfOperation;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(final String status)
    {
        this.status = status;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(final String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

}
