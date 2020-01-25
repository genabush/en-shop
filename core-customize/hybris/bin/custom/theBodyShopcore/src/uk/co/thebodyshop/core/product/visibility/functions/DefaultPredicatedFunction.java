/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.product.visibility.functions;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author vasanthramprakasam
 */
public class DefaultPredicatedFunction<T,U>  implements PredicatedFunction<T,U>
{
	 private final Predicate<T> predicate;

	 private final Function<T,U> function;

	 public DefaultPredicatedFunction(Predicate<T> predicate, Function<T,U> function)
	 {
			this.predicate = predicate;
			this.function = function;
	 }

	 @Override
	 public U applyIfValid(T type)
	 {
			if(predicate.test(type))
			{
				 return function.apply(type);
			}
			return null;
	 }
}
