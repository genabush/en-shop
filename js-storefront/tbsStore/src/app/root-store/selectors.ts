import { createSelector, MemoizedSelector } from '@ngrx/store';
import { ProductStoreSelectors } from './custom-product-store';

export const selectError: MemoizedSelector<object, string> = createSelector(
  ProductStoreSelectors.selectProductError,
  (productError: string) => {
    return productError;
  }
);

export const selectIsLoading: MemoizedSelector<object, boolean> = createSelector(
  ProductStoreSelectors.selectJokeIsLoading,
  (product: boolean) => {
    return product;
  }
);
