import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CheckoutFulfillmentNavTabsComponent } from './checkout-fulfillment-nav-tabs.component';
import { MatRadioModule } from '@angular/material';
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';

describe('CheckoutFulfillmentNavTabsComponent', () => {
  let component: CheckoutFulfillmentNavTabsComponent;
  let fixture: ComponentFixture<CheckoutFulfillmentNavTabsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [MatRadioModule],
      declarations: [CheckoutFulfillmentNavTabsComponent, MockCxTranslatePipe]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CheckoutFulfillmentNavTabsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should CREATE', () => {
    expect(component).toBeTruthy();
  });
});
