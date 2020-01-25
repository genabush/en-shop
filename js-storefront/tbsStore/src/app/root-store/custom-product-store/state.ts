import { createEntityAdapter, EntityAdapter, EntityState } from '@ngrx/entity';

import { Product } from '@spartacus/core';

export const featureAdapter: EntityAdapter<Product> = createEntityAdapter<Product>({
  selectId: model => model.code,
  sortComparer: (a: Product, b: Product): number => b.code.toString().localeCompare(a.code)
});

export interface State extends EntityState<Product> {
  isLoading?: boolean;
  error?: any;
}

export const initialState: State = featureAdapter.getInitialState({
  isLoading: false,
  error: null
});
