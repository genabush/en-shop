import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

// spartacus
import { CartDataService } from '@spartacus/core';

// vendor
import { Observable, throwError } from 'rxjs';
import { Store } from '@ngrx/store';
import { map } from 'rxjs/operators';
import { isEmpty, isUndefined } from 'lodash';

// services
import { OccService } from '../../occ/occ.service';
import { CustomCartService } from '../../cart/facade/cart.service';

// interfaces
import { CollectPoint, CollectorDetails } from 'src/app/interfaces/collection-point.interface';
import { CisStoreActions, CisStoreSelectors } from 'src/app/root-store/collect-in-store';

@Injectable({
  providedIn: 'root'
})
export class CheckoutCollectInStoreService {
  private apiUrl: string;
  private userId: string;
  private cartCode: string;
  private cisEndpoint: string;
  constructor(
    private http: HttpClient,
    private occService: OccService,
    private customCartService: CustomCartService,
    private cartData: CartDataService,
    private store: Store<any>
  ) {
    this.setCurrentConfig();
  }
  /**
   * Configure the current cart service
   */
  setCurrentConfig(): void {
    this.apiUrl = this.occService.getApiUrl();
    this.userId = this.customCartService.getUserId();
    this.cisEndpoint = `${this.apiUrl}/users/${this.userId}/collect-in-store`;
  }
  /**
   * Get a list of Collection Stores based on search criteria
   * * @param query : the search query
   * * @param lat : the latitude for searrch
   * * @param long : the longnitute for search
   * * @param curPage : the currentPage
   */
  getListOfCollectionStores(query, lat, long, curPage = '0'): Observable<any> {
    if (!isUndefined(this.cartData.cartId) && !isEmpty(this.cartData.cartId)) {
      return this.http.get<any>(`${this.cisEndpoint}/carts/${this.cartData.cartId}/search`, {
        params: {
          fields: 'DEFAULT',
          lat: lat,
          long: long,
          q: query,
          currentPage: curPage
        }
      });
    }
    return throwError('');
  }
  /**
   * Set a Collection Store
   * * @param collectionPoint Collection Point data
   */
  postCollectionStore(collectionPoint: CollectPoint): Observable<any> {
    if (!isUndefined(this.cartData.cartId) && !isEmpty(this.cartData.cartId)) {
      return this.http.post<any>(`${this.cisEndpoint}/carts/${this.cartData.cartId}/save`, collectionPoint);
    }
    return throwError('');
  }
  /**
   * Set Collection Store collector data
   * * @param collectorDetails Details on the collector
   */
  postCollectionStoreCollector(collectorDetails: CollectorDetails): Observable<any> {
    return this.http.post<any>(`${this.cisEndpoint}/carts/${this.cartData.cartId}/collectordetails`, collectorDetails);
  }
  /**
   * Get the currently set Collection Store
   */
  getSelectedCollectionStore() {
    return this.store.select(CisStoreSelectors.getCisState).pipe(
      map(data => {
        if (data && !isEmpty(data.selected)) {
          return data.selected;
        }
      })
    );
  }
  /**
   * Get a list of Collection Stores from the Store State
   */
  getCollectionStoresFromState() {
    return this.store.select(CisStoreSelectors.getCisState).pipe(
      map(data => {
        if (data && !isEmpty(data.searchResults)) {
          return data.searchResults;
        }
      })
    );
  }
  /**
   * Get the current search param from the Store State
   */
  getSearchQueryFromState() {
    return this.store.select(CisStoreSelectors.getCisState).pipe(
      map(data => {
        if (data && data.searchQuery) {
          return data.searchQuery;
        } else {
          return '';
        }
      })
    );
  }
  // NGRX
  setCollectionStore(collectionPoint: CollectPoint) {
    this.store.dispatch(new CisStoreActions.SetCollectionPointAction({ collectionPoint: collectionPoint }));
  }
  unsetCollectionStore() {
    this.store.dispatch(new CisStoreActions.SetCollectionPointAction({ collectionPoint: {} }));
  }
  setSearchResults(searchResults) {
    this.store.dispatch(new CisStoreActions.SetSearchResultsAction({ searchResults: searchResults }));
  }
  setSearchQuery(searchQuery) {
    this.store.dispatch(new CisStoreActions.SetSearchQueryAction({ searchQuery: searchQuery }));
  }
}
