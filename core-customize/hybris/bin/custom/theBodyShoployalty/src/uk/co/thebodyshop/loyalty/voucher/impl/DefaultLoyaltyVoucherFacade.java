/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.loyalty.voucher.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import uk.co.thebodyshop.core.calculation.strategies.OutstandingAmountCalculationStrategy;
import uk.co.thebodyshop.core.loyalty.discount.calculation.strategy.OrderEntryLoyaltyDiscountCalculationStrategy;
import uk.co.thebodyshop.loyalty.data.LoyaltyVoucherData;
import uk.co.thebodyshop.loyalty.data.LoyaltyVoucherDataList;
import uk.co.thebodyshop.loyalty.enums.BenefitStatus;
import uk.co.thebodyshop.loyalty.model.LoyaltyVoucherModel;
import uk.co.thebodyshop.loyalty.service.LoyaltyService;
import uk.co.thebodyshop.loyalty.voucher.LoyaltyVoucherFacade;

/**
 * @author Krishna
 */

public class DefaultLoyaltyVoucherFacade implements LoyaltyVoucherFacade
{
	private static final Logger LOG = Logger.getLogger(DefaultLoyaltyVoucherFacade.class);
	private static final String LOYALTY_VOUCHER_ALREADYAPPLIED = "loyalty.voucher.alreadyapplied";
	private static final String LOYALTY_VOUCHER_LIMITREACHED = "loyalty.voucher.limitreached";
	private static final String LOYALTY_VOUCHER_VALIDATIONISSUE = "loyalty.voucher.validationissue";
	private static final String LOYALTY_VOUCHER_EXCEEDS_BAGPRICE = "loyalty.voucher.exceeds.bag.price";

	private final LoyaltyService loyaltyService;

	private final CartService cartService;

	private final BaseStoreService baseStoreService;

	private final ModelService modelService;

	private final UserService userService;

	private final OutstandingAmountCalculationStrategy outstandingAmountCalculationStrategy;

	private final Converter<LoyaltyVoucherModel, LoyaltyVoucherData> loyaltyVoucherConverter;
	
	private final OrderEntryLoyaltyDiscountCalculationStrategy orderEntryLoyaltyDiscountCalculationStrategy;

	@Override
	public List<LoyaltyVoucherData> getLoyaltyVouchers()
	{
		final CustomerModel customer = (CustomerModel) getUserService().getCurrentUser();
		final List<LoyaltyVoucherModel> loyaltyVouchers = getLoyaltyService().getLoyaltyVouchers(customer);
		if (CollectionUtils.isNotEmpty(loyaltyVouchers))
		{
			return getLoyaltyVoucherConverter().convertAll(loyaltyVouchers);
		}
		return Collections.EMPTY_LIST;
	}

	@Override
	public LoyaltyVoucherDataList applyVoucherOnCart(final String voucherCode)
	{
		final LoyaltyVoucherDataList voucherDataList = new LoyaltyVoucherDataList();
		final CartModel cart = getCartService().getSessionCart();
		if (Objects.nonNull(cart))
		{
			CustomerModel customer = (CustomerModel) getUserService().getCurrentUser();
			if (cartContainsLoyaltyVoucher(voucherCode, cart))
			{
				return getResponse(voucherCode, cart, LOYALTY_VOUCHER_ALREADYAPPLIED, customer);
			}
			
			final LoyaltyVoucherModel loyaltyVoucher = getLoyaltyService().getUserLoyaltyVoucherForCode(voucherCode, customer);
			
			if (loyaltyVoucher == null)
			{
				return getResponse(voucherCode, cart, LOYALTY_VOUCHER_VALIDATIONISSUE, customer);
			}

			if (!checkVoucherValueWithOutstandingAmount(voucherCode, cart, loyaltyVoucher))
			{
				return getResponse(voucherCode, cart, LOYALTY_VOUCHER_EXCEEDS_BAGPRICE, customer);
			}

			if (!verifyMaximumVouchersPerOrderReached(cart))
			{
				final Set<LoyaltyVoucherModel> loyaltyVouchers = new HashSet<LoyaltyVoucherModel>();
				loyaltyVouchers.add(loyaltyVoucher);
				loyaltyVouchers.addAll(cart.getAppliedLoyaltyVouchers());
				cart.setAppliedLoyaltyVouchers(loyaltyVouchers);
				getModelService().save(cart);
				getOrderEntryLoyaltyDiscountCalculationStrategy().calculateOrderEntryLoyaltyDiscount(cart);
			}

			if (verifyMaximumVouchersPerOrderReached(cart))
			{
				voucherDataList.setMaxVouchersReached(Boolean.TRUE);
				voucherDataList.setErrorMessage(LOYALTY_VOUCHER_LIMITREACHED);
			}
			voucherDataList.setLoyaltyVouchers(convertLoyaltyVouchers(customer));
			return voucherDataList;
		}
		return voucherDataList;
	}
	
	@Override
	public LoyaltyVoucherDataList removeVoucherFromCart(final String voucherCode)
	{
		final LoyaltyVoucherDataList voucherDataList = new LoyaltyVoucherDataList();
		final CartModel cart = getCartService().getSessionCart();
		if (CollectionUtils.isNotEmpty(cart.getAppliedLoyaltyVouchers()))
		{
			final Set<LoyaltyVoucherModel> loyaltyVouchers = new HashSet<LoyaltyVoucherModel>();
			loyaltyVouchers.addAll(cart.getAppliedLoyaltyVouchers());
			for (final LoyaltyVoucherModel voucher : loyaltyVouchers)
			{
				if (voucherCode.equals(voucher.getVoucherCode()))
				{
					loyaltyVouchers.remove(voucher);
					cart.setAppliedLoyaltyVouchers(loyaltyVouchers);
					getModelService().save(cart);
					getOrderEntryLoyaltyDiscountCalculationStrategy().calculateOrderEntryLoyaltyDiscount(cart);
					break;
				}
			}
			final CustomerModel customer = (CustomerModel) getUserService().getCurrentUser();
			voucherDataList.setLoyaltyVouchers(convertLoyaltyVouchers(customer));
		}
		return voucherDataList;
	}

	private boolean cartContainsLoyaltyVoucher(final String voucherCode, final CartModel cart)
	{
		final Set<LoyaltyVoucherModel> appliedLoyaltyVouchers = cart.getAppliedLoyaltyVouchers();
		return appliedLoyaltyVouchers.stream().filter(loyaltyVoucher -> StringUtils.equalsIgnoreCase(voucherCode, loyaltyVoucher.getVoucherCode())).findAny().isPresent();
	}

	private boolean verifyMaximumVouchersPerOrderReached(final CartModel cart)
	{
		boolean maximumLoyaltyVouchersReached = false;
		final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();
		final int maxVoucherNumberAllowed = currentBaseStore.getMaximumVouchersPerOrder();
		int vouchersInCart = 0;

		if (CollectionUtils.isNotEmpty(cart.getAppliedLoyaltyVouchers()))
		{
			vouchersInCart = cart.getAppliedLoyaltyVouchers().size();
		}
		if (maxVoucherNumberAllowed <= vouchersInCart)
		{
			maximumLoyaltyVouchersReached = true;
		}
		return maximumLoyaltyVouchersReached;
	}

	private List<LoyaltyVoucherData> convertLoyaltyVouchers(final CustomerModel customer)
	{
		List<LoyaltyVoucherModel> loyaltyVouchers = getLoyaltyService().getLoyaltyVouchers(customer);
		if(CollectionUtils.isNotEmpty(loyaltyVouchers))
		{
			return getLoyaltyVoucherConverter().convertAll(loyaltyVouchers);
		}
		else
		{
			return Collections.EMPTY_LIST;
		}
	}

	private boolean checkVoucherValueWithOutstandingAmount(final String voucherCode, final CartModel cart, final LoyaltyVoucherModel loyaltyVoucher)
	{
		boolean isApplicable = false;

		final Double voucherValue = loyaltyVoucher.getValue();
		final Double outstandingAmount = Double.valueOf(getOutstandingAmountCalculationStrategy().getOutstandingAmount(cart).doubleValue());
		final Double appliedGiftcardAmount = getAppliedGiftcardAmount(cart);
		if (appliedGiftcardAmount == 0.0)
		{
			isApplicable = true;
			if (voucherValue >= outstandingAmount)
			{
				isApplicable = false;
			}
		}
		else
		{
			if (voucherValue <= outstandingAmount)
			{
				isApplicable = true;
			}
		}
		return isApplicable;
	}

	private LoyaltyVoucherDataList getResponse(final String voucherCode, final CartModel cart, final String message, CustomerModel customer)
	{
		final LoyaltyVoucherDataList loyaltyVoucherData = new LoyaltyVoucherDataList();
		loyaltyVoucherData.setErrorMessage(message);
		loyaltyVoucherData.setLoyaltyVouchers(convertLoyaltyVouchers(customer));
		loyaltyVoucherData.setMaxVouchersReached(verifyMaximumVouchersPerOrderReached(cart));
		return loyaltyVoucherData;
	}

	private Double getAppliedGiftcardAmount(final AbstractOrderModel abstractOrderModel)
	{
		return null != abstractOrderModel ? abstractOrderModel.getAmountGiftCards() : 0.0;
	}

	protected LoyaltyService getLoyaltyService()
	{
		return loyaltyService;
	}

	protected Converter<LoyaltyVoucherModel, LoyaltyVoucherData> getLoyaltyVoucherConverter()
	{
		return loyaltyVoucherConverter;
	}

	protected CartService getCartService()
	{
		return cartService;
	}

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	protected OutstandingAmountCalculationStrategy getOutstandingAmountCalculationStrategy()
	{
		return outstandingAmountCalculationStrategy;
	}
	
	protected OrderEntryLoyaltyDiscountCalculationStrategy getOrderEntryLoyaltyDiscountCalculationStrategy()
	{
		return orderEntryLoyaltyDiscountCalculationStrategy;
	}

	public DefaultLoyaltyVoucherFacade(final LoyaltyService loyaltyService, final CartService cartService, final Converter<LoyaltyVoucherModel, LoyaltyVoucherData> loyaltyVoucherConverter, final BaseStoreService baseStoreService,
			final ModelService modelService, final UserService userService, final OutstandingAmountCalculationStrategy outstandingAmountCalculationStrategy, final OrderEntryLoyaltyDiscountCalculationStrategy orderEntryLoyaltyDiscountCalculationStrategy)
	{
		this.loyaltyService = loyaltyService;
		this.cartService = cartService;
		this.loyaltyVoucherConverter = loyaltyVoucherConverter;
		this.baseStoreService = baseStoreService;
		this.modelService = modelService;
		this.userService = userService;
		this.outstandingAmountCalculationStrategy = outstandingAmountCalculationStrategy;
		this.orderEntryLoyaltyDiscountCalculationStrategy = orderEntryLoyaltyDiscountCalculationStrategy;
	}
}
