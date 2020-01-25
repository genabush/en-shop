import { TestBed } from '@angular/core/testing';

import { PageType, Product, ProductService, RoutingService } from '@spartacus/core';

import { Observable, of } from 'rxjs';
import { Store } from '@ngrx/store';

import { MockStore } from 'src/app/testing/mock.components';

import { CurrentProductService } from './current-product.service';

import { ICustomProduct } from 'src/app/interfaces/custom-product-item.interface';

const router = {
  state: {
    url: '/',
    queryParams: {},
    params: { productCode: '123456' },
    context: { id: '1', type: PageType.PRODUCT_PAGE },
    cmsRequired: false
  }
};

class MockRoutingService {
  getRouterState() {
    return of(router);
  }
}

const mockProduct: Product = { name: 'mockProduct' };

class MockProductService {
  get(): Observable<Product> {
    return of(mockProduct);
  }
}

describe('CurrentProductService', () => {
  let service: CurrentProductService;
  let routingService: RoutingService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [],
      providers: [
        CurrentProductService,
        {
          provide: ProductService,
          useClass: MockProductService
        },
        {
          provide: RoutingService,
          useClass: MockRoutingService
        },
        { provide: Store, useClass: MockStore }
      ]
    });
  });
  beforeEach(() => {
    service = TestBed.get(CurrentProductService);
    routingService = TestBed.get(RoutingService);
  });

  it('should fetch product data', () => {
    let result: Product | ICustomProduct;
    service.getProduct().subscribe(product => (result = product));
    expect(result).toEqual(mockProduct);
  });
  describe('getProduct method', () => {
    it('should invoke getRouterState', () => {
      spyOn(routingService, 'getRouterState').and.returnValue(of());
      service.getProduct();
      expect(routingService.getRouterState).toHaveBeenCalled();
    });
  });

  it('should fetch product details', () => {
    let result: Product | ICustomProduct;
    service.getProductDetails().subscribe(product => (result = product));
    expect(result).toEqual(mockProduct);
  });
  describe('getProductDetails method', () => {
    it('should invoke getRouterState', () => {
      spyOn(routingService, 'getRouterState').and.returnValue(of());
      service.getProductDetails();
      expect(routingService.getRouterState).toHaveBeenCalled();
    });
  });
});
