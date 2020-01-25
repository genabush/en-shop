import { Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import {
  ProductVariantSelectionStoreSelectors,
  ProductVariantSelectionStoreActions
} from 'src/app/root-store/product-variant-selection';
import { RootStoreState } from 'src/app/root-store';
import { map } from 'rxjs/operators';
import { Product } from '@spartacus/core';

@Injectable({
  providedIn: 'root'
})
export class ProductVariantsService {
  constructor(public store: Store<RootStoreState.State>) {}

  getSelectedVariant(): Observable<Product> {
    return this.store.select(ProductVariantSelectionStoreSelectors.getSelectedProductState).pipe(
      map(variant => {
        return variant.selected;
      })
    );
  }
}
