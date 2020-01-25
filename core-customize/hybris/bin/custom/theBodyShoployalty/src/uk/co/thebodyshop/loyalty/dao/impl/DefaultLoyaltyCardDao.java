/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.loyalty.dao.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import uk.co.thebodyshop.loyalty.dao.LoyaltyCardDao;
import uk.co.thebodyshop.loyalty.enums.BenefitStatus;
import uk.co.thebodyshop.loyalty.enums.LoyaltyCardStatus;
import uk.co.thebodyshop.loyalty.model.LoyaltyCardModel;
import uk.co.thebodyshop.loyalty.model.LoyaltyMembershipModel;
import uk.co.thebodyshop.loyalty.model.LoyaltyVoucherModel;

/**
 * @author Krishna
 */
public class DefaultLoyaltyCardDao implements LoyaltyCardDao
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultLoyaltyCardDao.class);

	private final FlexibleSearchService flexibleSearchService;

	private static final String AVAILABLE_LOYALTY_CARDS_QUERY = (new StringBuilder("SELECT {lc.pk} FROM { ").append(LoyaltyCardModel._TYPECODE).append(" AS lc JOIN ").append(LoyaltyMembershipModel._TYPECODE).append(" AS lm ON {lc.")
			.append(LoyaltyCardModel.LOYALTYMEMBERSHIP).append("} = {lm.pk} } WHERE {lm.pk} =?loyaltyMembership AND {lc.cardStatus} = ?cardStatus")).toString();

	private static final String GET_LOYALTY_VOUCHERS = "SELECT {" + LoyaltyVoucherModel.PK + "} " + "FROM {" + LoyaltyVoucherModel._TYPECODE + "} " + "WHERE {" + LoyaltyVoucherModel.CUSTOMER + "} = ?customer " + "AND {"
			+ LoyaltyVoucherModel.STATUS + "} = ?status" + " ORDER BY {" + LoyaltyVoucherModel.EXPIRYDATE + "} ASC";

	private static final String GET_LOYALTY_VOUCHER = "SELECT {" + LoyaltyVoucherModel.PK + "} " + "FROM {" + LoyaltyVoucherModel._TYPECODE + "} " + "WHERE {" + LoyaltyVoucherModel.VOUCHERCODE + "} = ?voucherCode " + "AND {" + LoyaltyVoucherModel.CUSTOMER + "} = ?customer " + "AND {"
			+ LoyaltyVoucherModel.STATUS + "} = ?status";
	
	private static final String LOYALTY_VOUCHER_QUERY = "SELECT {" + LoyaltyVoucherModel.PK + "} " + "FROM {" + LoyaltyVoucherModel._TYPECODE + "} " + "WHERE {" + LoyaltyVoucherModel.VOUCHERCODE + "} = ?voucherCode";
	
	@Override
	public LoyaltyCardModel fetchNewLoyaltyCard(final LoyaltyMembershipModel loyaltyMembershipModel)
	{
		validateParameterNotNull(loyaltyMembershipModel, "membership must not be null!");
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("loyaltyMembership", loyaltyMembershipModel);
		params.put("cardStatus", LoyaltyCardStatus.NEW);

		final SearchResult<LoyaltyCardModel> result = getFlexibleSearchService().search(AVAILABLE_LOYALTY_CARDS_QUERY, params);

		if (CollectionUtils.isEmpty(result.getResult()))
		{
			LOG.warn("No LoyaltyCard found with status [" + LoyaltyCardStatus.NEW + "] for membership[" + loyaltyMembershipModel + "]");
			return null;
		}
		return result.getResult().get(0);
	}

	@Override
	public List<LoyaltyVoucherModel> getLoyaltyVouchers(final CustomerModel customer)
	{
		validateParameterNotNull(customer, "customer must not be null!");
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("customer", customer);
		params.put("status", BenefitStatus.ACTIVE);
		final SearchResult<LoyaltyVoucherModel> results = getFlexibleSearchService().search(GET_LOYALTY_VOUCHERS, params);

		if (CollectionUtils.isEmpty(results.getResult()))
		{
			LOG.warn("No Loyalty Voucher found for current customer[" + customer.getUid() + "]");
			return null;
		}
		return results.getResult();
	}
	
	@Override
	public LoyaltyVoucherModel getUserLoyaltyVoucherForCode(final String voucherCode, final CustomerModel customer)
	{
		validateParameterNotNull(voucherCode, "voucherCode must not be null!");
		validateParameterNotNull(customer, "customer must not be null!");
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("voucherCode", voucherCode);
		params.put("customer", customer);
		params.put("status", BenefitStatus.ACTIVE);
		final SearchResult<LoyaltyVoucherModel> results = getFlexibleSearchService().search(GET_LOYALTY_VOUCHER, params);
		return CollectionUtils.isNotEmpty(results.getResult()) ? results.getResult().get(0) : null;
	}
	
	@Override
	public LoyaltyVoucherModel getLoyaltyVoucherForCode(final String voucherCode)
	{
		validateParameterNotNull(voucherCode, "voucherCode must not be null!");
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("voucherCode", voucherCode);
		final SearchResult<LoyaltyVoucherModel> results = getFlexibleSearchService().search(LOYALTY_VOUCHER_QUERY, params);
		return CollectionUtils.isNotEmpty(results.getResult()) ? results.getResult().get(0) : null;
	}

	public DefaultLoyaltyCardDao(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	protected FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}
}
