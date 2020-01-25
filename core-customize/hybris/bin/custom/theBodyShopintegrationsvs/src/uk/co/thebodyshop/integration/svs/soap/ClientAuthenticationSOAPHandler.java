/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.svs.soap;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.log4j.Logger;

public class ClientAuthenticationSOAPHandler implements SOAPHandler<SOAPMessageContext>
{
    private static final String SOAPENV = "soapenv";

    private static final String WSSE = "wsse";

    private static final String SOAPENV_URI = "http://schames.xmlsoap.org/soap/envelope";

    private static final String WSSE_URI = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";

    private static final Logger LOG = Logger.getLogger(ClientAuthenticationSOAPHandler.class);

    private String username;

    private String password;

    @Override
    public boolean handleFault(final SOAPMessageContext context)
    {
		LOG.debug("ClientAuthenticationSOAPHandler.handleFault() invoked. Soap message :\n");
        dumpSoapMessage(context);
        return true;
    }

    @Override
    public boolean handleMessage(final SOAPMessageContext context)
    {
        final boolean outboundDirection = ((Boolean) context.get(SOAPMessageContext.MESSAGE_OUTBOUND_PROPERTY)).booleanValue();
        if (outboundDirection)
        {
            addAuthenticationHeader(context);
        }
		LOG.debug("ClientAuthenticationSOAPHandler.handleMessage() invoked. Soap message :\n");
        dumpSoapMessage(context);
        return true;
    }

    private void addAuthenticationHeader(final SOAPMessageContext context)
    {
        try
        {
            final SOAPFactory soapFactory = SOAPFactory.newInstance();
            final SOAPElement securityElm = soapFactory.createElement("Security", WSSE, WSSE_URI);
            final QName mustUnderstand = new QName(SOAPENV_URI, "mustUnderstand", SOAPENV);
            securityElm.addAttribute(mustUnderstand, "1");

            final SOAPElement tokenElm = soapFactory.createElement("UsernameToken", WSSE, WSSE_URI);
            final SOAPElement usernameElement = soapFactory.createElement("Username", WSSE, WSSE_URI);
            usernameElement.addTextNode(username);

            final SOAPElement passwordElement = soapFactory.createElement("Password", WSSE, WSSE_URI);
            passwordElement.addTextNode(password);

            tokenElm.addChildElement(usernameElement);
            tokenElm.addChildElement(passwordElement);
            securityElm.addChildElement(tokenElm);

            final SOAPEnvelope envelope = context.getMessage().getSOAPPart().getEnvelope();
            SOAPHeader sh = envelope.getHeader();
            if (sh == null)
            {
                sh = envelope.addHeader();
            }
            sh.addChildElement(securityElm);
        }
        catch (final Exception ex)
        {
            LOG.error(ex.getMessage(), ex);
        }
    }

    private void dumpSoapMessage(final SOAPMessageContext context)
    {
        try
        {
            final SOAPMessage msg = context.getMessage();
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            msg.writeTo(baos);
			LOG.debug(baos.toString());
        }
        catch (final Exception ex)
        {
            LOG.error(ex.getMessage(), ex);
        }
    }

    @Override
    public Set<QName> getHeaders()
    {
        return Collections.emptySet();
    }

	@Override
	public void close(final javax.xml.ws.handler.MessageContext context)
	{
		// do nothing
	}

    public String getUsername()
    {
        return username;
    }

    public void setUsername(final String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(final String password)
    {
        this.password = password;
    }
}
