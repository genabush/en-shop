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
import { CollectionPointsService } from 'src/app/services/checkout/collection-points/collection-points.service';
import { isUndefined } from 'lodash';
import { CheckoutCollectInStoreService } from 'src/app/services/checkout/collect-in-store/collect-in-store.service';

const currentLocationPin = require('src/assets/images/maps-pins/current-location.svg') as string;
const primaryPin = require('src/assets/images/maps-pins/primary.svg') as string;
const primaryInactivePin = require('src/assets/images/maps-pins/primary-inactive.svg') as string;
const secondaryPin = require('src/assets/images/maps-pins/secondary.svg') as string;
const secondaryInactivePin = require('src/assets/images/maps-pins/secondary-inactive.svg') as string;

@Component({
  selector: 'app-custom-address-finder',
  templateUrl: './custom-address-finder.component.html',
  styleUrls: ['./custom-address-finder.component.scss']
})
export class CustomAddressFinderComponent implements OnInit, OnDestroy {
  showInfoWindow = false;
  isInfoTriggered = false;
  location: LatLngLiteral;
  mapCenter: LatLngLiteral;
  mapZoom = 12;
  storeInfo: CollectPoint;
  @ViewChild('collectPointModal', { static: false })
  collectPointModal: ElementRef;
  searchQuery$: Observable<any>;
  @Input() events: BehaviorSubject<void | true> = new BehaviorSubject<undefined | true>(undefined);
  @Output() setCollectionPointEmit: EventEmitter<any> = new EventEmitter<any>();
  eventsSubscription: any;
  listViewType = 'list';
  hasResults = true;
  collectionPointsList$: Observable<any>;
  iconTypes = ICON_TYPE;
  collectPointModalRef: NgbModalRef;
  collectPointModalOptions: NgbModalOptions = {
    windowClass: 'checkout-collection-point-modal modal--find-location',
    ariaLabelledBy: 'modal-collection-points'
  };
  findCollectionPoint: FormGroup = this.getFindCollectionPoint();
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
    private collectionPointsService: CollectionPointsService,
    private collectInStoreService: CheckoutCollectInStoreService
  ) {}

  ngOnInit() {
    this.searchQuery$ = this.collectionPointsService.getSearchQueryFromState();
    this.collectionPointsList$ = this.collectionPointsService.getCollectionPointsFromState();
    this.eventsSubscription = this.events.subscribe(data => {
      if (!isUndefined(data)) {
        this.openCollectionPointModal();
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
  getFindCollectionPoint() {
    return new FormGroup({
      location: new FormControl()
    });
  }

  onSubmit(ev) {
    ev.preventDefault();
    this.collectionPointsService.setSearchQuery(this.findCollectionPoint.value.location);
    this.displayListOfCollectionPoints(this.findCollectionPoint.value.location, '', '');
  }

  displayListOfCollectionPoints(query, lat, long) {
    this.collectionPointsService.getListOfCollectionPoints(query, lat, long).subscribe(data => {
      if (data.noresults) {
        this.hasResults = !data.noresults;
        this.triggerChanges();
      } else {
        if (query !== '') {
          this.addressToCoordinates(query);
          this.collectionPointsService.setSearchQuery(query);
        }
        if (lat !== '' && long !== '') {
          this.location = {
            lat: lat,
            lng: long
          };
          this.setMapCenter(lat, long);
        }
        this.collectionPointsService.setSearchResults(data.collectionPoint);

        this.hasResults = !data.noresults;
        this.modalService.closeActiveModal('');
        this.openCollectionPointModal();
        this.triggerChanges();
      }
    });
  }
  getGeolocation() {
    if ('geolocation' in navigator) {
      this.locationService.getPosition().then(pos => {
        this.collectionPointsService.setSearchQuery('');
        this.displayListOfCollectionPoints('', pos.lat, pos.lng);
      });
    }
  }

  openCollectionPointModal() {
    this.collectPointModalRef = this.modalService.open(this.collectPointModal, this.collectPointModalOptions);
  }
  closeCollectPointModal() {
    if (this.collectPointModalRef) {
      this.collectPointModalRef.close();
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
    this.collectionPointsList$.subscribe(data => {
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
    this.collectionPointsService.postCollectionPoint($ev).subscribe(data => {
      this.collectInStoreService.unsetCollectionStore();
      this.collectionPointsService.setCollectionPoint(data);
      this.closeCollectPointModal();
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
        if (this.findCollectionPoint.get('location').value !== val) {
          this.findCollectionPoint.get('location').setValue(val);
        }
      }
    });
  }
}
