import {
  ChangeDetectionStrategy,
  Component,
  Input,
  OnInit,
  ElementRef,
  ViewChild,
  Output,
  EventEmitter
} from '@angular/core';
import { Promotion, PromotionResult } from '@spartacus/core';

@Component({
  selector: 'app-promotion-free-delivery',
  templateUrl: './promotion-free-delivery.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PromotionFreeDeliveryComponent implements OnInit {
  @Input()
  promotions: PromotionResult[];
  @Input()
  minibasket: boolean;
  @Input()
  totals: boolean;
  freeDeliveryPromotion: Promotion;
  freeDeliveryPromotionDesc;
  visible = true;
  splitKeyword: string = '_SEP_';
  freeDeliveryCode: string = 'PTF-DELIVERY-MESSAGE';

  constructor() {}

  ngOnInit() {
    if (this.promotions) {
      this.freeDeliveryPromotion = this.promotions.find(
        element => element.promotion.code.indexOf(this.freeDeliveryCode) != -1
      );
      if (this.freeDeliveryPromotion) {
        this.freeDeliveryPromotionDesc = this.freeDeliveryPromotion.description.split(this.splitKeyword);
      }
    }
  }

  dismissFreeDelPromo() {
    this.visible = false;
  }
}
