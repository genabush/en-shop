import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { StoreModule } from '@ngrx/store';
import { featureReducer } from './reducer';

@NgModule({
  imports: [CommonModule, StoreModule.forFeature('wishlists', featureReducer)]
})
export class WishlistsStoreModule {}
