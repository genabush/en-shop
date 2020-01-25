import { createEntityAdapter, EntityAdapter, EntityState } from '@ngrx/entity';

export const featureAdapter: EntityAdapter<any> = createEntityAdapter<any>();

export interface State extends EntityState<any> {
  searchedResults?: [];
  searchedQuery?: '';
  searchedLocation?: '';
}

export const initialState: State = featureAdapter.getInitialState({
  searchedResults: [],
  searchedQuery: '',
  searchedLocation: ''
});
