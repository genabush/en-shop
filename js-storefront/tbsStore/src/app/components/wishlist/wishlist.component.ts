import { WishlistService } from '../../services/wishlist/wishlist.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { ModalService, ICON_TYPE, ModalRef } from '@spartacus/storefront';
import { Router } from '@angular/router';
import { Wishlists } from 'src/app/interfaces/wishlists.interface';
import { Product } from '@spartacus/core';
import { Store } from '@ngrx/store';
import { WishlistsStoreActions, WishlistsStoreState } from '../../root-store/wishlists';

@Component({
  selector: 'app-wishlist',
  templateUrl: './wishlist.component.html'
})
export class WishlistComponent implements OnInit, OnDestroy {
  wishlists$: Observable<Wishlists[]>;
  wishlistSub: Subscription;
  newWishlistSub: Subscription;
  iconTypes = ICON_TYPE;
  test: Observable<any>;
  sub: Subscription;
  wishlistGuestProdsSub: Subscription;
  modalRef: ModalRef;
  isUserLogged: boolean = false;
  guestListEmpty: boolean = true;
  listEmpty: boolean = false;
  products: Product[];
  showDeletionMessage: boolean = false;
  isEditWishlist: boolean = false;

  isOpenModal: boolean = false;

  constructor(
    public wishlistService: WishlistService,
    protected modalService: ModalService,
    public router: Router,
    public store: Store<WishlistsStoreState.State>
  ) {
    this.wishlistService.initAllWishlists();
  }

  ngOnInit() {
    this.isUserLogged = this.wishlistService.isUserLogged;

    if (this.isUserLogged) {
      // Logged in
      this.wishlistSub = this.wishlistService.getUserWishlists().subscribe(data => {
        if (data.wishlists) {
          this.listEmpty = Object.keys(data.wishlists).length === 0;
          this.wishlists$ = data.wishlists || [];
        }
      });
    }
    if (!this.isUserLogged) {
      // Guest
      this.refreshGuestLists();
    }
  }

  removeProductFromList($event) {
    this.wishlistService.guestRemoveFromList($event);
    this.refreshGuestLists();
    this.productRemovedMessage();
  }

  closeDeletionMessage() {
    this.showDeletionMessage = false;
  }

  productRemovedMessage() {
    this.showDeletionMessage = true;
  }

  refreshGuestLists() {
    this.wishlistSub = this.wishlistService.guestGetItemInWishlist().subscribe(guestLists => {
      this.guestListEmpty = Object.keys(guestLists).length === 0;
      if (!this.guestListEmpty) {
        this.wishlists$ = guestLists || [];
        this.wishlistGuestProdsSub = this.wishlistService.getWishlistProducts(guestLists, true).subscribe(products => {
          this.products = products.products;
        });
      }
      if (this.guestListEmpty) {
        this.products = [];
      }
    });
  }

  // modals
  openWishlistModal() {
    this.store.dispatch(
      new WishlistsStoreActions.OpenWishlistNameModalAction({
        nameModal: { isModalOpen: true, isModalNameEdit: false }
      })
    );
  }

  listAdded(wishlistId) {
    this.modalService.closeActiveModal('close');
    this.router.navigateByUrl('my-account/wishlist/' + wishlistId);
  }

  // Destroy
  destroyWishlistSub() {
    if (this.wishlistSub) {
      this.wishlistSub.unsubscribe();
    }
    if (this.newWishlistSub) {
      this.newWishlistSub.unsubscribe();
    }
    if (this.wishlistGuestProdsSub) {
      this.wishlistGuestProdsSub.unsubscribe();
    }
  }

  ngOnDestroy() {
    if (this.modalRef) {
      this.modalRef.close();
    }
    this.destroyWishlistSub();
  }
}
