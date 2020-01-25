/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.validation.validator;

import de.hybris.platform.core.Registry;
import org.apache.commons.collections.CollectionUtils;
import uk.co.thebodyshop.core.catalog.services.TbsCatalogVersionService;
import uk.co.thebodyshop.core.model.TbsBaseProductModel;
import uk.co.thebodyshop.core.validation.annotation.KeyIngredients;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Lakshmi
 **/
public class KeyIngredientsValidator implements ConstraintValidator<KeyIngredients, TbsBaseProductModel> {
	@Override
	public boolean isValid(final TbsBaseProductModel productModel, final ConstraintValidatorContext constraintValidatorContext) {
		boolean keyIngrediantsIsNull=Boolean.TRUE;
		final TbsCatalogVersionService tbsCatalogVersionService= Registry.getApplicationContext().getBean(TbsCatalogVersionService.class);
		if (tbsCatalogVersionService.isStagedGlobalProductCatalog(productModel.getCatalogVersion()) && CollectionUtils.isEmpty(productModel.getKeyIngredients()))
		{
			keyIngrediantsIsNull=Boolean.FALSE;
		}
		return keyIngrediantsIsNull;
	}
}
