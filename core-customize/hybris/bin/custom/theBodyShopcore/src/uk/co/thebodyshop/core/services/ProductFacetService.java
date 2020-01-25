/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services;

import uk.co.thebodyshop.core.model.TbsProductFacetModel;

public interface ProductFacetService
{
	TbsProductFacetModel getFacetByTypeAndCode(String derivedType, String code);
}
