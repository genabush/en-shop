/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.services.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import uk.co.thebodyshop.core.daos.TbsOrderDao;
import uk.co.thebodyshop.core.services.TbsOrderService;


/**
 * @author vasanthramprakasam
 */
public class DefaultTbsOrderService implements TbsOrderService
{
	private final TbsOrderDao tbsOrderDao;

	public DefaultTbsOrderService(TbsOrderDao tbsOrderDao)
	{
		this.tbsOrderDao = tbsOrderDao;
	}

	@Override
	public OrderModel getOrderForCode(String code)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("code",code);
		return getTbsOrderDao().findOrderByCode(code);
	}

	protected TbsOrderDao getTbsOrderDao()
	{
		return tbsOrderDao;
	}
}
