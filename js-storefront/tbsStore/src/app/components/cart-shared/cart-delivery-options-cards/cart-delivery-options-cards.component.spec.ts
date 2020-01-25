import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CartDeliveryOptionsCardsComponent } from './cart-delivery-options-cards.component';

describe('CartDeliveryOptionsCardsComponent', () => {
  let component: CartDeliveryOptionsCardsComponent;
  let fixture: ComponentFixture<CartDeliveryOptionsCardsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CartDeliveryOptionsCardsComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CartDeliveryOptionsCardsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should CREATE', () => {
    expect(component).toBeTruthy();
  });
});
