/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.ws.inbound.actions;

/**
 * @author vasanthramprakasam
 */
public interface FeedImportAction<T>
{
	T execute(T feed);
}
