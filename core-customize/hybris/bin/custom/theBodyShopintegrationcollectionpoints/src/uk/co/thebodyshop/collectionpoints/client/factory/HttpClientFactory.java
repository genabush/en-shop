/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.collectionpoints.client.factory;

import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.FactoryBean;

import javax.net.ssl.SSLContext;

public class HttpClientFactory implements FactoryBean<HttpClient>
{

    private static final Logger LOG = Logger.getLogger(HttpClientFactory.class);

    @Override
    public HttpClient getObject() throws Exception
    {
        try
        {

            SSLContext sslContext = SSLContexts.custom().build();
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, new String[] { "TLSv1.2" }, null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslConnectionSocketFactory).build();
            PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
            connManager.setMaxTotal(160);
            connManager.setDefaultMaxPerRoute(40);
            final CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connManager).build();

            return httpClient;
        }
        catch (final Exception localException)
        {
            LOG.error(localException);
        }
        return null;
    }

    @Override
    public Class<HttpClient> getObjectType()
    {
        return HttpClient.class;
    }

    @Override
    public boolean isSingleton()
    {
        return true;
    }
}