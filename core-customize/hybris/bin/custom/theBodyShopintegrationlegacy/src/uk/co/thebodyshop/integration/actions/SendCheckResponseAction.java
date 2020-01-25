/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.actions;

/**
 * @author vasanthramprakasam
 */
public interface SendCheckResponseAction<I>
{
	boolean sendToGatewayAndCheckResponse(I input);
}
