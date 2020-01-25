import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

// spartacus
import { CartDataService, UrlModule } from '@spartacus/core';

// vendor
import { MatInputModule, MatFormFieldModule } from '@angular/material';
import { AgmCoreModule } from '@agm/core';
import { Store } from '@ngrx/store';

// components
import { MockCxIconComponent, MockMapInfoAltWindowComponent, MockStore } from 'src/app/testing/mock.components';
import { CollectionPointItemComponent } from '../collection-point-item/collection-point-item.component';
import { CustomAddressFinderComponent } from './custom-address-finder.component';
import { MapInfoWindowComponent } from '../../components/map-info-window/map-info-window.component';

// services
import { OccService } from 'src/app/services/occ/occ.service';
import { CustomCartService } from 'src/app/services/cart/facade/cart.service';
import { GeocodeService } from 'src/app/services/google/geocode.service';
import { MockGeocodeService, MockCustomCartService, MockCartDataService } from 'src/app/testing/mock.services';

// pipes
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';
import { MockOccService } from 'src/app/testing/occ/occ.service.mock';
import { RouterModule } from '@angular/router';

describe('Address Finder Component', () => {
  let component: CustomAddressFinderComponent;
  let fixture: ComponentFixture<CustomAddressFinderComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        AgmCoreModule,
        BrowserAnimationsModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        HttpClientTestingModule,
        AgmCoreModule,
        UrlModule,
        RouterModule
      ],
      declarations: [
        CustomAddressFinderComponent,
        MapInfoWindowComponent,
        MockCxTranslatePipe,
        MockCxIconComponent,
        CollectionPointItemComponent,
        MapInfoWindowComponent,
        MockMapInfoAltWindowComponent
      ],
      providers: [
        { provide: CustomCartService, useClass: MockCustomCartService },
        { provide: CartDataService, useClass: MockCartDataService },
        { provide: OccService, useClass: MockOccService },
        { provide: GeocodeService, useClass: MockGeocodeService },
        { provide: Store, useClass: MockStore }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomAddressFinderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
