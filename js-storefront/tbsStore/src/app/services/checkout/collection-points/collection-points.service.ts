import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

// sparatacus
import { CartDataService } from '@spartacus/core';

// services
import { OccService } from '../../occ/occ.service';
import { CustomCartService } from '../../cart/facade/cart.service';

// vendor
import { Store } from '@ngrx/store';
import { Observable, throwError } from 'rxjs';
import { map } from 'rxjs/operators';
import { isEmpty, isUndefined } from 'lodash';

// interfaces
import { CollectPoint, CollectorDetails } from 'src/app/interfaces/collection-point.interface';
import { CollectionPointStoreActions, CollectionPointStoreSelectors } from 'src/app/root-store/collection-point';

@Injectable({
  providedIn: 'root'
})
export class CollectionPointsService {
  private apiUrl: string;
  private userId: string;
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
  }
  /**
   * Get a list of Collection Points based on search criteria
   * * @param query : the search query
   * * @param lat : the latitude for searrch
   * * @param long : the longnitute for search
   */
  getListOfCollectionPoints(query, lat, long): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/users/${this.userId}/collection-point/search`, {
      params: {
        fields: 'DEFAULT',
        lat: lat,
        long: long,
        q: query
      }
    });
  }
  /**
   * Set a Collection Point
   * * @param collectionPoint Collection Point data
   */
  postCollectionPoint(collectionPoint: CollectPoint): Observable<any> {
    if (!isUndefined(this.cartData.cartId) && !isEmpty(this.cartData.cartId)) {
      return this.http.post<any>(
        `${this.apiUrl}/users/${this.userId}/collection-point/carts/${this.cartData.cartId}/save`,
        collectionPoint
      );
    }
    return throwError('');
  }
  /**
   * Set CollectionPoint collector data
   * * @param collectorDetails Details on the collector
   */
  postCollectionPointCollector(collectorDetails: CollectorDetails): Observable<any> {
    if (!isUndefined(this.cartData.cartId) && !isEmpty(this.cartData.cartId)) {
      return this.http.post<any>(`${this.getCollectionsEndpoint()}/collectordetails`, collectorDetails);
    }
    return throwError('');
  }
  /**
   * Get the currently set Collection Point
   */
  getSelectedCollectionPoint() {
    return this.store.select(CollectionPointStoreSelectors.getCollectionPointState).pipe(
      map(data => {
        if (data && !isEmpty(data.selected)) {
          return data.selected;
        }
      })
    );
  }
  /**
   * Get the currently set Collection Point
   */
  getCollectionPointsFromState() {
    return this.store.select(CollectionPointStoreSelectors.getCollectionPointState).pipe(
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
    return this.store.select(CollectionPointStoreSelectors.getCollectionPointState).pipe(
      map(data => {
        if (data && data.searchQuery) {
          return data.searchQuery;
        } else {
          return '';
        }
      })
    );
  }
  /**
   * Get the current cart endpoint
   */
  getCollectionsEndpoint() {
    return `${this.apiUrl}/users/${this.userId}/collection-point/carts/${this.cartData.cartId}`;
  }
  // NGRX
  setCollectionPoint(collectionPoint: CollectPoint) {
    this.store.dispatch(new CollectionPointStoreActions.SetCollectionPointAction({ collectionPoint: collectionPoint }));
  }
  unsetCollectionPoint() {
    this.store.dispatch(new CollectionPointStoreActions.SetCollectionPointAction({ collectionPoint: {} }));
  }
  setSearchResults(searchResults) {
    this.store.dispatch(new CollectionPointStoreActions.SetSearchResultsAction({ searchResults: searchResults }));
  }
  setSearchQuery(searchQuery) {
    this.store.dispatch(new CollectionPointStoreActions.SetSearchQueryAction({ searchQuery: searchQuery }));
  }
}
