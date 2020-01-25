/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.util;

import java.math.BigDecimal;

public class MathUtils
{
	public static final int ROUNDING_MODE = BigDecimal.ROUND_HALF_EVEN;

	public static final int SCALE_NUMBER = 2;

	private MathUtils()
	{
		// private contructor
	}

	public static double round(final double amount)
	{
		if ((Double.isInfinite(amount)) || (Double.isNaN(amount)))
		{
			return 0d;
		}
		return round(new BigDecimal(amount)).doubleValue();
	}

	public static BigDecimal round(final BigDecimal amount)
	{
		return amount.setScale(SCALE_NUMBER, ROUNDING_MODE);
	}

	public static double round(final double amount, final int scale)
	{
		if ((Double.isInfinite(amount)) || (Double.isNaN(amount)))
		{
			return 0d;
		}

		return round(new BigDecimal(amount), scale).doubleValue();
	}

	public static BigDecimal round(final BigDecimal amount, final int scale)
	{
		return amount.setScale(scale, ROUNDING_MODE);
	}
}
