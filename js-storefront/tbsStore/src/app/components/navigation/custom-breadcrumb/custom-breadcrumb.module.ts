import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ConfigModule, CmsConfig, CmsPageTitleModule } from '@spartacus/core';

import { CustomBreadcrumbComponent } from './custom-breadcrumb.component';

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    ConfigModule.withConfig(<CmsConfig>{
      cmsComponents: {
        BreadcrumbComponent: {
          component: CustomBreadcrumbComponent
        }
      }
    }),
    CmsPageTitleModule
  ],
  declarations: [CustomBreadcrumbComponent],
  exports: [CustomBreadcrumbComponent],
  entryComponents: [CustomBreadcrumbComponent]
})
export class CustomBreadcrumbModule {}
