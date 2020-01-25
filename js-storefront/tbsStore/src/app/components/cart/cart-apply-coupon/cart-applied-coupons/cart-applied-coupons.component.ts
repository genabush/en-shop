import { Component, Input, ChangeDetectionStrategy, Output, EventEmitter, ViewChild, ElementRef } from '@angular/core';

// spartacus
import { Voucher } from '@spartacus/core';
import { ICON_TYPE, ModalService } from '@spartacus/storefront';

// services
import { CustomCartVoucherService } from 'src/app/services/custom-cart-voucher/custom-cart-voucher.service';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { FormGroup, FormBuilder } from '@angular/forms';

@Component({
  selector: 'cx-applied-coupons',
  templateUrl: './cart-applied-coupons.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CartAppliedCouponsComponent {
  @ViewChild('removeCouponModal', { static: false }) removeCouponModal: ElementRef;
  @Input() vouchers: Voucher[];
  @Input() cartIsLoading = false;
  @Input() isReadOnly = false;
  @Output() removeVoucherEmit: EventEmitter<undefined> = new EventEmitter<undefined>();
  removeModalForm: FormGroup = this.fb.group({});
  removeModalRef: NgbModalRef;
  iconTypes = ICON_TYPE;
  removingVoucherCode: string;
  constructor(
    private cartVoucherService: CustomCartVoucherService,
    private modalService: ModalService,
    private fb: FormBuilder
  ) {}

  public get sortedVouchers(): Voucher[] {
    this.vouchers = this.vouchers || [];
    return this.vouchers.slice().sort((a, b) => {
      return a.code.localeCompare(b.code);
    });
  }

  removeModalSubmit(ev: Event) {
    ev.preventDefault();
    this.removeVoucher();
  }

  removeVoucher() {
    this.cartVoucherService.customApiRemoveVoucher(this.removingVoucherCode).subscribe(() => {
      this.unsetRemovingCode();
      this.closeRemovalModal();
      this.removeVoucherEmit.emit();
    });
  }

  openRemovalModal(voucherCode: string) {
    this.removingVoucherCode = voucherCode;
    this.removeModalRef = this.modalService.open(this.removeCouponModal);
  }

  closeRemovalModal() {
    if (this.removeModalRef) {
      this.removeModalRef.close();
    }
  }

  private unsetRemovingCode() {
    this.removingVoucherCode = undefined;
  }

  ngOnDestroy() {
    this.closeRemovalModal();
  }
}
