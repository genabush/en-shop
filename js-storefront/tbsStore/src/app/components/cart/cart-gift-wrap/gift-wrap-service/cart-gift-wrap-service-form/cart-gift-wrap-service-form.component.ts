/*
 * Copyright (c)
 * 2020 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';

// interfaces
import { CustomCart, CustomGiftCMSBannerComponentData } from 'src/app/interfaces/custom-cart.interface';

@Component({
  selector: 'cx-gift-wrap-service-form',
  templateUrl: './cart-gift-wrap-service-form.component.html'
})
export class CartGiftWrapServiceFormComponent {
  @Input() cart: CustomCart;
  @Input() isRemoving: boolean = false;
  @Input() cmsData: CustomGiftCMSBannerComponentData;
  @Output() closeEmit: EventEmitter<undefined> = new EventEmitter<undefined>();
  @Output() submitEmit: EventEmitter<undefined> = new EventEmitter<undefined>();
  giftWrapServiceForm: FormGroup = this.getMessageFormGroup();
  constructor(private fb: FormBuilder) {}

  getMessageFormGroup() {
    return this.fb.group({});
  }

  submitGiftService(ev: Event) {
    ev.preventDefault();
    this.submitEmit.emit();
  }
}
