import { createFeatureSelector, createSelector, MemoizedSelector } from '@ngrx/store';

import { featureAdapter, State } from './state';
import { ProductVariantSelection, ProductVariant } from 'src/app/models';

export const getCollectionPointState = state => state['collection-point'];
