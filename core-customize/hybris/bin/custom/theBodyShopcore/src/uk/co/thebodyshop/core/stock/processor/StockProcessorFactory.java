/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.stock.processor;

import java.util.List;

import org.apache.log4j.Logger;

import de.hybris.platform.core.model.order.AbstractOrderModel;

/**
 * @author prateek.goel
 */
public class StockProcessorFactory {

	private static final Logger LOG = Logger.getLogger(StockProcessorFactory.class);

	private List<StockProcessStrategy> stockProcessStrategyList;

	private StockProcessStrategy emptyStrategy;

	public StockProcessStrategy getStockProcessStrategy(final AbstractOrderModel orderModel){
		for (final StockProcessStrategy stockProcessStrategy : getStockProcessStrategyList())
		{
			if (stockProcessStrategy.applicable(orderModel)){
				return stockProcessStrategy;
			}
		}
		LOG.error("No strategy configured for the order");
		return getEmptyStrategy();
	}

	protected List<StockProcessStrategy> getStockProcessStrategyList()
	{
		return stockProcessStrategyList;
	}

	public void setStockProcessStrategyList(final List<StockProcessStrategy> stockProcessStrategyList)
	{
		this.stockProcessStrategyList = stockProcessStrategyList;
	}

	protected StockProcessStrategy getEmptyStrategy()
	{
		return emptyStrategy;
	}

	public void setEmptyStrategy(final StockProcessStrategy emptyStrategy)
	{
		this.emptyStrategy = emptyStrategy;
	}

}
