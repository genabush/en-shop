import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { OccService } from '../occ/occ.service';
import { Store } from '@ngrx/store';
import { StoreFinderStoreActions, StoreFinderStoreSelectors } from 'src/app/root-store/store-finder';
import { isEmpty } from 'lodash';
import { map } from 'rxjs/operators';
import { LatLngLiteral } from '@agm/core';

@Injectable({
  providedIn: 'root'
})
export class CustomStoreFinderService {
  apiUrl: string;
  constructor(private http: HttpClient, private occService: OccService, private store: Store<any>) {
    this.apiUrl = this.occService.getApiUrl();
  }
  getListOfStores(query, lat, long) {
    return this.http.get<any>(`${this.apiUrl}/stores`, {
      params: {
        fields: 'FULL',
        latitude: lat,
        longitude: long,
        query: query
      }
    });
  }
  setStateSearchResults(listOfStores: [{}]) {
    this.store.dispatch(new StoreFinderStoreActions.SetSearchedResultsAction({ searchedResults: listOfStores }));
  }
  getStateSearchedResults() {
    return this.store.select(StoreFinderStoreSelectors.getStoreFinderState).pipe(
      map(data => {
        if (data && !isEmpty(data.searchedResults)) {
          return data.searchedResults;
        }
      })
    );
  }
  setStateSearchQuery(searchedQuery: string) {
    this.store.dispatch(new StoreFinderStoreActions.SetSearchedQueryAction({ searchedQuery: searchedQuery }));
  }
  getStateSearchQuery() {
    return this.store.select(StoreFinderStoreSelectors.getStoreFinderState).pipe(
      map(data => {
        if (data && !isEmpty(data.searchedQuery)) {
          return data.searchedQuery;
        }
      })
    );
  }
  setStateSearchedLocation(location: LatLngLiteral) {
    this.store.dispatch(new StoreFinderStoreActions.SetSearchedLocationAction({ location: location }));
  }
  getStateSearchedLocation() {
    return this.store.select(StoreFinderStoreSelectors.getStoreFinderState).pipe(
      map(data => {
        if (data && !isEmpty(data.searchedLocation)) {
          return data.searchedLocation;
        }
      })
    );
  }
}
