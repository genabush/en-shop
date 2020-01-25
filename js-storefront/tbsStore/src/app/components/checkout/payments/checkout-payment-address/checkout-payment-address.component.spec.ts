import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ReactiveFormsModule } from '@angular/forms';

// vendor
import { Store } from '@ngrx/store';
import { of } from 'rxjs';

import { MatCheckboxModule, MatSelectModule } from '@angular/material';

// spartacus
import { CardModule, SpinnerModule } from '@spartacus/storefront';
import { TranslationService, CartDataService, OccConfig } from '@spartacus/core';

// components
import { CheckoutPaymentAddressComponent } from './checkout-payment-address.component';
import { CustomCheckoutAddressCardsComponent } from '../../components/custom-checkout-address-cards/custom-checkout-address-cards.component';
import { MockCxIconComponent, MockStore } from 'src/app/testing/mock.components';

// services
import { MockTranslationService, MockCustomCartService, MockCartDataService } from 'src/app/testing/mock.services';
import { OccService } from 'src/app/services/occ/occ.service';
import { MockOccService } from 'src/app/testing/occ/occ.service.mock';
import { CustomCartService } from 'src/app/services/cart/facade/cart.service';
// pipes
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';
import { AddressSelectCardPipe } from 'src/app/pipes/address-select-card/address-select-card.pipe';

// constants
import { DUMMY_ADDRESS_DEFAULT } from 'src/app/testing/mock.constants';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('CheckoutPaymentAddressComponent', () => {
  let component: CheckoutPaymentAddressComponent;
  let fixture: ComponentFixture<CheckoutPaymentAddressComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        BrowserAnimationsModule,
        ReactiveFormsModule,
        MatSelectModule,
        MatCheckboxModule,
        CardModule,
        SpinnerModule
      ],
      declarations: [
        CheckoutPaymentAddressComponent,
        CustomCheckoutAddressCardsComponent,
        MockCxTranslatePipe,
        MockCxIconComponent,
        AddressSelectCardPipe
      ],
      providers: [
        { provide: TranslationService, useClass: MockTranslationService },
        { provide: Store, useClass: MockStore },
        { provide: OccService, useClass: MockOccService },
        { provide: CustomCartService, useClass: MockCustomCartService },
        { provide: CartDataService, useClass: MockCartDataService }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CheckoutPaymentAddressComponent);
    component = fixture.componentInstance;
    component.shippingAddress$ = of(DUMMY_ADDRESS_DEFAULT);
    component.existingAddresses$.next([DUMMY_ADDRESS_DEFAULT]);
    fixture.detectChanges();
  });

  it('should CREATE', () => {
    expect(component).toBeTruthy();
  });
});
