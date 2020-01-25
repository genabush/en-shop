import { Component, Input, Output, EventEmitter } from '@angular/core';
import { LoyaltyVoucher } from 'src/app/interfaces/custom-checkout.interface';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-loyalty-voucher',
  templateUrl: './loyalty-voucher.component.html',
  styleUrls: ['./loyalty-voucher.component.scss']
})
export class LoyaltyVoucherComponent {
  @Input() voucher: LoyaltyVoucher;
  @Input() blocked: boolean;
  @Output() add = new EventEmitter<LoyaltyVoucher>();
  @Output() remove = new EventEmitter<LoyaltyVoucher>();
  termsLink = environment.termsLink;
}
