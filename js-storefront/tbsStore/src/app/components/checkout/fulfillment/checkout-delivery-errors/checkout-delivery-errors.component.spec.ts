import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CheckoutDeliveryErrorsComponent } from './checkout-delivery-errors.component';
import { MockTranslatePipe } from '@spartacus/core';
import { MockCxGenericLinkComponent } from 'src/app/testing/mock.components';

describe('CheckoutDeliveryErrorsComponent', () => {
  let component: CheckoutDeliveryErrorsComponent;
  let fixture: ComponentFixture<CheckoutDeliveryErrorsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [],
      declarations: [CheckoutDeliveryErrorsComponent, MockTranslatePipe, MockCxGenericLinkComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CheckoutDeliveryErrorsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should CREATE', () => {
    expect(component).toBeTruthy();
  });
});
