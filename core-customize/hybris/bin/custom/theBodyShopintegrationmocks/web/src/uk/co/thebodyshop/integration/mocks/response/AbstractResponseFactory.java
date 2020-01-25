/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.mocks.response;

import java.util.function.Predicate;

/**
 * @author vasanthramprakasam
 */
public abstract class AbstractResponseFactory<T,U> implements ResponseFactory<T,U>
{
	 @Override
	 public U createResponse(T type)
	 {
			if(getFailurePredicate().test(type))
			{
				 return createFailureResponse(type);
			}
			return createSuccessResponse(type);
	 }

	 protected  Predicate<T> getFailurePredicate()
	 {
	 	 return (type) -> false;
	 }

	 protected abstract U createFailureResponse(T type);

	 protected abstract U createSuccessResponse(T type);

}
