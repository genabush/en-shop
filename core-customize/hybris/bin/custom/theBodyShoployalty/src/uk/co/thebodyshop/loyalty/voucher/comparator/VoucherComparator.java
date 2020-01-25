package uk.co.thebodyshop.loyalty.voucher.comparator;

import java.io.Serializable;
import java.util.Comparator;

import uk.co.thebodyshop.loyalty.model.LoyaltyVoucherModel;

/**
 * @author Marcin
 */
public class VoucherComparator implements Comparator<LoyaltyVoucherModel>, Serializable
{
	@Override
	public int compare(final LoyaltyVoucherModel voucher, final LoyaltyVoucherModel otherVoucher)
	{
		int result = voucher.getExpiryDate().compareTo(otherVoucher.getExpiryDate());
		if (result == 0)
		{
			result = voucher.getValue().compareTo(otherVoucher.getValue());
    }
		return result;
	}
}