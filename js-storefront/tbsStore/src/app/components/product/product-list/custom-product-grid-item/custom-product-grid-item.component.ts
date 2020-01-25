import { WishlistService } from './../../../../services/wishlist/wishlist.service';
import { ChangeDetectionStrategy, Component, Input, OnInit, ChangeDetectorRef } from '@angular/core';
import { ICON_TYPE, ModalService, ModalRef } from '@spartacus/storefront';
import { Store } from '@ngrx/store';
import { Observable, Subscription } from 'rxjs';
import { map } from 'rxjs/operators';
import {
  ProductVariantSelectionStoreSelectors,
  ProductVariantSelectionStoreActions
} from 'src/app/root-store/product-variant-selection';
import { RootStoreState } from 'src/app/root-store';

@Component({
  selector: 'app-product-grid-item',
  templateUrl: './custom-product-grid-item.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CustomProductGridItemComponent implements OnInit {
  selectedVariant$: Observable<any>;
  modalWishlistRef: ModalRef;
  modalVariants: ModalRef;
  modalVariantContent;
  ismultiVariant: boolean;
  selectedVariant: string;

  userWishlists$: Observable<any[]>;
  wishlistAdditionSub: Subscription;
  existInWishlistSub: Subscription;

  constructor(
    private modalService: ModalService,
    public store: Store<RootStoreState.State>,
    private wishlistService: WishlistService,
    private cd: ChangeDetectorRef
  ) {}
  @Input() product: any;
  @Input() guestFirst: boolean;

  iconTypes = ICON_TYPE;

  ngOnInit() {}

  setSelectedVariant() {
    // Subscribe to the selected variant changes and get the primary image
    this.selectedVariant$ = this.store.select(ProductVariantSelectionStoreSelectors.getSelectedProductState).pipe(
      map(variant => {
        this.selectedVariant = variant.selected.code;
        if (variant.selected.images) {
          return { container: variant.selected.images[0] };
        } else {
          return { container: null };
        }
      })
    );
  }

  openVariantsModal(content): void {
    this.setSelectedVariant();
    this.modalVariantContent = content;

    this.modalVariants = this.modalService.open(content, { ariaLabelledBy: 'variants' });
    this.modalVariants.result.then(
      () => {
        // When user closes
        this.unsetSelectedProductState();
      },
      () => {
        // Backdrop click
        this.unsetSelectedProductState();
      }
    );
  }

  unsetSelectedProductState() {
    this.selectedVariant = '';
    this.store.dispatch(
      new ProductVariantSelectionStoreActions.UnsetSelectedVariantAction({
        selectedProductVariant: {
          selected: {}
        }
      })
    );
  }

  shouldOpenVariantModal(ev?, variantModal?) {
    if (ev) {
      this.modalVariants = this.modalService.open(variantModal, { ariaLabelledBy: 'quickview' });
    } else {
      this.wishlistService.toggleQuickviewDisplay(false);
    }
  }
}
