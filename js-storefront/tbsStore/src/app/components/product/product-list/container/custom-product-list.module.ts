import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CustomProductListComponent } from './custom-product-list.component';
import { ConfigModule, UrlModule } from '@spartacus/core';
import { CustomResultsCountComponent } from '../custom-results-count/custom-results-count.component';
import { CustomProductGridItemComponent } from '../custom-product-grid-item/custom-product-grid-item.component';
import { AmplienceModule } from 'src/app/components/amplience/amplience.module';
import { I18nModule } from '@spartacus/core';
import {
  ListNavigationModule,
  GenericLinkModule,
  MediaModule,
  StarRatingModule,
  IconModule,
  OutletModule
} from '@spartacus/storefront';
import { SliderModule } from 'src/app/components/slider/slider.module';
import { AddToCartModule } from 'src/app/components/cart/add-to-cart/add-to-cart.module';
import { ProductSummaryModule } from '../../product-summary/product-summary.module';
import { ProductSummaryComponent } from '../../product-summary/product-summary.component';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    CommonModule,
    AmplienceModule,
    RouterModule,
    I18nModule,
    SliderModule,
    UrlModule,
    MediaModule,
    ListNavigationModule,
    ProductSummaryModule,
    StarRatingModule,
    AddToCartModule,
    GenericLinkModule,
    ProductSummaryModule,
    IconModule,
    ConfigModule.withConfig({
      cmsComponents: {
        CMSProductListComponent: {
          component: CustomProductListComponent
        },
        SearchResultsListComponent: {
          component: CustomProductListComponent
        },
        CMSProductGridComponent: {
          component: CustomProductGridItemComponent
        },
        ProductSummaryComponent: {
          component: ProductSummaryComponent
        }
      }
    })
  ],
  declarations: [CustomProductListComponent, CustomResultsCountComponent, CustomProductGridItemComponent],
  exports: [CustomProductListComponent, CustomProductGridItemComponent],
  entryComponents: [CustomProductListComponent, CustomProductGridItemComponent]
})
export class CustomProductListModule {}
