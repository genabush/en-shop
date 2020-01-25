import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CmsConfig, ConfigModule, I18nModule } from '@spartacus/core';
import { GenericLinkModule } from '@spartacus/storefront';
import { CustomNavigationModule } from '../custom-navigation/custom-navigation.module';
import { CustomFooterNavigationComponent } from './custom-footer-navigation.component';
import { NgbAccordionModule } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
  imports: [
    I18nModule,
    CommonModule,
    RouterModule,
    NgbAccordionModule,
    CustomNavigationModule,
    ConfigModule.withConfig(<CmsConfig>{
      cmsComponents: {
        FooterNavigationComponent: {
          component: CustomFooterNavigationComponent
        }
      }
    }),
    GenericLinkModule
  ],
  declarations: [CustomFooterNavigationComponent],
  entryComponents: [CustomFooterNavigationComponent],
  exports: [CustomFooterNavigationComponent]
})
export class CustomFooterNavigationModule {}
