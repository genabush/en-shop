import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FindInStoreComponent } from './find-in-store.component';
import { MockCxIconComponent } from 'src/app/testing/mock.components';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material';
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';
import { CollectionPointItemModule } from '../checkout/fulfillment/collection-point-item/collection-point-item.module';
import { AgmCoreModule } from '@agm/core';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { OccConfig, BaseSiteService } from '@spartacus/core';
import { MockBaseSiteService, MockGeocodeService } from 'src/app/testing/mock.services';
import { GeocodeService } from 'src/app/services/google/geocode.service';
import { MockOccConfig } from 'src/app/testing/occ/occ.config.mock';

describe('FindInStoreComponent', () => {
  let component: FindInStoreComponent;
  let fixture: ComponentFixture<FindInStoreComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, MatInputModule, CollectionPointItemModule, AgmCoreModule, HttpClientTestingModule],
      declarations: [FindInStoreComponent, MockCxIconComponent, MockCxTranslatePipe],
      providers: [
        { provide: OccConfig, useClass: MockOccConfig },
        { provide: BaseSiteService, useClass: MockBaseSiteService },
        { provide: GeocodeService, useClass: MockGeocodeService }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FindInStoreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
