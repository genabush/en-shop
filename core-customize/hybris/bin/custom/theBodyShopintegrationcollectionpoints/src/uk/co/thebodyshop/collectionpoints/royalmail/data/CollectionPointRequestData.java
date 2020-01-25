/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.collectionpoints.royalmail.data;

import java.io.Serializable;

public class CollectionPointRequestData implements Serializable
{

    private String licenceNumber;

    private String username;

    private String password;

    private String zip;

    private String countryCode;

    private String language;

    private double latitude;

    private double longitude;

    private String locationId;

    private String addressLine1;

    private String addressLine2;

    private int numberOfResults;

    public String getLicenceNumber()
    {
        return licenceNumber;
    }

    public void setLicenceNumber(final String licenceNumber)
    {
        this.licenceNumber = licenceNumber;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(final String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(final String password)
    {
        this.password = password;
    }

    public String getZip()
    {
        return zip;
    }

    public void setZip(final String zip)
    {
        this.zip = zip;
    }

    public String getCountryCode()
    {
        return countryCode;
    }

    public void setCountryCode(final String countryCode)
    {
        this.countryCode = countryCode;
    }

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(final String language)
    {
        this.language = language;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(final double latitude)
    {
        this.latitude = latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(final double longitude)
    {
        this.longitude = longitude;
    }

    public int getNumberOfResults()
    {
        return numberOfResults;
    }

    public void setNumberOfResults(final int numberOfResults)
    {
        this.numberOfResults = numberOfResults;
    }

    public String getLocationId()
    {
        return locationId;
    }

    public void setLocationId(final String locationId)
    {
        this.locationId = locationId;
    }

    public String getAddressLine1()
    {
        return addressLine1;
    }

    public void setAddressLine1(final String addressLine1)
    {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2()
    {
        return addressLine2;
    }

    public void setAddressLine2(final String addressLine2)
    {
        this.addressLine2 = addressLine2;
    }

}
