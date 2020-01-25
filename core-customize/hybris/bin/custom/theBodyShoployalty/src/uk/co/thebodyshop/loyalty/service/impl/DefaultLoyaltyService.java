/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.loyalty.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.payment.PaymentService;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.dto.TransactionStatusDetails;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.model.ModelService;

import uk.co.thebodyshop.core.util.MathUtils;
import uk.co.thebodyshop.loyalty.dao.LoyaltyCardDao;
import uk.co.thebodyshop.loyalty.enums.BenefitStatus;
import uk.co.thebodyshop.loyalty.enums.LoyaltyCardStatus;
import uk.co.thebodyshop.loyalty.model.LoyaltyCardModel;
import uk.co.thebodyshop.loyalty.model.LoyaltyMembershipModel;
import uk.co.thebodyshop.loyalty.model.LoyaltyVoucherModel;
import uk.co.thebodyshop.loyalty.service.LoyaltyService;
import uk.co.thebodyshop.loyalty.voucher.comparator.VoucherComparator;

/**
 * @author Krishna
 */
public class DefaultLoyaltyService implements LoyaltyService
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultLoyaltyService.class);

	private static final String VOUCHER = "Voucher";

	private final LoyaltyCardDao loyaltyCardDao;

	private final ModelService modelService;

	private final PaymentService paymentService;

	@Override
	public void registerForLybc(final CustomerModel newCustomer, final BaseSiteModel basesite)
	{
		final Set<LoyaltyCardModel> loyaltySet = new HashSet<LoyaltyCardModel>();
		final LoyaltyCardModel loyaltyCard = fetchNewLoyaltyCardForSite(basesite);
		if (Objects.nonNull(loyaltyCard))
		{
			loyaltySet.add(loyaltyCard);
			newCustomer.setLoyaltyCards(loyaltySet);
			newCustomer.setDefaultLoyaltyCard(loyaltyCard);
			getModelService().save(newCustomer);
		}
	}

	@Override
	public LoyaltyCardModel fetchNewLoyaltyCardForSite(final BaseSiteModel baseSite)
	{
		if (baseSite != null)
		{
			final LoyaltyMembershipModel loyaltyMembership = baseSite.getLoyaltyMembership();
			if (loyaltyMembership != null)
			{
				synchronized (loyaltyCardDao)
				{
					final LoyaltyCardModel loyaltyCard = loyaltyCardDao.fetchNewLoyaltyCard(loyaltyMembership);

					// Ensure that no single card is assigned to 2 different customer
					if (loyaltyCard != null)
					{
						loyaltyCard.setCardStatus(LoyaltyCardStatus.USED);
						modelService.save(loyaltyCard);
					}
					return loyaltyCard;
				}
			}
			else
			{
				LOG.info("No Loyalty Membership found for: " + baseSite.getName());
			}
		}
		return null;
	}

	@Override
	public List<LoyaltyVoucherModel> getLoyaltyVouchers(final CustomerModel customer)
	{
		return getLoyaltyCardDao().getLoyaltyVouchers(customer);
	}

	@Override
	public LoyaltyVoucherModel getUserLoyaltyVoucherForCode(final String code, final CustomerModel customer)
	{
		return getLoyaltyCardDao().getUserLoyaltyVoucherForCode(code, customer);
	}

	@Override
	public LoyaltyVoucherModel getLoyaltyVoucherForCode(final String code)
	{
		return getLoyaltyCardDao().getLoyaltyVoucherForCode(code);
	}

	@Override
	public boolean isCustomerLybc(final CustomerModel customerModel)
	{
		if (Objects.nonNull(customerModel) && Objects.nonNull(customerModel.getDefaultLoyaltyCard()) && LoyaltyCardStatus.USED.equals(customerModel.getDefaultLoyaltyCard().getCardStatus()))
		{
			return true;
		}
		return false;
	}

	@Override
	public void updateLoyaltyVouchersStatus(final Set<LoyaltyVoucherModel> appliedLoyaltyVouchers, final BenefitStatus status)
	{
		if (CollectionUtils.isNotEmpty(appliedLoyaltyVouchers))
		{
			final Set<LoyaltyVoucherModel> loyaltyVouchers = new HashSet<>();
			for (final LoyaltyVoucherModel loyaltyVoucher : appliedLoyaltyVouchers)
			{
				loyaltyVoucher.setStatus(getLoyaltyVoucherStatus(loyaltyVoucher, status));
				loyaltyVouchers.add(loyaltyVoucher);
			}
			getModelService().saveAll(loyaltyVouchers);
		}
	}

	private BenefitStatus getLoyaltyVoucherStatus(final LoyaltyVoucherModel loyaltyVoucher, final BenefitStatus status)
	{
		if (status.equals(BenefitStatus.APPLIED))
		{
			return status;
		}
		if (loyaltyVoucher.getExpiryDate().after(new Date()))
		{
			return status;
		}
		return BenefitStatus.EXPIRED;
	}

	@Override
	public double getCapturedOrderLoyaltyVouchersAmount(final OrderModel orderModel, final double calculatedCaptureAmount)
	{
		if (orderModel.getLoyaltyVoucherDiscount() <= calculatedCaptureAmount)
		{
			processFullVouchersCapture(orderModel);
			return orderModel.getLoyaltyVoucherDiscount().doubleValue();
		}
		else
		{
			processPartialVouchersCapture(orderModel, calculatedCaptureAmount);
			return calculatedCaptureAmount;
		}
	}

	private void processFullVouchersCapture(final OrderModel orderModel)
	{
		if (CollectionUtils.isNotEmpty(orderModel.getAppliedLoyaltyVouchers()))
		{
			for (final LoyaltyVoucherModel loyaltyVoucher : orderModel.getAppliedLoyaltyVouchers())
			{
				captureLoyaltyVoucher(orderModel, loyaltyVoucher, loyaltyVoucher.getValue());
			}
		}
	}

	private void processPartialVouchersCapture(final OrderModel orderModel, final double calculatedCaptureAmount)
	{
		if (CollectionUtils.isNotEmpty(orderModel.getAppliedLoyaltyVouchers()))
		{
			double amountCaptured = 0d;
			final List<LoyaltyVoucherModel> vouchers = new ArrayList(orderModel.getAppliedLoyaltyVouchers());
			// sorting vouchers on expiry date
			Collections.sort(vouchers, new VoucherComparator());
			for (final LoyaltyVoucherModel loyaltyVoucher : vouchers)
			{
				final double amountRemaining = MathUtils.round(calculatedCaptureAmount - amountCaptured);
				if (amountRemaining > 0)
				{
					if (amountRemaining >= loyaltyVoucher.getValue().doubleValue())
					{
						amountCaptured += loyaltyVoucher.getValue().doubleValue();
						captureLoyaltyVoucher(orderModel, loyaltyVoucher, loyaltyVoucher.getValue());
					}
					else
					{
						amountCaptured += amountRemaining;
						captureLoyaltyVoucher(orderModel, loyaltyVoucher, amountRemaining);
					}
				}
				else
				{
					releaseLoyaltyVoucher(loyaltyVoucher);
				}
			}
		}
	}

	@Override
	public double captureLoyaltyVoucher(final OrderModel orderModel, final LoyaltyVoucherModel loyaltyVoucher, final double amount)
	{
		if (null != loyaltyVoucher)
		{
			loyaltyVoucher.setStatus(BenefitStatus.CAPTURED);
			addVoucherTransaction(orderModel, loyaltyVoucher, PaymentTransactionType.CAPTURE, amount);
			getModelService().save(loyaltyVoucher);
			return (loyaltyVoucher.getValue() != null) ? loyaltyVoucher.getValue().doubleValue() : 0.0d;
		}
		return 0.0d;
	}

	@Override
	public void releaseLoyaltyVoucher(final LoyaltyVoucherModel loyaltyVoucher)
	{
		if (null != loyaltyVoucher)
		{
			loyaltyVoucher.setStatus(getLoyaltyVoucherStatus(loyaltyVoucher, BenefitStatus.ACTIVE));
			getModelService().save(loyaltyVoucher);
		}
	}

	private void addVoucherTransaction(final AbstractOrderModel orderModel, final LoyaltyVoucherModel voucherModel, final PaymentTransactionType paymentTransactionEntryType, final double amount)
	{
		final List<PaymentTransactionModel> transactions = orderModel.getPaymentTransactions();
		final List<PaymentTransactionModel> newTransactions = new ArrayList<PaymentTransactionModel>();
		PaymentTransactionModel transaction = null;
		if (CollectionUtils.isNotEmpty(transactions))
		{
			transaction = getVoucherTransaction(transactions, voucherModel.getVoucherCode());
		}

		if (transaction == null)
		{
			transaction = createPaymentTransaction(voucherModel, orderModel, amount);
			newTransactions.add(transaction);
		}

		final List<PaymentTransactionEntryModel> transactionEntries = new ArrayList<PaymentTransactionEntryModel>();
		transactionEntries.addAll(transaction.getEntries());
		final PaymentTransactionEntryModel entry = createPaymentTransactionEntry(voucherModel, transaction, paymentTransactionEntryType, amount);
		transactionEntries.add(entry);
		transaction.setEntries(transactionEntries);
		getModelService().saveAll(entry, transaction);

		if (CollectionUtils.isNotEmpty(newTransactions))
		{
			newTransactions.addAll(transactions);
			orderModel.setPaymentTransactions(newTransactions);
			getModelService().save(orderModel);
		}
	}

	private PaymentTransactionModel getVoucherTransaction(final List<PaymentTransactionModel> paymantTransactions, final String voucherCode)
	{
		if (!paymantTransactions.isEmpty())
		{
			for (final PaymentTransactionModel paymantTransaction : paymantTransactions)
			{
				if (voucherCode.equals(paymantTransaction.getMerchantReferenceCode()))
				{
					return paymantTransaction;
				}
			}
		}
		return null;
	}

	private PaymentTransactionModel createPaymentTransaction(final LoyaltyVoucherModel voucherModel, final AbstractOrderModel orderModel, final double amount)
	{
		final PaymentTransactionModel paymentTransaction = getModelService().create(PaymentTransactionModel._TYPECODE);
		paymentTransaction.setCode(voucherModel.getVoucherCode());
		paymentTransaction.setMerchantReferenceCode(voucherModel.getVoucherCode());
		paymentTransaction.setOrder(orderModel);
		paymentTransaction.setOwner(orderModel.getOwner());
		paymentTransaction.setPaymentProvider(VOUCHER);
		paymentTransaction.setPlannedAmount(BigDecimal.valueOf(amount).setScale(2, BigDecimal.ROUND_HALF_EVEN));
		paymentTransaction.setCurrency(voucherModel.getCurrency());
		paymentTransaction.setEntries(Collections.<PaymentTransactionEntryModel> emptyList());

		return paymentTransaction;
	}

	private PaymentTransactionEntryModel createPaymentTransactionEntry(final LoyaltyVoucherModel voucherModel, final PaymentTransactionModel voucherTransaction, final PaymentTransactionType transactionType, final double amount)
	{
		final PaymentTransactionEntryModel paymentTransaction = getModelService().create(PaymentTransactionEntryModel._TYPECODE);
		paymentTransaction.setAmount(BigDecimal.valueOf(amount).setScale(2, BigDecimal.ROUND_HALF_EVEN));
		paymentTransaction.setCode(getPaymentService().getNewPaymentTransactionEntryCode(voucherTransaction, transactionType));
		paymentTransaction.setCurrency(voucherModel.getCurrency());
		paymentTransaction.setOwner(voucherTransaction.getOwner());
		paymentTransaction.setPaymentTransaction(voucherTransaction);
		paymentTransaction.setType(transactionType);
		paymentTransaction.setTransactionStatus(TransactionStatus.ACCEPTED.name());
		paymentTransaction.setTransactionStatusDetails(TransactionStatusDetails.SUCCESFULL.name());
		paymentTransaction.setRequestId(voucherModel.getVoucherCode());
		paymentTransaction.setTime(Calendar.getInstance().getTime());
		return paymentTransaction;
	}


	public DefaultLoyaltyService(final LoyaltyCardDao loyaltyCardDao, final ModelService modelService, final PaymentService paymentService)
	{
		this.loyaltyCardDao = loyaltyCardDao;
		this.modelService = modelService;
		this.paymentService = paymentService;
	}

	/**
	 * @return the paymentService
	 */
	protected PaymentService getPaymentService()
	{
		return paymentService;
	}

	protected LoyaltyCardDao getLoyaltyCardDao()
	{
		return loyaltyCardDao;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}
}
