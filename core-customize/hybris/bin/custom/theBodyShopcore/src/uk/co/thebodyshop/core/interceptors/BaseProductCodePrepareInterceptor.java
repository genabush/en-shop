/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.interceptors;

import org.springframework.beans.factory.annotation.Autowired;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;

import uk.co.thebodyshop.core.catalog.services.TbsCatalogVersionService;
import uk.co.thebodyshop.core.model.TbsBaseProductModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class BaseProductCodePrepareInterceptor implements PrepareInterceptor<TbsBaseProductModel>
{
	private final PersistentKeyGenerator baseProductCodeGenerator;
	private final TbsCatalogVersionService tbsCatalogVersionService;
	private final String DEFAULT_ONLINE_DATE_VALUE= "01-01-2019 06:30:00";
	private final String ONLINE_DATE_FORMAT= "dd-MM-yyyy HH:mm:ss";

	@Override
	public void onPrepare(TbsBaseProductModel tbsBaseProductModel, InterceptorContext interceptorContext) throws InterceptorException
	{
			if (interceptorContext.getModelService().isNew(tbsBaseProductModel) && getTbsCatalogVersionService().isStagedGlobalProductCatalog(tbsBaseProductModel.getCatalogVersion()))
			{
				tbsBaseProductModel.setCode(((String) getBaseProductCodeGenerator().generate()).toLowerCase());
				SimpleDateFormat sdf = new SimpleDateFormat(ONLINE_DATE_FORMAT);
				try {
					tbsBaseProductModel.setOnlineDate(sdf.parse(DEFAULT_ONLINE_DATE_VALUE));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
	}

	@Autowired
	public BaseProductCodePrepareInterceptor(final PersistentKeyGenerator baseProductCodeGenerator, final TbsCatalogVersionService tbsCatalogVersionService)
	{
		this.baseProductCodeGenerator = baseProductCodeGenerator;
		this.tbsCatalogVersionService = tbsCatalogVersionService;
	}

	protected PersistentKeyGenerator getBaseProductCodeGenerator()
	{
		return this.baseProductCodeGenerator;
	}

	protected TbsCatalogVersionService getTbsCatalogVersionService()
	{
		return tbsCatalogVersionService;
	}
}
