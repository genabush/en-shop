import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { CmsConfig, ConfigModule, I18nModule, UrlModule } from '@spartacus/core';

// tslint:disable-next-line

import { CustomOrderConfirmationThankYouMessageComponent } from './components/custom-order-confirmation-thankyou-message/custom-order-confirmation-thank-you-message.component';
import { CartSharedModule, CardModule, PwaModule, OrderConfirmationGuard, MediaModule } from '@spartacus/storefront';
import { CustomOrderConfirmationItemsComponent } from './components/custom-order-confirmation-items/custom-order-confirmation-items.component';
import { CustomOrderConfirmationOverviewComponent } from './components/custom-order-confirmation-overview/custom-order-confirmation-overview.component';
import { CustomOrderConfirmationTotalsComponent } from './components/custom-order-confirmation-totals/custom-order-confirmation-totals.component';
import { RouterModule } from '@angular/router';
import { LybcMessageComponent } from '../lybc-message/lybc-message.component';

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    UrlModule,
    CartSharedModule,
    CardModule,
    PwaModule,
    I18nModule,
    MediaModule,
    ReactiveFormsModule,
    ConfigModule.withConfig(<CmsConfig>{
      cmsComponents: {
        OrderConfirmationThankMessageComponent: {
          component: CustomOrderConfirmationThankYouMessageComponent,
          guards: [OrderConfirmationGuard]
        },
        OrderConfirmationOverviewComponent: {
          component: CustomOrderConfirmationOverviewComponent,
          guards: [OrderConfirmationGuard]
        },
        OrderConfirmationItemsComponent: {
          component: CustomOrderConfirmationItemsComponent,
          guards: [OrderConfirmationGuard]
        },
        OrderConfirmationTotalsComponent: {
          component: CustomOrderConfirmationTotalsComponent,
          guards: [OrderConfirmationGuard]
        }
      }
    })
  ],
  declarations: [
    CustomOrderConfirmationThankYouMessageComponent,
    CustomOrderConfirmationOverviewComponent,
    CustomOrderConfirmationItemsComponent,
    CustomOrderConfirmationTotalsComponent,
    LybcMessageComponent
  ],
  exports: [
    CustomOrderConfirmationThankYouMessageComponent,
    CustomOrderConfirmationOverviewComponent,
    CustomOrderConfirmationItemsComponent,
    CustomOrderConfirmationTotalsComponent,
    LybcMessageComponent
  ],
  entryComponents: [
    CustomOrderConfirmationThankYouMessageComponent,
    CustomOrderConfirmationOverviewComponent,
    CustomOrderConfirmationItemsComponent,
    CustomOrderConfirmationTotalsComponent,
    LybcMessageComponent
  ]
})
export class CustomOrderConfirmationModule {}
