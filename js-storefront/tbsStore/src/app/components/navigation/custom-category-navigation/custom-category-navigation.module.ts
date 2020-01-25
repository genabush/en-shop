import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { CmsConfig, ConfigModule, I18nModule } from '@spartacus/core';
import { CustomNavigationModule } from '../custom-navigation/custom-navigation.module';
import { CustomCategoryNavigationComponent } from './custom-category-navigation.component';

@NgModule({
  imports: [
    CommonModule,
    CustomNavigationModule,
    I18nModule,
    ConfigModule.withConfig(<CmsConfig>{
      cmsComponents: {
        CustomCategoryNavigationComponent: {
          component: CustomCategoryNavigationComponent
        }
      }
    })
  ],
  declarations: [CustomCategoryNavigationComponent],
  entryComponents: [CustomCategoryNavigationComponent],
  exports: [CustomCategoryNavigationComponent]
})
export class CustomCategoryNavigationModule {}
