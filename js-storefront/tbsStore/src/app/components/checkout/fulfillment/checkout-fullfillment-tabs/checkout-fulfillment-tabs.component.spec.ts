import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { AgmCoreModule } from '@agm/core';

// vendor
import { MatSelectModule, MatCheckboxModule, MatRadioModule } from '@angular/material';
import { NgbAlertModule } from '@ng-bootstrap/ng-bootstrap';

// spartacus
import { SpinnerModule, CardModule } from '@spartacus/storefront';

// components
import { MockCxIconComponent, MockCxGenericLinkComponent } from 'src/app/testing/mock.components';
import { CustomCheckoutFulfillmentTabsComponent } from './checkout-fulfillment-tabs.component';
import { CustomCheckoutDeliveryAddressComponent } from '../custom-checkout-delivery-address/custom-checkout-delivery-address.component';
import { CustomCheckoutAddressFormComponent } from '../../components/custom-checkout-address-form/custom-checkout-address-form.component';
import { CustomCheckoutCollectionPointComponent } from '../custom-checkout-collection-point/custom-checkout-collection-point.component';
import { CustomCheckoutAddressCardsComponent } from '../../components/custom-checkout-address-cards/custom-checkout-address-cards.component';
import { CustomAddressFinderComponent } from '../custom-address-finder/custom-address-finder.component';
import { WhoWillCollectComponent } from '../who-will-collect/who-will-collect.component';
import { CollectionPointItemComponent } from '../collection-point-item/collection-point-item.component';
import { MapInfoWindowComponent } from '../../components/map-info-window/map-info-window.component';
import { MapInfoWindowAltComponent } from '../../components/map-info-window-alt/map-info-window-alt.component';

// pipes
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';
import { AddressFormShowFieldPipe } from 'src/app/pipes/address-form-showfield/address-form-show-field.pipe';
import { AddressSelectCardPipe } from 'src/app/pipes/address-select-card/address-select-card.pipe';
import { CustomCheckoutCollectInStoreComponent } from '../custom-checkout-collect-in-store/custom-checkout-collect-in-store.component';
import { CustomCisFinderComponent } from '../custom-cis-finder/custom-cis-finder.component';
import { UrlModule } from '@spartacus/core';
import { RouterModule } from '@angular/router';

describe('CustomCheckoutFulfillmentTabsComponent', () => {
  let component: CustomCheckoutFulfillmentTabsComponent;
  let fixture: ComponentFixture<CustomCheckoutFulfillmentTabsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        ReactiveFormsModule,
        NgbAlertModule,
        MatSelectModule,
        MatCheckboxModule,
        MatRadioModule,
        SpinnerModule,
        CardModule,
        AgmCoreModule,
        UrlModule,
        RouterModule
      ],
      declarations: [
        CustomCheckoutFulfillmentTabsComponent,
        CustomCheckoutDeliveryAddressComponent,
        CustomCheckoutAddressFormComponent,
        CustomCheckoutAddressCardsComponent,
        CustomCheckoutCollectionPointComponent,
        CustomAddressFinderComponent,
        WhoWillCollectComponent,
        MapInfoWindowComponent,
        MapInfoWindowAltComponent,
        CollectionPointItemComponent,
        AddressFormShowFieldPipe,
        AddressSelectCardPipe,
        MockCxTranslatePipe,
        MockCxGenericLinkComponent,
        MockCxIconComponent,
        MapInfoWindowComponent,
        CustomCheckoutCollectInStoreComponent,
        CustomCisFinderComponent
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomCheckoutFulfillmentTabsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should CREATE', () => {
    expect(component).toBeTruthy();
  });
});
