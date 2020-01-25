import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { TbsCartNormalizer } from './converters/tbs-cart-normalizer';
import { CART_NORMALIZER } from '@spartacus/core';

@NgModule({
  imports: [CommonModule, HttpClientModule],
  providers: [
    {
      provide: CART_NORMALIZER,
      useClass: TbsCartNormalizer,
      multi: true
    }
  ]
})
export class CartTbsModule {}
