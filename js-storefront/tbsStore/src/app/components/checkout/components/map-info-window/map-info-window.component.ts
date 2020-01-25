import { Component, OnInit, Input, OnChanges, Output, EventEmitter } from '@angular/core';

import { ICON_TYPE } from '@spartacus/storefront';
import { CollectPoint } from 'src/app/interfaces/collection-point.interface';

import { LatLngLiteral } from '@agm/core';
import { CheckoutCollectInStoreService } from 'src/app/services/checkout/collect-in-store/collect-in-store.service';
import { CollectionPointsService } from 'src/app/services/checkout/collection-points/collection-points.service';

@Component({
  selector: 'app-map-info-window',
  templateUrl: './map-info-window.component.html',
  styleUrls: ['./map-info-window.component.scss']
})
export class MapInfoWindowComponent {
  @Input() storeInfo: CollectPoint;
  @Input() showStoreDetailsBtn = false;
  @Input() showDirectionsBtn = false;
  @Input() showStockStatus = false;
  @Input() showUnavailableBtn = false;
  @Input() searchedLocation: LatLngLiteral;
  @Input() dataType: string;
  @Output() closeInfoEmit: EventEmitter<any> = new EventEmitter<any>();
  @Output() setCollectionPointEmit: EventEmitter<any> = new EventEmitter<any>();
  @Output() setCollectInStoreEmit: EventEmitter<any> = new EventEmitter<any>();
  iconTypes = ICON_TYPE;
  constructor(
    private collectionPointsService: CollectionPointsService,
    private collectInStoreService: CheckoutCollectInStoreService
  ) {}

  closeInfoWindow() {
    this.closeInfoEmit.emit();
  }

  onSelectCollectionPointEmit($ev) {
    this.collectionPointsService.postCollectionPoint($ev).subscribe(data => {
      this.setCollectionPointEmit.emit(data);
    });
  }
  onSelectInStoreEmit($ev) {
    this.collectInStoreService.postCollectionStore($ev).subscribe(data => {
      this.setCollectInStoreEmit.emit(data);
    });
  }
}
