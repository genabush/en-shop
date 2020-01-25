/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ConfigModule } from '@spartacus/core';

import { GigyaRaasComponent } from './gigya-raas.component';
import { EffectsModule } from '@ngrx/effects';
import { GigyaRaasEffects } from './store/effect/gigya-raas-effects';

@NgModule({
  imports: [
    CommonModule,
    ConfigModule.withConfig({
      cmsComponents: {
        GigyaRaasComponent: {
          component: GigyaRaasComponent
        }
      }
    }),
    EffectsModule.forRoot([GigyaRaasEffects])
  ],
  declarations: [GigyaRaasComponent],
  entryComponents: [GigyaRaasComponent],
  exports: [GigyaRaasComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GigyaRaasModule {}
