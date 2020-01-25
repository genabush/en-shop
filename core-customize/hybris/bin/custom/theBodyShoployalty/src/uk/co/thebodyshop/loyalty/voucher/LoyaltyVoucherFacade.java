/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.loyalty.voucher;

import java.util.List;

import uk.co.thebodyshop.loyalty.data.LoyaltyVoucherData;
import uk.co.thebodyshop.loyalty.data.LoyaltyVoucherDataList;

/**
 * @author Krishna
 */

public interface LoyaltyVoucherFacade
{
	/**
	 * This method used to get user loyalty vouchers
	 */
	public List<LoyaltyVoucherData> getLoyaltyVouchers();

	/**
	 * This method used to apply loyalty voucher on cart
	 */
	public LoyaltyVoucherDataList applyVoucherOnCart(String voucherCode);

	/**
	 * This method used to remove loyalty voucher from cart
	 */
	public LoyaltyVoucherDataList removeVoucherFromCart(String voucherCode);
}
