/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.facades.user;

import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;

import java.util.List;

/**
 * @author Balakrishna
 **/
public interface TbsUserFacade extends UserFacade
{
    /**
     * @param type
     * @return list of Address Data
     */
    List<AddressData> getAddressBookByAddressType(String type);
}
