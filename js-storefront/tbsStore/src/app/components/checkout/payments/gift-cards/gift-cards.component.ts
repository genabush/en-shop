import { Component, Input } from '@angular/core';
import { GiftCard } from '../../../../interfaces/custom-cart.interface';

@Component({
  selector: 'app-gift-cards',
  templateUrl: './gift-cards.component.html',
  styleUrls: ['./gift-cards.component.scss']
})
export class GiftCardsComponent {
  @Input() set config(val: GiftCard[]) {
    if (!!val) {
      this.giftCardFormElement = val;
      return;
    }
    this.giftCardFormElement = [null];
  }
  @Input() showAddButton: boolean;

  giftCardFormElement: GiftCard[] = [null];

  addGiftCardForm(): void {
    this.giftCardFormElement.push(null);
  }

  canAddNewGiftCard(): boolean {
    return this.showAddButton && !this.giftCardFormElement.includes(null);
  }
}
