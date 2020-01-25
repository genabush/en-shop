import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AccountNavigationComponent } from './account-navigation/account-navigation.component';
import { ConfigModule, CmsConfig, AuthGuard } from '@spartacus/core';

@NgModule({
  imports: [
    CommonModule,
    ConfigModule.withConfig(<CmsConfig>{
      cmsComponents: {
        AccountNavigationComponent: {
          component: AccountNavigationComponent,
          guards: [AuthGuard]
        }
      }
    })
  ],
  declarations: [AccountNavigationComponent],
  entryComponents: [AccountNavigationComponent],
  exports: [AccountNavigationComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyAccountModule {}
