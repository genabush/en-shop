/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package uk.co.thebodyshop.fulfilmentprocess.actions.consignment;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.enums.PaymentStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.action.AbstractAction;

import uk.co.thebodyshop.core.util.MathUtils;
import uk.co.thebodyshop.integrations.svs.data.GiftCardCaptureData;
import uk.co.thebodyshop.loyalty.service.LoyaltyService;
import uk.co.thebodyshop.payment.services.TbsPaymentService;
import uk.co.thebodyshop.payment.strategies.CaptureAmountCalculationStrategy;

public class TakePaymentAction extends AbstractAction<ConsignmentProcessModel>
{
	private static final Logger LOG = LoggerFactory.getLogger(TakePaymentAction.class);

	private TbsPaymentService tbsPaymentService;

	private CaptureAmountCalculationStrategy captureAmountCalculationStrategy;

	private LoyaltyService loyaltyService;

	public enum Transition
	{
		OK, WAIT, NOK;

		public static Set<String> getStringValues()
		{
			final Set<String> res = new HashSet<>();

			for (final Transition transition : Transition.values())
			{
				res.add(transition.toString());
			}
			return res;
		}
	}

	@Override
	public String execute(final ConsignmentProcessModel consignmentProcess)
	{
		final OrderModel order = consignmentProcess.getParentProcess().getOrder();
		final ConsignmentModel consignment = consignmentProcess.getConsignment();

		final double captureAmount = getCaptureAmountCalculationStrategy().calculateCaptureAmount(order, consignment);
		calculateAndSaveDeliveryOrderTotal(order, captureAmount);
		double amountRemaining = captureAmount - getLoyaltyService().getCapturedOrderLoyaltyVouchersAmount(order, captureAmount);
		boolean giftCardCaptureSuccessful = true;
		boolean capturedPrimaryPayment = true;

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Calculated capture amount [{}] for order [{}]", captureAmount, order.getCode());
		}

		final GiftCardCaptureData giftCardCaptureData = getTbsPaymentService().captureSecondaryPayment(order, amountRemaining);
		amountRemaining = giftCardCaptureData.getAmountRemaining();
		giftCardCaptureSuccessful = giftCardCaptureData.isCaptureSuccesfull();

		capturedPrimaryPayment = getTbsPaymentService().capturePrimaryPayment(amountRemaining, order);

		return getNextProcessTransition(order, consignment, giftCardCaptureSuccessful, capturedPrimaryPayment);
	}

	private void calculateAndSaveDeliveryOrderTotal(final OrderModel order, final double amountRemaining)
	{
		order.setDeliveredOrderTotal(Double.valueOf(MathUtils.round(amountRemaining)));
		getModelService().save(order);
	}

	private String getNextProcessTransition(final OrderModel order, final ConsignmentModel consignmentModel, final boolean giftCardCaptureSuccessful, final boolean capturedPrimaryPayment)
	{
		if (giftCardCaptureSuccessful && capturedPrimaryPayment)
		{
			setConsignmentStatus(consignmentModel, ConsignmentStatus.PAYMENT_CAPTURE_CHECK_REQUIRED);
			return setOrderCaptureSuccess(order, consignmentModel);
		}
		else
		{
			setConsignmentStatus(consignmentModel, ConsignmentStatus.PAYMENT_NOT_CAPTURED);
			return setOrderCaptureFailure(order, giftCardCaptureSuccessful, capturedPrimaryPayment);
		}
	}

	private void setConsignmentStatus(final ConsignmentModel consignmentModel, final ConsignmentStatus paymentCaptured)
	{
		consignmentModel.setStatus(paymentCaptured);
		getModelService().save(consignmentModel);
	}

	private String setOrderCaptureSuccess(final OrderModel order, final ConsignmentModel consignmentModel)
	{
		setOrderAndPaymentStatus(order, OrderStatus.PAYMENT_CAPTURE_CHECK_REQUIRED, PaymentStatus.CAPTURE_PENDING);
		return Transition.OK.toString();
	}

	private String setOrderCaptureFailure(final OrderModel order, final boolean giftCardCaptureSuccessful, final boolean capturedPrimaryPayment)
	{
		if (!capturedPrimaryPayment && giftCardCaptureSuccessful)
		{
			setOrderAndPaymentStatus(order, OrderStatus.PRIMARY_PAYMENT_NOT_CAPTURED, PaymentStatus.NOTPAID);
			return Transition.WAIT.toString();
		}
		else
		{
			setOrderAndPaymentStatus(order, OrderStatus.PAYMENT_NOT_CAPTURED, PaymentStatus.NOTPAID);
			return Transition.NOK.toString();
		}
	}

	private void setOrderAndPaymentStatus(final OrderModel order, final OrderStatus orderStatus, final PaymentStatus paymentStatus)
	{
		order.setPaymentStatus(paymentStatus);
		super.setOrderStatus(order, orderStatus);
	}

	protected TbsPaymentService getTbsPaymentService()
	{
		return tbsPaymentService;
	}

	public void setTbsPaymentService(final TbsPaymentService tbsPaymentService)
	{
		this.tbsPaymentService = tbsPaymentService;
	}

	protected CaptureAmountCalculationStrategy getCaptureAmountCalculationStrategy()
	{
		return captureAmountCalculationStrategy;
	}

	public void setCaptureAmountCalculationStrategy(final CaptureAmountCalculationStrategy captureAmountCalculationStrategy)
	{
		this.captureAmountCalculationStrategy = captureAmountCalculationStrategy;
	}

	@Override
	public Set<String> getTransitions()
	{
		return Transition.getStringValues();
	}

	/**
	 * @return the loyaltyService
	 */
	protected LoyaltyService getLoyaltyService()
	{
		return loyaltyService;
	}

	/**
	 * @param loyaltyService the loyaltyService to set
	 */
	public void setLoyaltyService(final LoyaltyService loyaltyService)
	{
		this.loyaltyService = loyaltyService;
	}
}
