/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.product.visibility.strategy.factory;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Iterables;

import uk.co.thebodyshop.core.product.visibility.context.ProductVisibilityContext;
import uk.co.thebodyshop.core.product.visibility.strategy.ProductVisibilityStrategy;

/**
 * @author vasanthramprakasam
 */
public class DefaultProductVisibilityStrategyFactory implements ProductVisibilityStrategyFactory
{

	 private static final Logger LOG = LoggerFactory.getLogger(DefaultProductVisibilityStrategyFactory.class);

	 private final List<ProductVisibilityStrategy> visibilityStrategies;

	 public DefaultProductVisibilityStrategyFactory(List<ProductVisibilityStrategy> visibilityStrategies)
	 {
			this.visibilityStrategies = visibilityStrategies;
	 }

	 @Override
	 public ProductVisibilityStrategy createStrategy(ProductVisibilityContext visibilityContext)
	 {
			Optional<ProductVisibilityStrategy> visibilityStrategy = getVisibilityStrategies().stream().filter(productVisibilityStrategy -> productVisibilityStrategy.canApply(visibilityContext))
					.findFirst();
			if (visibilityStrategy.isPresent())
			{
				  if (LOG.isDebugEnabled())
					{
						 LOG.debug("Applying strategy [{}]",visibilityStrategy.get());
					}
					return visibilityStrategy.get();
			}
			LOG.info("No strategy found,applying default");
			return Iterables.getLast(visibilityStrategies);
	 }

	 protected List<ProductVisibilityStrategy> getVisibilityStrategies()
	 {
			return visibilityStrategies;
	 }
}
