package uk.co.thebodyshop.integration.adyen.model.checkout;

import java.util.Objects;

import com.adyen.model.Address;
import com.adyen.model.checkout.DefaultPaymentMethodDetails;
import com.adyen.model.checkout.PaymentMethodDetails;
import com.google.gson.annotations.SerializedName;

public class TbsPaymentMethodDetails extends DefaultPaymentMethodDetails implements PaymentMethodDetails {

    @SerializedName("bic")
    private String bic;
    @SerializedName("billingAddress")
    private Address billingAddress;
    @SerializedName("deliveryAddress")
    private Address deliveryAddress;

    /**
     * @return the bic
     */
    public String getBic() {
	return bic;
    }

    /**
     * @param bic the bic to set
     */
    public void setBic(String bic) {
	this.bic = bic;
    }
    
    public TbsPaymentMethodDetails bic(String bic) {
        this.bic = bic;
        return this;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TbsPaymentMethodDetails that = (TbsPaymentMethodDetails) o;
        return Objects.equals(getType(), that.getType())
                && Objects.equals(getNumber(), that.getNumber())
                && Objects.equals(getExpiryMonth(), that.getExpiryMonth())
                && Objects.equals(getExpiryYear(), that.getExpiryYear())
                && Objects.equals(getHolderName(),
                                  that.getHolderName())
                && Objects.equals(getCvc(), that.getCvc())
                && Objects.equals(getInstallmentConfigurationKey(), that.getInstallmentConfigurationKey())
                && Objects.equals(getPersonalDetails(),
                                  that.getPersonalDetails())
                && Objects.equals(getBillingAddress(), that.getBillingAddress())
                && Objects.equals(getDeliveryAddress(), that.getDeliveryAddress())
                && Objects.equals(getEncryptedCardNumber(), that.getEncryptedCardNumber())
                && Objects.equals(getEncryptedExpiryMonth(), that.getEncryptedExpiryMonth())
                && Objects.equals(getEncryptedExpiryYear(), that.getEncryptedExpiryYear())
                && Objects.equals(getEncryptedSecurityCode(), that.getEncryptedSecurityCode())
                && Objects.equals(getRecurringDetailReference(), that.getRecurringDetailReference())
                && Objects.equals(getStoreDetails(), that.getStoreDetails())
                && Objects.equals(getIdealIssuer(), that.getIdealIssuer())
                && Objects.equals(getSepaIbanNumber(), that.getSepaIbanNumber())
                && Objects.equals(getSepaOwnerName(), that.getSepaOwnerName())
                && Objects.deepEquals(getBic(), that.getBic());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(),
        	getNumber(),
        	getExpiryMonth(),
        	getExpiryYear(),
        	getHolderName(),
        	getCvc(),
        	getInstallmentConfigurationKey(),
        	getPersonalDetails(),
        	getBillingAddress(),
        	getDeliveryAddress(),
        	getEncryptedCardNumber(),
        	getEncryptedExpiryMonth(),
        	getEncryptedExpiryYear(),
        	getEncryptedSecurityCode(),
        	getRecurringDetailReference(),
        	getStoreDetails(),
        	getIdealIssuer(),
        	getBic());
    }

    @Override
    public String toString() {
        return "DefaultPaymentMethodDetails{"
                + "type='"
                + getType()
                + '\''
                + ", expiryMonth='"
                + getExpiryMonth()
                + '\''
                + ", expiryYear='"
                + getExpiryYear()
                + '\''
                + ", holderName='"
                + getHolderName()
                + '\''
                + ", installmentConfigurationKey='"
                + getInstallmentConfigurationKey()
                + '\''
                + ", personalDetails="
                + getPersonalDetails()
                + ", billingAddress="
                + getBillingAddress()
                + ", deliveryAddress="
                + getDeliveryAddress()
                + ", encryptedExpiryMonth='"
                + getEncryptedExpiryMonth()
                + '\''
                + ", encryptedExpiryYear='"
                + getEncryptedExpiryYear()
                + '\''
                + ", recurringDetailReference='"
                + getRecurringDetailReference()
                + '\''
                + ", storeDetails="
                + getStoreDetails()
                + ", idealIssuer="
                + getIdealIssuer()
                + ", bic="
                + getBic()
                + '}';
    }

    /**
     * @return the billingAddress
     */
    public Address getBillingAddress() {
	return billingAddress;
    }

    /**
     * @param billingAddress the billingAddress to set
     */
    public void setBillingAddress(Address billingAddress) {
	this.billingAddress = billingAddress;
    }

    /**
     * @return the deliveryAddress
     */
    public Address getDeliveryAddress() {
	return deliveryAddress;
    }

    /**
     * @param deliveryAddress the deliveryAddress to set
     */
    public void setDeliveryAddress(Address deliveryAddress) {
	this.deliveryAddress = deliveryAddress;
    }
}
