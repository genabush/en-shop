import { ChangeDetectionStrategy, Component, OnInit, Input, OnDestroy } from '@angular/core';

// spartacus
import { Product, VariantOption } from '@spartacus/core';
import { ProductDetailOutlets } from '@spartacus/storefront';

// vendor
import { Observable, Subscription } from 'rxjs';
import { map } from 'rxjs/operators';
import { Store } from '@ngrx/store';

// custom
import { RootStoreState } from 'src/app/root-store';
import {
  ProductVariantSelectionStoreSelectors,
  ProductVariantSelectionStoreActions
} from 'src/app/root-store/product-variant-selection';

// services
import { CurrentProductService } from '../current-product.service';

// interfaces
import { ICustomProduct, ICustomVariantOption } from 'src/app/interfaces/custom-product-item.interface';
import { isUndefined } from 'lodash';

@Component({
  selector: 'cx-product-summary',
  templateUrl: './product-summary.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ProductSummaryComponent implements OnInit, OnDestroy {
  @Input() productCode: string | undefined;
  @Input() quickview: boolean = false;
  outlets = ProductDetailOutlets;

  product$: Observable<Product | ICustomProduct>;
  productSub: Subscription;
  productHasColours: boolean;
  selectedFromStore$: Observable<ICustomProduct | Product>;

  constructor(protected currentProductService: CurrentProductService, public store: Store<RootStoreState.State>) {}

  ngOnInit() {
    this.getProductDetails();
    this.setProductSubscription();
  }

  getProductDetails() {
    this.product$ = this.currentProductService.getProductDetails(this.productCode);
  }

  setProductSubscription() {
    this.productSub = this.product$.subscribe((productItem: Product | ICustomProduct) => {
      if (productItem) {
        this.getSelectedOptions(productItem);
        this.selectedFromStore$ = this.getVariantsStore();
      }
    });
  }

  getVariantsStore() {
    return this.store.select(ProductVariantSelectionStoreSelectors.getSelectedProductState).pipe(
      map(data => {
        if (data) {
          this.productHasColours = data.selected.variantType === 'COLOUR';
          return data.selected;
        }
      })
    );
  }

  getSelectedOptions(productItem: ICustomProduct) {
    if (productItem && productItem.variantOptions) {
      // First check for the the variantOptions with `selectedFlag: true`
      const filteredVariants = productItem.variantOptions.filter(select => select.selectedFlag);
      // They are all false, set the first one
      if (filteredVariants.length === 0) {
        // Update the custom `product-variant-selection` store
        this.setVariantsStore(productItem.variantOptions[0]);
        return productItem.variantOptions[0];
      } else if (filteredVariants.length >= 1) {
        this.setVariantsStore(filteredVariants[0]);
        return filteredVariants[0];
      }
      return filteredVariants;
    }
  }

  setVariantsStore(selectedVariant) {
    this.store.dispatch(
      new ProductVariantSelectionStoreActions.SetSelectedVariantAction({
        selectedProductVariant: {
          selected: selectedVariant
        }
      })
    );
  }

  changeSelected(event: Event, selection: VariantOption) {
    // Dispatch to store
    this.setVariantsStore(selection);
  }

  destroyProductSubscription() {
    if (this.productSub) {
      this.productSub.unsubscribe();
    }
  }

  ngOnDestroy() {
    this.destroyProductSubscription();
  }
}
