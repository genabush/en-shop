import { createFeatureSelector, createSelector, MemoizedSelector } from '@ngrx/store';

import { featureAdapter, State } from './state';
import { Product } from '@spartacus/core';

export const getError = (state: State): any => state.error;

export const getIsLoading = (state: State): boolean => state.isLoading;

export const selectProductState: MemoizedSelector<object, State> = createFeatureSelector<State>('custom-product');

export const selectProductError: MemoizedSelector<object, any> = createSelector(
  selectProductState,
  getError
);

export const selectJokeIsLoading: MemoizedSelector<object, boolean> = createSelector(
  selectProductState,
  getIsLoading
);
