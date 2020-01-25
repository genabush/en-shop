/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.paypal.command;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hybris.platform.payment.commands.VoidCommand;
import de.hybris.platform.payment.commands.request.VoidRequest;
import de.hybris.platform.payment.commands.result.VoidResult;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.dto.TransactionStatusDetails;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.store.BaseStoreModel;

import uk.co.thebodyshop.integration.paypal.service.PaypalPaymentService;
import uk.co.thebodyshop.payment.request.TbsVoidRequest;

/**
 * @author Balakrishna
 **/
public class PayPalVoidCommand implements VoidCommand {
    private static final Logger LOG = LoggerFactory.getLogger(PayPalVoidCommand.class);

    private static final Integer PAYPAL_VOID_SUCCESS_CODE=204;

    private PaypalPaymentService paypalPaymentService;

    @Override
    public VoidResult perform(final VoidRequest voidRequest)
    {

        ServicesUtil.validateParameterNotNull(voidRequest, "void  request must not be null");

        final String authorizationId = voidRequest.getRequestId();
        LOG.info("void command for authorization id : [{}]", authorizationId);
        ServicesUtil.validateParameterNotNull(authorizationId, "authorizationId must not be null");

        final BaseStoreModel baseStore=voidRequest instanceof TbsVoidRequest?((TbsVoidRequest) voidRequest).getBaseStore():null;
        ServicesUtil.validateParameterNotNull(baseStore, "baseStore must not be null");

        final VoidResult result = populateVoidResult(voidRequest);

        try {
            final Optional<Integer> responseCode = getPaypalPaymentService().voidAuthorize(authorizationId,baseStore);

            if (responseCode.isPresent() && PAYPAL_VOID_SUCCESS_CODE.equals(responseCode.get())) {
                result.setTransactionStatus(TransactionStatus.ACCEPTED);
                result.setTransactionStatusDetails(TransactionStatusDetails.SUCCESFULL);
            } else {
                result.setTransactionStatus(TransactionStatus.ERROR);
                result.setTransactionStatusDetails(TransactionStatusDetails.UNKNOWN_CODE);
            }
        } catch (final Exception e) {
            LOG.error("Exception while doing authorization with id  [{}]", authorizationId, e);
            result.setTransactionStatus(TransactionStatus.ERROR);
        }
        LOG.info("Void status: " + result.getTransactionStatus().name() +":" +result.getTransactionStatusDetails().name());
        return result;
    }

    private VoidResult populateVoidResult(final VoidRequest request)
    {
        final VoidResult result = new VoidResult();
        result.setMerchantTransactionCode(request.getMerchantTransactionCode());
        result.setRequestId(request.getRequestId());
        result.setRequestToken(request.getRequestToken());
        return result;
    }

    public PaypalPaymentService getPaypalPaymentService()
    {
        return paypalPaymentService;
    }

    public void setPaypalPaymentService(final PaypalPaymentService paypalPaymentService)
    {
        this.paypalPaymentService = paypalPaymentService;
    }
}
