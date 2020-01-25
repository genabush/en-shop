/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.ws.inbound.requesthandler;

import javax.xml.transform.Source;

/**
 * Created by Mario Pio Gioiosa
 */
public interface RequestHandler
{

    void printXmlSource(Source source);
}