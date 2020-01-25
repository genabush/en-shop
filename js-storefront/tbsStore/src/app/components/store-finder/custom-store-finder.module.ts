import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ConfigModule, I18nModule, UrlModule, CmsConfig } from '@spartacus/core';

import { CustomStoreFinderComponent } from './components/custom-store-finder/custom-store-finder.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material';
import { CollectionPointItemModule } from '../checkout/fulfillment/collection-point-item/collection-point-item.module';
import { AgmCoreModule } from '@agm/core';
import { SpinnerModule } from '@spartacus/storefront';
import { RouterModule } from '@angular/router';
import { CustomStoreFinderMainComponent } from './components/custom-store-finder-main/custom-store-finder-main.component';
import { StoreDetailsComponent } from './components/store-details/store-details.component';
import { environment } from 'src/environments/environment';
import { NoticeBannerComponent } from './components/notice-banner/notice-banner.component';
import { StoreBannerComponent } from './components/store-banner/store-banner.component';
import { OpeningHoursComponent } from './components/opening-hours/opening-hours.component';

@NgModule({
  imports: [
    SpinnerModule,
    CommonModule,
    RouterModule,
    I18nModule,
    UrlModule,
    MatInputModule,
    ReactiveFormsModule,
    CollectionPointItemModule,
    AgmCoreModule,
    ConfigModule.withConfig(<CmsConfig>{
      cmsComponents: {
        TbsStoreFinderComponent: {
          component: CustomStoreFinderMainComponent,
          childRoutes: [
            {
              path: '',
              component: CustomStoreFinderComponent
            },
            {
              path: 'store/:store',
              component: StoreDetailsComponent
            }
          ]
        }
      }
    })
  ],
  declarations: [
    CustomStoreFinderComponent,
    CustomStoreFinderMainComponent,
    StoreDetailsComponent,
    NoticeBannerComponent,
    StoreBannerComponent,
    OpeningHoursComponent
  ],
  entryComponents: [CustomStoreFinderMainComponent, CustomStoreFinderComponent, StoreDetailsComponent],
  exports: [CustomStoreFinderComponent]
})
export class CustomStoreFinderModule {}
