/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.collectionpoints.response.data;

public class Address
{

    private String consigneeName;

    private String addressLine;

    private String addressLine1;

    private String county;

    private String city;

    private String postcode;

    private String countryCode;

    private String regionIsoCode;

    public String getConsigneeName()
    {
        return consigneeName;
    }

    public void setConsigneeName(final String consigneeName)
    {
        this.consigneeName = consigneeName;
    }

    public String getAddressLine()
    {
        return addressLine;
    }

    public void setAddressLine(final String addressLine)
    {
        this.addressLine = addressLine;
    }

    public String getAddressLine1()
    {
        return addressLine1;
    }

    public void setAddressLine1(final String addressLine1)
    {
        this.addressLine1 = addressLine1;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(final String city)
    {
        this.city = city;
    }

    public String getCounty()
    {
        return county;
    }

    public void setCounty(final String county)
    {
        this.county = county;
    }

    public String getPostcode()
    {
        return postcode;
    }

    public void setPostcode(final String postcode)
    {
        this.postcode = postcode;
    }

    public String getCountryCode()
    {
        return countryCode;
    }

    public String getRegionIsoCode()
    {
        return regionIsoCode;
    }

    public void setRegionIsoCode(String regionIsoCode)
    {
        this.regionIsoCode = regionIsoCode;
    }

    public void setCountryCode(final String countryCode)
    {
        this.countryCode = countryCode;
    }

}
