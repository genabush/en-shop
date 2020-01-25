import { CommonModule } from '@angular/common';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

// spartacus
import { CmsConfig, ConfigModule, I18nModule, UrlModule } from '@spartacus/core';

// components
import { CartEmptyParagraphComponent } from './cart-empty-paragraph.component';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  imports: [
    SharedModule,
    CommonModule,
    RouterModule,
    UrlModule,
    I18nModule,
    ConfigModule.withConfig(<CmsConfig>{
      cmsComponents: {
        EmptyCartPageComponent: {
          component: CartEmptyParagraphComponent
        }
      }
    }),
    I18nModule
  ],
  declarations: [CartEmptyParagraphComponent],
  exports: [CartEmptyParagraphComponent],
  entryComponents: [CartEmptyParagraphComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CartEmptyParagraphModule {}
