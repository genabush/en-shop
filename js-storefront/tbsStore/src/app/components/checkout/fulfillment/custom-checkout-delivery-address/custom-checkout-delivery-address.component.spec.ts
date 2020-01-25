import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { DebugElement } from '@angular/core';
import { By } from '@angular/platform-browser';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { MatSelectModule, MatInputModule, MatCheckboxModule } from '@angular/material';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

// spartacus
import {
  CartService,
  RoutingConfig,
  CartDataService,
  TranslationService,
  UserService,
  GlobalMessageService,
  BaseSiteService,
  LanguageService
} from '@spartacus/core';

// vendor
import { Store } from '@ngrx/store';
import { of } from 'rxjs';

// components
import { MockCxGenericLinkComponent, MockCxIconComponent, MockStore } from 'src/app/testing/mock.components';
import { CustomCheckoutDeliveryAddressComponent } from './custom-checkout-delivery-address.component';
import { CustomCheckoutAddressFormComponent } from '../../components/custom-checkout-address-form/custom-checkout-address-form.component';
import { CustomCheckoutAddressCardsComponent } from '../../components/custom-checkout-address-cards/custom-checkout-address-cards.component';

// services
import { CustomCheckoutDeliveryService } from 'src/app/services/checkout/delivery/custom-checkout-delivery.service';
import { CardModule, SpinnerModule, CheckoutConfig, CheckoutConfigService } from '@spartacus/storefront';
import { CustomUserAddressService } from 'src/app/services/checkout/custom-user-address.service';
import {
  MockCustomCartService,
  MockRoutingConfig,
  MockCheckoutConfigService,
  MockTranslationService,
  MockUserService,
  MockGlobalMessagingService,
  MockBaseSiteService,
  MockCustomDeliveryService,
  MockCartService,
  MockCheckoutConfig,
  MockLanguageService
} from 'src/app/testing/mock.services';

// pipes
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';
import { AddressFormShowFieldPipe } from 'src/app/pipes/address-form-showfield/address-form-show-field.pipe';
import { AddressSelectCardPipe } from 'src/app/pipes/address-select-card/address-select-card.pipe';

// constants
import { DUMMY_ADDRESS_DEFAULT } from 'src/app/testing/mock.constants';
import { CustomCartService } from 'src/app/services/cart/facade/cart.service';
import { OccService } from 'src/app/services/occ/occ.service';
import { MockOccService } from 'src/app/testing/occ/occ.service.mock';

describe('CustomCheckoutDeliveryAddressComponent', () => {
  let component: CustomCheckoutDeliveryAddressComponent;
  let fixture: ComponentFixture<CustomCheckoutDeliveryAddressComponent>;
  let baseSiteService: BaseSiteService;
  let userAddressService: CustomUserAddressService;
  let checkoutDeliveryService: CustomCheckoutDeliveryService;
  let languageService: LanguageService;
  let userAddressLoadingSpy: jasmine.Spy;
  let userAddressGetSpy: jasmine.Spy;
  let userAddressGetDeliverySpy: jasmine.Spy;
  let userAddressGetCountriesSpy: jasmine.Spy;
  let userAddressGetRegionsSpy: jasmine.Spy;
  let baseSiteSpy: jasmine.Spy;
  let siteLanguageSpy: jasmine.Spy;
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        NgbModule,
        CardModule,
        SpinnerModule,
        ReactiveFormsModule,
        MatSelectModule,
        MatCheckboxModule,
        MatInputModule,
        RouterTestingModule.withRoutes([]),
        HttpClientTestingModule,
        BrowserAnimationsModule
      ],
      declarations: [
        CustomCheckoutDeliveryAddressComponent,
        CustomCheckoutAddressFormComponent,
        CustomCheckoutAddressCardsComponent,
        MockCxGenericLinkComponent,
        MockCxTranslatePipe,
        MockCxIconComponent,
        AddressFormShowFieldPipe,
        AddressSelectCardPipe
      ],
      providers: [
        { provide: Store, useClass: MockStore },
        { provide: OccService, useClass: MockOccService },
        { provide: CustomCartService, useClass: MockCustomCartService },
        { provide: OccService, useClass: MockOccService },
        { provide: RoutingConfig, useClass: MockRoutingConfig },
        { provide: CartDataService, useClass: MockCartService },
        { provide: CheckoutConfigService, useClass: MockCheckoutConfigService },
        { provide: CheckoutConfig, useClass: MockCheckoutConfig },
        { provide: TranslationService, useClass: MockTranslationService },
        { provide: LanguageService, useClass: MockLanguageService },
        { provide: UserService, useClass: MockUserService },
        { provide: BaseSiteService, useClass: MockBaseSiteService },
        { provide: GlobalMessageService, useClass: MockGlobalMessagingService },
        { provide: CustomCheckoutDeliveryService, useClass: MockCustomDeliveryService },
        { provide: CustomCartService, useClass: MockCustomCartService }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomCheckoutDeliveryAddressComponent);
    component = fixture.componentInstance;
    // service injectors
    baseSiteService = fixture.componentRef.injector.get(BaseSiteService);
    userAddressService = fixture.componentRef.injector.get(CustomUserAddressService);
    checkoutDeliveryService = fixture.componentRef.injector.get(CustomCheckoutDeliveryService);
    languageService = fixture.componentRef.injector.get(LanguageService);
    // spies
    baseSiteSpy = spyOn(baseSiteService, 'getActive').and.returnValue(of('thebodyshop-uk'));
    spyOn(checkoutDeliveryService, 'getAddressVerificationResults').and.returnValue(of());
    userAddressGetSpy = spyOn(userAddressService, 'getAddresses').and.returnValue(of([DUMMY_ADDRESS_DEFAULT]));
    userAddressLoadingSpy = spyOn(userAddressService, 'getAddressesLoading').and.returnValue(of(false));
    userAddressGetDeliverySpy = spyOn(checkoutDeliveryService, 'getDeliveryAddress').and.returnValue(
      of(DUMMY_ADDRESS_DEFAULT)
    );
    userAddressGetCountriesSpy = spyOn(userAddressService, 'getDeliveryCountries').and.returnValue(
      of([{ isocode: 'GB', name: 'United Kingdom' }])
    );
    userAddressGetRegionsSpy = spyOn(userAddressService, 'getRegions').and.returnValue(of([]));
    siteLanguageSpy = spyOn(languageService, 'getActive').and.returnValue(of('en_GB'));
  });

  it('should CREATE', async(() => {
    fixture.detectChanges();
    fixture.whenStable().then(() => {
      expect(component).toBeTruthy();
    });
  }));
  it('should SHOW an Add New Address Button', async(() => {
    userAddressGetSpy.and.returnValue(of([]));
    fixture.detectChanges();
    fixture.whenStable().then(() => {
      fixture.detectChanges();
      expect(fixture.debugElement.query(By.css('.mat-form-field-address-overlay'))).toBeTruthy();
    });
  }));
  it('should SHOW a NEW ADDRESS MODAL when there is ADDRESS SELECTED and ADD is CLICKED', async(() => {
    userAddressGetSpy.and.returnValue(of([]));
    fixture.detectChanges();
    fixture.whenStable().then(() => {
      fixture.detectChanges();
      const addButton: DebugElement = fixture.debugElement.query(By.css('.mat-form-field-address-overlay'));
      addButton.nativeElement.dispatchEvent(new MouseEvent('click'));
      fixture.whenStable().then(() => {
        fixture.detectChanges();
        const modalWindow = document.querySelector('.checkout-shipping-address-modal');
        expect(modalWindow).toBeTruthy();
      });
    });
  }));
  it('should SHOW an EDIT ADDRESS MODAL when there is ADDRESS SELECTED and EDIT is CLICKED', async(() => {
    userAddressGetSpy.and.returnValue(of([DUMMY_ADDRESS_DEFAULT]));
    fixture.detectChanges();
    fixture.whenStable().then(() => {
      fixture.detectChanges();
      const editButton: DebugElement = fixture.debugElement.query(By.css('.mat-form-field-address-btn-edit'));
      editButton.nativeElement.dispatchEvent(new MouseEvent('click'));
      fixture.whenStable().then(() => {
        fixture.detectChanges();
        const modalWindow = document.querySelector('.checkout-shipping-address-modal');
        expect(modalWindow).toBeTruthy();
        expect((document.querySelector('.mat-form-control[name="firstName"]') as HTMLInputElement).value).toBe(
          DUMMY_ADDRESS_DEFAULT.firstName
        );
        expect((document.querySelector('.mat-form-control[name="lastName"]') as HTMLInputElement).value).toBe(
          DUMMY_ADDRESS_DEFAULT.lastName
        );
        expect((document.querySelector('.mat-form-control[name="companyName"]') as HTMLInputElement).value).toBe(
          DUMMY_ADDRESS_DEFAULT.companyName
        );
        expect((document.querySelector('.mat-form-control[name="phone"]') as HTMLInputElement).value).toBe(
          DUMMY_ADDRESS_DEFAULT.phone
        );
      });
    });
  }));
  afterEach(() => {
    if (component.openModalRef) {
      component.openModalRef.close();
    }
    component.ngOnDestroy();
    fixture.destroy();
  });
});
