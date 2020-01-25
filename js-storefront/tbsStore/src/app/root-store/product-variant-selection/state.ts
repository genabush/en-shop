import { createEntityAdapter, EntityAdapter, EntityState } from '@ngrx/entity';
import { ProductVariantSelection, ProductVariant } from 'src/app/models';

export const featureAdapter: EntityAdapter<ProductVariantSelection> = createEntityAdapter<ProductVariantSelection>();

export interface State extends EntityState<ProductVariantSelection> {
  selected?: ProductVariant;
}

export const initialState: State = featureAdapter.getInitialState({
  selected: {
    code: null,
    colour: '',
    name: '',
    priceData: {
      currencyIso: '',
      formattedValue: '',
      pricePerMetric: '',
      priceType: '',
      value: ''
    },
    size: '',
    stock: {
      stockLevel: '',
      stockLevelStatus: ''
    },
    url: '',
    variantType: ''
  }
});
