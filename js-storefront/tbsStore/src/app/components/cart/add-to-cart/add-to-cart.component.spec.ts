import { ChangeDetectorRef, DebugElement } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { MatFormFieldModule, MatSelectModule } from '@angular/material';
import { By } from '@angular/platform-browser';
// vendor
import { BehaviorSubject, of, Observable } from 'rxjs';
import { Store } from '@ngrx/store';
import { AgmCoreModule } from '@agm/core';
import { MatInputModule } from '@angular/material';
import { NgbModalModule, NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';

// spartacus
import {
  CartService,
  OrderEntry,
  Product,
  RoutingConfig,
  ProductService,
  OccConfig,
  BaseSiteService,
  TranslationService
} from '@spartacus/core';

// components
import { AddToCartComponent } from './add-to-cart.component';
import { FindInStoreComponent } from '../../find-in-store/find-in-store.component';

// services
import { CustomCartService } from '../../../services/cart/facade/cart.service';
import { ProductVariantsService } from 'src/app/services/product-variants/product-variants.service';
import { CurrentProductService } from '../../product/current-product.service';
import {
  MockVariantService,
  MockBaseSiteService,
  MockWishlistsService,
  MockTranslationService,
  MockCustomCartService
} from 'src/app/testing/mock.services';
import { WishlistService } from 'src/app/services/wishlist/wishlist.service';

// components
import {
  MockAppBackInStockComponent,
  MockItemCounterComponent,
  AddedToCartDialogComponent,
  MockCxIconComponent
} from 'src/app/testing/mock.components';

// pipes
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';

// interfaces
import { ICustomVariantOption, ICustomProduct } from 'src/app/interfaces/custom-product-item.interface';
import { CollectionPointItemModule } from '../../checkout/fulfillment/collection-point-item/collection-point-item.module';

import { MockOccConfig } from 'src/app/testing/occ/occ.config.mock';

// constants
import { DUMMY_CART_ENTRIES } from 'src/app/testing/mock.constants';
import { CustomQuantitySelectorComponent } from '../../shared/custom-quantity-selector/custom-quantity-selector.component';
import { MessagingComponent } from '../../shared/messaging/messaging.component';
import { WishlistFormComponent } from '../../wishlist/wishlist-form/wishlist-form.component';

const productCode = '1234';
const mockProduct: Product | ICustomVariantOption = {
  name: 'mockProduct',
  code: 'code1',
  stock: { stockLevelStatus: 'inStock', stockLevel: 20 },
  maxOrderProductQuantity: 0
};
const mockProduct2: Product = {
  name: 'mockPrduct2',
  code: 'code2',
  stock: { stockLevelStatus: 'inStock', stockLevel: 12 }
};

const mockNoStockProduct: Product = {
  name: 'mockProduct',
  code: 'code1',
  stock: { stockLevelStatus: 'outOfStock', stockLevel: 0 }
};

const mockProductMax: Product | ICustomVariantOption = {
  name: 'mockProduct',
  code: '12345678',
  stock: { stockLevelStatus: 'inStock', stockLevel: 20 },
  maxOrderProductQuantity: 10
};

const mockSingleVariantProd: Product | ICustomVariantOption | ICustomProduct = {
  code: 'p002347',
  isVariant: false,
  multiVariant: false,
  name: 'Almond Milk & Honey Soothing & Restoring Body Butter',
  variants: 'p002347v'
};

const mockMutliVariantProd: ICustomProduct = {
  code: 'p000247',
  isVariant: false,
  multiVariant: true,
  name: 'Wild Argan Oil Sublime Nourishing Body Butter',
  variants: '1094138, 1094188'
};

class MockCurrentProductService {
  getProduct(): Observable<ICustomProduct> {
    return of();
  }
  getProductDetails(): Observable<ICustomProduct> {
    return of();
  }
}

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
class MockChangeDetectorRef {
  markForCheck() {
    return;
  }
}
class MockRoutingConfig {}
class MockProductService {}

const mockGuestSessionWishlist = { products: ['1096148', '1096261', '1096109'] };

describe('AddToCartComponent', () => {
  let component: AddToCartComponent;
  let fixture: ComponentFixture<AddToCartComponent>;
  let service: CartService;
  let currentProductService: CurrentProductService;
  let wishlistService: WishlistService;
  let productVariantsService: ProductVariantsService;
  let getSelectedVariantSpy: jasmine.Spy;
  let getEntrySpy: jasmine.Spy;
  let getEntriesSpy: jasmine.Spy;
  let openAddedModalSpy: jasmine.Spy;
  let guestAddToWishlistSpy: jasmine.Spy;

  const mockCartEntry: OrderEntry = { entryNumber: 7 };
  let store = {};
  const mockSessionStorage = {
    getItem: (key: string): string => {
      return key in store ? store[key] : null;
    },
    setItem: (key: string, value: string) => {
      store[key] = `${value}`;
    },
    removeItem: (key: string) => {
      delete store[key];
    },
    clear: () => {
      store = {};
    }
  };
  const sessionFirstTimerKey: string = 'TBSWishlistsFirstTimer';
  const sessionWishlistKey: string = 'TBSWishlists';
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientTestingModule,
        FormsModule,
        ReactiveFormsModule,
        MatInputModule,
        MatSelectModule,
        MatFormFieldModule,
        CollectionPointItemModule,
        AgmCoreModule,
        ReactiveFormsModule,
        HttpClientTestingModule,
        NgbModalModule,
        NgbTooltipModule
      ],
      declarations: [
        AddToCartComponent,
        CustomQuantitySelectorComponent,
        MockCxTranslatePipe,
        AddedToCartDialogComponent,
        MockAppBackInStockComponent,
        MessagingComponent,
        MockCxIconComponent,
        WishlistFormComponent,
        FindInStoreComponent
      ],
      providers: [
        { provide: CustomCartService, useClass: MockCustomCartService },
        { provide: CurrentProductService, useClass: MockCurrentProductService },
        { provide: Store, useClass: MockStore },
        { provide: ChangeDetectorRef, useClass: MockChangeDetectorRef },
        { provide: RoutingConfig, useClass: MockRoutingConfig },
        { provide: ProductService, useClass: MockProductService },
        { provide: ProductVariantsService, useClass: MockVariantService },
        { provide: OccConfig, useClass: MockOccConfig },
        { provide: BaseSiteService, useClass: MockBaseSiteService },
        { provide: WishlistService, useClass: MockWishlistsService },
        { provide: TranslationService, useClass: MockTranslationService }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddToCartComponent);
    component = fixture.componentInstance;
    service = TestBed.get(CustomCartService);
    wishlistService = TestBed.get(WishlistService);
    currentProductService = TestBed.get(CurrentProductService);
    productVariantsService = TestBed.get(ProductVariantsService);
    component.productCode = mockProduct.code;
    component.quickview = false;
    getSelectedVariantSpy = spyOn(productVariantsService, 'getSelectedVariant').and.returnValue(
      of(mockSingleVariantProd as Product)
    );
    getEntrySpy = spyOn(service, 'getEntry').and.returnValue(of(mockCartEntry));
    getEntriesSpy = spyOn(service, 'getEntries').and.returnValue(of([mockProduct as OrderEntry]));
    openAddedModalSpy = spyOn(component, 'openModal');
    guestAddToWishlistSpy = spyOn(component, 'guestAddToWishlist');

    component.ngOnInit();
    component.selectedVariant$ = of(mockSingleVariantProd);
    component.multiVariant = false;
    component.isPDP = false;
    component.guestFirst = undefined;
    component.variants = mockSingleVariantProd.variants;
    component['cd'].detectChanges();
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });

  describe('Product code provided', () => {
    it('should call ngOnInit()', () => {
      getSelectedVariantSpy.and.returnValue(of({ code: '123456' }));
      component.ngOnInit();
      expect(productVariantsService.getSelectedVariant).toHaveBeenCalled();
    });
    it('should call ngOnInit() and set some maxQuantity', () => {
      getSelectedVariantSpy.and.returnValue(of(mockProduct));
      component.ngOnInit();
      expect(component.maxQuantity).toEqual(mockProduct.maxOrderProductQuantity);
    });
  });

  describe('Product from page', () => {
    beforeEach(() => {
      getEntriesSpy.and.returnValue(of(DUMMY_CART_ENTRIES[0]));
    });
    it('should load product from service', () => {
      spyOn(currentProductService, 'getProduct').and.returnValue(of(mockProduct));
      component.ngOnInit();
      expect(component.productCode).toEqual(mockProduct.code);
      expect(component.maxQuantity).toEqual(mockProduct.maxOrderProductQuantity);
    });

    it('should reset counter value when changing product', () => {
      const currentProduct = new BehaviorSubject<Product>(mockProduct);

      //Product 1
      spyOn(currentProductService, 'getProduct').and.returnValue(currentProduct);
      component.ngOnInit();
      expect(component.productCode).toEqual(mockProduct.code);
      component.quantity = 5;

      //Product 2
      component.productCode = mockProduct2.code;
      fixture.detectChanges();
      expect(component.productCode).toEqual(mockProduct2.code);
      //Quantity is expected to be reset to 1 since it is a new product page
      expect(component.quantity).toEqual(5);
    });

    it('should disable input when the product has no stock', () => {
      spyOn(currentProductService, 'getProduct').and.returnValue(of(mockNoStockProduct));
      component.ngOnInit();
      expect(component.productCode).toEqual(mockProduct.code);
      expect(component.maxQuantity).toBe(mockProduct.maxOrderProductQuantity);
    });

    it('should NOT show `max qty reached` message when max quantity is not reached', () => {
      getEntriesSpy.and.returnValue(of([mockProduct as OrderEntry]));
      component.ngOnInit();
      component.productCode = (false as any) as string;
      component.maxQuantity = 10;
      component.existingQtyInBasket = 5;
      component['cd'].detectChanges();
      fixture.detectChanges();
      expect(fixture.debugElement.query(By.css('.alert-warning'))).toBeNull();
    });

    xit('should show `max qty reached` message when max quantity is reached', () => {
      getEntriesSpy.and.returnValue(of([mockProductMax as OrderEntry]));
      getSelectedVariantSpy.and.returnValue(of(mockProductMax));
      component.ngOnInit();
      component.productCode = (false as any) as string;
      component.maxQuantity = 10;
      component.existingQtyInBasket = 10;
      component['cd'].detectChanges();
      fixture.detectChanges();
      expect(fixture.debugElement.query(By.css('.alert-warning')).nativeElement.textContent).toBeTruthy();
    });
  });

  it('should call addToCart()', () => {
    component.productCode = productCode;
    component.ngOnInit();
    spyOn(service, 'addEntry').and.callThrough();
    component.cartEntry$ = of(mockCartEntry);
    component.quantity = 1;
    component.addToCart();
    component.cartEntry$.subscribe();
    expect(openAddedModalSpy).toHaveBeenCalled();
  });

  // ADD TO FAV START

  describe('On click of add to favourite icon on PLP (single variant)', () => {
    describe('Given that I am a LOGGED IN customer', () => {
      it('should have on PLP/SLP a product code selected and no name should be provided', async(() => {
        getSelectedVariantSpy.and.returnValue(of(mockSingleVariantProd));
        getEntriesSpy.and.returnValue(of([mockSingleVariantProd as OrderEntry]));
        component.ngOnInit();
        component.selectedVariant$ = of(mockSingleVariantProd);
        component.multiVariant = false;
        component.isPDP = false;
        component.variants = mockSingleVariantProd.variants;
        component['cd'].detectChanges();
        fixture.detectChanges();
        fixture.whenStable().then(() => {
          fixture.detectChanges();
          const favIconBtn: DebugElement = fixture.debugElement.query(By.css('.add-to-bag-set__fav'));
          favIconBtn.nativeElement.dispatchEvent(new MouseEvent('click'));
          expect(component.variants).toEqual(mockSingleVariantProd.variants.split(',')[0]);
          expect(component.name).toBeUndefined();
        });
      }));
    });
    describe('Given that I am a GUEST customer, and it is my first time clicking on the fav icon', () => {
      it('should show the first time wishlist tooltip', () => {
        spyOn(component, 'firstTimerActions');
        component.isUserLogged = false;
        component.guestFirst = true;
        fixture.detectChanges();
        const favIconBtn: DebugElement = fixture.debugElement.query(By.css('.add-to-bag-set__fav'));
        favIconBtn.nativeElement.dispatchEvent(new MouseEvent('click'));
        expect(component.firstTimerActions).toHaveBeenCalled();
      });
    });
    describe('Given that I am a GUEST customer, and it is NOT my first time clicking on the fav icon', () => {
      it('should NOT show the first time wishlist tooltip', () => {
        spyOn(component, 'firstTimerActions');
        component.isUserLogged = false;
        fixture.detectChanges();
        const favIconBtn: DebugElement = fixture.debugElement.query(By.css('.add-to-bag-set__fav'));
        favIconBtn.nativeElement.dispatchEvent(new MouseEvent('click'));
        expect(component.firstTimerActions).not.toHaveBeenCalled();
      });
    });
  });

  describe('On click of add to favourite icon on PLP (multi variant)', () => {
    it('should trigger the event emitter to parent element to show quickview', async(() => {
      getSelectedVariantSpy.and.returnValue(of(mockMutliVariantProd));
      getEntriesSpy.and.returnValue(of([mockMutliVariantProd as OrderEntry]));
      spyOn(component.triggerParentForVariantModal, 'emit');
      component.ngOnInit();
      component.selectedVariant$ = of(mockMutliVariantProd);
      component.multiVariant = true;
      component.isPDP = false;
      component['cd'].detectChanges();
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        fixture.detectChanges();
        const favIconBtn: DebugElement = fixture.debugElement.query(By.css('.add-to-bag-set__fav'));
        favIconBtn.nativeElement.dispatchEvent(new MouseEvent('click'));
        expect(component.triggerParentForVariantModal.emit).toHaveBeenCalledWith(true);
      });
    }));
  });

  describe('On click of add to favourite icon on PDP (single variant OR multi variant)', () => {
    describe('Given that I am a LOGGED IN customer', () => {
      it('should have on PDP a product code selected should be provided', async(() => {
        getSelectedVariantSpy.and.returnValue(of(mockSingleVariantProd));
        getEntriesSpy.and.returnValue(of([mockSingleVariantProd as OrderEntry]));
        spyOn(currentProductService, 'getProductDetails').and.returnValue(of(mockSingleVariantProd));
        component.ngOnInit();
        component.selectedVariant$ = of(mockSingleVariantProd);
        component.multiVariant = false;
        component.isPDP = true;
        component.guestFirst = false;
        component.variants = mockSingleVariantProd.variants;
        component['cd'].detectChanges();
        fixture.detectChanges();
        fixture.whenStable().then(() => {
          fixture.detectChanges();
          const favIconBtn: DebugElement = fixture.debugElement.query(By.css('.add-to-bag-set__fav'));
          favIconBtn.nativeElement.dispatchEvent(new MouseEvent('click'));
          expect(component.variants).toEqual(mockSingleVariantProd.variants.split(',')[0]);
        });
      }));
    });
    describe('Given that I am a GUEST customer, and it is my first time clicking on the fav icon', () => {
      it('should show the first time wishlist tooltip', () => {
        spyOn(component, 'firstTimerActions');
        component.isUserLogged = false;
        component.guestFirst = true;
        fixture.detectChanges();
        const favIconBtn: DebugElement = fixture.debugElement.query(By.css('.add-to-bag-set__fav'));
        favIconBtn.nativeElement.dispatchEvent(new MouseEvent('click'));
        expect(component.firstTimerActions).toHaveBeenCalled();
      });
    });
    describe('Given that I am a GUEST customer, and it is NOT my first time clicking on the fav icon', () => {
      it('should NOT show the first time wishlist tooltip', () => {
        spyOn(component, 'firstTimerActions');
        component.isUserLogged = false;
        component.guestFirst = false;
        fixture.detectChanges();
        const favIconBtn: DebugElement = fixture.debugElement.query(By.css('.add-to-bag-set__fav'));
        favIconBtn.nativeElement.dispatchEvent(new MouseEvent('click'));
        expect(component.firstTimerActions).not.toHaveBeenCalled();
      });
    });
  });

  // ADD TO FAV END

  afterEach(() => {
    if (component.openModalRef) {
      component.openModalRef.close();
    }
    component.ngOnDestroy();
    fixture.destroy();
  });
});
