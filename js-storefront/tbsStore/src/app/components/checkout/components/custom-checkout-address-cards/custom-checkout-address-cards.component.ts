/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
import { Component, Input, EventEmitter, Output, OnChanges } from '@angular/core';
import { FormControl } from '@angular/forms';

// spartacus
import { ICON_TYPE } from '@spartacus/storefront';

// vendor
import { MatSelectChange } from '@angular/material';

// services
import { CustomUserAddressService } from 'src/app/services/checkout/custom-user-address.service';

// interfaces
import { ICustomAddress } from 'src/app/interfaces/custom-checkout.interface';

@Component({
  selector: 'cx-checkout-address-cards',
  templateUrl: './custom-checkout-address-cards.component.html'
})
export class CustomCheckoutAddressCardsComponent implements OnChanges {
  @Input() isLoadingAddresses: boolean;
  @Input() selectedAddress: string | null;
  @Input() userAddresses: ICustomAddress[];
  @Output() addressSelectEmit: EventEmitter<ICustomAddress> = new EventEmitter<ICustomAddress>();
  @Output() addressAddEmit: EventEmitter<any> = new EventEmitter<any>();
  selectFormFieldControl: FormControl = new FormControl();
  iconTypes = ICON_TYPE;
  constructor(private customUserAddressService: CustomUserAddressService) {}

  ngOnChanges() {
    // fix for mat-select binding issue
    if (this.selectFormFieldControl.value !== this.selectedAddress) {
      this.selectFormFieldControl.patchValue(this.selectedAddress);
    }
  }

  addressSelected(ev: MatSelectChange) {
    const selectedAddress = this.customUserAddressService.getAddressById(ev.value, this.userAddresses);
    this.addressSelectEmit.emit(selectedAddress);
  }

  addressAdd() {
    this.addressAddEmit.emit();
  }

  selectClick() {
    if (this.userAddresses.length === 0) {
      this.addressAddEmit.emit();
    }
  }
}
