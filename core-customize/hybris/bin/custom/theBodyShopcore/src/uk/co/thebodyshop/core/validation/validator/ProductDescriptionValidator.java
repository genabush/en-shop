/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.validation.validator;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.microsoft.sqlserver.jdbc.StringUtils;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.user.UserService;

import uk.co.thebodyshop.core.validation.annotation.ProductDescription;

public class ProductDescriptionValidator implements ConstraintValidator<ProductDescription, ProductModel>
{
	private static final String USER_GROUP_LOCALE_MAPPING = "userGroupLocaleMapping";

	@Override
	public boolean isValid(final ProductModel productModel, final ConstraintValidatorContext constraintValidatorContext)
	{
		final Map<String, List<String>> userGroupLocaleMap = Registry.getApplicationContext().getBean(USER_GROUP_LOCALE_MAPPING, java.util.HashMap.class);
		final UserService userService = Registry.getApplicationContext().getBean("userService", UserService.class);
		final UserModel userModel = userService.getCurrentUser();
		if (userService.isAdminEmployee(userModel))
		{
			return true;
		}

		final Set<Locale> mandatoryLocales = getMandatoryLocales(userService.getAllUserGroupsForUser(userModel), userGroupLocaleMap);
		for (final Locale loc : mandatoryLocales)
		{
			if (StringUtils.isEmpty(productModel.getDescription(loc)))
			{
				return false;
			}
		}
		return true;
	}

	private Set<Locale> getMandatoryLocales(final Set<UserGroupModel> userGroups, final Map<String, List<String>> userGroupLocaleMap)
	{
		final CommonI18NService commonI18NService = Registry.getApplicationContext().getBean("commonI18NService", CommonI18NService.class);

		final Set<Locale> mandatoryLocales = new HashSet<>();
		for (final UserGroupModel userGroup : userGroups)
		{
			final List<String> groupLocales = userGroupLocaleMap.get(userGroup.getUid());
			if (null != groupLocales)
			{
				for (final String locale : groupLocales)
				{
					mandatoryLocales.add(commonI18NService.getLocaleForIsoCode(locale));
				}
			}
		}
		return mandatoryLocales;
	}
}