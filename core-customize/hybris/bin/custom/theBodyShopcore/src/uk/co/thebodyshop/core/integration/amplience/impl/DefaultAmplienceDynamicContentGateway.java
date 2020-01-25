/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.integration.amplience.impl;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.thebodyshop.core.integration.amplience.AmplienceDynamicContentGateway;

/**
 * @author j.wong
 */
public class DefaultAmplienceDynamicContentGateway implements AmplienceDynamicContentGateway
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultAmplienceDynamicContentGateway.class);

	@Override
	public String getDynamicContent(String url) throws Exception
	{
		String responseBody = null;

		try(CloseableHttpClient httpclient=HttpClients.createDefault())
		{
			final HttpGet httpget = new HttpGet(url);

			final ResponseHandler<String> responseHandler = response -> {
				final int status = response.getStatusLine().getStatusCode();
				if (status != HttpStatus.SC_OK)
				{
					LOG.error("Received error http status " + status + " when reading from " + url);
					return null;
				}

				final HttpEntity entity = response.getEntity();

				return ((entity != null) ? EntityUtils.toString(entity) : null);
			};

			responseBody = httpclient.execute(httpget, responseHandler);
		}
		catch (final Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			throw ex;
		}

		return responseBody;
	}
}
