/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package uk.co.thebodyshop.integration.adyen.service;

import com.adyen.v6.service.AdyenBusinessProcessService;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;

/**
 * @author Marcin
 */
public interface TbsAdyenBusinessProcessService extends AdyenBusinessProcessService
{
	/**
	 * Trigger consignment-process event
	 */
	void triggerConsignmentProcessEvent(final ConsignmentModel consignmentModel, final String event);

}
