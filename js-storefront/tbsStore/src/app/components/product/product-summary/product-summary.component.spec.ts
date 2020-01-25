import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { Component, Input } from '@angular/core';
import { By } from '@angular/platform-browser';

// spartacus
import { OutletDirective } from '@spartacus/storefront';

// vendor
import { Store } from '@ngrx/store';
import { of } from 'rxjs';

// custom
import { RootStoreState } from '../../../root-store';
import { MockStore } from 'src/app/testing/mock.components';

// components
import { ProductSummaryComponent } from '../product-summary/product-summary.component';

// services
import { MockCurrentProductService } from 'src/app/testing/mock.services';
import { CurrentProductService } from '../current-product.service';

// pipes
import { MockCxTranslatePipe, MockSortBySizePipe } from 'src/app/testing/mock.pipes';

// constants
import {
  DUMMY_PLP_CUSTOM_PRODUCT_COLOUR_VARIANT_IN_STOCK,
  DUMMY_PLP_CUSTOM_PRODUCT_COLOUR_VARIANT_OUT_STOCK,
  DUMMY_PLP_CUSTOM_PRODUCT_SIZE_VARIANT_IN_STOCK,
  DUMMY_PLP_CUSTOM_PRODUCT_SIZE_VARIANT_OUT_STOCK
} from '../../../testing/mock.constants';

@Component({
  selector: 'app-product-variants-size',
  template: '<div></div>'
})
class MockProductVariantSizeComponent {
  @Input() variant;
}

describe('ProductSummaryComponent - COLOUR VARIANT - in product IN STOCK', () => {
  let component: ProductSummaryComponent;
  let fixture: ComponentFixture<ProductSummaryComponent>;
  let store: Store<RootStoreState.State>;
  let productService: CurrentProductService;
  let variantStoreSpy;
  let storeSelectSpy;
  let storeDispatchSpy;
  let productVariantSpy;
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [],
      declarations: [
        ProductSummaryComponent,
        MockProductVariantSizeComponent,
        OutletDirective,
        MockCxTranslatePipe,
        MockSortBySizePipe
      ],
      providers: [
        {
          provide: CurrentProductService,
          useClass: MockCurrentProductService
        },
        {
          provide: Store,
          useClass: MockStore
        }
      ]
    }).compileComponents();
  }));
  beforeEach(() => {
    fixture = TestBed.createComponent(ProductSummaryComponent);
    component = fixture.componentInstance;
    store = TestBed.get(Store);
    productService = fixture.componentRef.injector.get(CurrentProductService);
    // spies
    storeDispatchSpy = spyOn(store, 'dispatch');
    productVariantSpy = spyOn(productService, 'getProductDetails').and.returnValue(
      of(DUMMY_PLP_CUSTOM_PRODUCT_COLOUR_VARIANT_IN_STOCK)
    );
    storeSelectSpy = spyOn(store, 'select').and.returnValue(of(DUMMY_PLP_CUSTOM_PRODUCT_COLOUR_VARIANT_IN_STOCK));
    variantStoreSpy = spyOn(component, 'getVariantsStore').and.returnValue(
      of(DUMMY_PLP_CUSTOM_PRODUCT_COLOUR_VARIANT_IN_STOCK.variantOptions[0])
    );
    // vars
    component.productCode = DUMMY_PLP_CUSTOM_PRODUCT_COLOUR_VARIANT_IN_STOCK.variantOptions[0].code;
    component.productHasColours =
      DUMMY_PLP_CUSTOM_PRODUCT_COLOUR_VARIANT_IN_STOCK.variantOptions[0].variantType === 'COLOUR';

    fixture.detectChanges();
  });
  it('should CREATE', async(() => {
    expect(component).toBeTruthy();
  }));
  it('should CHOOSE a DEFAULT PRODUCT COLOUR VARIANT when NONE is selected', async(() => {
    fixture.whenStable().then(() => {
      fixture.detectChanges();
      expect(fixture.debugElement.query(By.css('.colour-variants__preview-name')).nativeElement.textContent).toContain(
        DUMMY_PLP_CUSTOM_PRODUCT_COLOUR_VARIANT_IN_STOCK.variantOptions[0].colourName
      );
      expect(
        fixture.debugElement.queryAll(By.css('.colour-variants__wrapper > .colour-variants__swatch'))[0].nativeElement
          .classList
      ).toContain('colour-variants__swatch--active');
    });
  }));
  it('should NOT show `out of stock` UI if product swatch is in stock', async(() => {
    fixture.whenStable().then(() => {
      fixture.detectChanges();
      expect(fixture.debugElement.query(By.css('.colour-variants__swatch')).nativeElement.classList).not.toContain(
        'colour-variants__swatch--disabled'
      );
    });
  }));
  it('should have a colour selection label that contains `colour`', async(() => {
    fixture.whenStable().then(() => {
      fixture.detectChanges();
      expect(
        fixture.debugElement
          .query(By.css('.price__heading'))
          .nativeElement.textContent.toUpperCase()
          .indexOf('COLOUR') !== -1
      ).toBe(true);
    });
  }));
  afterEach(() => {
    productService = null;
    component = null;
    fixture.destroy();
  });
});

describe('ProductSummaryComponent - COLOUR VARIANT - in product OUT OF STOCK', () => {
  let component: ProductSummaryComponent;
  let fixture: ComponentFixture<ProductSummaryComponent>;
  let store: Store<RootStoreState.State>;
  let productService: CurrentProductService;
  let variantStoreSpy;
  let storeSelectSpy;
  let storeDispatchSpy;
  let productVariantSpy;
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [],
      declarations: [
        ProductSummaryComponent,
        MockProductVariantSizeComponent,
        OutletDirective,
        MockCxTranslatePipe,
        MockSortBySizePipe
      ],
      providers: [
        {
          provide: CurrentProductService,
          useClass: MockCurrentProductService
        },
        {
          provide: Store,
          useClass: MockStore
        }
      ]
    }).compileComponents();
  }));
  beforeEach(() => {
    fixture = TestBed.createComponent(ProductSummaryComponent);
    component = fixture.componentInstance;
    store = TestBed.get(Store);
    productService = fixture.componentRef.injector.get(CurrentProductService);
    // spies
    storeDispatchSpy = spyOn(store, 'dispatch');
    productVariantSpy = spyOn(productService, 'getProductDetails').and.returnValue(
      of(DUMMY_PLP_CUSTOM_PRODUCT_COLOUR_VARIANT_OUT_STOCK)
    );
    storeSelectSpy = spyOn(store, 'select').and.returnValue(of(DUMMY_PLP_CUSTOM_PRODUCT_COLOUR_VARIANT_OUT_STOCK));
    variantStoreSpy = spyOn(component, 'getVariantsStore').and.returnValue(
      of(DUMMY_PLP_CUSTOM_PRODUCT_COLOUR_VARIANT_OUT_STOCK.variantOptions[0])
    );
    // vars
    component.productCode = DUMMY_PLP_CUSTOM_PRODUCT_COLOUR_VARIANT_OUT_STOCK.variantOptions[0].code;
    component.productHasColours =
      DUMMY_PLP_CUSTOM_PRODUCT_COLOUR_VARIANT_OUT_STOCK.variantOptions[0].variantType === 'COLOUR';
    fixture.detectChanges();
  });
  it('should SHOW `out of stock` UI if product colour swatch has no stock', async(() => {
    fixture.whenStable().then(() => {
      fixture.detectChanges();
      expect(fixture.debugElement.query(By.css('.colour-variants__swatch')).nativeElement.classList).toContain(
        'colour-variants__swatch--disabled'
      );
    });
  }));
  afterEach(() => {
    productService = null;
    component = null;
    fixture.destroy();
  });
});

describe('ProductSummaryComponent - SIZE VARIANT - in product IN STOCK', () => {
  let component: ProductSummaryComponent;
  let fixture: ComponentFixture<ProductSummaryComponent>;
  let store: Store<RootStoreState.State>;
  let productService: CurrentProductService;
  let variantStoreSpy;
  let storeSelectSpy;
  let storeDispatchSpy;
  let productVariantSpy;
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [],
      declarations: [
        ProductSummaryComponent,
        MockProductVariantSizeComponent,
        OutletDirective,
        MockCxTranslatePipe,
        MockSortBySizePipe
      ],
      providers: [
        {
          provide: CurrentProductService,
          useClass: MockCurrentProductService
        },
        {
          provide: Store,
          useClass: MockStore
        }
      ]
    }).compileComponents();
  }));
  beforeEach(() => {
    fixture = TestBed.createComponent(ProductSummaryComponent);
    component = fixture.componentInstance;
    store = TestBed.get(Store);
    productService = fixture.componentRef.injector.get(CurrentProductService);
    // spies
    storeDispatchSpy = spyOn(store, 'dispatch');
    productVariantSpy = spyOn(productService, 'getProductDetails').and.returnValue(
      of(DUMMY_PLP_CUSTOM_PRODUCT_SIZE_VARIANT_IN_STOCK)
    );
    storeSelectSpy = spyOn(store, 'select').and.returnValue(of(DUMMY_PLP_CUSTOM_PRODUCT_SIZE_VARIANT_IN_STOCK));
    variantStoreSpy = spyOn(component, 'getVariantsStore').and.returnValue(
      of(DUMMY_PLP_CUSTOM_PRODUCT_SIZE_VARIANT_IN_STOCK.variantOptions[0])
    );
    // vars
    component.productCode = DUMMY_PLP_CUSTOM_PRODUCT_SIZE_VARIANT_IN_STOCK.variantOptions[0].code;
    component.productHasColours =
      DUMMY_PLP_CUSTOM_PRODUCT_SIZE_VARIANT_IN_STOCK.variantOptions[0].variantType === 'COLOUR';
    fixture.detectChanges();
  });
  it('should CREATE', async(() => {
    expect(component).toBeTruthy();
  }));
  it('should CHOOSE a DEFAULT PRODUCT SIZE VARIANT when NONE is selected', async(() => {
    fixture.whenStable().then(() => {
      fixture.detectChanges();
      expect(fixture.debugElement.query(By.css('.price__block')).nativeElement).toBeTruthy();
    });
  }));
  it('should NOT show `out of stock` UI if product swatch is in stock', async(() => {
    fixture.whenStable().then(() => {
      fixture.detectChanges();
      expect(fixture.debugElement.query(By.css('.price__block')).nativeElement.classList).not.toContain(
        'price__block--disabled'
      );
    });
  }));
  it('should NOT show `out of stock` message if product size variant has no stock', async(() => {
    fixture.whenStable().then(() => {
      fixture.detectChanges();
      expect(fixture.debugElement.query(By.css('.price__block .price__stock'))).toBeNull();
    });
  }));
  it('should have a colour selection label that contains `size`', async(() => {
    fixture.whenStable().then(() => {
      fixture.detectChanges();
      expect(
        fixture.debugElement
          .query(By.css('.price__heading'))
          .nativeElement.textContent.toUpperCase()
          .indexOf('SIZE') !== -1
      ).toBe(true);
    });
  }));
  afterEach(() => {
    productService = null;
    component = null;
    fixture.destroy();
  });
});

describe('ProductSummaryComponent - SIZE VARIANT - in product OUT OF STOCK', () => {
  let component: ProductSummaryComponent;
  let fixture: ComponentFixture<ProductSummaryComponent>;
  let store: Store<RootStoreState.State>;
  let productService: CurrentProductService;
  let variantStoreSpy;
  let storeSelectSpy;
  let storeDispatchSpy;
  let productVariantSpy;
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [],
      declarations: [
        ProductSummaryComponent,
        MockProductVariantSizeComponent,
        OutletDirective,
        MockCxTranslatePipe,
        MockSortBySizePipe
      ],
      providers: [
        {
          provide: CurrentProductService,
          useClass: MockCurrentProductService
        },
        {
          provide: Store,
          useClass: MockStore
        }
      ]
    }).compileComponents();
  }));
  beforeEach(() => {
    fixture = TestBed.createComponent(ProductSummaryComponent);
    component = fixture.componentInstance;
    store = TestBed.get(Store);
    productService = fixture.componentRef.injector.get(CurrentProductService);
    // spies
    storeDispatchSpy = spyOn(store, 'dispatch');
    productVariantSpy = spyOn(productService, 'getProductDetails').and.returnValue(
      of(DUMMY_PLP_CUSTOM_PRODUCT_SIZE_VARIANT_OUT_STOCK)
    );
    storeSelectSpy = spyOn(store, 'select').and.returnValue(of(DUMMY_PLP_CUSTOM_PRODUCT_SIZE_VARIANT_OUT_STOCK));
    variantStoreSpy = spyOn(component, 'getVariantsStore').and.returnValue(
      of(DUMMY_PLP_CUSTOM_PRODUCT_SIZE_VARIANT_OUT_STOCK.variantOptions[0])
    );
    // vars
    component.productCode = DUMMY_PLP_CUSTOM_PRODUCT_SIZE_VARIANT_OUT_STOCK.variantOptions[0].code;
    component.productHasColours =
      DUMMY_PLP_CUSTOM_PRODUCT_SIZE_VARIANT_OUT_STOCK.variantOptions[0].variantType === 'COLOUR';
    fixture.detectChanges();
  });
  it('should CREATE', async(() => {
    expect(component).toBeTruthy();
  }));
  it('should SHOW `out of stock` UI if product size variant has no stock', async(() => {
    fixture.whenStable().then(() => {
      fixture.detectChanges();
      expect(fixture.debugElement.query(By.css('.price__block')).nativeElement.classList).toContain(
        'price__block--disabled'
      );
    });
  }));
  it('should SHOW `out of stock` message if product size variant has no stock', async(() => {
    fixture.whenStable().then(() => {
      fixture.detectChanges();
      expect(fixture.debugElement.query(By.css('.price__block .price__stock'))).toBeTruthy();
    });
  }));
  afterEach(() => {
    productService = null;
    component = null;
    fixture.destroy();
  });
});
