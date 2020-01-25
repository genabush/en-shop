/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.order;

import java.util.List;
import java.util.Map;

import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.stock.exception.InsufficientStockLevelException;
import de.hybris.platform.store.BaseStoreModel;

import uk.co.thebodyshop.core.collection.CollectionPointData;
import uk.co.thebodyshop.core.deliveryrestrictions.error.DeliveryRestrictionError;
import uk.co.thebodyshop.core.deliveryrestrictions.error.ProductDeliveryRestrictionError;
import uk.co.thebodyshop.core.enums.FulfillmentMethodEnum;
/**
 * @author Marcin
 */
public interface TbsCheckoutFacade extends CheckoutFacade
{
	/**
	 * Checks if there is an outstanding amount left to pay after all the secondary payments were applied
	 */
	public boolean hasOutstaningAmount();

	/**
	 * Authorises secondary payments - uses public Map<String, String> performSVSAuth(final CartModel sessionCart) to make the service call
	 *
	 * @return @Map<String, String> which contains errors and corresponding error codes
	 */
	public Map<String, String> authSecondaryPayments();

	/**
	 * Authorises secondary payments - this methods performs the call to the service
	 *
	 * @return @Map<String, String> which contains errors and corresponding error codes
	 */
	public Map<String, String> performSVSAuth(final CartModel sessionCart);

	/**
	 * Releases secondary payments - uses public public Map<String, String> performSVSReverseAuth(final CartModel sessionCart) to make the service call
	 *
	 * @return @boolean to flag if all secondary payments were successfully released
	 */
	public boolean releaseSecondaryPayments();

	/**
	 * Releases secondary payments - this methods performs the call to the service
	 *
	 * @return @Map<String, String> which contains errors and corresponding error codes
	 */
	public Map<String, String> performSVSReverseAuth(final CartModel sessionCart);

	/**
	 * Checks if order payment status should be set to Authorised
	 */
	public void verifyOrderAuthorizationStatus(final OrderData orderData);

	Map<DeliveryRestrictionError, List<? extends DeliveryModeData>> getSupportedDeliveryModeWithRestrictionMap();

	ProductDeliveryRestrictionError getProductDeliveryRestrictionError();

	void updateFulfillmentMethod(final FulfillmentMethodEnum fulfillmentMethod);

	AddressData getCollectionDeliveryAddress(FulfillmentMethodEnum fulFillMentMethod);

	void addCollectionPointInfo(CollectionPointData collectionPointData);

	public List<PointOfServiceData> getConsolidatedPickupOptions(List<PointOfServiceData> results);

	public void setStoreDetails(PointOfServiceData posData);

	public boolean resetDeliveryDetails();

	public void updateCisDeliveryMethod(BaseStoreModel baseStoreModel);

	public boolean processStock() throws InsufficientStockLevelException;

	public void releaseStock();
}