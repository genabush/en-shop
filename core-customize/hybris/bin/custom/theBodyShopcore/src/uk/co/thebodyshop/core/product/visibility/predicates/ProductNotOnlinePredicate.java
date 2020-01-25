/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.product.visibility.predicates;

import java.util.Date;
import java.util.function.Predicate;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.time.TimeService;

import uk.co.thebodyshop.core.product.visibility.context.ProductVisibilityContext;

/**
 * @author vasanthramprakasam
 */
public class ProductNotOnlinePredicate implements Predicate<ProductVisibilityContext>
{
	 private final TimeService timeService;

	 public ProductNotOnlinePredicate(TimeService timeService)
	 {
			this.timeService = timeService;
	 }

	 @Override
	 public boolean test(ProductVisibilityContext visibilityContext)
	 {
			final Date today = getTimeService().getCurrentTime();
			final ProductModel productModel = visibilityContext.getProduct();
			if (null == productModel.getOnlineDate() && null == productModel.getOfflineDate())
			{
				 return false;
			}
			else if (null != productModel.getOnlineDate() && null != productModel.getOfflineDate())
			{
				 if (productModel.getOnlineDate().before(today) && productModel.getOfflineDate().after(today))
				 {
						return false;
				 }
			}
			else
			{
				 if (null != productModel.getOnlineDate())
				 {
						if (productModel.getOnlineDate().before(today))
						{
							 return false;
						}
				 }
				 if (null != productModel.getOfflineDate())
				 {
						if (productModel.getOfflineDate().after(today))
						{
							 return false;
						}
				 }
			}
			return true;
	 }

	 protected TimeService getTimeService()
	 {
			return timeService;
	 }
}
