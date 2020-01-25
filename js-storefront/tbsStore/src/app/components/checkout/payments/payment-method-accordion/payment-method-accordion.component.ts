/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
// vendor
import { ReplaySubject } from 'rxjs';
import { NgbTabChangeEvent, NgbTabset } from '@ng-bootstrap/ng-bootstrap';
// interfaces
import { PaymentMethodsResponse, SavedPaymentsItem } from '../../../../interfaces/payment-methods-response.interface';
import { CustomCart } from '../../../../interfaces/custom-cart.interface';
import { StoreConfigService } from '../../../../services/config/store-config.service';

@Component({
  selector: 'cx-payment-method-accordion',
  templateUrl: './payment-method-accordion.component.html'
})
export class PaymentMethodAccordionComponent implements OnInit {
  @Input() paymentMethodConfig: PaymentMethodsResponse | null;
  @Input() hasSavedPaymentCards: boolean;
  @Input() savedPaymentCards: SavedPaymentsItem[];
  @Input() savedCardSelectedCode: any;
  @Input() savedPaymentData: SavedPaymentsItem[];
  @Input() cart$: ReplaySubject<CustomCart>;
  @Output() tabChangedEvent: EventEmitter<NgbTabChangeEvent> = new EventEmitter<NgbTabChangeEvent>();
  @Output() setSavedCardEvent: EventEmitter<any> = new EventEmitter<any>();
  @Output() setPaymentDetailEvent: EventEmitter<any> = new EventEmitter<any>();
  @Output() savedPaymentDataEmit: EventEmitter<any> = new EventEmitter<any>();

  savedCardForm: FormGroup = this.getSavedCardFormGroup();
  constructor(private fb: FormBuilder, private paymentConfigService: StoreConfigService) {}

  ngOnInit() {}

  savedCardSelected(event: Event, tabSet: NgbTabset): void {
    if ((event.target as HTMLInputElement).value === 'addNew') {
      tabSet.select('ngb-tab-1');
    } else {
      this.savedCardForm.patchValue({
        savedCard: (event.target as HTMLInputElement).value
      });
      this.savedCardSelectedCode = this.savedCardForm.get(['savedCard']).value;

      const savedPaymentData = this.savedPaymentCards.filter(
        card => (card.adyenSelectedReference = this.savedCardSelectedCode)
      );

      this.savedPaymentDataEmit.emit(savedPaymentData);
    }
  }

  isPaymentModeVisible(paymentMode: string): boolean {
    return this.paymentConfigService.isPaymentVisible(paymentMode);
  }

  getSavedCardFormGroup() {
    return this.fb.group({
      savedCard: [''],
      cvc: ['']
    });
  }
}
