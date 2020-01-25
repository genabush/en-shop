/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.dataimport.batch.aop;

import org.aspectj.lang.ProceedingJoinPoint;

import de.hybris.platform.servicelayer.user.UserService;

/**
 * @author prateek.goel
 */
public class SessionUserAsAdminAspect
{

	private UserService userService;


	/**
	 * Invokes a method and set admin user in session
	 *
	 * @param pjp
	 * @return result of the invocation
	 * @throws Throwable
	 */
	public Object execute(final ProceedingJoinPoint pjp) throws Throwable // NOSONAR
	{
		getUserService().setCurrentUser(getUserService().getAdminUser());
		return pjp.proceed();
	}

	protected UserService getUserService()
	{
		return userService;
	}

	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

}
