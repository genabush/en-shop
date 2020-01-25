import { Observable, of, BehaviorSubject, Subscription, EMPTY } from 'rxjs';
import {
  Cart,
  Product,
  PageMeta,
  StateWithSiteContext,
  SiteContextConfig,
  Language,
  AuthRedirectService,
  ClientToken
} from '@spartacus/core';
import {
  DUMMY_CART_ENTRIES,
  DUMMY_ACTIVE_CART,
  DUMMY_PLP_CUSTOM_PRODUCT_COLOUR_VARIANT_IN_STOCK,
  MOCK_DUMMY_SEARCH_MODEL,
  DUMMY_WISHLIST
} from './mock.constants';
import { ICustomProduct } from '../interfaces/custom-product-item.interface';
import { CheckoutStep, CheckoutStepType } from '@spartacus/storefront';
import { ActivatedRoute } from '@angular/router';
import { Injectable } from '@angular/core';
import { Type } from '@angular/compiler';
import { Route } from '@angular/compiler/src/core';
import { GigyaRaasConfig, MockComponentTypes, PaypalConfig } from 'src/environments/environment.interface';
import { Store } from '@ngrx/store';

export class MockCurrentProductService {
  getProduct(): Observable<ICustomProduct | Product> {
    return of(DUMMY_PLP_CUSTOM_PRODUCT_COLOUR_VARIANT_IN_STOCK);
  }
  getProductDetails(code?: string): Observable<ICustomProduct | Product> {
    return of(DUMMY_PLP_CUSTOM_PRODUCT_COLOUR_VARIANT_IN_STOCK);
  }
  getSelectedSwatch() {}
  doesProductHasSwatch() {}
  isContainingColour() {
    return true;
  }
}

export class MockContentPageSearchService {
  searchContentPages() {
    return of(MOCK_DUMMY_SEARCH_MODEL);
  }
}

export class MockPageMetaService {
  getMeta(): Observable<PageMeta> {
    return of(<PageMeta>{
      title: 'Test title',
      description: 'Test description'
    });
  }
}

@Injectable()
export class MockCustomCartService {
  constructor() {}
  getActive(): Observable<Cart> {
    return of(DUMMY_ACTIVE_CART);
  }
  getActiveCartCode() {
    return of(DUMMY_ACTIVE_CART.code);
  }
  getEntries() {
    return of(DUMMY_CART_ENTRIES);
  }
  addEntry(param1, param2, param3) {
    return;
  }
  getEntry() {
    return of({});
  }
  getLoaded(): Observable<boolean> {
    return of(true);
  }
  getUserId() {
    return '123';
  }
  getCartMergeComplete() {
    return of(false);
  }
  getLoyaltyVouchers() {
    return EMPTY;
  }
  getAllPromotions() {
    return of([]);
  }
}

export class MockBaseSiteService {
  getActive() {
    return of('thebodyshop-uk');
  }
  setActive() {
    return;
  }
  getBaseSiteData() {
    return of({});
  }
}

export class MockLanguageService {
  protected store: Store<StateWithSiteContext>;
  protected config: SiteContextConfig;
  private sessionStorage;
  getAll(): Observable<Language[]> {
    return of();
  }
  getActive(): Observable<string> {
    return of('test');
  }
  setActive(isocode: string): Subscription {
    return new Subscription();
  }
  initialize(): void {}
}

@Injectable()
export class MockTranslationService {
  translate(param, param2) {
    return of('test');
  }
}

export class MockRoutingService {
  isNavigating() {
    return of({});
  }
  go(data) {}
}

@Injectable()
export class MockRoutingConfigService {}

export class MockRoutingConfig {}

export class MockCheckoutConfig {}

export class MockCheckoutDeliveryService {
  getSupportedDeliveryModes() {
    return of([]);
  }
  getSelectedDeliveryMode() {
    return of({});
  }
  getApiSupportedDeliveryModes() {
    return of({});
  }
}

@Injectable()
export class MockCustomDeliveryService {
  getSelectedDeliveryMode() {
    return of();
  }
  getDeliveryAddress() {
    return;
  }
  getApiSupportedDeliveryModes() {
    return;
  }
  getAddressVerificationResults() {
    return of({
      decision: 'ACCEPT'
    });
  }
  getApiProductDeliveryRestriction() {
    return of({ restrictedProducts: [] });
  }
  clearAddressVerificationResults() {
    return;
  }
  setDeliveryAddress() {
    return;
  }
}

@Injectable()
export class MockCheckoutConfigService {
  private checkoutConfig;
  private routingConfigService;
  steps: CheckoutStep[];
  constructor() {}
  getCheckoutStep(currentStepType: CheckoutStepType): CheckoutStep | any {
    return;
  }
  getNextCheckoutStepUrl(activatedRoute: ActivatedRoute): string {
    return '';
  }
  getPreviousCheckoutStepUrl(activatedRoute: ActivatedRoute): string {
    return '';
  }
  getCurrentStepIndex(activatedRoute: ActivatedRoute): number {
    return 0;
  }
}

export class MockGlobalMessagingService {}

export class MockRouterService {
  events: BehaviorSubject<any> = new BehaviorSubject<any>(null);
  navigate() {}
  navigateByUrl() {}
  subscribe() {
    return of({});
  }
}

@Injectable()
export class MockCartService {
  getActive() {
    return of({});
  }
  getCartMergeComplete() {
    return of(false);
  }
}
@Injectable()
export class MockCustomCartVoucherService {
  customApiRemoveVoucher() {
    return of({});
  }
  resetAddVoucherProcessingState() {}
  getAddVoucherResultLoading() {
    return of(false);
  }
  applyVoucherState() {}
  getCartVouchersState() {
    return of({
      appliedVouchers: []
    });
  }
}

export class MockCartDataService {}

export class MockUserService {
  getTitles() {
    return of();
  }
  loadTitles() {
    return of();
  }
}

export class MockAuthService {
  logout() {
    return;
  }
  getUserToken() {
    return of({});
  }
  getClientToken() {
    return of({});
  }
}

export class MockVariantService {
  getSelectedVariant() {
    return of({ code: '123456', maxOrderProductQuantity: 10, stock: { stockLevelStatus: 'inStock', stockLevel: 20 } });
  }
}

export class MockWishlistsService {
  getUserWishlists() {
    return of([
      {
        wishlistId: '1234567890',
        wishlistName: 'Xmas 2019',
        created: '27 Nov 2019'
      }
    ]);
  }
  createNew() {
    return of({
      wishlistId: '1234567890',
      wishlistName: 'Summer 2020',
      created: '14 Dec 2019'
    });
  }
  initAllWishlists() {
    return of({
      wishlistId: '1234567890',
      wishlistName: 'Summer 2020',
      created: '14 Dec 2019'
    });
  }
  guestGetItemInWishlist() {
    return of({});
  }
  getFirstTimer() {
    return of({});
  }
  sendFirstTimer() {}
  isProductInWishlists() {
    return of({
      wishlistId: '1234567890',
      wishlistName: 'Summer 2020',
      created: '14 Dec 2019'
    });
  }
  guestAddToWishlist() {}
  guestSetToSessionWishlist() {}
  getItemExistingProductInGuestWishlist() {}
  createsOrAddsProductToList() {}

  getWishlistById() {}
  getWishlistProducts() {
    return of();
  }
}

export class MockUserPaymentService {
  getAllBillingCountries() {
    return of({});
  }
  loadBillingCountries() {}
}
export class MockCustomCheckoutService {
  makeAdyenCreditCardPayment(data) {
    return of({});
  }
  getAdyenConfiguration() {
    return of({});
  }
  getLoyaltyVouchers() {
    return EMPTY;
  }
}

export class MockGeocodeService {
  geocodeAddress() {
    return of({});
  }
}
export class MockCollectionPointsService {
  getSelectedCollectionPoint() {
    return of({});
  }
  getSearchQueryFromState() {
    return of({});
  }
  getCollectionPointsFromState() {
    return of({});
  }
}

export class MockStoreConfigService {
  getPaypalConfiguration(): PaypalConfig {
    return null;
  }

  isPaymentVisible(paymentMode: string): boolean {
    return true;
  }

  getGigyaConfiguration(): GigyaRaasConfig {
    return {
      apiKey: 'apiKey',
      gigyaDataCenter: 'datacenter',
      enableDebug: true
    };
  }
}

export class MockAuthRedirectService {
  redirect(): void {}
  reportAuthGuard(): void {}
  reportNotAuthGuard(): void {}
}
