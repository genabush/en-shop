import { MockCxTranslatePipe } from './../../../../testing/mock.pipes';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { CustomProductListItemComponent } from './custom-product-list-item.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Input, Component, Pipe, PipeTransform } from '@angular/core';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { Store } from '@ngrx/store';
import { ProductService, BaseSiteService } from '@spartacus/core';
import { OccService } from '../../../../services/occ/occ.service';
import {
  MockCxMediaComponent,
  MockStore,
  MockCxStartRatingComponent,
  MockCxAddToCartComponent
} from 'src/app/testing/mock.components';
import { MockBaseSiteService } from 'src/app/testing/mock.services';
import { MockCxUrlPipe } from 'src/app/testing/mock.pipes';
import { MockOccService } from 'src/app/testing/occ/occ.service.mock';
import { MockComponent } from 'ng-mocks';

class MockProductService {}

const mockProduct = {
  images: '/test.jpg',
  summary: '',
  nameHtml: 'test',
  stock: { stockLevelStatus: '' },
  code: 123
};
describe('CustomProductListItemComponent', () => {
  let component: CustomProductListItemComponent;
  let fixture: ComponentFixture<CustomProductListItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule, HttpClientTestingModule],
      declarations: [
        CustomProductListItemComponent,
        MockCxMediaComponent,
        MockCxStartRatingComponent,
        MockComponent(MockCxAddToCartComponent),
        MockCxUrlPipe,
        MockCxTranslatePipe
      ],
      providers: [
        { provide: Store, useClass: MockStore },
        { provide: ProductService, useClass: MockProductService },
        { provide: BaseSiteService, useClass: MockBaseSiteService },
        { provide: OccService, useClass: MockOccService }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomProductListItemComponent);
    component = fixture.componentInstance;
    component.product = mockProduct;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
