import { Component, Input } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { CheckoutService, I18nTestingModule, Order } from '@spartacus/core';
import { Observable, of } from 'rxjs';
import createSpy = jasmine.createSpy;
import { CustomOrderConfirmationItemsComponent } from './custom-order-confirmation-items.component';
import { Item } from '@spartacus/storefront';

@Component({ selector: 'cx-cart-item-list', template: '' })
class MockReviewSubmitComponent {
  @Input()
  items: Item[];
  @Input()
  isReadOnly: boolean;
}

class MockCheckoutService {
  clearCheckoutData = createSpy();

  getOrderDetails(): Observable<Order> {
    return of({
      entries: [
        {
          entryNumber: 1,
          quantity: 1
        }
      ]
    });
  }
}

describe('Custom Order Confirmation Items Component', () => {
  let component: CustomOrderConfirmationItemsComponent;
  let fixture: ComponentFixture<CustomOrderConfirmationItemsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [I18nTestingModule],
      declarations: [CustomOrderConfirmationItemsComponent, MockReviewSubmitComponent],
      providers: [{ provide: CheckoutService, useClass: MockCheckoutService }]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomOrderConfirmationItemsComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    component.ngOnInit();
    expect(component).toBeTruthy();
  });
});
