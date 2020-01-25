import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CmsConfig, ConfigModule, I18nModule } from '@spartacus/core';
import { CustomLinkComponent } from './custom-link.component';
import { GenericLinkModule, IconModule } from '@spartacus/storefront';
import { HtmlSanitizePipe } from 'src/app/pipes/html-sanitizer/html-sanitize.pipe';

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    GenericLinkModule,
    IconModule,
    I18nModule,

    ConfigModule.withConfig(<CmsConfig>{
      cmsComponents: {
        CMSLinkComponent: { component: CustomLinkComponent }
      }
    })
  ],
  declarations: [CustomLinkComponent, HtmlSanitizePipe],
  exports: [CustomLinkComponent],
  entryComponents: [CustomLinkComponent]
})
export class CustomLinkModule {}
