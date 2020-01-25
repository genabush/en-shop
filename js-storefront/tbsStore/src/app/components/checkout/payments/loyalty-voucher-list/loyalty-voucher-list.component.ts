import { Component, Input } from '@angular/core';
import { LoyaltyVoucher, LoyaltyVouchers } from 'src/app/interfaces/custom-checkout.interface';
import { CustomCheckoutService } from 'src/app/services/checkout/custom-checkout.service';

@Component({
  selector: 'app-loyalty-voucher-list',
  templateUrl: './loyalty-voucher-list.component.html',
  styleUrls: ['./loyalty-voucher-list.component.scss']
})
export class LoyaltyVoucherListComponent {
  @Input() set config(data: LoyaltyVouchers) {
    this.vouchersConfig = { ...data };
  }
  @Input() currency: string;

  vouchersConfig: LoyaltyVouchers;

  constructor(private customCheckoutService: CustomCheckoutService) {}

  useVoucher(voucher: LoyaltyVoucher): void {
    if (this.vouchersConfig && this.vouchersConfig.maxVouchersReached) {
      return;
    }
    this.customCheckoutService.addVoucherToBasket(voucher.voucherId).subscribe(data => {
      this.vouchersConfig = { ...data };
      this.refreshCheckout();
    });
  }

  removeVoucher(voucher: LoyaltyVoucher): void {
    this.customCheckoutService.removeVoucherFromBasket(voucher.voucherId).subscribe(data => {
      this.vouchersConfig = { ...data };
      this.refreshCheckout();
    });
  }

  get availableVouchers(): LoyaltyVoucher[] {
    return this.vouchersConfig.loyaltyVouchers.filter(voucher => !voucher.applied);
  }

  get appliedVouchers(): LoyaltyVoucher[] {
    return this.vouchersConfig.loyaltyVouchers.filter(voucher => voucher.applied);
  }

  private refreshCheckout(): void {
    this.customCheckoutService.refreshCheckout();
  }
}
