/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.populators;

import java.util.Optional;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import uk.co.thebodyshop.integration.jaxb.order.AddressType;

/**
 * @author vasanthramprakasam
 */
public class PlaceOrderAddressPopulator implements Populator<AddressModel, AddressType>
{
	 @Override
	 public void populate(AddressModel addressModel, AddressType addressType) throws ConversionException
	 {
			addressType.setAddressID(addressModel.getPk().getLongValueAsString());
			addressType.setAddressLine1(addressModel.getLine1());
			addressType.setAddressLine2(addressModel.getLine2());
			addressType.setFirstName(addressModel.getFirstname());
			addressType.setLastName(addressModel.getLastname());
			addressType.setCity(addressModel.getTown());
			addressType.setCountry(Optional.ofNullable(addressModel.getCountry()).map(CountryModel::getIsocode).orElse(null));
			addressType.setPostCode(addressModel.getPostalcode());
			addressType.setRegion(Optional.ofNullable(addressModel.getRegion()).map(RegionModel::getIsocodeShort).orElse(null));
	 }
}
