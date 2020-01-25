import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomCheckoutCollectInStoreComponent } from './custom-checkout-collect-in-store.component';
import { CustomCisFinderComponent } from '../custom-cis-finder/custom-cis-finder.component';
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';
import { Component, Input } from '@angular/core';
import { MatSelectModule, MatInputModule } from '@angular/material';
import { MockCxIconComponent, MockStore, MockCollectionPointComponent } from 'src/app/testing/mock.components';
import { AgmCoreModule } from '@agm/core';
import { MapInfoWindowComponent } from '../../components/map-info-window/map-info-window.component';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { OccConfig, BaseSiteService, CartDataService } from '@spartacus/core';
import {
  MockBaseSiteService,
  MockCustomCartService,
  MockGeocodeService,
  MockCartDataService
} from 'src/app/testing/mock.services';
import { CustomCartService } from 'src/app/services/cart/facade/cart.service';
import { Store } from '@ngrx/store';
import { GeocodeService } from 'src/app/services/google/geocode.service';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CollectionPointItemModule } from '../collection-point-item/collection-point-item.module';
import { MockOccConfig } from 'src/app/testing/occ/occ.config.mock';

describe('Checkout Collect In Store Component', () => {
  let component: CustomCheckoutCollectInStoreComponent;
  let fixture: ComponentFixture<CustomCheckoutCollectInStoreComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        MatSelectModule,
        AgmCoreModule,
        ReactiveFormsModule,
        HttpClientTestingModule,
        MatInputModule,
        BrowserAnimationsModule,
        CollectionPointItemModule
      ],
      declarations: [
        CustomCheckoutCollectInStoreComponent,

        CustomCisFinderComponent,
        MockCxTranslatePipe,
        MockCxIconComponent
      ],
      providers: [
        { provide: OccConfig, useClass: MockOccConfig },
        { provide: BaseSiteService, useClass: MockBaseSiteService },
        { provide: CustomCartService, useClass: MockCustomCartService },
        { provide: Store, useClass: MockStore },
        { provide: GeocodeService, useClass: MockGeocodeService },
        { provide: CartDataService, useClass: MockCartDataService }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomCheckoutCollectInStoreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
