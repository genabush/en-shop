import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';

// vendor
import { Store } from '@ngrx/store';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { CustomMiniCartEntryItemComponent } from '../../components/mini-cart/custom-mini-cart-entry-item/custom-mini-cart-entry-item.component';

// components
import { CustomMiniCartComponent } from '../../components/mini-cart/custom-mini-cart-component/custom-mini-cart.component';
import { CustomMiniCartEntryVariantsComponent } from '../../components/mini-cart/custom-mini-cart-entry-variants/custom-mini-cart-entry-variants.component';
import {
  MockCxIconComponent,
  MockCxMediaComponent,
  MockStore,
  MockCmsComponentData,
  MockPromotionFreeDeliveryComponent
} from '../../testing/mock.components';

// pipes
import { MockCxTranslatePipe, MockCxUrlPipe } from '../../testing/mock.pipes';

// services
import { CustomCartService } from '../../services/cart/facade/cart.service';
import { ModalService, CmsComponentData } from '@spartacus/storefront';
import { BaseSiteService, CartService } from '@spartacus/core';
import { MockBaseSiteService, MockCustomCartService } from '../../testing/mock.services';
import { OccService } from '../../services/occ/occ.service';
import { MockOccService } from 'src/app/testing/occ/occ.service.mock';

describe('CustomMiniCartDirective', () => {
  let component: CustomMiniCartComponent;
  let fixture: ComponentFixture<CustomMiniCartComponent>;
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [NgbModule, RouterTestingModule.withRoutes([])],
      declarations: [
        CustomMiniCartComponent,
        CustomMiniCartEntryItemComponent,
        CustomMiniCartEntryVariantsComponent,
        MockCxIconComponent,
        MockPromotionFreeDeliveryComponent,
        MockCxMediaComponent,
        MockCxUrlPipe,
        MockCxTranslatePipe
      ],
      providers: [
        { provide: Store, useClass: MockStore },
        {
          provide: ModalService,
          useClass: ModalService
        },
        {
          provide: CmsComponentData,
          useClass: MockCmsComponentData
        },
        { provide: BaseSiteService, useClass: MockBaseSiteService },
        { provide: OccService, useClass: MockOccService },
        { provide: CartService, useClass: MockCustomCartService },
        { provide: CustomCartService, useClass: MockCustomCartService }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomMiniCartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should CREATE an instance', async(() => {
    expect(component.appCustomCartDirective).toBeTruthy();
  }));
});
