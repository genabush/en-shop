/* tslint: disable */
import { async, ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { RouterTestingModule } from '@angular/router/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';

// spartacus
import { CartDataService, RoutingConfig, CheckoutDeliveryService, CartService, BaseSiteService } from '@spartacus/core';
import {
  MockCartDataService,
  MockRoutingConfig,
  MockCheckoutConfig,
  MockCheckoutConfigService,
  MockCheckoutDeliveryService,
  MockCustomCartService,
  MockBaseSiteService
} from 'src/app/testing/mock.services';
import { CheckoutConfig, CheckoutConfigService, CardModule } from '@spartacus/storefront';

// vendor
import { MatSelectModule } from '@angular/material';
import { of } from 'rxjs';
import { NgbModalModule } from '@ng-bootstrap/ng-bootstrap';
import { Store } from '@ngrx/store';

// components
import { CustomDeliveryModeComponent } from './custom-checkout-delivery-mode.component';
import { MockCxSpinnerComponent, MockStore, MockCxIconComponent } from 'src/app/testing/mock.components';
import { CustomCheckoutDeliveryModesCardsComponent } from '../custom-checkout-delivery-modes-cards/custom-checkout-delivery-modes-cards.component';

//services
import { OccService } from 'src/app/services/occ/occ.service';
import { CustomCheckoutDeliveryService } from 'src/app/services/checkout/delivery/custom-checkout-delivery.service';
import { CustomCartService } from 'src/app/services/cart/facade/cart.service';

// pipes
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';
import { DeliveryModesSelectedCardPipe } from 'src/app/pipes/delivery-modes-card/delivery-modes-selected-card.pipe';
import { DeliveryModesCardPipe } from 'src/app/pipes/delivery-modes-card/delivery-modes-card.pipe';

// constants
import { DUMMY_ADDRESS_DEFAULT } from 'src/app/testing/mock.constants';
import { MockOccService } from 'src/app/testing/occ/occ.service.mock';

describe('CustomDeliveryModeComponent', () => {
  let component: CustomDeliveryModeComponent;
  let fixture: ComponentFixture<CustomDeliveryModeComponent>;
  let checkoutDeliveryService: CustomCheckoutDeliveryService;
  let checkoutDeliverySelectedSpy: jasmine.Spy;
  let checkoutDeliverySpy: jasmine.Spy;
  let checkoutDeliveryAddressSpy: jasmine.Spy;
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        ReactiveFormsModule,
        RouterTestingModule.withRoutes([]),
        HttpClientTestingModule,
        MatSelectModule,
        NgbModalModule,
        CardModule
      ],
      declarations: [
        CustomDeliveryModeComponent,
        MockCxIconComponent,
        MockCxTranslatePipe,
        DeliveryModesCardPipe,
        DeliveryModesSelectedCardPipe,
        MockCxSpinnerComponent,
        CustomCheckoutDeliveryModesCardsComponent
      ],
      providers: [
        { provide: Store, useClass: MockStore },
        { provide: CartDataService, useClass: MockCartDataService },
        { provide: RoutingConfig, useClass: MockRoutingConfig },
        { provide: CheckoutConfig, useClass: MockCheckoutConfig },
        { provide: CheckoutConfigService, useClass: MockCheckoutConfigService },
        { provide: CheckoutDeliveryService, useClass: MockCheckoutDeliveryService },
        { provide: CustomCartService, useClass: MockCustomCartService },
        { provide: BaseSiteService, useClass: MockBaseSiteService },
        { provide: OccService, useClass: MockOccService }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomDeliveryModeComponent);
    component = fixture.componentInstance;
    checkoutDeliveryService = fixture.componentRef.injector.get(CustomCheckoutDeliveryService);
    checkoutDeliveryAddressSpy = spyOn(checkoutDeliveryService, 'getDeliveryAddress').and.returnValue(
      of(DUMMY_ADDRESS_DEFAULT)
    );
    checkoutDeliverySelectedSpy = spyOn(checkoutDeliveryService, 'getSelectedDeliveryMode').and.returnValue(of());
    checkoutDeliverySpy = spyOn(checkoutDeliveryService, 'getApiSupportedDeliveryModes').and.returnValue(of());
  });

  it('should CREATE', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });
  afterEach(() => {
    component.ngOnDestroy();
    fixture.destroy();
    checkoutDeliverySpy = undefined;
  });
});
