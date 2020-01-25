/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.mocks.response;

/**
 * @author vasanthramprakasam
 */
public interface ResponseFactory<T,U>
{
	 U createResponse(T type);
}
