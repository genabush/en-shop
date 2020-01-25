/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.product.visibility.functions;

/**
 * @author vasanthramprakasam
 */
public interface PredicatedFunction<T,U>
{
	 U applyIfValid(T type);
}
