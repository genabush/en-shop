/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.collectionpoints.response.data;

public class Error
{

    private String errorCode;

    private String errorDescription;

    public String getErrorCode()
    {
        return errorCode;
    }

    public void setErrorCode(final String errorCode)
    {
        this.errorCode = errorCode;
    }

    public String getErrorDescription()
    {
        return errorDescription;
    }

    public void setErrorDescription(final String errorDescription)
    {
        this.errorDescription = errorDescription;
    }

}
