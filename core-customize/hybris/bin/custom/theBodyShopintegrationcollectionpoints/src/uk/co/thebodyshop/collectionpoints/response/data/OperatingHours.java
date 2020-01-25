/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.collectionpoints.response.data;

public class OperatingHours
{

    private String day;

    private String openHours;

    private String closeHours;

    private String closedIndicator;

    private String open24HoursIndicator;

    private String hoursType;

    public String getDay()
    {
        return day;
    }

    public void setDay(final String day)
    {
        this.day = day;
    }

    public String getOpenHours()
    {
        return openHours;
    }

    public void setOpenHours(final String openHours)
    {
        this.openHours = openHours;
    }

    public String getCloseHours()
    {
        return closeHours;
    }

    public void setCloseHours(final String closeHours)
    {
        this.closeHours = closeHours;
    }

    public String getClosedIndicator()
    {
        return closedIndicator;
    }

    public void setClosedIndicator(final String closedIndicator)
    {
        this.closedIndicator = closedIndicator;
    }

    public String getOpen24HoursIndicator()
    {
        return open24HoursIndicator;
    }

    public void setOpen24HoursIndicator(final String open24HoursIndicator)
    {
        this.open24HoursIndicator = open24HoursIndicator;
    }

    public String getHoursType()
    {
        return hoursType;
    }

    public void setHoursType(final String hoursType)
    {
        this.hoursType = hoursType;
    }

}
