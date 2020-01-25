import { AdyenCreditCards } from './../../interfaces/adyen-credit-cards.interface';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AdyenCreditCardsService {
  private images: AdyenCreditCards = {
    names: ['visa', 'mc', 'amex', 'maestro', 'diners', 'discover'],
    src: 'https://checkoutshopper-live.adyen.com/checkoutshopper/images/logos/small/',
    format: '.png'
  };
  constructor() {}
  getImages() {
    return this.images;
  }
}
