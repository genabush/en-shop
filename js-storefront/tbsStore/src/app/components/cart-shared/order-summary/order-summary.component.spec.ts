import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

// spartacus
import { CartDataService, BaseSiteService, OccConfig } from '@spartacus/core';

// vendors
import { Store } from '@ngrx/store';

// components
import { OrderSummaryComponent } from './order-summary.component';
import { CartLoyaltyPointsComponent } from '../cart-loyalty-points/cart-loyalty-points.component';
import { MockCxIconComponent, MockStore, MockPromotionFreeDeliveryComponent } from 'src/app/testing/mock.components';
import { CartDeliveryOptionsCardsComponent } from '../cart-delivery-options-cards/cart-delivery-options-cards.component';

// services
import { MockCustomCartService, MockCartDataService, MockBaseSiteService } from 'src/app/testing/mock.services';
import { CustomCartService } from 'src/app/services/cart/facade/cart.service';

// pipes
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';
import { MockOccConfig } from 'src/app/testing/occ/occ.config.mock';

describe('OrderSummaryComponent', () => {
  let component: OrderSummaryComponent;
  let fixture: ComponentFixture<OrderSummaryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [
        OrderSummaryComponent,
        CartLoyaltyPointsComponent,
        CartDeliveryOptionsCardsComponent,
        MockPromotionFreeDeliveryComponent,
        MockCxTranslatePipe,
        MockCxIconComponent
      ],
      providers: [
        { provide: BaseSiteService, useClass: MockBaseSiteService },
        { provide: OccConfig, useClass: MockOccConfig },
        { provide: CustomCartService, useClass: MockCustomCartService },
        { provide: CartDataService, useClass: MockCartDataService },
        { provide: Store, useClass: MockStore }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OrderSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should CREATE', () => {
    expect(component).toBeTruthy();
  });
});
