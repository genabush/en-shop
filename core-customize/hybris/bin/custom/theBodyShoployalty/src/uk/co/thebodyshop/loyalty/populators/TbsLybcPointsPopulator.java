/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.loyalty.populators;

import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.services.BaseStoreService;
import uk.co.thebodyshop.core.loyalty.points.calculation.strategies.LoyaltyPointsCalculationStrategy;
import uk.co.thebodyshop.loyalty.service.LoyaltyService;

/**
 * @author Hemchand
 */
public class TbsLybcPointsPopulator implements Populator<OrderModel, AbstractOrderData>
{

    private final LoyaltyPointsCalculationStrategy loyaltyPointsCalculationStrategy;
    private final BaseStoreService baseStoreService;
    private final UserService userService;
    private final LoyaltyService loyaltyService;

    @Override
    public void populate(final OrderModel source, final AbstractOrderData target) throws ConversionException
    {
        CustomerModel customerModel = (CustomerModel) getUserService().getCurrentUser();
        if (getLoyaltyService().isCustomerLybc(customerModel))
        {
            target.setLoyaltyPoints(getLoyaltyPointsCalculationStrategy().calculate(source, getBaseStoreService().getCurrentBaseStore()));
        }

    }

    public TbsLybcPointsPopulator(final LoyaltyPointsCalculationStrategy loyaltyPointsCalculationStrategy, final BaseStoreService baseStoreService, final UserService userService, final LoyaltyService loyaltyService)
    {
        this.loyaltyPointsCalculationStrategy = loyaltyPointsCalculationStrategy;
        this.baseStoreService = baseStoreService;
        this.userService = userService;
        this.loyaltyService = loyaltyService;
    }

    protected LoyaltyPointsCalculationStrategy getLoyaltyPointsCalculationStrategy()
    {
        return loyaltyPointsCalculationStrategy;
    }

    protected BaseStoreService getBaseStoreService()
    {
        return baseStoreService;
    }

    protected UserService getUserService()
    {
        return userService;
    }

    protected LoyaltyService getLoyaltyService()
    {
        return loyaltyService;
    }
}
