import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CmsConfig, ConfigModule } from '@spartacus/core';

import { MediaModule, OutletModule } from '@spartacus/storefront';
import { ProductImagesComponent } from './product-images.component';
import { CarouselModule } from '../../carousel';

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    MediaModule,
    OutletModule,
    ConfigModule.withConfig(<CmsConfig>{
      cmsComponents: {
        ProductImagesComponent: {
          component: ProductImagesComponent
        }
      }
    }),
    CarouselModule
  ],
  declarations: [ProductImagesComponent],
  entryComponents: [ProductImagesComponent],
  exports: [ProductImagesComponent]
})
export class ProductImagesModule {}
