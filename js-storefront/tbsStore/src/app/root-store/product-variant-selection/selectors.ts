import { createFeatureSelector, createSelector, MemoizedSelector } from '@ngrx/store';

import { featureAdapter, State } from './state';
import { ProductVariantSelection, ProductVariant } from 'src/app/models';

export const getSelectedProductState = state => state['product-variant-selection'];
