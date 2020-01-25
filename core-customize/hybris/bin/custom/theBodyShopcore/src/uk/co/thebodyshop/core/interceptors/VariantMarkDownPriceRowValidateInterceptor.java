/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.interceptors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

import uk.co.thebodyshop.core.helpers.MarkDownPriceValidationHelper;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;

/**
 * @author Jagadeesh
 */
public class VariantMarkDownPriceRowValidateInterceptor implements ValidateInterceptor<TbsVariantProductModel>
{
	private static final Logger LOG = LoggerFactory.getLogger(VariantMarkDownPriceRowValidateInterceptor.class);

	private final MarkDownPriceValidationHelper markDownPriceValidationHelper;

	public VariantMarkDownPriceRowValidateInterceptor(final MarkDownPriceValidationHelper markDownPriceValidationHelper)
	{
		this.markDownPriceValidationHelper = markDownPriceValidationHelper;
	}

	@Override
	public void onValidate(final TbsVariantProductModel tbsVariantProduct, final InterceptorContext interceptorContext) throws InterceptorException
	{
		if (CollectionUtils.isNotEmpty(tbsVariantProduct.getOwnMarkDownPrices()))
		{
			final String markDownPricesErrors = markDownPriceValidationHelper.getMarkDownPriceRowsErrors(tbsVariantProduct.getOwnMarkDownPrices());
			if (StringUtils.isNotEmpty(markDownPricesErrors))
			{
				LOG.error(markDownPricesErrors);
				throw new InterceptorException(markDownPricesErrors);
			}
		}
	}

	/**
	 * @return the markDownPriceValidationHelper
	 */
	protected MarkDownPriceValidationHelper getMarkDownPriceValidationHelper()
	{
		return markDownPriceValidationHelper;
	}
}