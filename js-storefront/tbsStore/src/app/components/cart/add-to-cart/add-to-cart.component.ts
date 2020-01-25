import { WishlistService } from '../../../services/wishlist/wishlist.service';
import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  Input,
  OnInit,
  Output,
  EventEmitter,
  OnDestroy,
  ViewChild,
  ElementRef
} from '@angular/core';
import { OrderEntry, TranslationService } from '@spartacus/core';
import { CustomCartService } from '../../../services/cart/facade/cart.service';
import { Observable, Subscription, of } from 'rxjs';
import { ModalRef, ModalService, ICON_TYPE } from '@spartacus/storefront';
import { CurrentProductService } from '../../product/current-product.service';
import { AddedToCartDialogComponent } from './added-to-cart-dialog/added-to-cart-dialog.component';
import { Store } from '@ngrx/store';
import { map } from 'rxjs/operators';
import { RootStoreState } from 'src/app/root-store';
import { ProductVariantsService } from 'src/app/services/product-variants/product-variants.service';
import { ICustomVariantOption, ICustomProduct } from 'src/app/interfaces/custom-product-item.interface';
import { ModalDismissReasons, NgbTooltipConfig, NgbTooltip, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { isUndefined } from 'lodash';
import { ProductVariantSelectionStoreSelectors } from 'src/app/root-store/product-variant-selection';
@Component({
  selector: 'cx-add-to-cart',
  templateUrl: './add-to-cart.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AddToCartComponent implements OnInit, OnDestroy {
  @Input() productCode: string;
  @Input() showQuantity = true;
  @Input() productVariantsData: any;
  @Input() hasStock: any;
  @Input() showEmailMeWhenInStockToggle: any;
  @Input() doNotShowAddedToBasketInterstitial: boolean = false;
  @Input() quickview?: boolean = false;
  @Input() wishlistOnlyForProduct?: any;
  @Input() product?: any;
  @Input() multiVariant: boolean;
  @Input() variants: string;
  @Input() variant: ICustomVariantOption;
  @Input() variantCode: string;
  @Input() guestFirst;
  @Input() hideFav: boolean = false;
  @Output() triggerParentForVariantModal: EventEmitter<any> = new EventEmitter();
  @ViewChild('tooltipFirstTimers', { static: false }) tooltip: NgbTooltip;
  @ViewChild('addToWishlistModal', { static: false }) addToWishlistModal: ElementRef;
  @Input() isCart = false;
  @Input() showNotifyMe? = false;

  productId: string;

  iconTypes = ICON_TYPE;

  userWishlists$: Observable<any[]>;
  wishlistAdditionSub: Subscription;

  maxQuantity: number = 0;
  modalRef: ModalRef;
  quantity = 1;
  increment = false;
  value: 1;

  cartEntry$: Observable<OrderEntry>;
  selectedVariant$: Observable<ICustomVariantOption>;
  selectedProdCode: string;
  ariaLabelledByWishlists: string;
  ariaLabelledByAddToCart: string;
  existingQtyInBasket: number = 0;
  adjustedMaxQty: number = 0;
  isFindInStoreEnabled = false;
  existInWishlistSub: Subscription;
  productName: string;
  selectedProdIsMultiVariant: boolean = false;
  isPDP: boolean = false;
  selected: string;
  name: string;
  isCreateFormVisible: boolean = false;
  isUserLogged: boolean = false;
  wishlistFirstSub: Subscription;
  getSelectedVariantSub: Subscription;
  openModalRef: NgbModalRef;
  selectedVarStoreSub: Subscription;

  first: boolean[] = [];

  constructor(
    protected cartService: CustomCartService,
    protected modalService: ModalService,
    protected currentProductService: CurrentProductService,
    public store: Store<RootStoreState.State>,
    protected productVariantsService: ProductVariantsService,
    private cd: ChangeDetectorRef,
    private wishlistService: WishlistService,
    private ngbTooltipConfig: NgbTooltipConfig,
    private translation: TranslationService
  ) {
    ngbTooltipConfig.placement = 'top';
    ngbTooltipConfig.triggers = 'manual';
    this.isUserLogged = this.wishlistService.isUserLogged;

    this.translation.translate('wishlists.title').subscribe(text => {
      this.ariaLabelledByWishlists = text;
    });
    this.translation.translate('addToCart.itemsAddedToYourCart').subscribe(text => {
      this.ariaLabelledByAddToCart = text;
    });
  }

  ngOnInit() {
    this.checkFindInStoreFlag();
    if (!this.quickview && !this.variants && !this.variantCode) {
      this.isPDP = true;
      if (this.isUserLogged) {
        this.wishlistService.initAllWishlists();
      } else {
        this.wishlistFirstSub = this.wishlistService.getFirstTimer().subscribe(val => {
          let subLength = this.first.length;
          this.guestFirst = val;
          if (!val) {
            // If it's not a first time guest
            if (subLength === 1) {
              // should overwrite existing value instead of adding
              this.first = [];
            }
          }
          this.first.push(this.guestFirst);
        });
      }
    }
    this.selectedVariant$ = this.productVariantsService.getSelectedVariant().pipe(
      map((data: ICustomVariantOption) => {
        this.selectedProdCode = data.code;
        this.maxQuantity = data.maxOrderProductQuantity;
        if (data.code) {
          this.cartService.getEntries().subscribe(cartItems => {
            // TODO determine the number in the basket all entries
            // TODOD determint the quantity for the selected Variant
            this.existingQtyInBasket = 0;
            if (cartItems.length > 0) {
              const entry = cartItems.filter(el => el.product.code === this.selectedProdCode);
              if (entry.length > 0) {
                this.existingQtyInBasket = this.quantity = entry[0].quantity;
              } else {
                this.quantity = 1;
              }
            }
            if (this.maxQuantity < this.existingQtyInBasket) {
              this.adjustedMaxQty = this.maxQuantity - this.existingQtyInBasket;
            } else {
              this.adjustedMaxQty = this.maxQuantity;
            }
          });
        }
        if (data.stock) {
          this.hasStock = data.stock && data.stock.stockLevelStatus !== 'outOfStock' && data.stock.stockLevel > 0;
          this.showNotifyMe = !this.hasStock && data.emailMeWhenInStockToggle;
        }
        return data;
      })
    );

    if (this.variant) {
      this.selectedProdCode = this.variant.code;
      this.maxQuantity = this.variant.maxOrderProductQuantity;
      this.selectedVariant$ = of(this.variant);
    }
    if (this.productCode) {
      // PLPs add to basket
      this.selectedVariant$ = of(this.productVariantsData);
      if (!this.hasStock && this.showEmailMeWhenInStockToggle) {
        this.showNotifyMe = true;
      }
    }
  }

  /**
   * As guest custom, logic for the first time you click on add to fav
   */
  firstTimerActions() {
    // send message to subscribers via observable subject
    this.wishlistService.sendFirstTimer(false);
    // trigger the tooltip first time message
    this.tooltip.open();
    return;
  }

  /**
   * Logic to get all the information needed from the product and
   * stores it in the component variable to be used by other functions
   */
  productSelectionToAddToList() {
    if (this.multiVariant) {
      // Multivariant product should trigger quickview
      this.triggerParentForVariantModal.emit(true);
    } else {
      // Not a multivariant, should open wishlist
      if (this.isPDP) {
        // check on base prod if multi or single
        this.selectedVarStoreSub = this.store
          .select(ProductVariantSelectionStoreSelectors.getSelectedProductState)
          .subscribe(data => {
            if (data) {
              this.selected = data.selected.code;
              this.name = data.selected.name;
              this.selectedProdIsMultiVariant = data.selected.multiVariant;
            }
          });
      } else {
        // PLPs/SLPs
        if (this.variants && !this.variantCode) {
          // single
          this.selected = this.variants.split(',')[0];
          this.selectedProdIsMultiVariant = false;
        }
        if (!this.variants && this.variantCode) {
          // variant listing
          this.selectedProdIsMultiVariant = false;
          this.selected = this.variantCode;
        }
        if (!this.variants && !this.variantCode) {
          // selected variant store
          this.getSelectedVariantSub = this.productVariantsService.getSelectedVariant().subscribe(data => {
            this.selected = data.code;
            this.name = data.name;
            this.selectedProdIsMultiVariant = true;
          });
        }
      }
    }
  }

  /**
   * Click event on the fav icon from the variant quickview
   * @param wishlistModal
   */
  triggerAddToWishlist(param?) {
    this.productSelectionToAddToList();

    if (this.isUserLogged && !this.multiVariant) {
      this.triggerRegisteredAddToWishlist();
      return;
    }

    // If this is a guest user & not multi
    if (this.guestFirst && !this.multiVariant) {
      // First time, a tooltip should show
      this.firstTimerActions();
      return;
    }

    // On click again the guest add to wishlist logic should be triggered
    this.triggerGuestAddToWishlist();
  }

  /**
   * Add to wishlist logic for registered users
   */
  triggerRegisteredAddToWishlist() {
    this.showWishlistModal(this.selected, this.name);
  }

  /**
   * Add to wishlist logic for guest users
   */
  triggerGuestAddToWishlist() {
    if (this.selected) {
      const productsObj = this.guestAddToWishlist(this.selected);
      this.wishlistService.guestSetToSessionWishlist(productsObj);
    }
  }

  /**
   * Get the existing stored wishlists prods, and create/add the new ones
   * Return the object to store in sessionStorage.
   */
  guestAddToWishlist(product: string) {
    // Read existing
    let existing = this.wishlistService.getItemExistingProductInGuestWishlist() || [];
    // Creates or add to existing session list
    return this.wishlistService.createsOrAddsProductToList(existing, product);
  }

  /**
   * For registered user, show the modal where they can create/select which wishlist to add the product to
   * @param selectedProduct
   * @param productVariantName
   */
  showWishlistModal(selectedProduct, productVariantName?) {
    this.productId = selectedProduct;

    if (this.quickview) {
      // Hide quickview first, and then show wishlist
      this.wishlistService.toggleQuickviewDisplay(true);
    }
    this.openModalRef = this.modalService.open(this.addToWishlistModal, {
      ariaLabelledBy: this.ariaLabelledByWishlists
    });
    this.openModalRef.result.then(
      result => {},
      reason => {
        if (
          reason === ModalDismissReasons.ESC || // if you want to check ESC as well
          reason === ModalDismissReasons.BACKDROP_CLICK
        ) {
          this.closeWishlistModalMulti();
        }
      }
    );
    this.checkForProductInWishlists(selectedProduct);
    if (productVariantName) {
      this.productName = productVariantName;
    }
  }

  /**
   * Providing the product code, check in each withlist if the product exist there to be able to add a UI flag
   * @param prodCode
   */
  checkForProductInWishlists(prodCode) {
    this.existInWishlistSub = this.wishlistService.isProductInWishlists(prodCode).subscribe(
      success => {
        // success
        // Create `userWishlists$` that will populate in the DOM a list with a checked icon or not
        this.userWishlists$ = success;
      },
      error => {
        //catch the error
        console.error('An error occurred, ', error);
      }
    );
  }
  /**
   * Add the product to an existing list created by the registered user
   * @param ev
   * @param listId
   */
  triggerAddToExistingList(ev, listId) {
    this.wishlistAdditionSub = this.wishlistService.addToListId(listId, this.selected).subscribe(
      success => {
        let existing;
        // Show the added icon
        if (ev) {
          ev.target.classList.add('wishlists__add--added');
        }

        // UPDATE THE STATE WITH NEW PRODUCT IN WISHLIST
        this.wishlistService.getUserWishlists().subscribe(data => {
          existing = data.wishlists;
        });

        const updated = existing ? this.wishlistService.updateExistingWishlists(existing, success) : [success];

        this.wishlistService.setUserWishlists(updated);
        this.closeWishlistModalMulti();
      },
      error => {
        //catch the error
        console.error('An error occurred, ', error);
      }
    );
  }

  /**
   * Trigger to add to an existing wishlist
   */
  listAdded(listId) {
    this.isCreateFormVisible = false;
    this.triggerAddToExistingList('', listId);
  }

  /**
   * Click event to close the Wishlist Modal when you're viewing multivariant only
   */
  closeWishlistModalMulti() {
    // this.modalService.dismissActiveModal();
    if (this.quickview) {
      this.modalService.closeActiveModal();
      this.triggerParentForVariantModal.emit(false);
    } else {
      this.modalService.closeActiveModal();
    }
  }

  /**
   * Toggle for the create form within the Wishlist Modal
   */
  createNewWishlistInModal(productId?) {
    this.isCreateFormVisible = true;
  }

  updateCount(value: number): void {
    this.quantity = value;
  }

  closeForm(event) {
    this.isCreateFormVisible = false;
  }

  addToCart() {
    // start -  TO BE REPLACED WITH OOTB VARIANT SAP SOLUTION
    if (this.selectedProdCode) {
      this.productCode = this.selectedProdCode;
    }
    // end -  TO BE REPLACED WITH OOTB VARIANT SAP SOLUTION

    if (!this.productCode || this.quantity <= 0) {
      return;
    }

    // check item is already present in the cart
    // so modal will have proper header text displayed
    this.cartService
      .getEntry(this.productCode)
      .subscribe(entry => {
        if (entry) {
          this.increment = true;
        }
        if (this.doNotShowAddedToBasketInterstitial) {
          // PLP, SLP
          this.modalService.dismissActiveModal();
        } else {
          // PDPs
          this.openModal();
        }

        this.cartService.addEntry(this.productCode, this.quantity, entry);

        this.increment = false;
      })
      .unsubscribe();
  }

  openModal() {
    let modalInstance: any;
    this.modalRef = this.modalService.open(AddedToCartDialogComponent, {
      centered: true,
      size: 'lg',
      ariaLabelledBy: this.ariaLabelledByAddToCart
    });

    // start -  TO BE REPLACED WITH OOTB VARIANT SAP SOLUTION
    // if a product variant has been selected,
    // overwrite the cartEntry$ to the product variant code
    if (this.selectedProdCode !== null) {
      this.cartEntry$ = this.cartService.getEntry(this.selectedProdCode);
    }
    // end -  TO BE REPLACED WITH OOTB VARIANT SAP SOLUTION

    modalInstance = this.modalRef.componentInstance;
    modalInstance.entry$ = this.cartEntry$;
    modalInstance.cart$ = this.cartService.getActive();
    modalInstance.loaded$ = this.cartService.getLoaded();
    modalInstance.quantity = this.quantity;
    modalInstance.increment = this.increment;
  }

  checkFindInStoreFlag() {
    this.currentProductService.getProductDetails(this.productCode).subscribe((productData: ICustomProduct) => {
      if (!isUndefined(productData && productData.findInStoreEnabled)) {
        this.isFindInStoreEnabled = productData.findInStoreEnabled;
      }
    });
  }
  destroySubscriptions() {
    if (this.wishlistAdditionSub) {
      this.wishlistAdditionSub.unsubscribe();
    }
    if (this.existInWishlistSub) {
      this.existInWishlistSub.unsubscribe();
    }
  }
  destroyWishlistFirstSubscription() {
    if (this.wishlistFirstSub) {
      this.wishlistFirstSub.unsubscribe();
    }
  }
  destroySelectedVarStoreSub() {
    if (this.selectedVarStoreSub) {
      this.selectedVarStoreSub.unsubscribe();
    }
  }
  destroyGetSelectedVariantSub() {
    if (this.getSelectedVariantSub) {
      this.getSelectedVariantSub.unsubscribe();
    }
  }
  ngOnDestroy() {
    this.destroySubscriptions();
    this.destroyWishlistFirstSubscription();
    this.destroySelectedVarStoreSub();
  }
}
