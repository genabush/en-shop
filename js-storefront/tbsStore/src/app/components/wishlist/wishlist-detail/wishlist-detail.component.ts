import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { WishlistService } from 'src/app/services/wishlist/wishlist.service';
import { Subscription, Observable } from 'rxjs';
import { ICON_TYPE } from '@spartacus/storefront';
import { WishlistsStoreActions, WishlistsStoreState } from '../../../root-store/wishlists';
import { Store } from '@ngrx/store';
import { getNewWishlistsName } from '../../../root-store/wishlists/selectors';

@Component({
  selector: 'app-wishlist-detail',
  templateUrl: './wishlist-detail.component.html'
})
export class WishlistDetailComponent implements OnInit, OnDestroy {
  showDeletionMessage: boolean = false;
  currentWishlistSub: Subscription;
  wishlistId: string;
  wishlistName: string;
  currentWishlistProducts$: Observable<any>;
  iconTypes = ICON_TYPE;

  constructor(
    public store: Store<WishlistsStoreState.State>,
    public wishlistService: WishlistService,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.wishlistId = this.route.snapshot.params.id;
    this.refreshUserLists();

    this.lookForWishlistChanges();
  }

  lookForWishlistChanges() {
    this.store.select(getNewWishlistsName).subscribe(newWishlistName => {
      if (newWishlistName) {
        this.wishlistName = newWishlistName;
      }
    });
  }

  refreshUserLists() {
    this.currentWishlistSub = this.wishlistService.getWishlistById(this.wishlistId).subscribe(data => {
      this.wishlistName = data.wishlistName;
      if (data.products) {
        this.currentWishlistProducts$ = data.products;
      }
    });
  }

  openWishlistNameModal() {
    this.store.dispatch(
      new WishlistsStoreActions.OpenWishlistNameModalAction({
        nameModal: {
          isModalOpen: true,
          isModalNameEdit: true
        }
      })
    );
  }

  closeDeletionMessage() {
    this.showDeletionMessage = false;
  }

  productRemovedMessage() {
    this.showDeletionMessage = true;
  }

  removeProductFromList(code) {
    this.wishlistService.userRemoveFromList(this.wishlistId, code).subscribe(
      () => {
        this.refreshUserLists();
        this.productRemovedMessage();
      },
      err => {
        console.error('removeProductFromList - ', err);
      }
    );
  }

  deleteWishlist() {
    this.store.dispatch(
      new WishlistsStoreActions.OpenWishlistDeleteModalAction({
        deleteModal: { isModalOpen: true, wishlistId: this.wishlistId }
      })
    );
  }

  ngOnDestroy() {
    this.showDeletionMessage = false;
    if (this.currentWishlistSub) {
      this.currentWishlistSub.unsubscribe();
    }
  }
}
