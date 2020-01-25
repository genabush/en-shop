/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.stock.processor;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.stock.exception.InsufficientStockLevelException;

/**
 * @author prateek.goel
 */
public interface StockProcessStrategy {

	void reserve(AbstractOrderModel abstractOrderModel) throws InsufficientStockLevelException;

	void release(AbstractOrderModel order) throws InsufficientStockLevelException;

	void release(ConsignmentModel consignment) throws InsufficientStockLevelException;

	void fulfill(final ConsignmentModel consignment, final boolean reset) throws InsufficientStockLevelException;

	boolean applicable(AbstractOrderModel abstractOrderModel);
}
