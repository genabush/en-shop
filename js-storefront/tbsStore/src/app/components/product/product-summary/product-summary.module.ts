import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { CmsConfig, ConfigModule, I18nModule } from '@spartacus/core';
import { OutletModule } from '@spartacus/storefront';
import { ProductSummaryComponent } from './product-summary.component';
import { ProductVariantsSizeComponent } from '../product-variants-size/product-variants-size.component';
import { SortProductBySizePipe } from '../../../pipes/sort-product-by-size/sort-product-by-size.pipe';

@NgModule({
  imports: [
    CommonModule,
    I18nModule,
    ConfigModule.withConfig(<CmsConfig>{
      cmsComponents: {
        ProductSummaryComponent: {
          component: ProductSummaryComponent
        },
        ProductVariantsSizeComponent: {
          component: ProductVariantsSizeComponent
        }
      }
    })
  ],
  providers: [SortProductBySizePipe],
  declarations: [ProductSummaryComponent, ProductVariantsSizeComponent],
  entryComponents: [ProductSummaryComponent],
  exports: [ProductSummaryComponent]
})
export class ProductSummaryModule {}
