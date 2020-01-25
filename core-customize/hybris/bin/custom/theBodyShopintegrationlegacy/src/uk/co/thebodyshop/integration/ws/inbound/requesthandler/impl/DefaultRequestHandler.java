/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.ws.inbound.requesthandler.impl;

import org.apache.log4j.Logger;
import uk.co.thebodyshop.integration.ws.inbound.requesthandler.RequestHandler;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

/**
 * Created by mario on 21/04/15.
 */
public class DefaultRequestHandler implements RequestHandler
{
    private static final Logger LOG = Logger.getLogger(DefaultRequestHandler.class);

    @Override
    public void printXmlSource(final Source source)
    {
        try
        {
            if (LOG.isInfoEnabled())
            {
                LOG.info(getWellFormattedPayloadStringFromSource(source));
            }

        }
        catch (TransformerException e)
        {
            LOG.error("Error during the printing of the request", e);
        }
    }

    private String getWellFormattedPayloadStringFromSource(final Source source) throws TransformerException
    {
        final StringWriter requestXmlWriter = new StringWriter();
        final Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.transform(source, new StreamResult(requestXmlWriter));
        return requestXmlWriter.toString();
    }
}
