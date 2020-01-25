import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientTestingModule } from '@angular/common/http/testing';

// spartacus
import { CartDataService } from '@spartacus/core';

// vendor
import { MockComponent } from 'ng-mocks';
import { Store } from '@ngrx/store';
import { AgmCoreModule } from '@agm/core';
import { MatInputModule, MatFormFieldModule, MatSelectModule } from '@angular/material';

// components
import { CustomCheckoutCollectionPointComponent } from './custom-checkout-collection-point.component';
import { CustomAddressFinderComponent } from '../custom-address-finder/custom-address-finder.component';
import { WhoWillCollectComponent } from '../who-will-collect/who-will-collect.component';
import { MockCxIconComponent, MockStore } from 'src/app/testing/mock.components';
import { CollectionPointItemComponent } from '../collection-point-item/collection-point-item.component';
import { MapInfoWindowComponent } from '../../components/map-info-window/map-info-window.component';
import { MapInfoWindowAltComponent } from '../../components/map-info-window-alt/map-info-window-alt.component';

// services
import {
  MockCustomCartService,
  MockGeocodeService,
  MockCollectionPointsService,
  MockCartDataService
} from 'src/app/testing/mock.services';
import { OccService } from 'src/app/services/occ/occ.service';
import { CustomCartService } from 'src/app/services/cart/facade/cart.service';
import { GeocodeService } from 'src/app/services/google/geocode.service';
import { CollectionPointsService } from 'src/app/services/checkout/collection-points/collection-points.service';

// pipes
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';
import { MockOccService } from 'src/app/testing/occ/occ.service.mock';

describe('Checkout Collection Point Component', () => {
  let component: CustomCheckoutCollectionPointComponent;
  let fixture: ComponentFixture<CustomCheckoutCollectionPointComponent>;
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        AgmCoreModule,
        MatInputModule,
        MatSelectModule,
        MatFormFieldModule,
        ReactiveFormsModule,
        BrowserAnimationsModule,
        HttpClientTestingModule
      ],
      declarations: [
        CustomCheckoutCollectionPointComponent,
        MockCxTranslatePipe,
        MockComponent(MapInfoWindowComponent),
        MockComponent(CustomAddressFinderComponent),
        MockComponent(WhoWillCollectComponent),
        MockComponent(CollectionPointItemComponent),
        MockCxIconComponent,
        MockComponent(MapInfoWindowAltComponent)
      ],
      providers: [
        { provide: Store, useClass: MockStore },
        { provide: CustomCartService, useClass: MockCustomCartService },
        { provide: CartDataService, useClass: MockCartDataService },
        { provide: GeocodeService, useClass: MockGeocodeService },
        { provide: CollectionPointsService, useClass: MockCollectionPointsService },
        { provide: OccService, useClass: MockOccService },
        { provide: Store, useClass: MockStore }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomCheckoutCollectionPointComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should CREATE', () => {
    expect(component).toBeTruthy();
  });
});
