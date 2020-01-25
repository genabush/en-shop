/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.order.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.log4j.Logger;

import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.impl.DefaultCheckoutFacade;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.enums.PaymentStatus;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.PaymentModeService;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.stock.exception.InsufficientStockLevelException;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.storelocator.pos.PointOfServiceService;

import uk.co.thebodyshop.core.calculation.strategies.OutstandingAmountCalculationStrategy;
import uk.co.thebodyshop.core.collection.CollectionPointData;
import uk.co.thebodyshop.core.constants.TheBodyShopCoreConstants;
import uk.co.thebodyshop.core.delivery.TbsDeliveryService;
import uk.co.thebodyshop.core.deliveryrestrictions.error.DeliveryRestrictionError;
import uk.co.thebodyshop.core.deliveryrestrictions.error.ProductDeliveryRestrictionError;
import uk.co.thebodyshop.core.deliveryrestrictions.manager.RestrictionsManager;
import uk.co.thebodyshop.core.enums.FulfillmentMethodEnum;
import uk.co.thebodyshop.core.model.CollectionPointModel;
import uk.co.thebodyshop.core.model.StoreDetailModel;
import uk.co.thebodyshop.core.order.TbsCheckoutFacade;
import uk.co.thebodyshop.core.services.order.TbsAcceleratorCheckoutService;
import uk.co.thebodyshop.core.stock.TbsCommerceStockService;
import uk.co.thebodyshop.integration.svs.facades.GiftCardFacade;
import uk.co.thebodyshop.integration.svs.model.GiftCardModel;
import uk.co.thebodyshop.integration.svs.services.SvsService;

/**
 * @author Marcin
 */
public class DefaultTbsCheckoutFacade extends DefaultCheckoutFacade implements TbsCheckoutFacade
{
	private static final String AUTHORIZATION_PENDING = "AUTHORIZATION_PENDING";

	protected static final Logger LOG = Logger.getLogger(DefaultTbsCheckoutFacade.class);

	private static final String ACCEPTED = "ACCEPTED";

	private GiftCardFacade giftCardFacade;

	private PaymentModeService paymentModeService;

	private OutstandingAmountCalculationStrategy outstandingAmountCalculationStrategy;

	private TbsDeliveryService tbsDeliveryService;

	private RestrictionsManager restrictionsManager;

	private Converter<CollectionPointData, CollectionPointModel> collectionPointReverseConverter;

	private TbsAcceleratorCheckoutService tbsAcceleratorCheckoutService;

	private PointOfServiceService pointOfServiceService;

	private Converter<PointOfServiceData, StoreDetailModel> storeDetailReverseConverter;

	private TbsCommerceStockService tbsCommerceStockService;

	public boolean hasOutstaningAmount()
	{
		final CartModel cartModel = getCart();
		if (Objects.nonNull(cartModel))
		{
			final BigDecimal outstandingAmount = getOutstandingAmountCalculationStrategy().getOutstandingAmount(cartModel);
			if (outstandingAmount.compareTo(BigDecimal.ZERO) == 0)
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public Map<String, String> authSecondaryPayments()
	{
		final CartModel cartModel = getCart();
		final Map<String, String> errors = new HashMap<>();
		if (PaymentStatus.PARTAUTHORIZED.equals(cartModel.getPaymentStatus()))
		{
			if (null == cartModel.getPaymentInfo())
			{
				giftCardFacade.createGiftcardPaymentInfo(cartModel);
			}
			// we have already attempted to auth secondary payments, therefore no need to do anything
			return errors;
		}

		// authorise gift cards if those are present in the cart
		if (giftCardFacade.cartHasGiftCard(cartModel))
		{
			errors.putAll(performSVSAuth(cartModel));
			if (MapUtils.isNotEmpty(errors))
			{
				// release vouchers/loyalty
				LOG.error("Error when authorizing secondary payments on cart [" + cartModel.getCode() + "], user [" + cartModel.getUser().getUid() + "]. Error map: " + errors);
				return errors;
			}
			else
			{
				giftCardFacade.createGiftcardPaymentInfo(cartModel);
			}
		}
		cartModel.setPaymentStatus(PaymentStatus.PARTAUTHORIZED);
		getModelService().save(cartModel);
		return errors;
	}

	@Override
	public Map<String, String> performSVSAuth(final CartModel cartModel)
	{
		final Map<String, String> errors = new HashMap<>();

		if (CollectionUtils.isNotEmpty(cartModel.getGiftCards()))
		{
			for (final GiftCardModel giftCard : cartModel.getGiftCards())
			{
				if (giftCard.getAmountAppliedForOrder() == null || giftCard.getAmountAppliedForOrder() == 0.0)
				{
					continue;
				}

				final String reasonCode = giftCardFacade.authorise(giftCard.getGiftCardNumber(), giftCard.getPinNumber(), cartModel, giftCard.getAmountAppliedForOrder(), cartModel.getCurrency().getIsocode());
				if (SvsService.APPROVED.equalsIgnoreCase(reasonCode))
				{
					if (cartModel.getPaymentMode() == null || !cartModel.getPaymentMode().getCode().equals(TheBodyShopCoreConstants.PaymentMode.Code.GiftCard)) {
						cartModel.setPaymentMode(getPaymentModeService().getPaymentModeForCode(TheBodyShopCoreConstants.PaymentMode.Code.GiftCard));
						cartModel.setPaymentStatus(PaymentStatus.AUTHORIZED);
						cartModel.setCalculated(true);
						getModelService().save(cartModel);
					}
				}
				else
				{
					errors.put(giftCard.getGiftCardNumber(), reasonCode);
				}
			}
		}
		return errors;
	}

	@Override
	public boolean releaseSecondaryPayments()
	{
		final CartModel cartModel = getCart();
		// reset payment status on cart
		if (!PaymentStatus.REJECTED.equals(cartModel.getPaymentStatus()))
		{
			cartModel.setPaymentStatus(null);
		}
		getModelService().save(cartModel);

		// Release all vouchers

		// undo gift card auth
		if (giftCardFacade.cartHasGiftCard(cartModel))
		{
			final Map<String, String> errors = performSVSReverseAuth(cartModel);
			if (MapUtils.isNotEmpty(errors))
			{
				LOG.error("Error when authorizing gift cart [" + cartModel.getCode() + "], user [" + cartModel.getUser().getUid() + "]. Error map: " + errors);
				return false;
			}
		}
		return true;
	}

	@Override
	public Map<String, String> performSVSReverseAuth(final CartModel cartModel)
	{
		final Map<String, String> errors = new HashMap<>();

		if (CollectionUtils.isNotEmpty(cartModel.getGiftCards()))
		{
			for (final GiftCardModel giftCard : cartModel.getGiftCards())
			{
				if (giftCard.getAmountAppliedForOrder() == null || giftCard.getAmountAppliedForOrder() == 0.0)
				{
					continue;
				}

				if (!giftCardFacade.reverseAuthorise(giftCard, cartModel))
				{
					errors.put(giftCard.getGiftCardNumber(), "Unable to reverse auth");
				}
			}
		}
		cartModel.setPaymentMode(null);
		cartModel.setPaymentStatus(null);
		getModelService().save(cartModel);
		return errors;
	}

	@Override
	public void verifyOrderAuthorizationStatus(final OrderData orderData)
	{
		if (Objects.nonNull(orderData))
		{
			final OrderModel orderModel = getCustomerAccountService().getOrderForCode(getCurrentUserForCheckout(), orderData.getCode(), getBaseStoreService().getCurrentBaseStore());
			boolean isOrderAuthorised = true;
			boolean isAuthPending = false;
			if (CollectionUtils.isNotEmpty(orderModel.getPaymentTransactions()))
			{
				for (final PaymentTransactionModel paymentTransaction : orderModel.getPaymentTransactions())
				{
					if (CollectionUtils.isNotEmpty(paymentTransaction.getEntries()))
					{
						boolean transactionAuthorised = false;
						boolean transactionAuthorisationPending = false;
						for (final PaymentTransactionEntryModel paymentEntry : paymentTransaction.getEntries())
						{
							if (PaymentTransactionType.AUTHORIZATION.equals(paymentEntry.getType()))
							{
								if (ACCEPTED.equals(paymentEntry.getTransactionStatus()))
								{
									transactionAuthorised = true;
								}
								if (AUTHORIZATION_PENDING.equals(paymentEntry.getTransactionStatus()))
								{
									transactionAuthorisationPending = true;
								}
							}
						}
						if (!transactionAuthorised)
						{
							isOrderAuthorised = false;
							if (isAuthPending || transactionAuthorisationPending)
							{
								isAuthPending = true;
							}
						}
					}
				}
			}
			setOrderAuthStatus(orderModel, isOrderAuthorised, isAuthPending);
		}
	}

	/**
	 * @param orderModel
	 * @param isOrderAuthorised
	 * @param isAuthPending
	 */
	private void setOrderAuthStatus(final OrderModel orderModel, final boolean isOrderAuthorised, final boolean isAuthPending)
	{
		if (isOrderAuthorised)
		{
			orderModel.setPaymentStatus(PaymentStatus.AUTHORIZED);
		}
		else
		{
			if (isAuthPending)
			{
				orderModel.setPaymentStatus(PaymentStatus.AUTHORIZATION_PENDING);
			}
			else
			{
				orderModel.setPaymentStatus(PaymentStatus.PARTAUTHORIZED);
			}
		}
		getModelService().save(orderModel);
	}

	@Override
	public Map<DeliveryRestrictionError, List<? extends DeliveryModeData>> getSupportedDeliveryModeWithRestrictionMap()
	{
		final Map<DeliveryRestrictionError, List<? extends DeliveryModeData>> supportedDeliveryModeWithRestrictionMap = new HashMap<>();
		final List<DeliveryModeData> result = new ArrayList<>();
		final CartModel cartModel = getCart();
		if (cartModel != null)
		{
			final Map<DeliveryRestrictionError, List<DeliveryModeModel>> deliveryModesMap = getTbsDeliveryService().getDeliveryModesWithDeliveryRestrictionError(cartModel);
			final DeliveryRestrictionError restrictionError = deliveryModesMap.keySet().iterator().next();
			for (final DeliveryModeModel deliveryModeModel : deliveryModesMap.get(restrictionError))
			{
				result.add(convert(deliveryModeModel));
			}
			supportedDeliveryModeWithRestrictionMap.put(restrictionError, result);
		}
		return supportedDeliveryModeWithRestrictionMap;
	}

	@Override
	public ProductDeliveryRestrictionError getProductDeliveryRestrictionError()
	{
		final CartModel cart = getCart();
		ProductDeliveryRestrictionError productDeliveryRestrictionError = new ProductDeliveryRestrictionError();
		if (null != cart)
		{
			productDeliveryRestrictionError = getRestrictionsManager().handleProductDeliveryModeRestrictions(cart.getDeliveryAddress(), cart);
		}
		return productDeliveryRestrictionError;
	}

	@Override
	public void updateFulfillmentMethod(final FulfillmentMethodEnum fulfillmentMethod)
	{
		final CartModel cart = getCart();
		if (null != cart && fulfillmentMethod != cart.getFulfillmentMethod())
		{
			cart.setFulfillmentMethod(fulfillmentMethod);
			if (fulfillmentMethod == FulfillmentMethodEnum.DIRECT)
			{
				cart.setCollectionPoint(null);
				cart.setDeliveryPointOfService(null);
				cart.setExtendedStoreDtl(null);
				removeNonDeliveryAddress();
			}
			getModelService().save(cart);
		}
	}

	protected void removeNonDeliveryAddress()
	{
		final UserModel user = getUserService().getCurrentUser();
		if (user instanceof CustomerModel)
		{
			final List<AddressModel> userAddresses = getCustomerAccountService().getAllAddressEntries((CustomerModel) user);
			if (CollectionUtils.isNotEmpty(userAddresses))
			{
				final List<AddressModel> nonDeliveryAddresses = userAddresses.stream().filter((address) -> BooleanUtils.isFalse(address.getShippingAddress())).collect(Collectors.toList());
				if (CollectionUtils.isNotEmpty(nonDeliveryAddresses))
				{
					nonDeliveryAddresses.forEach((address) -> {
						getCustomerAccountService().deleteAddressEntry((CustomerModel) user, address);
					});
				}
			}
		}
	}

	@Override
	public AddressData getCollectionDeliveryAddress(final FulfillmentMethodEnum fulFillMentMethod)
	{
		final CartModel cart = getCart();
		if (null != cart && null != cart.getFulfillmentMethod() && cart.getFulfillmentMethod() == fulFillMentMethod && null != cart.getDeliveryAddress())
		{
			return getAddressConverter().convert(cart.getDeliveryAddress());
		}
		return null;
	}

	@Override
	public void addCollectionPointInfo(final CollectionPointData collectionPointData)
	{
		final CartModel cart = getCart();
		CollectionPointModel collectionPoint = cart.getCollectionPoint();
		if (null == collectionPoint)
		{
			collectionPoint = getModelService().create(CollectionPointModel.class);
		}
		collectionPoint = getCollectionPointReverseConverter().convert(collectionPointData, collectionPoint);
		cart.setDeliveryPointOfService(null);
		getModelService().save(collectionPoint);
		cart.setCollectionPoint(collectionPoint);
		cart.setDeliveryPointOfService(null);
		cart.setExtendedStoreDtl(null);
		getModelService().save(cart);
	}

	@Override
	public List<PointOfServiceData> getConsolidatedPickupOptions(final List<PointOfServiceData> results)
	{
		if (CollectionUtils.isNotEmpty(results))
		{
			final CartModel cart = getCart();
			final List<PointOfServiceModel> posList = getPosList(results);
			return getTbsAcceleratorCheckoutService().getConsolidatedPickupOptions(cart, posList);
		}
		return null;
	}

	private List<PointOfServiceModel> getPosList(final List<PointOfServiceData> results)
	{
		final List<PointOfServiceModel> posList = new ArrayList<>();
		for (final PointOfServiceData pointOfServiceData : results)
		{
			if (null != pointOfServiceData && pointOfServiceData.isAvailable())
			{
				final PointOfServiceModel pos = getPointOfServiceService().getPointOfServiceForName(pointOfServiceData.getName());
				posList.add(pos);
			}
		}
		return posList;
	}

	@Override
	public void setStoreDetails(final PointOfServiceData posData)
	{
		if (null != posData && StringUtils.isNotBlank(posData.getName()))
		{
			final CartModel cart = getCart();

			StoreDetailModel storeDetail = cart.getExtendedStoreDtl();
			if (null == storeDetail)
			{
				storeDetail = getModelService().create(StoreDetailModel.class);
			}
			storeDetail = getStoreDetailReverseConverter().convert(posData, storeDetail);
			getModelService().save(storeDetail);

			final PointOfServiceModel pos = getPointOfServiceService().getPointOfServiceForName(posData.getName());
			cart.setDeliveryPointOfService(pos);
			cart.setExtendedStoreDtl(storeDetail);
			cart.setCollectionPoint(null);
			getModelService().save(cart);
		}
	}

	@Override
	public boolean resetDeliveryDetails()
	{
		final CartModel cart = getCart();
		if (null != cart)
		{
			cart.setCollectionPoint(null);
			cart.setDeliveryPointOfService(null);
			cart.setExtendedStoreDtl(null);
			cart.setDeliveryAddress(null);
			cart.setDeliveryMode(null);
			cart.setFulfillmentMethod(FulfillmentMethodEnum.DIRECT);
			this.getModelService().save(cart);
			return true;
		}
		return false;

	}

	@Override
	public void updateCisDeliveryMethod(final BaseStoreModel baseStore)
	{
		final CartModel cart = getCart();
		if (null != cart && null != baseStore)
		{
			cart.setDeliveryMode(baseStore.getCisDeliveryMode());
			this.getModelService().save(cart);
		}

	}

	@Override
	protected void beforePlaceOrder(@SuppressWarnings("unused") final CartModel cartModel) // NOSONAR
	{
		if (getTbsAcceleratorCheckoutService().isCollectInStoreOrder(cartModel))
		{
			getTbsAcceleratorCheckoutService().updateCisOrders(cartModel.getDeliveryPointOfService(), true, false);
		}
	}

	@Override
	public boolean processStock() throws InsufficientStockLevelException
	{
		final CartModel cart = getCart();
		if (null == cart)
		{
			throw new InsufficientStockLevelException("Cart is not found so stock can not be processed");
		}
		getTbsCommerceStockService().processStock(cart);
		return true;
	}

	@Override
	public void releaseStock()
	{
		final CartModel cart = getCart();
		if (null != cart)
		{
			try
			{
				getTbsCommerceStockService().releaseStock(cart);
			}
			catch (final InsufficientStockLevelException e)
			{
				LOG.error("Getting error in stock release");
			}
		}
	}

	/**
	 * @return the giftCardFacade
	 */
	public GiftCardFacade getGiftCardFacade()
	{
		return giftCardFacade;
	}

	/**
	 * @param giftCardFacade
	 *          the giftCardFacade to set
	 */
	public void setGiftCardFacade(final GiftCardFacade giftCardFacade)
	{
		this.giftCardFacade = giftCardFacade;
	}

	/**
	 * @return the paymentModeService
	 */
	public PaymentModeService getPaymentModeService()
	{
		return paymentModeService;
	}

	/**
	 * @param paymentModeService
	 *          the paymentModeService to set
	 */
	public void setPaymentModeService(final PaymentModeService paymentModeService)
	{
		this.paymentModeService = paymentModeService;
	}

	/**
	 * @return the outstandingAmountCalculationStrategy
	 */
	public OutstandingAmountCalculationStrategy getOutstandingAmountCalculationStrategy()
	{
		return outstandingAmountCalculationStrategy;
	}

	/**
	 * @param outstandingAmountCalculationStrategy
	 *          the outstandingAmountCalculationStrategy to set
	 */
	public void setOutstandingAmountCalculationStrategy(final OutstandingAmountCalculationStrategy outstandingAmountCalculationStrategy)
	{
		this.outstandingAmountCalculationStrategy = outstandingAmountCalculationStrategy;
	}

	protected TbsDeliveryService getTbsDeliveryService()
	{
		return tbsDeliveryService;
	}

	public void setTbsDeliveryService(final TbsDeliveryService tbsDeliveryService)
	{
		this.tbsDeliveryService = tbsDeliveryService;
	}

	protected RestrictionsManager getRestrictionsManager()
	{
		return restrictionsManager;
	}

	public void setRestrictionsManager(final RestrictionsManager restrictionsManager)
	{
		this.restrictionsManager = restrictionsManager;
	}

	protected Converter<CollectionPointData, CollectionPointModel> getCollectionPointReverseConverter()
	{
		return collectionPointReverseConverter;
	}

	public void setCollectionPointReverseConverter(final Converter<CollectionPointData, CollectionPointModel> collectionPointReverseConverter)
	{
		this.collectionPointReverseConverter = collectionPointReverseConverter;
	}

	protected TbsAcceleratorCheckoutService getTbsAcceleratorCheckoutService()
	{
		return tbsAcceleratorCheckoutService;
	}

	public void setTbsAcceleratorCheckoutService(final TbsAcceleratorCheckoutService tbsAcceleratorCheckoutService)
	{
		this.tbsAcceleratorCheckoutService = tbsAcceleratorCheckoutService;
	}

	protected PointOfServiceService getPointOfServiceService()
	{
		return pointOfServiceService;
	}

	public void setPointOfServiceService(final PointOfServiceService pointOfServiceService)
	{
		this.pointOfServiceService = pointOfServiceService;
	}

	protected Converter<PointOfServiceData, StoreDetailModel> getStoreDetailReverseConverter()
	{
		return storeDetailReverseConverter;
	}

	public void setStoreDetailReverseConverter(final Converter<PointOfServiceData, StoreDetailModel> storeDetailReverseConverter)
	{
		this.storeDetailReverseConverter = storeDetailReverseConverter;
	}

	protected TbsCommerceStockService getTbsCommerceStockService()
	{
		return tbsCommerceStockService;
	}

	public void setTbsCommerceStockService(final TbsCommerceStockService tbsCommerceStockService)
	{
		this.tbsCommerceStockService = tbsCommerceStockService;
	}

}
