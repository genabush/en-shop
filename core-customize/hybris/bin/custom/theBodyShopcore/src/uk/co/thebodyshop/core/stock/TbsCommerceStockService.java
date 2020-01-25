/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.stock;

import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.stock.exception.InsufficientStockLevelException;
import de.hybris.platform.store.BaseStoreModel;

/**
 * @author prateek.goel
 */
public interface TbsCommerceStockService extends CommerceStockService
{
	boolean isExtendedStockAvailable(AbstractOrderEntryModel entry, WarehouseModel warehouse);

	void processStock(AbstractOrderModel order) throws InsufficientStockLevelException;

	void releaseStock(AbstractOrderModel order) throws InsufficientStockLevelException;

	void releaseStock(final ConsignmentModel consignment) throws InsufficientStockLevelException;

	void fulfillStock(ConsignmentModel consignment, boolean reset) throws InsufficientStockLevelException;

	Long getProductStockLevelForStore(ProductModel product, Long stockLevel, final BaseStoreModel store);

}
