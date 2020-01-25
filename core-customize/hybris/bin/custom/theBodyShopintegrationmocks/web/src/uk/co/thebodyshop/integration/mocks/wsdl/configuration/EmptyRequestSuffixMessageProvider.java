/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.mocks.wsdl.configuration;

import org.springframework.util.Assert;
import org.springframework.ws.wsdl.wsdl11.provider.SuffixBasedMessagesProvider;
import org.w3c.dom.Element;

/**
 * @author vasanthramprakasam
 *
 * This enables us to use empty suffixes for request. OOTB spring uses suffixes like request/response/error
 * to determine which elements to use as request/response for the web service, we over ride this to allow
 * empty suffixes for cases like place order
 *
 */
public class EmptyRequestSuffixMessageProvider extends SuffixBasedMessagesProvider {

    protected String requestSuffix = DEFAULT_REQUEST_SUFFIX;

    public String getRequestSuffix() {
        return this.requestSuffix;
    }

    public void setRequestSuffix(String requestSuffix) {
        this.requestSuffix = requestSuffix;
    }

    @Override
    protected boolean isMessageElement(Element element) {
        if (isMessageElement0(element)) {
            String elementName = getElementName(element);
            Assert.hasText(elementName, "Element has no name");
            return elementName.endsWith(getResponseSuffix())
                    || (getRequestSuffix().isEmpty() ? true : elementName.endsWith(getRequestSuffix()))
                    || elementName.endsWith(getFaultSuffix());
        }
        return false;
    }

    protected boolean isMessageElement0(Element element) {
        return "element".equals(element.getLocalName())
                && "http://www.w3.org/2001/XMLSchema".equals(element.getNamespaceURI());
    }

}
