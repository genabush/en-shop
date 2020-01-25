/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.collectionpoints.royalmail.client;

import de.hybris.platform.util.Config;

import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import uk.co.thebodyshop.collectionpoints.client.BasicHttpClient;
import uk.co.thebodyshop.collectionpoints.royalmail.data.CollectionPointRequestData;
import uk.co.thebodyshop.collectionpoints.royalmail.jaxb.getlcdeliverylocationsresponse.GetLCDeliveryLocationsResponse;

/**
 * Created by alexjollands on 21/09/2016.
 */
public class DefaultRoyalMailCollectionPointClient implements RoyalMailCollectionPointClient
{

    private static final String ENDPOINT = Config.getString("collection.point.royalmail.service.url", "");
    private static final Logger LOG = Logger.getLogger(DefaultRoyalMailCollectionPointClient.class);
    private static final String HEADER_CLIENTID = "X-IBM-Client-Id";
    private static final String HEADER_CLIENTSECRET = "X-IBM-Client-Secret";
    private static final String HEADER_SOAPACTION = "urn:getLCDeliveryLocations";


    @Resource(name = "simpleHttpClient")
    private BasicHttpClient httpClient;

    @Resource(name = "royalMailCollectionPointClientHelper")
    private RoyalMailCollectionPointClientHelper collectionPointClientHelper;

    @Override public GetLCDeliveryLocationsResponse processRequest(final CollectionPointRequestData requestData) throws ClientProtocolException, IOException
    {
        GetLCDeliveryLocationsResponse response = null;
        if (StringUtils.isNotBlank(ENDPOINT))
        {
            final String requestXml = collectionPointClientHelper.createRequest(requestData);
            if (LOG.isDebugEnabled()){
                LOG.debug("Making HTTP request to " + ENDPOINT + ", with XML message of: \n" + requestXml);
            }
            final ArrayList<Header> headers = createHeaders(requestData);
            final HttpResponse httpResponse = httpClient.doPostWithHeaders(ENDPOINT, requestXml, headers);
            final String responseString = EntityUtils.toString(httpResponse.getEntity());
            try
            {
                response = collectionPointClientHelper.createResponse(responseString);
            }
            catch (final Exception e)
            {
                LOG.error("Unable to process response : " + responseString, e);
                throw e;
            }
        }
        return response;
    }

    private ArrayList<Header> createHeaders(final CollectionPointRequestData requestData)
    {
        final ArrayList<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("Content-Type", "application/xml"));
        headers.add(new BasicHeader("SOAPAction", HEADER_SOAPACTION));
        headers.add(new BasicHeader(HEADER_CLIENTID, requestData.getUsername()));
        headers.add(new BasicHeader(HEADER_CLIENTSECRET, requestData.getPassword()));
        return headers;
    }

}
