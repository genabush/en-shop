import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { OccConfig, BaseSiteService, TranslationService, MockDatePipe } from '@spartacus/core';

// pipes & mocks
import { MockCxTranslatePipe, MockCxUrlPipe } from 'src/app/testing/mock.pipes';
import { MockOccConfig } from 'src/app/testing/occ/occ.config.mock';
import { MockBaseSiteService, MockWishlistsService, MockTranslationService } from 'src/app/testing/mock.services';

// Modules
import { SpinnerModule, MediaModule, LayoutConfig } from '@spartacus/storefront';
import { HttpClientTestingModule } from '@angular/common/http/testing';

// Comp
import { MockCxIconComponent, MockCxAddToCartComponent } from 'src/app/testing/mock.components';
import { WishlistNameModalComponent } from '../wishlist-name-modal/wishlist-name-modal.component';
import { WishlistDetailComponent } from './wishlist-detail.component';
import { WishlistProductsListingComponent } from '../wishlist-products-listing/wishlist-products-listing.component';
import { WishlistProductsListingRefineComponent } from '../wishlist-products-listing-refine/wishlist-products-listing-refine.component';
import { WishlistProductItemComponent } from '../wishlist-product-item/wishlist-product-item.component';
import { Store } from '@ngrx/store';
import { BehaviorSubject, of } from 'rxjs';
import { WishlistService } from 'src/app/services/wishlist/wishlist.service';
import { DUMMY_WISHLIST, DUMMY_WISHLISTS_PRODUCTS } from 'src/app/testing/mock.constants';
import { By } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

class MockStore<T> {
  private state = new BehaviorSubject(undefined);
  setState(data) {
    this.state.next(data);
  }
  select(selector?: any) {
    return of({ selected: { code: 123 } });
  }
  dispatch(action: any) {}
}

const MockLayoutConfig: LayoutConfig = {
  breakpoints: {
    xs: 320,
    sm: 768,
    md: 1024,
    lg: 1366
  }
};

describe('WishlistDetailComponent', () => {
  let component: WishlistDetailComponent;
  let fixture: ComponentFixture<WishlistDetailComponent>;
  let wishlistService: WishlistService;
  let getWishlistByIdSpy: jasmine.Spy;
  let getWishlistProductsSpy: jasmine.Spy;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SpinnerModule, RouterTestingModule, MediaModule, HttpClientTestingModule],
      declarations: [
        WishlistDetailComponent,
        MockCxTranslatePipe,
        MockCxUrlPipe,
        MockCxIconComponent,
        WishlistProductsListingComponent,
        WishlistProductsListingRefineComponent,
        WishlistProductItemComponent,
        MockCxAddToCartComponent,
        WishlistNameModalComponent,
        MockDatePipe
      ],
      providers: [
        { provide: ActivatedRoute, useValue: { snapshot: { params: { id: DUMMY_WISHLIST[0].wishlistId } } } },
        { provide: OccConfig, useClass: MockOccConfig },
        { provide: BaseSiteService, useClass: MockBaseSiteService },
        { provide: WishlistService, useClass: MockWishlistsService },
        { provide: Store, useClass: MockStore },
        { provide: LayoutConfig, useValue: MockLayoutConfig },
        { provide: TranslationService, useClass: MockTranslationService }
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WishlistDetailComponent);
    wishlistService = TestBed.get(WishlistService);
    component = fixture.componentInstance;
    getWishlistByIdSpy = spyOn(wishlistService, 'getWishlistById').and.returnValue(of(DUMMY_WISHLIST));
    getWishlistProductsSpy = spyOn(wishlistService, 'getWishlistProducts').and.returnValue(
      of(DUMMY_WISHLISTS_PRODUCTS)
    );
    fixture.detectChanges();
    component.ngOnInit();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  xit('should show wishlist name', () => {
    component.ngOnInit();
    component.currentWishlistProducts$ = of(DUMMY_WISHLISTS_PRODUCTS);
    component.wishlistName = DUMMY_WISHLIST[0].wishlistName;
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('.wishlists__header-title')).nativeElement.innerText).toEqual(
      DUMMY_WISHLIST[0].wishlistName
    );
  });
  xit('should have a change wishlist name link', () => {
    expect(fixture.debugElement.query(By.css('.wishlists__header-change')).nativeElement.innerText).toBeTruthy();
  });
  it('should have a back button to wishlist hub', () => {
    expect(fixture.debugElement.query(By.css('.wishlists__header-back')).nativeElement.innerText).toBeTruthy();
  });
  describe('Given that you have products in your wishlist', () => {
    beforeEach(() => {
      component.ngOnInit();
      component.wishlistName = DUMMY_WISHLIST[0].wishlistName;
      const productsArray = DUMMY_WISHLISTS_PRODUCTS.products.map(prod => {
        return prod.code;
      });
      component.wishlistService.getWishlistProducts(productsArray, false).subscribe(data => {
        component.currentWishlistProducts$ = data.products;
      });
      fixture.detectChanges();
    });
    it('should list products in your wishlist', () => {
      expect(fixture.debugElement.query(By.css('.product-tile--wishlist')).nativeElement).toBeTruthy();
    });
    it('should display the total count of products in my wishlist', () => {
      expect(fixture.debugElement.query(By.css('.results-count__value > span')).nativeElement.innerText).toEqual(
        DUMMY_WISHLISTS_PRODUCTS.products.length.toString()
      );
    });
    it('should show the timestamp added', () => {
      expect(fixture.debugElement.query(By.css('span[date-added]')).attributes['date-added']).toEqual(
        DUMMY_WISHLISTS_PRODUCTS.products[0].wishlistCreationDate
      );
    });
  });
});
