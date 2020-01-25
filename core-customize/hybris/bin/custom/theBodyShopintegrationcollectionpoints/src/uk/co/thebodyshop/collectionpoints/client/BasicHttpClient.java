/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.collectionpoints.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

public interface BasicHttpClient
{

    public HttpResponse doGet(final String endpoint, final Map<String, String> params) throws ClientProtocolException, IOException;

    public HttpResponse doGetWithHeaders(final String endpoint, final Map<String, String> params, final ArrayList<Header> headers) throws ClientProtocolException, IOException;

    public HttpResponse doPost(final String endpoint, final String strXmlRequest) throws ClientProtocolException, IOException;

    public HttpResponse doPostWithHeaders(final String endpoint, final String strXmlRequest, final ArrayList<Header> headers) throws ClientProtocolException, IOException;

}
