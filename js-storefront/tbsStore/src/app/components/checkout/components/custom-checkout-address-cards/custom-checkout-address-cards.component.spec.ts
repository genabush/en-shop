import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';

// spartacus
import { CardModule, SpinnerModule } from '@spartacus/storefront';

// vendor
import { Store } from '@ngrx/store';

// components
import { CustomCheckoutAddressCardsComponent } from './custom-checkout-address-cards.component';

// pipes
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';
import { MockCxIconComponent, MockStore } from 'src/app/testing/mock.components';
import { MatSelectModule } from '@angular/material';
import { AddressSelectCardPipe } from 'src/app/pipes/address-select-card/address-select-card.pipe';
import { TranslationService } from '@spartacus/core';
import { MockTranslationService } from 'src/app/testing/mock.services';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CustomUserAddressService } from 'src/app/services/checkout/custom-user-address.service';

export class MockCustomUserAddressService {}

describe('CustomCheckoutAddressCardsComponent', () => {
  let component: CustomCheckoutAddressCardsComponent;
  let fixture: ComponentFixture<CustomCheckoutAddressCardsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        BrowserAnimationsModule,
        ReactiveFormsModule,
        CardModule,
        SpinnerModule,
        MatSelectModule
      ],
      declarations: [
        CustomCheckoutAddressCardsComponent,
        MockCxTranslatePipe,
        MockCxIconComponent,
        AddressSelectCardPipe
      ],
      providers: [
        { provide: TranslationService, useClass: MockTranslationService },
        { provide: CustomUserAddressService, useClass: MockCustomUserAddressService },
        { provide: Store, useClass: MockStore }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomCheckoutAddressCardsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should CREATE', () => {
    expect(component).toBeTruthy();
  });
});
