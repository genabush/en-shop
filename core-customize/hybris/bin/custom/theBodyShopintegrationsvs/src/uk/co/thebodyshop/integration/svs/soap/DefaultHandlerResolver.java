/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.svs.soap;

import java.util.List;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;


public class DefaultHandlerResolver implements HandlerResolver
{
	private List<Handler> handlerList;

	@Override
	public List<Handler> getHandlerChain(final PortInfo portInfo)
	{
		return handlerList;
	}

	public void setHandlerList(final List<Handler> handlerList)
	{
		this.handlerList = handlerList;
	}
}