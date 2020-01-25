import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

// vendor
import { NgbAccordionModule } from '@ng-bootstrap/ng-bootstrap';
import { MockComponent } from 'ng-mocks';

// components
import { CheckoutBasketSummaryComponent } from './checkout-basket-summary.component';
import { CartItemListComponent } from 'src/app/components/cart-shared/cart-item-list/cart-item-list.component';

// services
import { CustomCartService } from 'src/app/services/cart/facade/cart.service';
import { MockCustomCartService } from 'src/app/testing/mock.services';

// pipes
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';

describe('CheckoutBasketSummaryComponent', () => {
  let component: CheckoutBasketSummaryComponent;
  let fixture: ComponentFixture<CheckoutBasketSummaryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [BrowserAnimationsModule, NgbAccordionModule],
      declarations: [CheckoutBasketSummaryComponent, MockComponent(CartItemListComponent), MockCxTranslatePipe],
      providers: [{ provide: CustomCartService, useClass: MockCustomCartService }]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CheckoutBasketSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should CREATE', () => {
    expect(component).toBeTruthy();
  });
});
