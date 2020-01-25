import { Component, ViewChild, ElementRef, Input } from '@angular/core';
import { NgbModalRef, NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { ICON_TYPE, ModalService } from '@spartacus/storefront';
import { FormGroup, FormControl } from '@angular/forms';
import { FindInStoreService } from 'src/app/services/find-in-store/find-in-store.service';
import { LocationService } from 'src/app/services/geo-location/location-service';
import { CollectPoint } from 'src/app/interfaces/collection-point.interface';
import { Observable } from 'rxjs';
import { LatLngLiteral } from '@agm/core';
import { GeocodeService } from 'src/app/services/google/geocode.service';

const currentLocationPin = require('src/assets/images/maps-pins/current-location.svg') as string;
const primaryPin = require('src/assets/images/maps-pins/primary.svg') as string;
const primaryInactivePin = require('src/assets/images/maps-pins/primary-inactive.svg') as string;
const secondaryPin = require('src/assets/images/maps-pins/secondary.svg') as string;
const secondaryInactivePin = require('src/assets/images/maps-pins/secondary-inactive.svg') as string;

@Component({
  selector: 'app-find-in-store',
  templateUrl: './find-in-store.component.html'
})
export class FindInStoreComponent {
  @Input() productCode: string;
  @ViewChild('findInStoreModal', { static: false })
  findInStoreModal: ElementRef;
  findInStoreModalRef: NgbModalRef;
  findInStoreModalOptions: NgbModalOptions = {
    windowClass: 'modal-find-in-store modal--find-location',
    ariaLabelledBy: 'modal-find-in-store'
  };
  iconTypes = ICON_TYPE;
  findInStoreForm: FormGroup = this.getFindInStoreForm();
  listOfStores = [];
  showErrorMsg = false;
  isInfoTriggered = false;
  showInfoWindow = false;
  listViewType = 'list';
  storesList$: Observable<any>;
  mapZoom = 12;
  mapCenter: LatLngLiteral;
  location: LatLngLiteral;
  storeInfo: CollectPoint;
  mapIcons = {
    currentLocation: currentLocationPin,
    primary: primaryPin,
    primaryInactive: primaryInactivePin,
    secondary: secondaryPin,
    secondaryInactive: secondaryInactivePin
  };
  constructor(
    private modalService: ModalService,
    private findInStoreService: FindInStoreService,
    private locationService: LocationService,
    private geocodeService: GeocodeService
  ) {}

  openFindInStoreModal() {
    this.findInStoreModalRef = this.modalService.open(this.findInStoreModal, this.findInStoreModalOptions);
  }
  closeFindInStoreModal() {
    this.findInStoreModalRef.close();
  }
  onSubmit(ev) {
    ev.preventDefault();
    this.getListOfStores(this.findInStoreForm.value.location, '', '', this.productCode);
  }
  getFindInStoreForm() {
    return new FormGroup({
      location: new FormControl()
    });
  }
  getGeolocation() {
    if ('geolocation' in navigator) {
      this.locationService.getPosition().then(pos => {
        this.getListOfStores('', pos.lat, pos.lng, this.productCode);
      });
    }
  }
  getListOfStores(query, lat, long, productCode) {
    this.findInStoreService.getProductStock(query, lat, long, productCode).subscribe(data => {
      if (data.stores && data.stores.length) {
        this.listOfStores = data.stores;
        this.showErrorMsg = false;
        if (query !== '') {
          this.addressToCoordinates(query);
        }
        if (lat !== '' && long !== '') {
          this.location = {
            lat: lat,
            lng: long
          };
          this.setMapCenter(lat, long);
        }
      } else {
        this.showErrorMsg = true;
      }
      this.listViewType = 'list';
    });
  }
  onInfoCloseEmit() {
    if (this.isInfoTriggered) {
      this.toggleListView('list');
      this.isInfoTriggered = false;
    }
    this.unsetActivePin();
  }
  onInfoEmit($ev) {
    this.displayLocationOnMap($ev);
  }
  displayLocationOnMap(collectionPoint: CollectPoint) {
    this.listViewType = 'map';
    this.isInfoTriggered = true;

    this.markerClicked(collectionPoint);
  }
  toggleListView(type) {
    this.listViewType = type;
    this.storeInfo = null;
    this.unsetActivePin();
    this.setMapZoom(12);
  }
  markerClicked(collectionPoint: CollectPoint) {
    this.unsetActivePin();
    this.storeInfo = collectionPoint;
    this.showInfoWindow = true;
    collectionPoint.isActive = true;
    this.setMapCenter(collectionPoint.geoPoint.latitude, collectionPoint.geoPoint.longitude);
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

  addressToCoordinates(address) {
    this.geocodeService.geocodeAddress(address).subscribe((location: LatLngLiteral) => {
      this.location = location;
      this.setMapCenter(location.lat, location.lng);
    });
  }
}
