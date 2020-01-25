import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { Product } from '@spartacus/core';
import { ModalService } from '@spartacus/storefront';

import { Observable, of } from 'rxjs';

import { CurrentProductService } from '../../current-product.service';

import { ProductKeyIngredientsTabComponent } from './product-key-ingredients.component';
import { MockCxIconComponent, MockCxGenericLinkComponent } from 'src/app/testing/mock.components';

import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';

import { ICustomProduct } from 'src/app/interfaces/custom-product-item.interface';

const mockProduct: Product = { name: 'mockProduct' };
class MockCurrentProductService {
  getProduct(): Observable<Product> {
    return of(mockProduct);
  }
}

class MockModalService {
  open(param1, param2) {
    return [param1, param2];
  }
}

describe('ProductKeyIngredientsTabComponent', () => {
  let component: ProductKeyIngredientsTabComponent;
  let fixture: ComponentFixture<ProductKeyIngredientsTabComponent>;
  let modalService: ModalService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        ProductKeyIngredientsTabComponent,
        MockCxTranslatePipe,
        MockCxGenericLinkComponent,
        MockCxIconComponent
      ],
      providers: [
        {
          provide: CurrentProductService,
          useClass: MockCurrentProductService
        },
        {
          provide: ModalService,
          useClass: MockModalService
        }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProductKeyIngredientsTabComponent);
    component = fixture.componentInstance;
    modalService = TestBed.get(ModalService);
  });

  it('should create', () => {
    expect(ProductKeyIngredientsTabComponent).toBeTruthy();
  });

  it('should get product', () => {
    component.ngOnInit();
    let result: Product | ICustomProduct;
    component.product$.subscribe(product => (result = product));
    expect(result).toEqual(mockProduct);
  });
  it('should open modal', () => {
    const spy = spyOn(modalService, 'open');
    component.openFullListIngredientsModal({});
    expect(spy).toHaveBeenCalled();
  });
});
