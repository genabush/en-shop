/**
 *
 */
package uk.co.thebodyshop.core.strategies.impl;

import java.util.Collection;

import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commerceservices.stock.strategies.impl.CommerceStockLevelStatusStrategy;
import de.hybris.platform.ordersplitting.model.StockLevelModel;


/**
 * @author Marcin
 *
 */
public class TbsStockLevelStatusStrategy extends CommerceStockLevelStatusStrategy
{
	@Override
	public StockLevelStatus checkStatus(final Collection<StockLevelModel> stockLevels)
	{
		StockLevelStatus resultStatus = StockLevelStatus.OUTOFSTOCK;
		StockLevelStatus tmpStatus = StockLevelStatus.OUTOFSTOCK;

		for (final StockLevelModel level : stockLevels)
		{
			tmpStatus = checkStatus(level);
			if (StockLevelStatus.INSTOCK.equals(tmpStatus))
			{
				resultStatus = tmpStatus;
				break;
			}
			else if (StockLevelStatus.LOWSTOCK.equals(tmpStatus))
			{
				resultStatus = tmpStatus;
			}
		}

		return resultStatus;
	}
}
