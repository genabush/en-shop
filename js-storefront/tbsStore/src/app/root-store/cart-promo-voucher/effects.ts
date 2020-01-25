import { Injectable } from '@angular/core';
import { Actions } from '@ngrx/effects';
import { CurrentProductService } from 'src/app/components/product/current-product.service';
import { CustomCartService } from 'src/app/services/cart/facade/cart.service';

@Injectable()
export class CartGiftWrapEffects {
  constructor(private cartService: CustomCartService, private actions$: Actions) {}
}
