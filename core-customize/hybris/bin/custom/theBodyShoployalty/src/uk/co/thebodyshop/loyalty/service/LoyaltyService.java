/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.loyalty.service;

import java.util.List;
import java.util.Set;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;

import uk.co.thebodyshop.loyalty.enums.BenefitStatus;
import uk.co.thebodyshop.loyalty.model.LoyaltyCardModel;
import uk.co.thebodyshop.loyalty.model.LoyaltyVoucherModel;

/**
 * @author Krishna
 */
public interface LoyaltyService
{
	/**
	 * Register LYBC Account
	 */
	void registerForLybc(final CustomerModel newCustomer, final BaseSiteModel basesite);

	/**
	 * Fetch New loyalty card for Base site
	 */
	LoyaltyCardModel fetchNewLoyaltyCardForSite(BaseSiteModel baseSite);

	/**
	 * This method used to get user loyalty vouchers
	 */
	public List<LoyaltyVoucherModel> getLoyaltyVouchers(final CustomerModel customer);

	/**
	 * This method used to get user loyalty voucher for code
	 */
	public LoyaltyVoucherModel getUserLoyaltyVoucherForCode(String code, final CustomerModel customer);

	/**
	 * This method used to get loyalty voucher for code
	 */
	public LoyaltyVoucherModel getLoyaltyVoucherForCode(String code);

	/**
	 * This method is used to check weather LYBC registered customer
	 */
	public boolean isCustomerLybc(final CustomerModel customerModel);

	/**
	 * This method updates @LoyaltyVoucherModel BenefitStatus
	 */
	public void updateLoyaltyVouchersStatus(final Set<LoyaltyVoucherModel> loyaltyVouchers, final BenefitStatus status);

	/**
	 * This method captures all @loyaltyVouchers appleid to an order and return the captured amount
	 */
	public double getCapturedOrderLoyaltyVouchersAmount(final OrderModel orderModel, final double calculatedCaptureAmount);

	/**
	 * This method captures specific @LoyaltyVoucherModel
	 */
	public double captureLoyaltyVoucher(final OrderModel orderModel, final LoyaltyVoucherModel loyaltyVoucher, final double amount);

	/**
	 * This method releases specific @LoyaltyVoucherModel
	 */
	public void releaseLoyaltyVoucher(final LoyaltyVoucherModel loyaltyVoucher);
}
