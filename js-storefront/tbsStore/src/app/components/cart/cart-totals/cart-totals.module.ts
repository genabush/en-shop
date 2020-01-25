import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

// spartacus
import { UrlModule, ConfigModule, CmsConfig, I18nModule } from '@spartacus/core';

// components
import { CartTotalsComponent } from './cart-totals.component';
import { CartSharedModule } from '../../cart-shared/cart-shared.module';

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    UrlModule,
    ConfigModule.withConfig(<CmsConfig>{
      cmsComponents: {
        CartTotalsComponent: {
          component: CartTotalsComponent
        }
      }
    }),
    CartSharedModule,
    I18nModule
  ],
  declarations: [CartTotalsComponent],
  exports: [CartTotalsComponent],
  entryComponents: [CartTotalsComponent]
})
export class CartTotalsModule {}
