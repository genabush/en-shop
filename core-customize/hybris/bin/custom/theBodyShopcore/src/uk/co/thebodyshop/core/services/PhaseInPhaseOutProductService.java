/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services;

import de.hybris.platform.cms2.model.site.CMSSiteModel;

import uk.co.thebodyshop.core.model.TbsVariantProductModel;

/**
 * @author Balakrishna
 */
public interface PhaseInPhaseOutProductService {

	boolean changePhaseInOutProductStatus(final TbsVariantProductModel phaseOutProduct);
	void emailStockNotificationForPhaseInOutProduct(final TbsVariantProductModel phaseOutProduct, final CMSSiteModel cmsSite);
	void replacePhaseOutWithPhaseInProductsInWishList(final TbsVariantProductModel phaseOutProduct);
}
