import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomStoreFinderComponent } from './custom-store-finder.component';
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';
import { MatFormFieldModule } from '@angular/material';
import { ReactiveFormsModule } from '@angular/forms';

import { MockCxIconComponent, MockCmsComponentData, MockStore } from 'src/app/testing/mock.components';
import { AgmCoreModule } from '@agm/core';

import { CmsComponentData } from '@spartacus/storefront';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { OccConfig, BaseSiteService, UrlModule } from '@spartacus/core';
import { MockBaseSiteService, MockGeocodeService } from 'src/app/testing/mock.services';
import { GeocodeService } from 'src/app/services/google/geocode.service';
import { MockOccConfig } from 'src/app/testing/occ/occ.config.mock';
import { CollectionPointItemComponent } from 'src/app/components/checkout/fulfillment/collection-point-item/collection-point-item.component';
import { MapInfoWindowComponent } from 'src/app/components/checkout/components/map-info-window/map-info-window.component';
import { MapInfoWindowAltComponent } from 'src/app/components/checkout/components/map-info-window-alt/map-info-window-alt.component';
import { RouterModule } from '@angular/router';
import { Store } from '@ngrx/store';

describe('Custom Store Finder Component', () => {
  let component: CustomStoreFinderComponent;
  let fixture: ComponentFixture<CustomStoreFinderComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        ReactiveFormsModule,
        MatFormFieldModule,
        AgmCoreModule,
        HttpClientTestingModule,
        UrlModule,
        RouterModule
      ],
      declarations: [
        CustomStoreFinderComponent,
        CollectionPointItemComponent,
        MockCxTranslatePipe,
        MockCxIconComponent,
        MapInfoWindowComponent,
        MapInfoWindowAltComponent
      ],
      providers: [
        {
          provide: CmsComponentData,
          useClass: MockCmsComponentData
        },
        { provide: OccConfig, useClass: MockOccConfig },
        { provide: BaseSiteService, useClass: MockBaseSiteService },
        { provide: GeocodeService, useClass: MockGeocodeService },
        { provide: Store, useClass: MockStore }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomStoreFinderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
