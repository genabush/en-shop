import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatFormFieldModule, MatInputModule } from '@angular/material';
import { ReactiveFormsModule } from '@angular/forms';

// spartacus
import { CartDataService, BaseSiteService } from '@spartacus/core';

// vendor
import { Store } from '@ngrx/store';
import { AgmCoreModule } from '@agm/core';

// components
import {
  MockCxIconComponent,
  MockMapInfoWindowComponent,
  MockMapInfoAltWindowComponent,
  MockStore
} from 'src/app/testing/mock.components';
import { CollectionPointItemComponent } from '../collection-point-item/collection-point-item.component';
import { CustomCisFinderComponent } from './custom-cis-finder.component';

// services
import { OccService } from 'src/app/services/occ/occ.service';
import { CustomCartService } from '../../../../services/cart/facade/cart.service';
import { GeocodeService } from '../../../../services/google/geocode.service';
import {
  MockGeocodeService,
  MockCustomCartService,
  MockCartDataService,
  MockBaseSiteService
} from '../../../../../../src/app/testing/mock.services';

// pipes
import { MockCxTranslatePipe } from '../../../../../../src/app/testing/mock.pipes';
import { MockComponent } from 'ng-mocks';
import { MockOccService } from 'src/app/testing/occ/occ.service.mock';

describe('Collect In Store Component', () => {
  let component: CustomCisFinderComponent;
  let fixture: ComponentFixture<CustomCisFinderComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        BrowserAnimationsModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        HttpClientTestingModule,
        AgmCoreModule
      ],
      declarations: [
        CustomCisFinderComponent,
        MockCxTranslatePipe,
        MockCxIconComponent,
        MockComponent(CollectionPointItemComponent),
        MockMapInfoWindowComponent,
        MockMapInfoAltWindowComponent
      ],
      providers: [
        { provide: BaseSiteService, useClass: MockBaseSiteService },
        { provide: CustomCartService, useClass: MockCustomCartService },
        { provide: CartDataService, useClass: MockCartDataService },
        { provide: OccService, useClass: MockOccService },
        { provide: GeocodeService, useClass: MockGeocodeService },
        { provide: Store, useClass: MockStore },
        { provide: CartDataService, useClass: MockCartDataService },
        { provide: BaseSiteService, useClass: MockBaseSiteService }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomCisFinderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should CREATE', () => {
    expect(component).toBeTruthy();
  });
});
