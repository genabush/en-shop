/* tslint:disable */
import { Component, Input, Output, EventEmitter } from '@angular/core';

// spartacus
import { ICON_TYPE } from '@spartacus/storefront';
import { Currency } from '@spartacus/core';

// vendor
import { BehaviorSubject, Observable, ReplaySubject, Subscription, of } from 'rxjs';
import { DUMMMY_CART_CMS_DATA } from './mock.constants';

export class MockCmsComponentData {
  data$ = new ReplaySubject();
}

export class CartMockCmsComponentData {
  data$ = new ReplaySubject();
  constructor() {
    this.data$.next(DUMMMY_CART_CMS_DATA);
  }
}

export class MockStore<T> {
  private state: BehaviorSubject<T> = new BehaviorSubject(undefined);
  setState(data: T) {
    this.state.next(data);
  }
  select(selector?: any): Observable<T> {
    return this.state.asObservable();
  }
  dispatch(action: any) {}
  pipe(func) {
    return of({});
  }
}

@Component({
  selector: 'cx-media',
  template:
    '<div class="d-flex w-100">' +
    '<a class="cx-product-image-container" href="#">' +
    '<div *ngIf="product?.images" class="cx-product-image is-initialized" [class.is-missing]="product.images.PRIMARY === \'undefined\'" format="product" ng-reflect-format="product">' +
    '</div>' +
    '</a></div>'
})
export class MockCxMediaComponent {
  @Input() container;
  @Input() alt;
}

@Component({
  selector: 'cx-icon',
  template: `
    <div>X</div>
  `
})
export class MockCxIconComponent {
  @Input() type: ICON_TYPE;
}

@Component({
  selector: 'cx-spinner',
  template: `
    <div>*</div>
  `
})
export class MockCxSpinnerComponent {}

@Component({
  selector: 'cx-generic-link',
  template: '<a href="#">test component link</a>'
})
export class MockCxGenericLinkComponent {
  @Input() url: string | any[];
  @Input() target: string;
}

@Component({
  selector: 'cx-star-rating',
  template: '<div></div>'
})
export class MockCxStartRatingComponent {
  @Input() rating;
  @Input() disabled;
}

@Component({
  selector: 'cx-add-to-cart',
  template: '<div></div>'
})
export class MockCxAddToCartComponent {
  @Input() showQuantity;
  @Input() productCode;
  @Input() isCart = false;
  @Input() variant;
  @Input() hideFav;
}

export declare class MockCurrencyService {
  getAll(): Observable<Currency[]>;
  getActive(): Observable<string>;
  setActive(isocode: string): Subscription;
  initialize(): void;
}

export declare abstract class MockSiteContextConfig {
  context?: {
    urlParameters?: string[];
    [contextName: string]: string[];
  };
}

// -------------------------------------------------- PLP
export class MockPageLayoutService {
  templateName$ = new ReplaySubject();
}

export class MockCustomProductListComponentService {
  clearSearchResults() {}
  viewPage() {}
  sort() {}
}

@Component({
  selector: 'app-product-grid-item',
  template: '<div style="border:solid 1px;width:100%;min-height:1000px;"></div>'
})
export class MockProductGridItemComponent {
  @Input() product;
  @Input() guestFirst: boolean;
}

@Component({
  selector: 'app-amplience',
  template: '<div></div>'
})
export class MockAmplienceComponent {
  @Input() amplienceInput;
}
@Component({
  selector: 'cx-pagination',
  template: '<div></div>'
})
export class MockCxPaginationComponent {
  @Input() pagination;
  @Output() viewPageEvent = new EventEmitter();
}

@Component({
  selector: 'cx-product-view',
  template: '<div></div>'
})
export class MockCxProductViewComponent {
  @Input() mode;
  @Output() modeChange = new EventEmitter();
}

@Component({
  selector: 'app-results-count',
  template: '<div></div>'
})
export class MockResultCountComponent {
  @Input() paginationData;
}

@Component({
  selector: 'app-slider',
  template: '<div></div>'
})
export class MockSliderComponent {
  @Input() slides;
  @Input() configs;
}

@Component({
  selector: 'app-mock-routed',
  template: '<h1>navigated</h1>'
})
export class MockRoutedComponent {}

export class MockChangeDetectorRef {
  markForCheck() {
    return;
  }
}

@Component({
  selector: 'app-back-in-stock',
  template: '<div></div>'
})
export class MockAppBackInStockComponent {
  @Input() productCode;
}

@Component({
  template: '',
  selector: 'cx-cart-quantity-selector'
})
export class MockItemCounterComponent {
  @Input() step;
  @Input() min;
  @Input() max;
  @Input() cartIsLoading;
  @Input() value;
  @Output() update = new EventEmitter();
}

@Component({
  selector: 'cx-added-to-cart-dialog',
  template: '<div></div>'
})
export class AddedToCartDialogComponent {}

@Component({
  selector: 'app-adyen-credit-cards',
  template: '<div></div>'
})
export class MockCAdyenCreditCardsComponent {
  @Input() paymentMethod;
  @Output() creditCarDetails = new EventEmitter();
  @Output() hasSavedCardChecked = new EventEmitter();
}
@Component({
  selector: 'app-adyen-saved-credit-cards',
  template: '<div></div>'
})
export class MockCAdyenSavedCreditCardsComponent {
  @Input() savedPaymentMethod;
  @Output() savedCreditCarDetails = new EventEmitter();
}
@Component({
  selector: 'app-gift-cards',
  template: '<div></div>'
})
export class MockGiftCardsComponent {
  @Input() config;
  @Input() showAddButton;
}

@Component({
  selector: 'cx-billing-address-form',
  template: '<div></div>'
})
export class MockCxBillingAddressFormComponent {
  @Input() billingAddress;
  @Input() countries$;
}
@Component({
  selector: 'app-map-info-alt-window',
  template: '<div></div>'
})
export class MockMapInfoAltWindowComponent {
  @Input() storeInfo;
  @Input() dataType;
}
@Component({
  selector: 'app-map-info-window',
  template: '<div></div>'
})
export class MockMapInfoWindowComponent {
  @Input() storeInfo;
  @Input() showUnavailableBtn;
  @Input() dataType;
}

@Component({
  selector: 'app-paypal',
  template: `
    <script></script>
  `
})
export class MockPaypalComponent {}

@Component({
  selector: 'app-collection-point-item',
  template: '<div></div>'
})
export class MockCollectionPointComponent {
  @Input() data;
  @Input() showInfoModalBtn;
  @Input() showInfoBtn;
  @Input() showChangeBtn;
  @Input() showUnavailableBtn;
  @Input() showOpeningHrs;
}
@Component({
  selector: 'app-promotion-free-delivery',
  template: '<div></div>'
})
export class MockPromotionFreeDeliveryComponent {
  @Input()
  promotions;
  @Input()
  minibasket;
  @Input()
  totals;
}
