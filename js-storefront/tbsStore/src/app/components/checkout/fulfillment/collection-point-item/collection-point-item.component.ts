import { Component, OnInit, Input, Output, EventEmitter, ElementRef, ViewChild, OnChanges } from '@angular/core';
import { CollectPoint } from 'src/app/interfaces/collection-point.interface';
import { ICON_TYPE, ModalService } from '@spartacus/storefront';
import { NgbModalRef, NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs';

import { LatLngLiteral } from '@agm/core';
import { Router } from '@angular/router';
import { CollectionPointsService } from 'src/app/services/checkout/collection-points/collection-points.service';
import { CheckoutCollectInStoreService } from 'src/app/services/checkout/collect-in-store/collect-in-store.service';

const secondaryPin = require('src/assets/images/maps-pins/secondary.svg') as string;

@Component({
  selector: 'app-collection-point-item',
  templateUrl: './collection-point-item.component.html'
})
export class CollectionPointItemComponent implements OnInit {
  @Input() data: CollectPoint;
  @Input() dataType: string;
  @Input() showOpeningHrs = false;
  @Input() showInfoBtn = true;
  @Input() showInfoModalBtn = false;
  @Input() showChangeBtn = false;
  @Input() showUnavailableBtn = false;
  @Input() showStockStatus = false;
  @Input() showSelectBtn = false;
  @Input() showDirectionsBtn = false;
  @Input() showStoreDetailsBtn = false;
  @Input() searchedLocation: LatLngLiteral;
  @Output() displayInfoEmit: EventEmitter<CollectPoint> = new EventEmitter<CollectPoint>();
  @Output() selectCollectionPointEmit: EventEmitter<CollectPoint> = new EventEmitter<CollectPoint>();
  @Output() selectCollectInStoreEmit: EventEmitter<CollectPoint> = new EventEmitter<CollectPoint>();
  @Output() changeCollectionPointEmit: EventEmitter<CollectPoint> = new EventEmitter<CollectPoint>();
  iconTypes = ICON_TYPE;
  collectionPoint$: Observable<any>;
  collectInStore$: Observable<any>;
  mapIcons = {
    secondary: secondaryPin
  };
  currentRoute: string;

  @ViewChild('collectPointInfoModal', { static: false })
  collectPointInfoModal: ElementRef;
  collectPointInfoModalRef: NgbModalRef;
  collectPointModalInfoOptions: NgbModalOptions = {
    windowClass: 'checkout-collection-point--info-modal modal--find-location',
    ariaLabelledBy: 'modal-collection--info-point'
  };
  @ViewChild('collectInStoreInfoModal', { static: false })
  collectInStoreInfoModal: ElementRef;
  collectInStoreInfoModalRef: NgbModalRef;
  collectInStoreInfoModalOptions: NgbModalOptions = {
    windowClass: 'checkout-collection-point--info-modal modal--find-location',
    ariaLabelledBy: 'modal-collect-in-store--info-point'
  };

  constructor(
    protected modalService: ModalService,
    public collectionPointsService: CollectionPointsService,
    public collectInStoreService: CheckoutCollectInStoreService,
    public router: Router
  ) {
    this.currentRoute = this.router.routerState.snapshot.url;
  }

  ngOnInit() {
    this.collectionPoint$ = this.collectionPointsService.getSelectedCollectionPoint();
    this.collectInStore$ = this.collectInStoreService.getSelectedCollectionStore();
  }
  displayInfo(data: CollectPoint) {
    this.displayInfoEmit.emit(data);
  }
  selectPoint(data: CollectPoint, dataType: string) {
    if (dataType === 'cis') {
      this.selectCollectInStoreEmit.emit(data);
    } else {
      this.selectCollectionPointEmit.emit(data);
    }
  }
  changePoint() {
    this.changeCollectionPointEmit.emit();
  }
  displayInfoModal() {
    if (this.dataType === 'cis') {
      this.collectInStoreInfoModalRef = this.modalService.open(
        this.collectInStoreInfoModal,
        this.collectInStoreInfoModalOptions
      );
    } else {
      this.collectPointInfoModalRef = this.modalService.open(
        this.collectPointInfoModal,
        this.collectPointModalInfoOptions
      );
    }
  }
  closeCollectPointInfoModal() {
    this.collectPointInfoModalRef.close();
  }
  closeCollectInStoreInfoModal() {
    this.collectInStoreInfoModalRef.close();
  }
}
