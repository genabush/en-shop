import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
// import { ProductStoreEffects } from './effects';
import { featureReducer } from './reducer';

@NgModule({
  imports: [
    CommonModule,
    StoreModule.forFeature('product-variant-selection', featureReducer)
    // EffectsModule.forFeature([ProductStoreEffects])
  ]
  // providers: [ProductStoreEffects]
})
export class ProductVariantSelectionStoreModule {}
