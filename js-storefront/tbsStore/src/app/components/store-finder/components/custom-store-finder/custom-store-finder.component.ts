import { Component, OnInit, ChangeDetectorRef, Input } from '@angular/core';
import { Observable } from 'rxjs';
import { CmsComponentData } from '@spartacus/storefront';
import { FormGroup, FormControl } from '@angular/forms';
import { LocationService } from 'src/app/services/geo-location/location-service';
import { CustomStoreFinderService } from 'src/app/services/custom-store-finder/custom-store-finder.service';
import { isUndefined } from 'lodash';
import { LatLngLiteral } from '@agm/core';
import { GeocodeService } from 'src/app/services/google/geocode.service';
import { CollectPoint } from 'src/app/interfaces/collection-point.interface';

const currentLocationPin = require('src/assets/images/maps-pins/current-location.svg') as string;
const primaryPin = require('src/assets/images/maps-pins/primary.svg') as string;
const primaryInactivePin = require('src/assets/images/maps-pins/primary-inactive.svg') as string;
const secondaryPin = require('src/assets/images/maps-pins/secondary.svg') as string;
const secondaryInactivePin = require('src/assets/images/maps-pins/secondary-inactive.svg') as string;

@Component({
  selector: 'app-custom-store-finder',
  templateUrl: './custom-store-finder.component.html'
})
export class CustomStoreFinderComponent implements OnInit {
  cmsComponent$: Observable<any>;
  storeFinderForm: FormGroup = this.getStoreFinderForm();
  hasError = false;
  hasGeoError = false;
  searchedQuery: string;
  listOfStores$: Observable<any>;
  searchedLocation$: Observable<any>;
  listOfStores = [];
  location: LatLngLiteral;
  listViewType = 'list';
  storeInfo: CollectPoint;
  showInfoWindow = false;
  isInfoTriggered = false;
  mapZoom = 12;
  mapCenter: LatLngLiteral;
  mapIcons = {
    currentLocation: currentLocationPin,
    primary: primaryPin,
    primaryInactive: primaryInactivePin,
    secondary: secondaryPin,
    secondaryInactive: secondaryInactivePin
  };
  constructor(
    public cmsComponent: CmsComponentData<any>,
    private locationService: LocationService,
    private storeFinderService: CustomStoreFinderService,
    private cd: ChangeDetectorRef,
    private geocodeService: GeocodeService
  ) {
    this.cmsComponent$ = cmsComponent.data$;
  }
  ngOnInit() {
    this.listOfStores$ = this.storeFinderService.getStateSearchedResults();
    this.subSearchedLocation();
  }
  ngOnDestroy() {
    this.cd.detach();
  }
  onSubmit($ev) {
    $ev.preventDefault();
    this.displayListOfStores(this.storeFinderForm.value.location, '', '', '');
  }
  getStoreFinderForm() {
    return new FormGroup({
      location: new FormControl()
    });
  }
  displayListOfStores(query, lat, lng, searchType) {
    this.hasError = false;
    this.hasGeoError = false;
    this.searchedQuery = query;
    this.storeFinderService.getListOfStores(query, lat, lng).subscribe(data => {
      if (!isUndefined(data) && data.stores.length) {
        if (query !== '') {
          this.geocodeService.geocodeAddress(query).subscribe((location: LatLngLiteral) => {
            this.location = location;
            this.listOfStores = data.stores;
            this.storeFinderService.setStateSearchResults(data.stores);
            this.storeFinderService.setStateSearchQuery(query);
            this.storeFinderService.setStateSearchedLocation(location);

            this.triggerChanges();
          });
        } else if (lat !== '' && lng !== '') {
          this.location = {
            lat: lat,
            lng: lng
          };
          this.listOfStores = data.stores;
          this.storeFinderService.setStateSearchResults(data.stores);
          this.storeFinderService.setStateSearchedLocation(this.location);

          this.triggerChanges();
        }
      } else {
        this.toggleErrorMsg(searchType);
      }
    });
  }
  getGeolocation($ev) {
    $ev.preventDefault();
    if ('geolocation' in navigator) {
      this.locationService.getPosition().then(pos => {
        this.displayListOfStores('', pos.lat, pos.lng, 'geo');
      });
    }
  }
  triggerChanges() {
    if (!this.cd['destroyed']) {
      this.cd.detectChanges();
    }
  }
  toggleErrorMsg(searchType) {
    if (searchType === 'geo') {
      this.hasGeoError = true;
      this.hasError = false;
    } else {
      this.hasGeoError = false;
      this.hasError = true;
    }
    this.triggerChanges();
  }
  toggleListView(type) {
    this.listViewType = type;
    this.storeInfo = null;
    this.unsetActivePin();
    this.setMapZoom(12);
  }
  unsetActivePin() {
    this.listOfStores.map(el => {
      el.isActive = false;
    });
  }
  setMapZoom(zoomValue: number) {
    this.mapZoom = zoomValue;
  }
  setMapCenter(lat: number, lng: number) {
    this.mapCenter = {
      lat: lat,
      lng: lng
    };
  }
  markerClicked(collectionPoint: CollectPoint) {
    this.unsetActivePin();
    this.storeInfo = collectionPoint;
    this.showInfoWindow = true;
    collectionPoint.isActive = true;
    this.setMapCenter(collectionPoint.geoPoint.latitude, collectionPoint.geoPoint.longitude);
    this.setMapZoom(12);
  }
  onInfoCloseEmit() {
    if (this.isInfoTriggered) {
      this.toggleListView('list');
      this.isInfoTriggered = false;
    }
    this.unsetActivePin();
  }
  subSearchedLocation() {
    this.storeFinderService.getStateSearchedLocation().subscribe((location: LatLngLiteral) => {
      if (!location) {
        return;
      }
      this.location = {
        lat: location.lat,
        lng: location.lng
      };
      this.setMapCenter(location.lat, location.lng);
    });
  }
}
