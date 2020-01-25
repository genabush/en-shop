/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.loyalty.dao;

import java.util.List;

import de.hybris.platform.core.model.user.CustomerModel;

import uk.co.thebodyshop.loyalty.model.LoyaltyCardModel;
import uk.co.thebodyshop.loyalty.model.LoyaltyMembershipModel;
import uk.co.thebodyshop.loyalty.model.LoyaltyVoucherModel;

/**
 * @author Krishna
 */
public interface LoyaltyCardDao
{
	/**
	 * Fetch new loyalty card
	 */
	LoyaltyCardModel fetchNewLoyaltyCard(LoyaltyMembershipModel loyaltyMembershipModel);

	/**
	 * This method used to get user loyalty vouchers
	 */
	public List<LoyaltyVoucherModel> getLoyaltyVouchers(CustomerModel customer);

	/**
	 * This method used to get user loyalty voucher for code
	 */
	public LoyaltyVoucherModel getUserLoyaltyVoucherForCode(String code, CustomerModel customer);

	/**
	 * This method used to get loyalty voucher for code
	 */
	public LoyaltyVoucherModel getLoyaltyVoucherForCode(String code);
}
