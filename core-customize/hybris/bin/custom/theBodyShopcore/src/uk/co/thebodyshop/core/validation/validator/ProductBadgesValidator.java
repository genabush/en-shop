/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.validation.validator;

import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import uk.co.thebodyshop.core.constants.TheBodyShopCoreConstants;
import uk.co.thebodyshop.core.model.ProductBadgeModel;
import uk.co.thebodyshop.core.validation.annotation.ProductBadges;

public class ProductBadgesValidator implements ConstraintValidator<ProductBadges, Set<ProductBadgeModel>> {

	@Override
	public boolean isValid(final Set<ProductBadgeModel> productBadgeModels, final ConstraintValidatorContext constraintValidatorContext) {
		return productBadgeModels == null || doesNotExceedMaximumCount(productBadgeModels);
	}

	private boolean doesNotExceedMaximumCount(final Set<ProductBadgeModel> productBadgeModels) {
		final ConfigurationService configurationService = Registry.getApplicationContext().getBean("configurationService", ConfigurationService.class);
		final int productBadgesMaxCount = configurationService.getConfiguration().getInteger(TheBodyShopCoreConstants.PRODUCT_BADGES_COUNT_MAX, TheBodyShopCoreConstants.PRODUCT_BADGES_COUNT_MAX_DEFAULT);
		return productBadgeModels != null && productBadgeModels.size() <= productBadgesMaxCount;
	}
}
