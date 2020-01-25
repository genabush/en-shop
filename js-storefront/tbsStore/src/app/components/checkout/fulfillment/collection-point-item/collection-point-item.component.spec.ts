import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';

// spartacus
import { CartDataService } from '@spartacus/core';

// vendor
import { Store } from '@ngrx/store';
import { MatFormFieldModule, MatSelectModule } from '@angular/material';
import { AgmCoreModule } from '@agm/core';

// components
import { CollectionPointItemComponent } from './collection-point-item.component';
import { CustomAddressFinderComponent } from '../custom-address-finder/custom-address-finder.component';
import { WhoWillCollectComponent } from '../who-will-collect/who-will-collect.component';
import { MockCxIconComponent, MockStore } from 'src/app/testing/mock.components';
import { MapInfoWindowComponent } from '../../components/map-info-window/map-info-window.component';
import { MapInfoWindowAltComponent } from '../../components/map-info-window-alt/map-info-window-alt.component';

// services
import { OccService } from 'src/app/services/occ/occ.service';
import { MockCustomCartService, MockCartDataService } from 'src/app/testing/mock.services';
import { CustomCartService } from 'src/app/services/cart/facade/cart.service';

// pipes
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';
import { RouterTestingModule } from '@angular/router/testing';
import { MockOccService } from 'src/app/testing/occ/occ.service.mock';

describe('Collection Point Item Component', () => {
  let component: CollectionPointItemComponent;
  let fixture: ComponentFixture<CollectionPointItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        ReactiveFormsModule,
        MatFormFieldModule,
        MatSelectModule,
        AgmCoreModule,
        HttpClientTestingModule,
        RouterTestingModule
      ],
      declarations: [
        CollectionPointItemComponent,
        CustomAddressFinderComponent,
        WhoWillCollectComponent,
        MockCxTranslatePipe,
        MockCxIconComponent,
        MapInfoWindowComponent,
        MapInfoWindowAltComponent
      ],
      providers: [
        { provide: OccService, useClass: MockOccService },
        { provide: CartDataService, useClass: MockCartDataService },
        { provide: CustomCartService, useClass: MockCustomCartService },
        { provide: Store, useClass: MockStore }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CollectionPointItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
