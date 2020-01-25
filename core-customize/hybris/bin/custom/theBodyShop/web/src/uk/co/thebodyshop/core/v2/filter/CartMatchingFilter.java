/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package uk.co.thebodyshop.core.v2.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.commercewebservicescommons.strategies.CartLoaderStrategy;


/**
 * Filter that puts cart from the requested url into the session.
 */
public class CartMatchingFilter extends AbstractUrlMatchingFilter
{
	public static final String REFRESH_CART_PARAM = "refreshCart";
	private List<String> regexps;
	private CartLoaderStrategy cartLoaderStrategy;
	private boolean cartRefreshedByDefault = true;

	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain filterChain) throws ServletException, IOException
	{
		for (final String regexp : regexps)
		{
			if (matchesUrl(request, regexp))
			{
				final String cartId = getValue(request, regexp);
				if (StringUtils.isNotBlank(cartId))
				{
					cartLoaderStrategy.loadCart(cartId, shouldCartBeRefreshed(request));
					break;
				}
			}
		}
		filterChain.doFilter(request, response);
	}

	protected boolean shouldCartBeRefreshed(final HttpServletRequest request)
	{
		final String refreshParam = request.getParameter(REFRESH_CART_PARAM);
		return refreshParam == null ? isCartRefreshedByDefault() : Boolean.parseBoolean(refreshParam);
	}

	protected List<String> getRegexps()
	{
		return regexps;
	}

	public void setRegexps(final List<String> regexps)
	{
		this.regexps = regexps;
	}

	public CartLoaderStrategy getCartLoaderStrategy()
	{
		return cartLoaderStrategy;
	}

	@Required
	public void setCartLoaderStrategy(final CartLoaderStrategy cartLoaderStrategy)
	{
		this.cartLoaderStrategy = cartLoaderStrategy;
	}

	public boolean isCartRefreshedByDefault()
	{
		return cartRefreshedByDefault;
	}

	public void setCartRefreshedByDefault(final boolean cartRefreshedByDefault)
	{
		this.cartRefreshedByDefault = cartRefreshedByDefault;
	}
}
