/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
import { Component, OnInit, Input, ChangeDetectionStrategy } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

// vendor
import { ReplaySubject } from 'rxjs';

// services
import { CustomCheckoutService } from '../../../../services/checkout/custom-checkout.service';

// validators
import { numbersOnlyValidator } from '../../../../validators/numbers-only-validator';

// interfaces
import { AddGiftCardResponse } from '../../../../interfaces/payment-methods-response.interface';
import { GiftCard } from '../../../../interfaces/custom-cart.interface';

@Component({
  selector: 'app-gift-card-form',
  templateUrl: './gift-card-form.component.html',
  styleUrls: ['./gift-card-form.component.scss'],
  changeDetection: ChangeDetectionStrategy.Default
})
export class GiftCardFormComponent implements OnInit {
  @Input() set cardInfo(val: GiftCard) {
    if (!!val) {
      this.addedGiftCardResponse$.next({
        success: true,
        giftCardNumber: val.giftCardNumber,
        giftCardBalance: val.initialBalance.value,
        giftCardAppliedAmount: val.appliedAmount.formattedValue
      });
    }
  }
  formGroup: FormGroup;
  addedGiftCardResponse$ = new ReplaySubject<AddGiftCardResponse>(1);
  showValidationError = false;

  constructor(private fb: FormBuilder, private customCheckoutService: CustomCheckoutService) {}

  ngOnInit(): void {
    this.initForm();
  }

  addGiftCard(): void {
    this.showValidationError = false;
    if (this.formGroup.valid) {
      this.customCheckoutService.addGiftCart(this.formGroup.value).subscribe((resp: AddGiftCardResponse) => {
        this.addedGiftCardResponse$.next(resp);
        if (resp.success) {
          this.refreshCheckout();
        }
      });
      return;
    }
    this.showValidationError = true;
  }

  removeGiftCard(giftCardNumber: string): void {
    this.customCheckoutService.removeGiftCart({ giftCardNumber }).subscribe(resp => {
      this.addedGiftCardResponse$.next({ success: false } as AddGiftCardResponse);
      this.refreshCheckout();
    });
  }

  private refreshCheckout(): void {
    this.customCheckoutService.refreshCheckout();
  }

  private initForm(): void {
    this.formGroup = this.fb.group({
      giftCardNumber: [
        null,
        Validators.compose([Validators.required, numbersOnlyValidator(), Validators.minLength(19)])
      ],
      giftCardPin: [null, Validators.compose([Validators.required, numbersOnlyValidator(), Validators.minLength(4)])]
    });
  }
}
