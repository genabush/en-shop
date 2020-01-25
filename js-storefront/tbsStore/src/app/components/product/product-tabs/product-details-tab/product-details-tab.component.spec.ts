import { async, ComponentFixture, TestBed } from '@angular/core/testing';

// spartacus
import { Product } from '@spartacus/core';

// vendor
import { Observable, of } from 'rxjs';

// services
import { CurrentProductService } from '../../current-product.service';

// components
import { ProductDetailsTabComponent } from './product-details-tab.component';

// interfaces
import { ICustomProduct } from 'src/app/interfaces/custom-product-item.interface';

const mockProduct: Product = { name: 'mockProduct' };

class MockCurrentProductService {
  getProduct(): Observable<Product | ICustomProduct> {
    return of(mockProduct);
  }
}

describe('ProductDetailsTabComponent', () => {
  let productDetailsTabComponent: ProductDetailsTabComponent;
  let fixture: ComponentFixture<ProductDetailsTabComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ProductDetailsTabComponent],
      providers: [
        {
          provide: CurrentProductService,
          useClass: MockCurrentProductService
        }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProductDetailsTabComponent);
    productDetailsTabComponent = fixture.componentInstance;
  });

  it('should create', () => {
    expect(productDetailsTabComponent).toBeTruthy();
  });

  it('should get product', () => {
    productDetailsTabComponent.ngOnInit();
    let result: Product | ICustomProduct;
    productDetailsTabComponent.product$.subscribe(product => (result = product));
    expect(result).toEqual(mockProduct);
  });
});
