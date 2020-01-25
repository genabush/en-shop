import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { CmsConfig, ConfigModule, I18nModule } from '@spartacus/core';
import { PageComponentModule, OutletModule } from '@spartacus/storefront';

import { MatExpansionModule } from '@angular/material/expansion';
import { CustomTabParagraphContainerComponent } from './custom-tab-paragraph-container.component';

@NgModule({
  imports: [
    MatExpansionModule,
    CommonModule,
    ConfigModule.withConfig(<CmsConfig>{
      cmsComponents: {
        CMSTabParagraphContainer: {
          component: CustomTabParagraphContainerComponent
        }
      }
    }),
    PageComponentModule,
    OutletModule,
    I18nModule
  ],
  declarations: [CustomTabParagraphContainerComponent],
  entryComponents: [CustomTabParagraphContainerComponent],
  exports: [CustomTabParagraphContainerComponent]
})
export class CustomTabParagraphContainerModule {}
