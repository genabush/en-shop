import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';

// spartacus
import { PageLayoutService } from '@spartacus/storefront';

// services
import { CustomProductListComponentService } from './custom-product-list-component.service';
import { ContentPageSearchService } from '../../../../services/content-page-search/content-page-search.service';
import { MockContentPageSearchService, MockRoutingService } from 'src/app/testing/mock.services';

// spartacus
import { RoutingService } from '@spartacus/core';

// vendor
import { Store } from '@ngrx/store';

// components
import { CustomProductListComponent } from './custom-product-list.component';
import {
  MockCxPaginationComponent,
  MockResultCountComponent,
  MockSliderComponent,
  MockProductGridItemComponent,
  MockAmplienceComponent,
  MockPageLayoutService,
  MockCustomProductListComponentService,
  MockCxProductViewComponent,
  MockStore
} from 'src/app/testing/mock.components';

// pipes
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CustomQuantitySelectorComponent } from 'src/app/components/shared/custom-quantity-selector/custom-quantity-selector.component';
import { AddToCartComponent } from 'src/app/components/cart/add-to-cart/add-to-cart.component';

xdescribe('CustomProductListComponent (TODO implement service mocks for model$ and template$)', () => {
  let component: CustomProductListComponent;
  let fixture: ComponentFixture<CustomProductListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [BrowserAnimationsModule, RouterTestingModule.withRoutes([])],
      declarations: [
        CustomProductListComponent,
        AddToCartComponent,
        CustomQuantitySelectorComponent,
        MockProductGridItemComponent,
        MockAmplienceComponent,
        MockCxPaginationComponent,
        MockCxProductViewComponent,
        MockResultCountComponent,
        MockSliderComponent,
        MockCxTranslatePipe
      ],
      providers: [
        { provide: Store, useClass: MockStore },
        { provide: PageLayoutService, useClass: MockPageLayoutService },
        { provide: CustomProductListComponentService, useClass: MockCustomProductListComponentService },
        { provide: ContentPageSearchService, useClass: MockContentPageSearchService },
        { provide: RoutingService, useClass: MockRoutingService }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomProductListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should CREATE', async(() => {
    fixture.whenStable().then(() => {
      expect(component).toBeTruthy();
    });
  }));
  afterEach(() => {
    component.ngOnDestroy();
    fixture.destroy();
  });
});
