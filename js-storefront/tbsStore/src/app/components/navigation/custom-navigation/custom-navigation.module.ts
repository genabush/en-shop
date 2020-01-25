import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CmsConfig, ConfigModule, I18nModule } from '@spartacus/core';
import { IconModule, GenericLinkModule } from '@spartacus/storefront';
import { AccordionUIComponent } from './accordion-navigation-ui.component';
import { CustomNavigationUIComponent } from './custom-navigation-ui.component';
import { CustomNavigationComponent } from './custom-navigation.component';
import { AmplienceModule } from '../../amplience/amplience.module';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/compiler/src/core';
import { NgbAccordionModule } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    IconModule,
    GenericLinkModule,
    AmplienceModule,
    NgbAccordionModule,
    ConfigModule.withConfig(<CmsConfig>{
      cmsComponents: {
        NavigationComponent: {
          component: CustomNavigationComponent
        }
      }
    }),
    I18nModule
  ],
  declarations: [CustomNavigationComponent, CustomNavigationUIComponent, AccordionUIComponent],
  entryComponents: [CustomNavigationComponent],
  exports: [CustomNavigationComponent, CustomNavigationUIComponent, AccordionUIComponent]
})
export class CustomNavigationModule {}
