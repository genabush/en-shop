import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { StoreModule } from '@ngrx/store';
import { featureReducer } from './reducer';

@NgModule({
  imports: [CommonModule, StoreModule.forFeature('cart-promo-voucher', featureReducer)]
})
export class CartPromoVoucherStoreModule {}
