/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.collectionpoints.services.impl;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.io.IOException;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.common.base.Preconditions;

import uk.co.thebodyshop.collectionpoints.response.data.CollectionPointResponseData;
import uk.co.thebodyshop.collectionpoints.royalmail.client.RoyalMailCollectionPointClient;
import uk.co.thebodyshop.collectionpoints.royalmail.client.exception.XmlMarshellingException;
import uk.co.thebodyshop.collectionpoints.royalmail.data.CollectionPointRequestData;
import uk.co.thebodyshop.collectionpoints.royalmail.jaxb.getlcdeliverylocationsresponse.GetLCDeliveryLocationsResponse;
import uk.co.thebodyshop.collectionpoints.services.CollectionPointService;

/**
 * Created by alexjollands on 21/09/2016.
 */
public class DefaultRoyalMailCollectionPointService implements CollectionPointService<CollectionPointRequestData, CollectionPointResponseData>
{

    private static final Logger LOG = Logger.getLogger(DefaultRoyalMailCollectionPointService.class);

    private static final String REQUIRED_TEMPLATE = "The %s cannot be blank or null.";

    private static final String CLIENT_ID = "collection.point.royalmail.service.clientid";

    private static final String PASSWORD = "collection.point.royalmail.service.password";

    private static final String UK_POSTCODE_REGEX_NO_SPACES = "^([Gg][Ii][Rr] 0[Aa]{2})|((([A-Za-z][0-9]{1,2})|(([A-Za-z][A-Ha-hJ-Yj-y][0-9]{1,2})|(([A-Za-z][0-9][A-Za-z])|([A-Za-z][A-Ha-hJ-Yj-y][0-9]?[A-Za-z])))) [0-9][A-Za-z]{2})";

    private static final String UK_POSTCODE_REGEX_WITH_SPACES = "^([Gg][Ii][Rr] 0[Aa]{2})|((([A-Za-z][0-9]{1,2})|(([A-Za-z][A-Ha-hJ-Yj-y][0-9]{1,2})|(([A-Za-z][0-9][A-Za-z])|([A-Za-z][A-Ha-hJ-Yj-y][0-9]?[A-Za-z])))) {0,1}[0-9][A-Za-z]{2})$";

    @Resource(name = "defaultRoyalMailCollectionPointClient")
    private RoyalMailCollectionPointClient client;

    @Resource(name = "configurationService")
    private ConfigurationService configurationService;

    @Resource(name = "royalMailCollectionPointsConverter")
    private Converter<GetLCDeliveryLocationsResponse, CollectionPointResponseData> convertor;

    @Override
    public CollectionPointResponseData findCollectionPoints(final CollectionPointRequestData requestData)
    {
        addMandatoryData(requestData);
        validateRequest(requestData);

        GetLCDeliveryLocationsResponse response = null;
        if (LOG.isDebugEnabled())
        {
            LOG.debug("CollectionPoints request received for search string [" + requestData.getZip() + "]");
        }
        try
        {
            response = client.processRequest(requestData);
            return convertor.convert(response);
        }
        catch (final XmlMarshellingException e)
        {
            LOG.info(e.getMessage());
        }
        catch (final IOException e)
        {
            LOG.info("Error while processing request for retrieving CollectionPoints", e);
        }

        return null;
    }

    @Override
    public boolean isValidSearchQuery(final String searchText)
    {
        return searchText.matches(UK_POSTCODE_REGEX_WITH_SPACES) || searchText.matches(UK_POSTCODE_REGEX_NO_SPACES);
    }

    private void addMandatoryData(final CollectionPointRequestData request)
    {
        final String clientId = configurationService.getConfiguration().getString(CLIENT_ID);
        final String password = configurationService.getConfiguration().getString(PASSWORD);
        request.setUsername(clientId);
        request.setPassword(password);
    }

    private void validateRequest(final CollectionPointRequestData request)
    {
        Preconditions.checkNotNull(request, "The request cannot be null");
        Preconditions.checkArgument(StringUtils.isNotEmpty(request.getUsername()), REQUIRED_TEMPLATE, "Client ID");
        Preconditions.checkArgument(StringUtils.isNotEmpty(request.getPassword()), REQUIRED_TEMPLATE, "Password");
        Preconditions.checkArgument(StringUtils.isNotEmpty(request.getCountryCode()), REQUIRED_TEMPLATE, "Country Code");
    }
}
