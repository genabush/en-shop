/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.backoffice.excel.export.wizard.renderer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import com.hybris.backoffice.attributechooser.Attribute;
import com.hybris.backoffice.attributechooser.AttributeChooserForm;
import com.hybris.backoffice.excel.export.wizard.renderer.ExcelExportRenderer;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;

import uk.co.thebodyshop.backoffice.excel.export.helpers.ExcelHelper;

/**
 * @author Marcin
 */
public class TbsExcelExportRenderer extends ExcelExportRenderer
{
	private ExcelHelper excelHelper;

	private UserService userService;

	@Override
	protected void populateAttributesChooserForm(final AttributeChooserForm attributesForm, final String typeCode)
	{
		final Set<String> supportedLanguages = getSupportedLanguages();
		final String currentLanguage = getCockpitLocaleService().getCurrentLocale().toLanguageTag();
		final Set<String> otherLanguages = new HashSet<>(supportedLanguages);
		otherLanguages.remove(currentLanguage);

		final Set<Attribute> available = new HashSet<>();
		final Set<Attribute> mandatory = new HashSet<>();

		List<String> excludedAttributes = new ArrayList<>();
		final UserModel userModel = getUserService().getCurrentUser();
		final boolean isFullExportEnabled = getExcelHelper().getFullExportFlagForCurrentUser(userModel);
		if (!isFullExportEnabled)
		{
			excludedAttributes = getExcelHelper().getExcAttributeForCurrentUser(userModel, excludedAttributes, typeCode);
		}

		for (final AttributeDescriptorModel attrDesc : getSupportedAttributes(typeCode))
		{
			if (getRequiredFilters().test(attrDesc))
			{
				if (isFullExportEnabled || getExcelHelper().isAllowedAttribute(attrDesc, excludedAttributes))
				{
					final Attribute attribute;
					if (isLocalized(attrDesc))
					{
						attribute = createAttributeWithLocalizedChildren(attrDesc, Sets.newHashSet(currentLanguage), true);
						attribute.setMandatory(false);
						available.add(createAttributeWithLocalizedChildren(attrDesc, otherLanguages, false));
					}
					else
					{
						attribute = new Attribute(createAttributeQualifier(attrDesc), createAttributeName(attrDesc), true);
					}
					mandatory.add(attribute);
				}
			}
			else
			{
				if (isFullExportEnabled || getExcelHelper().isAllowedAttribute(attrDesc, excludedAttributes))
				{
					available.add(createAttributeWithLocalizedChildren(attrDesc, supportedLanguages, false));
				}
			}
		}

		attributesForm.setAvailableAttributes(available);
		attributesForm.setChosenAttributes(mandatory);
	}

	/**
	 * @return the excelHelper
	 */
	public ExcelHelper getExcelHelper()
	{
		return excelHelper;
	}

	/**
	 * @param excelHelper
	 *          the excelHelper to set
	 */
	public void setExcelHelper(ExcelHelper excelHelper)
	{
		this.excelHelper = excelHelper;
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *          the userService to set
	 */
	public void setUserService(UserService userService)
	{
		this.userService = userService;
	}

}
