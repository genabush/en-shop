import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CartLoyaltyPointsComponent } from './cart-loyalty-points.component';
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';
import { CustomCartService } from 'src/app/services/cart/facade/cart.service';
import { MockCustomCartService } from 'src/app/testing/mock.services';
import { DUMMY_ACTIVE_CART } from 'src/app/testing/mock.constants';
import { of } from 'rxjs';

describe('CartLoyaltyPointsComponent', () => {
  let component: CartLoyaltyPointsComponent;
  let fixture: ComponentFixture<CartLoyaltyPointsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CartLoyaltyPointsComponent, MockCxTranslatePipe],
      providers: [{ provide: CustomCartService, useClass: MockCustomCartService }]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CartLoyaltyPointsComponent);
    component = fixture.componentInstance;
    component.cart = DUMMY_ACTIVE_CART;
    fixture.detectChanges();
  });

  it('should CREATE', () => {
    expect(component).toBeTruthy();
  });
});
