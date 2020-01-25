import { Component, OnInit, Output, EventEmitter } from '@angular/core';

// vendor
import { BehaviorSubject, Observable } from 'rxjs';
import { isEmpty } from 'lodash';

// services
import { CheckoutCollectInStoreService } from 'src/app/services/checkout/collect-in-store/collect-in-store.service';

@Component({
  selector: 'cx-checkout-collect-in-store',
  templateUrl: './custom-checkout-collect-in-store.component.html'
})
export class CustomCheckoutCollectInStoreComponent implements OnInit {
  cisInfo$: Observable<any>;
  searchQuery = '';
  eventsSubject: BehaviorSubject<void | true> = new BehaviorSubject<void | true>(undefined);
  @Output() shippingAddressStateEmit: EventEmitter<boolean> = new EventEmitter<boolean>();
  constructor(private collectInStoreService: CheckoutCollectInStoreService) {}

  ngOnInit() {
    this.cisInfo$ = this.collectInStoreService.getSelectedCollectionStore();
    this.setCollectionPointSub();
  }

  onChangeCollectionPoint() {
    this.eventsSubject.next(true);
  }

  setCollectionPointSub() {
    this.cisInfo$.subscribe(cis => {
      if (!isEmpty(cis)) {
        this.shippingAddressStateEmit.emit(true);
      } else {
        this.shippingAddressStateEmit.emit(false);
      }
    });
  }
}
