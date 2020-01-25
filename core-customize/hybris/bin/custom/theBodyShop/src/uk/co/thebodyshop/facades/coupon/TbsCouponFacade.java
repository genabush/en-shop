/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.facades.coupon;

import de.hybris.platform.commercefacades.voucher.VoucherFacade;

/**
 * @author Jagadeesh
 */
public interface TbsCouponFacade extends VoucherFacade
{
	/**
	 * This method used to check the maximum coupon allowed on base store and to the cart coupon applied and allow to apply the new coupon
	 *
	 * @return boolean
	 */
	public boolean checkMaximumCouponAllowedForCart();
}
