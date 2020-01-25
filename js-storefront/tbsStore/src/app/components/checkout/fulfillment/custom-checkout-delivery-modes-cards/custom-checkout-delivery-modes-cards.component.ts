import { Component, OnInit, Input, Output, EventEmitter, ChangeDetectionStrategy, OnChanges } from '@angular/core';
import { ICON_TYPE } from '@spartacus/storefront';
import { DeliveryMode } from '@spartacus/core';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'cx-checkout-delivery-modes-cards',
  templateUrl: './custom-checkout-delivery-modes-cards.component.html'
})
export class CustomCheckoutDeliveryModesCardsComponent implements OnChanges {
  iconTypes = ICON_TYPE;
  @Input() isLoading: boolean;
  @Input() selectedDeliveryMode: number | null;
  @Output() deliveryModesChooseEmit: EventEmitter<any> = new EventEmitter<any>();
  @Input() supportedDeliveryModes: DeliveryMode[];
  deliveryModesFormControl = new FormControl({ disabled: true });
  constructor() {}
  ngOnChanges() {
    if (this.deliveryModesFormControl.value !== this.selectedDeliveryMode) {
      this.deliveryModesFormControl.patchValue(this.selectedDeliveryMode);
    }
  }
  deliveryModeChoose() {
    if (this.supportedDeliveryModes.length !== 1) {
      this.deliveryModesChooseEmit.emit();
    }
  }
}
