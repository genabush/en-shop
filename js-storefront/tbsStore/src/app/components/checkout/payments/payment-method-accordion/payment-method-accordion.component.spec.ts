import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import {
  MatRadioModule,
  MatInputModule,
  MatCheckboxModule,
  MatRadioButton,
  MatLabel,
  MatFormField
} from '@angular/material';
import { HttpClientTestingModule } from '@angular/common/http/testing';

// spartacus
import { SpinnerModule } from '@spartacus/storefront';
import { BaseSiteService, OccConfig } from '@spartacus/core';

// vendor
import { NgbAccordionModule, NgbTabsetModule } from '@ng-bootstrap/ng-bootstrap';
import { NgxPayPalModule } from 'ngx-paypal';

// components
import { PaymentMethodAccordionComponent } from './payment-method-accordion.component';
import { AdyenSavedCreditCardsComponent } from '../adyen-saved-credit-cards/adyen-saved-credit-cards.component';
import { AdyenCreditCardsComponent } from '../adyen-credit-cards/adyen-credit-cards.component';
import { PaypalComponent } from '../paypal/paypal.component';

// services
import { MockBaseSiteService } from 'src/app/testing/mock.services';

// pipes
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';
import { MockComponent } from 'ng-mocks';
import { AdyenIdealComponent } from 'src/app/components/checkout/payments/adyen-ideal/adyen-ideal.component';
import { AdyenGiropayComponent } from 'src/app/components/checkout/payments/adyen-giropay/adyen-giropay.component';
import { MockOccConfig } from 'src/app/testing/occ/occ.config.mock';
import { KlarnaComponent } from 'src/app/components/checkout/payments/klarna/klarna.component';

describe('PaymentMethodAccordionComponent', () => {
  let component: PaymentMethodAccordionComponent;
  let fixture: ComponentFixture<PaymentMethodAccordionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ReactiveFormsModule, NgbAccordionModule, NgbTabsetModule],
      declarations: [
        PaymentMethodAccordionComponent,
        MockComponent(AdyenCreditCardsComponent),
        MockComponent(AdyenSavedCreditCardsComponent),
        MockComponent(PaypalComponent),
        MockComponent(AdyenIdealComponent),
        MockComponent(AdyenGiropayComponent),
        MockComponent(MatRadioButton),
        MockComponent(MatLabel),
        MockComponent(MatFormField),
        MockComponent(KlarnaComponent),
        MockCxTranslatePipe
      ],
      providers: [
        { provide: BaseSiteService, useClass: MockBaseSiteService },
        { provide: OccConfig, useClass: MockOccConfig }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PaymentMethodAccordionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should CREATE', () => {
    expect(component).toBeTruthy();
  });
});
