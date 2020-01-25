/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.loyalty.order.hook;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import de.hybris.platform.commerceservices.order.hook.CommercePlaceOrderMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import uk.co.thebodyshop.loyalty.enums.BenefitStatus;
import uk.co.thebodyshop.loyalty.model.LoyaltyVoucherModel;
import uk.co.thebodyshop.loyalty.service.LoyaltyService;

/**
 * @author Krishna
 */
public class LoyaltyVoucherRedemptionPlaceOrderMethodHook implements CommercePlaceOrderMethodHook
{
	private final ModelService modelService;

	private final LoyaltyService loyaltyService;

	private final UserService userService;

	@Override
	public void afterPlaceOrder(final CommerceCheckoutParameter parameter, final CommerceOrderResult result) throws InvalidCartException
	{
		validateParameterNotNullStandardMessage("result", result);

		final OrderModel orderModel = result.getOrder();

		validateParameterNotNullStandardMessage("orderModel", orderModel);

		loyaltyService.updateLoyaltyVouchersStatus(orderModel.getAppliedLoyaltyVouchers(), BenefitStatus.APPLIED);
	}

	@Override
	public void beforePlaceOrder(final CommerceCheckoutParameter parameter) throws InvalidCartException
	{
		// not implemented
	}

	@Override
	public void beforeSubmitOrder(final CommerceCheckoutParameter parameter, final CommerceOrderResult result) throws InvalidCartException
	{
		validateParameterNotNullStandardMessage("parameter", parameter);

		final CartModel cartModel = parameter.getCart();

		validateParameterNotNullStandardMessage("cartModel", cartModel);

		final Set<LoyaltyVoucherModel> appliedLoyaltyVouchers = cartModel.getAppliedLoyaltyVouchers();
		if (CollectionUtils.isNotEmpty(appliedLoyaltyVouchers))
		{
			final CustomerModel customer = (CustomerModel) getUserService().getCurrentUser();
			for (final LoyaltyVoucherModel loyaltyVoucher : appliedLoyaltyVouchers)
			{
				final LoyaltyVoucherModel voucher = getLoyaltyService().getUserLoyaltyVoucherForCode(loyaltyVoucher.getVoucherCode(), customer);
				if (voucher == null)
				{
					throw new InvalidCartException(String.format("Order [%s] contains invalid loyalty voucher: [%s]", cartModel.getCode(), loyaltyVoucher.getVoucherCode()));
				}
			}
		}
	}

	public LoyaltyVoucherRedemptionPlaceOrderMethodHook(final ModelService modelService, final LoyaltyService loyaltyService, final UserService userService)
	{
		this.modelService = modelService;
		this.loyaltyService = loyaltyService;
		this.userService = userService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected LoyaltyService getLoyaltyService()
	{
		return loyaltyService;
	}

	protected UserService getUserService()
	{
		return userService;
	}
}
