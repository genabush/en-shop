/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.svs.soap;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Binding;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.log4j.Logger;

public class SOAPRequestResponseSpitter implements SOAPHandler<SOAPMessageContext>
{

    private static final Logger LOG = Logger.getLogger(SOAPRequestResponseSpitter.class);

    @Override
    public boolean handleMessage(final SOAPMessageContext context)
    {
        logToSystemOut(context);
        return true;
    }

    @Override
    public boolean handleFault(final SOAPMessageContext context)
    {
        logToSystemOut(context);
        return true;
    }

    private void logToSystemOut(final SOAPMessageContext smc)
    {
		final Boolean outboundProperty = (Boolean) smc.get(SOAPMessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if (outboundProperty.booleanValue())
        {
            LOG.debug("\nOutbound message:");
        }
        else
        {
            LOG.debug("\nInbound message:");
        }

        final SOAPMessage message = smc.getMessage();
        try
        {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            message.writeTo(baos);
            LOG.debug(baos.toString());
        }
        catch (final Exception e)
        {
            LOG.debug("Exception in handler: " + e, e);
        }
    }

    @Override
    public Set<QName> getHeaders()
    {
        return Collections.emptySet();
    }

    /**
     * This static method adds the handler to the provided port's binding object.
     *
     * @param binding
     *            - The binding object can be fetched by <code>((BindingProvider) port).getBinding()</code>
     */
    public static void addToPort(final Binding binding)
    {
        final List<Handler> handlerChain = binding.getHandlerChain();
        handlerChain.add(new SOAPRequestResponseSpitter());

        /*
         * Check List<Handler> javax.xml.ws.Binding.getHandlerChain() javadocs. It states: Gets a copy of the handler chain for a protocol binding instance. If the returned chain is modified a call to setHandlerChain is required to configure the
         * binding instance with the new chain.
         */
        binding.setHandlerChain(handlerChain);
    }

	@Override
	public void close(final javax.xml.ws.handler.MessageContext context)
	{
		// do nothing
	}
}