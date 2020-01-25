/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.ws.response.handlers;

/**
 * @author vasanthramprakasam
 */
public interface ResponseHandler<I,O>
{
	boolean handleResponse(I input, O output);
}
