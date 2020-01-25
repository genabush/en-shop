/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.collectionpoints.client.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import uk.co.thebodyshop.collectionpoints.client.BasicHttpClient;

public class SimpleHttpClient implements BasicHttpClient
{

    private static final String UTF_8 = "UTF-8";

    @Resource(name = "collectionsHttpClient")
    private HttpClient collectionsHttpClient;

    @Override
    public HttpResponse doGet(final String endpoint, final Map<String, String> params) throws ClientProtocolException, IOException
    {
        final List<NameValuePair> qparams = createQueryParameter(params);
        final HttpGet request = new HttpGet(endpoint + "?" + URLEncodedUtils.format(qparams, UTF_8));
        return collectionsHttpClient.execute(request);
    }

    @Override
    public HttpResponse doGetWithHeaders(String endpoint, Map<String, String> params, ArrayList<Header> headers) throws ClientProtocolException, IOException
    {
        final List<NameValuePair> qparams = createQueryParameter(params);
        final HttpGet request = new HttpGet(endpoint + "?" + URLEncodedUtils.format(qparams, UTF_8));
        for (Header header : headers)
        {
            request.setHeader(header);
        }
        return collectionsHttpClient.execute(request);
    }

    @Override
    public HttpResponse doPost(final String endpoint, final String strXmlRequest) throws ClientProtocolException, IOException
    {
        final HttpPost httpPost = new HttpPost(endpoint);
        final HttpEntity entity = new StringEntity(strXmlRequest);
        httpPost.setEntity(entity);
        return collectionsHttpClient.execute(httpPost);
    }

    @Override
    public HttpResponse doPostWithHeaders(final String endpoint, final String strXmlRequest, ArrayList<Header> headers) throws ClientProtocolException, IOException
    {
        final HttpPost httpPost = new HttpPost(endpoint);
        final HttpEntity entity = new StringEntity(strXmlRequest);
        httpPost.setEntity(entity);
        for (Header header : headers)
        {
            httpPost.setHeader(header);
        }
        return collectionsHttpClient.execute(httpPost);
    }

    private List<NameValuePair> createQueryParameter(final Map<String, String> params)
    {
        final List<NameValuePair> qparams = new ArrayList<NameValuePair>();

        if (params != null)
        {
            for (final Map.Entry<String, String> param : params.entrySet())
            {
                qparams.add(new BasicNameValuePair(param.getKey(), param.getValue()));
            }
        }
        return qparams;
    }

}
