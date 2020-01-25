import { createEntityAdapter, EntityAdapter, EntityState } from '@ngrx/entity';
import { ProductVariantSelection, ProductVariant } from 'src/app/models';
import { CollectPoint, CollectPointSelection } from 'src/app/interfaces/collection-point.interface';

export const featureAdapter: EntityAdapter<CollectPoint> = createEntityAdapter<CollectPoint>();

export interface State extends EntityState<CollectPoint> {
  selected?: CollectPoint;
  searchResults?: [];
  searchQuery?: '';
}

export const initialState: State = featureAdapter.getInitialState({
  selected: {},
  searchResults: [],
  searchQuery: ''
});
