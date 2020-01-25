/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.event;

import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

/**
 * @author Balakrishna
 **/
public class PhaseInOutStockEvent extends AbstractEvent
{
    private String productCode;

    private WarehouseModel warehouseModel;

    public PhaseInOutStockEvent(String productCode, WarehouseModel warehouseModel) {
        this.productCode = productCode;
        this.warehouseModel = warehouseModel;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public WarehouseModel getWarehouseModel() {
        return warehouseModel;
    }

    public void setWarehouseModel(WarehouseModel warehouseModel) {
        this.warehouseModel = warehouseModel;
    }
}

