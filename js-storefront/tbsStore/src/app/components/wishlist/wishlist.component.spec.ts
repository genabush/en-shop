// angular
import { Router } from '@angular/router';
import { By } from '@angular/platform-browser';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatInputModule, MatSelectModule } from '@angular/material';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';

// sparta & mocks
import { ICON_TYPE, ModalService } from '@spartacus/storefront';
import {
  OccConfig,
  BaseSiteService,
  UrlModule,
  RoutingConfig,
  TranslationService,
  MockDatePipe
} from '@spartacus/core';

// state
import { of, BehaviorSubject, Observable } from 'rxjs';
import { Store } from '@ngrx/store';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

// services
import { WishlistService } from './../../services/wishlist/wishlist.service';
import {
  MockWishlistsService,
  MockBaseSiteService,
  MockRoutingConfig,
  MockCustomCartService,
  MockCurrentProductService,
  MockTranslationService
} from './../../testing/mock.services';

// comps
import { WishlistComponent } from './wishlist.component';
import {
  MockCxIconComponent,
  MockCxMediaComponent,
  MockAppBackInStockComponent
} from 'src/app/testing/mock.components';

// pipes
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';
import { DUMMY_WISHLIST } from 'src/app/testing/mock.constants';
import { Wishlists } from 'src/app/interfaces/wishlists.interface';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MockOccConfig } from 'src/app/testing/occ/occ.config.mock';
import { WishlistIntroComponent } from './wishlist-intro/wishlist-intro.component';
import { WishlistListsComponent } from './wishlist-lists/wishlist-lists.component';
import { WishlistProductItemComponent } from './wishlist-product-item/wishlist-product-item.component';
import { WishlistProductsListingComponent } from './wishlist-products-listing/wishlist-products-listing.component';
import { AddToCartComponent } from '../cart/add-to-cart/add-to-cart.component';
import { FindInStoreComponent } from '../find-in-store/find-in-store.component';
import { CollectionPointItemModule } from '../checkout/fulfillment/collection-point-item/collection-point-item.module';
import { AgmCoreModule } from '@agm/core';
import { CustomCartService } from 'src/app/services/cart/facade/cart.service';
import { CurrentProductService } from '../product/current-product.service';
import { WishlistProductsListingRefineComponent } from './wishlist-products-listing-refine/wishlist-products-listing-refine.component';
import { CustomQuantitySelectorComponent } from '../shared/custom-quantity-selector/custom-quantity-selector.component';
import { WishlistFormComponent } from './wishlist-form/wishlist-form.component';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { MessagingComponent } from '../shared/messaging/messaging.component';

class MockStore<T> {
  private state: BehaviorSubject<T> = new BehaviorSubject(undefined);
  setState(data: T) {
    this.state.next(data);
  }
  select(selector?: any): Observable<T> {
    return this.state.asObservable();
  }
  dispatch(action: any) {}
}

export class MockRouterService {
  events: BehaviorSubject<any> = new BehaviorSubject<any>(null);
  navigate() {}
  navigateByUrl() {}
  subscribe() {
    return of({});
  }
}

describe('WishlistComponent', () => {
  let component: WishlistComponent;
  let fixture: ComponentFixture<WishlistComponent>;
  let wishlistService: WishlistService;
  let router: Router;
  let routerSpy: jasmine.Spy;
  let getUserWishlistsSpy: jasmine.Spy;
  let wishlistsSpy;
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        NgbModule,
        FormsModule,
        ReactiveFormsModule,
        MatInputModule,
        HttpClientTestingModule,
        RouterTestingModule,
        UrlModule,
        BrowserAnimationsModule,
        CollectionPointItemModule,
        AgmCoreModule,
        MatSelectModule
      ],
      declarations: [
        AddToCartComponent,
        MockCxMediaComponent,
        MessagingComponent,
        WishlistComponent,
        MockCxTranslatePipe,
        MockCxIconComponent,
        WishlistFormComponent,
        WishlistIntroComponent,
        WishlistListsComponent,
        WishlistProductsListingComponent,
        WishlistProductItemComponent,
        MockAppBackInStockComponent,
        FindInStoreComponent,
        CustomQuantitySelectorComponent,
        WishlistProductsListingRefineComponent,
        MockDatePipe
      ],
      providers: [
        { provide: OccConfig, useClass: MockOccConfig },
        { provide: BaseSiteService, useClass: MockBaseSiteService },
        { provide: Store, useClass: MockStore },
        { provide: WishlistService, useClass: MockWishlistsService },
        { provide: RoutingConfig, useClass: MockRoutingConfig },
        { provide: CurrentProductService, useClass: MockCurrentProductService },
        { provide: CustomCartService, useClass: MockCustomCartService },
        { provide: TranslationService, useClass: MockTranslationService },
        {
          provide: ModalService,
          useClass: ModalService
        }
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WishlistComponent);
    component = fixture.componentInstance;
    router = fixture.componentRef.injector.get(Router);
    wishlistService = fixture.componentRef.injector.get(WishlistService);
    routerSpy = spyOn(router, 'navigateByUrl').and.returnValue(Promise.resolve(true));
    fixture.detectChanges();
  });

  it('should CREATE', async(() => {
    fixture.detectChanges();
    fixture.whenStable().then(() => {
      fixture.detectChanges();
      expect(component).toBeTruthy();
    });
  }));

  describe('Given that I have existing wishlists', () => {
    describe('Given that I am a logged in customer', () => {
      beforeEach(() => {
        component.ngOnInit();
        component.guestListEmpty = false;
        component.isUserLogged = true;
        component.wishlistService.getUserWishlists().subscribe(data => {
          component.wishlists$ = data;
        });
        fixture.detectChanges();
      });
      it('should navigate to the wishlist detail page on click of a list name', async(() => {
        fixture.detectChanges();
        const wishlistName = fixture.debugElement.query(By.css('.wishlists__link')).nativeElement;
        const url = wishlistName.getAttribute('href');
        fixture.whenStable().then(() => {
          fixture.detectChanges();
          expect(url).toEqual('my-account/wishlist/1234567890');
        });
      }));
    });
  });
  describe('Given that I have NO existing wishlists', () => {
    describe('Given that I am a logged in customer', () => {
      beforeEach(() => {
        getUserWishlistsSpy = spyOn(wishlistService, 'getUserWishlists').and.returnValue(of([]));
        component.ngOnInit();
        component.guestListEmpty = false;
        component.isUserLogged = true;
        component.listEmpty = true;
        fixture.detectChanges();
      });
      xit('should open the create modal on click of create cta, with input, cancel and save cta', async(() => {
        fixture.detectChanges();
        fixture.whenStable().then(() => {
          fixture.detectChanges();
          const createWishlistCta = fixture.debugElement.query(By.css('.wishlists__cta'));
          createWishlistCta.nativeElement.dispatchEvent(new MouseEvent('click', null));
          fixture.whenStable().then(() => {
            fixture.detectChanges();
            const modalCreateWishlist = document.querySelector('.modal__body--create-wishlist');
            // opens modal
            expect(modalCreateWishlist).toBeTruthy();
            // has input
            expect(modalCreateWishlist.querySelector('.form-group')).toBeTruthy();
            // has cancel btn
            expect(modalCreateWishlist.querySelector('.wishlists__cta-cancel')).toBeTruthy();
            // has save cta
            expect(modalCreateWishlist.querySelector('.wishlists__cta-save')).toBeTruthy();
          });
        });
      }));
      it('should navigated to the wishlist details page for the wishlist I just created', async(() => {
        component.listAdded(DUMMY_WISHLIST[0].wishlistId);
        fixture.detectChanges();
        fixture.whenStable().then(() => {
          fixture.detectChanges();
          expect(routerSpy).toHaveBeenCalledWith('my-account/wishlist/' + DUMMY_WISHLIST[0].wishlistId);
        });
      }));
    });
    describe('Given that I am a guest customer', () => {
      beforeEach(() => {
        component.ngOnInit();
        component.guestListEmpty = true;
        component.isUserLogged = false;
        component.wishlists$ = of([]) as Observable<Wishlists[]>;
        fixture.detectChanges();
      });
      it('should be displaying intro text, cta start shopping, and no wishlists', () => {
        expect(fixture.debugElement.query(By.css('.wishlists__cta')).nativeElement.textContent).toContain('start');
        expect(fixture.debugElement.query(By.css('.wishlists__intro')).nativeElement).toBeTruthy();
        expect(fixture.debugElement.query(By.css('.wishlists'))).toBeNull();
      });
    });
  });

  afterEach(() => {
    wishlistService = null;
    component.ngOnDestroy();
    fixture.destroy();
  });
});
