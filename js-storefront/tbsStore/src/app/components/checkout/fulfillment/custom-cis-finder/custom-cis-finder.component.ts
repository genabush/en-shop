import {
  Component,
  OnInit,
  ElementRef,
  ViewChild,
  Input,
  ChangeDetectorRef,
  Output,
  EventEmitter,
  OnDestroy
} from '@angular/core';

import { FormGroup, FormControl } from '@angular/forms';
import { LocationService } from '../../../../services/geo-location/location-service';

import { Observable, BehaviorSubject, of } from 'rxjs';
import { ModalService, ICON_TYPE } from '@spartacus/storefront';
import { NgbModalRef, NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { GeocodeService } from '../../../../services/google/geocode.service';
import { LatLngLiteral } from '@agm/core';
import { CollectPoint } from 'src/app/interfaces/collection-point.interface';

import { isUndefined } from 'lodash';
import { CheckoutCollectInStoreService } from 'src/app/services/checkout/collect-in-store/collect-in-store.service';
import { CustomCheckoutDeliveryService } from 'src/app/services/checkout/delivery/custom-checkout-delivery.service';
import { CollectionPointsService } from 'src/app/services/checkout/collection-points/collection-points.service';

const currentLocationPin = require('src/assets/images/maps-pins/current-location.svg') as string;
const primaryPin = require('src/assets/images/maps-pins/primary.svg') as string;
const primaryInactivePin = require('src/assets/images/maps-pins/primary-inactive.svg') as string;
const secondaryPin = require('src/assets/images/maps-pins/secondary.svg') as string;
const secondaryInactivePin = require('src/assets/images/maps-pins/secondary-inactive.svg') as string;

@Component({
  selector: 'app-custom-cis-finder',
  templateUrl: './custom-cis-finder.component.html'
})
export class CustomCisFinderComponent implements OnInit, OnDestroy {
  showInfoWindow = false;
  isInfoTriggered = false;
  location: LatLngLiteral;
  mapCenter: LatLngLiteral;
  mapZoom = 12;
  storeInfo: CollectPoint;
  @ViewChild('cisModal', { static: false })
  cisModal: ElementRef;
  searchQuery$: Observable<any>;
  @Input() events: BehaviorSubject<void | true> = new BehaviorSubject<undefined | true>(undefined);
  @Output() setCollectionPointEmit: EventEmitter<any> = new EventEmitter<any>();
  eventsSubscription: any;
  listViewType = 'list';
  hasResults = true;
  cisList$: Observable<any>;
  iconTypes = ICON_TYPE;
  cisModalRef: NgbModalRef;

  cisModalOptions: NgbModalOptions = {
    windowClass: 'checkout-cis-modal modal--find-location',
    ariaLabelledBy: 'modal-cis'
  };
  findCisForm: FormGroup = this.getFindCisForm();
  mapIcons = {
    currentLocation: currentLocationPin,
    primary: primaryPin,
    primaryInactive: primaryInactivePin,
    secondary: secondaryPin,
    secondaryInactive: secondaryInactivePin
  };
  constructor(
    private locationService: LocationService,
    protected modalService: ModalService,
    private geocodeService: GeocodeService,
    private ref: ChangeDetectorRef,
    private cisService: CheckoutCollectInStoreService,
    private checkoutDeliveryService: CustomCheckoutDeliveryService,
    public collectionPointsService: CollectionPointsService
  ) {}

  ngOnInit() {
    this.searchQuery$ = this.cisService.getSearchQueryFromState();
    this.cisList$ = this.cisService.getCollectionStoresFromState();
    this.eventsSubscription = this.events.subscribe(data => {
      if (!isUndefined(data)) {
        this.openCisModal();
      }
    });
    this.subToSearchQuery();
  }
  ngOnDestroy() {
    if (this.eventsSubscription) {
      this.eventsSubscription.unsubscribe();
    }
    this.ref.detach();
  }
  getFindCisForm() {
    return new FormGroup({
      location: new FormControl()
    });
  }

  onSubmit(ev) {
    ev.preventDefault();
    this.cisService.setSearchQuery(this.findCisForm.value.location);
    this.displayListOfCollectionPoints(this.findCisForm.value.location, '', '');
  }

  displayListOfCollectionPoints(query, lat, long) {
    this.cisService.getListOfCollectionStores(query, lat, long).subscribe(data => {
      if (!data.stores.length) {
        this.hasResults = false;
        this.triggerChanges();
      } else {
        if (query !== '') {
          this.addressToCoordinates(query);
          this.cisService.setSearchQuery(query);
        }
        if (lat !== '' && long !== '') {
          this.location = {
            lat: lat,
            lng: long
          };
          this.setMapCenter(lat, long);
        }
        this.cisService.setSearchResults(data.stores);
        this.hasResults = true;
        this.modalService.closeActiveModal('');
        this.openCisModal();
        this.triggerChanges();
      }
    });
  }
  getGeolocation() {
    if ('geolocation' in navigator) {
      this.locationService.getPosition().then(pos => {
        this.cisService.setSearchQuery('');
        this.displayListOfCollectionPoints('', pos.lat, pos.lng);
      });
    }
  }

  openCisModal() {
    this.cisModalRef = this.modalService.open(this.cisModal, this.cisModalOptions);
  }
  closeCisModal() {
    if (this.cisModalRef) {
      this.cisModalRef.close();
    }
  }
  toggleListView(type) {
    this.listViewType = type;
    this.storeInfo = null;
    this.unsetActivePin();
    this.setMapZoom(12);
  }
  addressToCoordinates(address) {
    this.geocodeService.geocodeAddress(address).subscribe((location: LatLngLiteral) => {
      this.location = location;
      this.setMapCenter(location.lat, location.lng);
    });
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
    this.cisList$.subscribe(data => {
      data.map((el: CollectPoint) => {
        el.isActive = false;
      });
    });
  }
  onInfoCloseEmit() {
    if (this.isInfoTriggered) {
      this.toggleListView('list');
      this.isInfoTriggered = false;
    }

    this.unsetActivePin();
    this.storeInfo = null;
  }
  onInfoEmit($ev) {
    this.displayLocationOnMap($ev);
  }

  onSelectCollectionPointEmit($ev) {
    this.cisService.postCollectionStore($ev).subscribe(data => {
      this.checkoutDeliveryService.clearDeliveryAddressTab();
      this.collectionPointsService.unsetCollectionPoint();
      this.cisService.setCollectionStore(data);
      this.closeCisModal();
    });
  }

  displayLocationOnMap(collectionPoint: CollectPoint) {
    this.listViewType = 'map';
    this.isInfoTriggered = true;

    this.markerClicked(collectionPoint);
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
  triggerChanges() {
    if (!this.ref['destroyed']) {
      this.ref.detectChanges();
    }
  }
  subToSearchQuery() {
    this.searchQuery$.subscribe(val => {
      if (!isUndefined(val)) {
        if (this.findCisForm.get('location').value !== val) {
          this.findCisForm.get('location').setValue(val);
        }
      }
    });
  }
}
