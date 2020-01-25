/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.collectionpoints.response.data;

import java.io.Serializable;
import java.util.List;

public class CollectionPointResponseData implements Serializable
{

    private String responseStatusCode;

    private String responseStatusDescription;

    private List<Error> error;

    private List<CollectionPoint> collectionPoints;

    private double sourceLatitude;

    private double sourceLongitude;

    public String getResponseStatusCode()
    {
        return responseStatusCode;
    }

    public void setResponseStatusCode(final String responseStatusCode)
    {
        this.responseStatusCode = responseStatusCode;
    }

    public String getResponseStatusDescription()
    {
        return responseStatusDescription;
    }

    public void setResponseStatusDescription(final String responseStatusDescription)
    {
        this.responseStatusDescription = responseStatusDescription;
    }

    public List<Error> getError()
    {
        return error;
    }

    public void setError(final List<Error> error)
    {
        this.error = error;
    }

    public List<CollectionPoint> getCollectionPoints()
    {
        return collectionPoints;
    }

    public void setCollectionPoints(final List<CollectionPoint> collectionPoints)
    {
        this.collectionPoints = collectionPoints;
    }

    public double getSourceLatitude()
    {
        return sourceLatitude;
    }

    public void setSourceLatitude(final double sourceLatitude)
    {
        this.sourceLatitude = sourceLatitude;
    }

    public double getSourceLongitude()
    {
        return sourceLongitude;
    }

    public void setSourceLongitude(final double sourceLongitude)
    {
        this.sourceLongitude = sourceLongitude;
    }

}
