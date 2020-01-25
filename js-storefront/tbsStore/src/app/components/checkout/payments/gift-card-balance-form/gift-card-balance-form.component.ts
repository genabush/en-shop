/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { numbersOnlyValidator } from '../../../../validators/numbers-only-validator';
import { CustomCheckoutService } from '../../../../services/checkout/custom-checkout.service';
import { ReplaySubject } from 'rxjs';
import { AddGiftCardResponse } from '../../../../interfaces/payment-methods-response.interface';

@Component({
  selector: 'app-gift-card-balance-form',
  templateUrl: './gift-card-balance-form.component.html'
})
export class GiftCardBalanceFormComponent implements OnInit {
  formGroup: FormGroup;
  giftCardBalanceResponse$ = new ReplaySubject<AddGiftCardResponse>(1);
  showValidationError = false;
  invalidGiftCard = false;

  constructor(private fb: FormBuilder, private customCheckoutService: CustomCheckoutService) {}

  ngOnInit(): void {
    this.initForm();
  }

  checkGiftCardBalance(): void {
    this.showValidationError = false;
    if (this.formGroup.valid) {
      this.customCheckoutService.checkGiftCartBalance(this.formGroup.value).subscribe((resp: AddGiftCardResponse) => {
        this.invalidGiftCard = !resp.success;
        this.giftCardBalanceResponse$.next(resp);
      });
      return;
    }
    this.showValidationError = true;
  }

  checkAnotherGiftCard(): void {
    this.formGroup.reset();
    this.invalidGiftCard = false;
    this.giftCardBalanceResponse$.next({ success: false } as AddGiftCardResponse);
  }

  private initForm(): void {
    this.formGroup = this.fb.group({
      giftCardNumber: [
        '',
        Validators.compose([
          Validators.required,
          numbersOnlyValidator(),
          Validators.maxLength(19),
          Validators.minLength(19)
        ])
      ],
      giftCardPin: [
        '',
        Validators.compose([
          Validators.required,
          numbersOnlyValidator(),
          Validators.maxLength(4),
          Validators.minLength(4)
        ])
      ]
    });
  }
}
