import { OccService } from './../occ/occ.service';
import { Injectable, OnDestroy } from '@angular/core';
import { OccConfig, BaseSiteService, AuthService, CartSelectors } from '@spartacus/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { Subscription, of, Observable, Subject, BehaviorSubject } from 'rxjs';
import { select, Store } from '@ngrx/store';
import { RootStoreState } from 'src/app/root-store';
import { WishlistsStoreActions, WishlistsStoreSelectors } from 'src/app/root-store/wishlists';
import { Wishlist } from 'src/app/interfaces/wishlists.interface';
import { ModalRef } from '@spartacus/storefront';

@Injectable({
  providedIn: 'root'
})
export class WishlistService implements OnDestroy {
  baseSiteSub: Subscription;
  baseSite: string;
  baseUrl: string = this.occService.getBaseUrl();

  wishlistsSub: Subscription;
  activeWishlistsWithProduct: Array<Wishlist>;
  modalWishlistRef: ModalRef;

  userTokenSub: Subscription;
  isUserLogged: boolean = false;

  private subject = new BehaviorSubject<boolean>(true);
  sessionFirstTimerKey: string = 'TBSWishlistsFirstTimer';
  sessionWishlistKey: string = 'TBSWishlists';

  constructor(
    public occ: OccConfig,
    protected http: HttpClient,
    public occService: OccService,
    private baseSiteService: BaseSiteService,
    public store: Store<RootStoreState.State>,
    private authService: AuthService
  ) {
    this.setBaseSiteSub();
    this.setAuthSub();
  }

  sendFirstTimer(value: boolean) {
    this.subject.next(value);
    this.setFirstTimeSession(value);
  }

  getFirstTimer(): Observable<any> {
    return this.subject.asObservable();
  }

  checkFirstTimeSession(): boolean {
    let isTrueSet, myItem;
    myItem = sessionStorage.getItem(this.sessionFirstTimerKey);
    // Convert from session storage which is a string to a boolean;
    if (myItem) {
      isTrueSet = myItem === 'true';
    } else {
      isTrueSet = true;
    }
    return isTrueSet;
  }

  setFirstTimeSession(value) {
    sessionStorage.setItem(this.sessionFirstTimerKey, value);
    this.subject.next(value);
  }

  getItemExistingProductInGuestWishlist() {
    return JSON.parse(sessionStorage.getItem(this.sessionWishlistKey));
  }

  createsOrAddsProductToList(existing, product) {
    let newEntry;
    if (existing.products) {
      // If there are already product, add to existing & prevent duplicates
      newEntry = existing.products.indexOf(product) === -1 ? [...existing.products, product] : [...existing.products];
    }
    if (!existing.products) {
      // add first new product
      newEntry = [product];
    }

    return { products: newEntry };
  }

  setAuthSub() {
    // check if logged in
    //  This will help define if the wishlist comes from the hybris or localstorage
    this.baseSiteSub = this.authService.getUserToken().subscribe(
      res => {
        this.isUserLogged = Object.keys(res).length > 0;
      },
      error => {
        console.error('(setAuthSub) ERROR:', error);
      }
    );

    if (!this.isUserLogged) {
      // User is GUEST
      const result = this.checkFirstTimeSession();
      if (result === null || result === undefined) {
        this.setFirstTimeSession(true);
      } else {
        this.setFirstTimeSession(result);
      }
    }
  }

  initAllWishlists() {
    if (this.isUserLogged) {
      this.wishlistsSub = this.getAllWishlists().subscribe(
        data => {
          if (data) {
            this.store.dispatch(new WishlistsStoreActions.SetWishlistsAction({ wishlists: data.wishlists }));
            return data.wishlists;
          }
        },
        error => {
          console.error('(initAllWishlists) ERROR:', error);
        }
      );
    }
  }

  guestSetToSessionWishlist(result) {
    // Create item:
    sessionStorage.setItem(this.sessionWishlistKey, JSON.stringify(result));
  }

  guestGetItemInWishlist(): Observable<any> {
    let lists = sessionStorage.getItem('TBSWishlists');
    lists === null ? [] : (lists = JSON.parse(lists).products);
    return of(lists);
  }

  // Get the basesite to make any HTTP request for the wishlists
  setBaseSiteSub() {
    this.baseSiteSub = this.baseSiteService.getActive().subscribe(
      (baseSite: string) => {
        this.baseSite = baseSite;
      },
      error => {
        console.error('(setBaseSiteSub) ERROR:', error);
      }
    );
  }

  // Set the wishlists in the ngrx Store
  setUserWishlists(lists) {
    this.store.dispatch(new WishlistsStoreActions.SetWishlistsAction({ wishlists: lists }));
  }

  // Get user wishlist from the store
  getUserWishlists() {
    return this.store.select(WishlistsStoreSelectors.getWishlistsState).pipe(
      map(data => {
        if (data) {
          return data;
        }
      })
    );
  }

  // Add product to existing list
  addToListId(wishlistId: string, productCode: string) {
    return this.http.put<any>(
      `${this.baseUrl}/rest/v2/${this.baseSite}/users/current/wishlist/${wishlistId}?productCode=${productCode}`,
      {
        wishlistId: wishlistId,
        productCode: productCode
      }
    );
  }

  // Check if product is in any wishlist and return an array of wishlist with a flag if it's in or not
  isProductInWishlists(selectedVariant) {
    return this.getUserWishlists().pipe(
      map(data => {
        if (data.wishlists) {
          // define the query selector and the initial array
          const query: string = selectedVariant;
          const initialState: Array<any> = data.wishlists;
          // Find the product code in all wishlist
          this.activeWishlistsWithProduct = initialState.filter(item => {
            if (item.products) {
              const hasProductInList = item.products.filter(prod => {
                return prod.code === query;
              });
              // If `hasProductInList` array has entries = there is a match, so the product exist in this wishlist
              //  Otherwise, there is no watch, the `hasProductInList` should be set to false
              if (hasProductInList.length > 0) {
                item.hasProductInList = true;
                return hasProductInList;
              } else {
                item.hasProductInList = false;
                return hasProductInList;
              }
            }
          });
          // merge the new result and remove duplicate
          let merged = [...this.activeWishlistsWithProduct, ...data.wishlists];
          merged = merged.reduce((unique, item) => (unique.includes(item) ? unique : [...unique, item]), []);
          // return the list of all wishlist with the boolean flag `hasProductInList` on a wishlist level
          return of(merged);
        } else {
          // return an empty obvs
          return of([]);
        }
      })
    );
  }

  // Call the endpoint to get all the users wishlists
  getAllWishlists() {
    return this.http.get(`${this.baseUrl}/rest/v2/${this.baseSite}/users/current/wishlist/all`).pipe(
      map((data: any) => {
        return data;
      })
    );
  }

  getWishlistById(wishlistId) {
    // TODO: reuse ${this.baseUrl}/rest/v2/${this.baseSite}
    return this.http.get(`${this.baseUrl}/rest/v2/${this.baseSite}/users/current/wishlist/${wishlistId}`).pipe(
      map((data: any) => {
        return data;
      })
    );
  }

  // Call the endpoint to create a new wishlist name
  createNew(name) {
    return this.http.post<any>(`${this.baseUrl}/rest/v2/${this.baseSite}/users/current/wishlist`, {
      wishlistName: name
    });
  }

  // Call the endpoint to retrieve guest wishlist products data
  getWishlistProducts(arr, guest) {
    arr = arr.toString();
    const dynamicPath = guest ? 'users/anonymous' : 'users/current';
    return this.http.get<any>(
      `${this.baseUrl}/rest/v2/${this.baseSite}/${dynamicPath}/wishlist/product?productCodes=${arr}`
    );
  }

  // Removing a product from wishlist
  guestRemoveFromList(code) {
    let existing = this.getItemExistingProductInGuestWishlist() || [];
    let updatedList = existing.products.filter(e => e !== code);
    this.guestSetToSessionWishlist({ products: updatedList });
  }

  userRemoveFromList(wishlistId, productCode) {
    return this.http.delete<any>(
      `${this.baseUrl}/rest/v2/${this.baseSite}/users/current/wishlist/${wishlistId}/product/${productCode}`
    );
  }

  deleteWishlist(wishlistId) {
    return this.http.delete(`${this.baseUrl}/rest/v2/${this.baseSite}/users/current/wishlist/${wishlistId}`);
  }

  renameWishlist(wishlistId, newWishlistName) {
    return this.http.post(`${this.baseUrl}/rest/v2/${this.baseSite}/users/current/wishlist`, {
      wishlistId: wishlistId,
      wishlistName: newWishlistName
    });
  }

  /**
   * Update list of existing wishlists
   * @param arr = existing array of all wishlist
   * @param obj = new entry
   */
  updateExistingWishlists(arr, obj) {
    const index = arr.findIndex(e => e.wishlistId === obj.wishlistId);
    if (index === -1) {
      arr.push(obj);
    } else {
      arr[index] = obj;
    }
    return arr;
  }

  toggleQuickviewDisplay(ev) {
    let prevModal = document.querySelector('.modal');
    const prevModalClass = prevModal.classList;
    let prevModalBackDrop = document.querySelector('.modal-backdrop');
    const prevModalBackDropClass = prevModalBackDrop.classList;
    if (ev) {
      prevModalClass.remove('show');
      prevModalClass.remove('d-block');
      prevModalBackDropClass.remove('show');
    } else {
      prevModalClass.add('show');
      prevModalClass.add('d-block');
      prevModalBackDropClass.add('show');
    }
  }

  openAddFromCartModal() {
    this.store.dispatch(new WishlistsStoreActions.OpenAddFromCartModalAction());
  }

  addFromCart() {
    this.store.dispatch(new WishlistsStoreActions.AddFromCartAction());
  }

  resetAddFromCart() {
    this.store.dispatch(new WishlistsStoreActions.ResetAddFromCartAction());
  }

  // Lifecycle cleanup
  destroyBaseSiteSub() {
    if (this.baseSiteSub) {
      this.baseSiteSub.unsubscribe();
    }
  }
  destroyWishlistsSub() {
    if (this.wishlistsSub) {
      this.wishlistsSub.unsubscribe();
    }
  }

  ngOnDestroy() {
    this.destroyBaseSiteSub();
    this.destroyWishlistsSub();
  }
}
