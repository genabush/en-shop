import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { DebugElement } from '@angular/core';
import { By } from '@angular/platform-browser';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule, MatSelectModule, MatCheckboxModule } from '@angular/material';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClient } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';

// spartacus
import {
  CartDataService,
  UserService,
  GlobalMessageService,
  UserAddressService,
  BaseSiteService,
  LanguageService,
  TranslationService
} from '@spartacus/core';

// vendor
import { Store } from '@ngrx/store';
import { of } from 'rxjs';
import { NgbAlertModule } from '@ng-bootstrap/ng-bootstrap';

// components
import { CustomCheckoutAddressFormComponent } from './custom-checkout-address-form.component';
import { MockCxGenericLinkComponent, MockCxIconComponent, MockStore } from 'src/app/testing/mock.components';

// services
import {
  MockCustomCartService,
  MockUserService,
  MockGlobalMessagingService,
  MockBaseSiteService,
  MockTranslationService,
  MockLanguageService,
  MockCartDataService
} from 'src/app/testing/mock.services';
import { CustomCheckoutDeliveryService } from 'src/app/services/checkout/delivery/custom-checkout-delivery.service';
import { OccService } from 'src/app/services/occ/occ.service';
import { CustomUserAddressService } from 'src/app/services/checkout/custom-user-address.service';

// pipes
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';
import { AddressFormShowFieldPipe } from 'src/app/pipes/address-form-showfield/address-form-show-field.pipe';
import { MockOccService } from 'src/app/testing/occ/occ.service.mock';
import { CustomCartService } from 'src/app/services/cart/facade/cart.service';

describe('CustomCheckoutAddressFormComponent', () => {
  let component: CustomCheckoutAddressFormComponent;
  let fixture: ComponentFixture<CustomCheckoutAddressFormComponent>;
  let userAddressService: CustomUserAddressService;
  let checkoutDeliveryService: CustomCheckoutDeliveryService;
  let userAddressGetCountriesSpy: jasmine.Spy;
  let userAddressGetRegionsSpy: jasmine.Spy;
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        BrowserAnimationsModule,
        ReactiveFormsModule,
        NgbAlertModule,
        MatSelectModule,
        MatInputModule,
        MatCheckboxModule,
        HttpClientTestingModule
      ],
      declarations: [
        CustomCheckoutAddressFormComponent,
        MockCxGenericLinkComponent,
        MockCxTranslatePipe,
        MockCxIconComponent,
        AddressFormShowFieldPipe
      ],
      providers: [
        { provide: Store, useClass: MockStore },
        { provide: CustomCartService, useClass: MockCustomCartService },
        { provide: CartDataService, useClass: MockCartDataService },
        { provide: UserService, useClass: MockUserService },
        { provide: GlobalMessageService, useClass: MockGlobalMessagingService },
        { provide: BaseSiteService, useClass: MockBaseSiteService },
        { provide: OccService, useClass: MockOccService },
        { provide: LanguageService, useClass: MockLanguageService },
        { provide: TranslationService, useClass: MockTranslationService },
        HttpClient
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomCheckoutAddressFormComponent);
    component = fixture.componentInstance;
    userAddressService = fixture.componentRef.injector.get(CustomUserAddressService);
    checkoutDeliveryService = fixture.componentRef.injector.get(CustomCheckoutDeliveryService);
    spyOn(checkoutDeliveryService, 'getAddressVerificationResults').and.returnValue(of());
    userAddressGetCountriesSpy = spyOn(userAddressService, 'getDeliveryCountries').and.returnValue(
      of([{ isocode: 'GB', name: 'United Kingdom' }])
    );
    userAddressGetRegionsSpy = spyOn(userAddressService, 'getRegions').and.returnValue(of());
  });

  it('should CREATE', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  xit('should SHOW the ADDRESS FORM when the MANUAL ADDRESS ENTRY is triggered (TODO: awaiting implementation)', async(() => {
    component.manualAddressEntry = false;
    fixture.detectChanges();
    fixture.whenStable().then(() => {
      const manualButton: DebugElement = fixture.debugElement.query(By.css('.switch-manual'));
      manualButton.nativeElement.dispatchEvent(new MouseEvent('click'));
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        fixture.detectChanges();
        expect(document.querySelector('.mat-form-control[name="line1"]')).toBeTruthy();
        expect(document.querySelector('.mat-form-control[name="line2"]')).toBeTruthy();
        expect(document.querySelector('.mat-form-control[name="town"]')).toBeTruthy();
        expect(document.querySelector('.mat-form-control[name="postalCode"]')).toBeTruthy();
      });
    });
  }));
  it('should CLEAR the ADDRESS FORM when the CLEAR ADDRESS ENTRY is triggered', async(() => {
    fixture.detectChanges();
    fixture.whenStable().then(() => {
      component.manualAddressEntry = true;
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        fixture.detectChanges();

        const clearButton: DebugElement = fixture.debugElement.query(By.css('.cx-address-clear-button'));
        clearButton.nativeElement.dispatchEvent(new MouseEvent('click'));
        fixture.detectChanges();
        fixture.whenStable().then(() => {
          fixture.detectChanges();
          expect((document.querySelector('.mat-form-control[name="line1"]') as HTMLInputElement).value).toEqual('');
          expect((document.querySelector('.mat-form-control[name="line2"]') as HTMLInputElement).value).toEqual('');
          expect((document.querySelector('.mat-form-control[name="town"]') as HTMLInputElement).value).toEqual('');
          expect((document.querySelector('.mat-form-control[name="postalCode"]') as HTMLInputElement).value).toEqual(
            ''
          );
        });
      });
    });
  }));
  afterEach(() => {
    component.ngOnDestroy();
    fixture.destroy();
  });
});
