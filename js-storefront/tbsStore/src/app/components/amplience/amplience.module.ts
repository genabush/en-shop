import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ConfigModule } from '@spartacus/core';

import { AmplienceComponent } from './amplience.component';

@NgModule({
  imports: [
    CommonModule,
    ConfigModule.withConfig({
      cmsComponents: {
        AmplienceCMSComponent: {
          component: AmplienceComponent
        }
      }
    })
  ],
  declarations: [AmplienceComponent],
  entryComponents: [AmplienceComponent],
  exports: [AmplienceComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AmplienceModule {}
