/**
 *
 */
package uk.co.thebodyshop.core.strategies.impl;

import java.util.Collection;

import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.commerceservices.stock.strategies.impl.DefaultCommerceAvailabilityCalculationStrategy;
import de.hybris.platform.ordersplitting.model.StockLevelModel;

/**
 * @author Marcin
 *
 */
public class TbsStockAvailabilityCalculationStrategy extends DefaultCommerceAvailabilityCalculationStrategy
{
	@Override
	public Long calculateAvailability(final Collection<StockLevelModel> stockLevels)
	{
		long totalActualAmount = 0;
		for (final StockLevelModel stockLevel : stockLevels)
		{
			// If any stock level is flagged as FORCEINSTOCK then return null to indicate in stock
			if (InStockStatus.FORCEINSTOCK.equals(stockLevel.getInStockStatus()))
			{
				return Long.valueOf(0);
			}

			// If any stock level is flagged as FORCEOUTOFSTOCK then we skip over it
			if (!InStockStatus.FORCEOUTOFSTOCK.equals(stockLevel.getInStockStatus()))
			{
				final long availableToSellQuantity = getAvailableToSellQuantity(stockLevel);
				if (availableToSellQuantity > 0 || !stockLevel.isTreatNegativeAsZero())
				{
					totalActualAmount += availableToSellQuantity;
				}
			}
		}
		return Long.valueOf(totalActualAmount);
	}

	@Override
	protected long getAvailableToSellQuantity(final StockLevelModel stockLevel)
	{
		return stockLevel.getAvailable() - stockLevel.getReserved() - stockLevel.getFulfilled() + (long) stockLevel.getOverSelling() + stockLevel.getExtendedStock();
	}

}
