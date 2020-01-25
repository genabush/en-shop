/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.payment.request;

import de.hybris.platform.payment.commands.request.VoidRequest;
import de.hybris.platform.store.BaseStoreModel;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * @author Balakrishna
 **/
public class TbsVoidRequest extends VoidRequest {
    private BaseStoreModel baseStore;

    public TbsVoidRequest(String merchantTransactionCode, String requestId, String requestToken, String paymentProvider,BaseStoreModel baseStore) {
        super(merchantTransactionCode, requestId, requestToken, paymentProvider);
        this.baseStore=baseStore;
    }

    public TbsVoidRequest(String merchantTransactionCode, String requestId, String requestToken, String paymentProvider, Currency currency, BigDecimal totalAmount, BaseStoreModel baseStore) {
        super(merchantTransactionCode, requestId, requestToken, paymentProvider, currency, totalAmount);
        this.baseStore = baseStore;
    }

    public BaseStoreModel getBaseStore() {
        return baseStore;
    }

    public void setBaseStore(BaseStoreModel baseStore) {
        this.baseStore = baseStore;
    }
}
