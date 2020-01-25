import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs';
import { CustomCartService } from 'src/app/services/cart/facade/cart.service';
import { CollectionPointsService } from 'src/app/services/checkout/collection-points/collection-points.service';
import { isEmpty } from 'lodash';

@Component({
  selector: 'cx-checkout-collection-point',
  templateUrl: './custom-checkout-collection-point.component.html'
})
export class CustomCheckoutCollectionPointComponent implements OnInit {
  storeInfo$: Observable<any>;
  searchQuery = '';
  eventsSubject: BehaviorSubject<void | true> = new BehaviorSubject<void | true>(undefined);
  @Output() shippingAddressStateEmit: EventEmitter<boolean> = new EventEmitter<boolean>();
  constructor(public customCartService: CustomCartService, public collectionPointsService: CollectionPointsService) {}
  ngOnInit() {
    this.storeInfo$ = this.collectionPointsService.getSelectedCollectionPoint();
    this.setCollectionPointSub();
  }

  onChangeCollectionPoint() {
    this.eventsSubject.next(true);
  }
  setCollectionPointSub() {
    this.storeInfo$.subscribe(collectionPoint => {
      if (!isEmpty(collectionPoint)) {
        this.shippingAddressStateEmit.emit(true);
      } else {
        this.shippingAddressStateEmit.emit(false);
      }
    });
  }
}
