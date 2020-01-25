import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MapInfoWindowComponent } from './map-info-window.component';
import { MockCxIconComponent } from 'src/app/testing/mock.components';
import { CollectionPointItemComponent } from '../../fulfillment/collection-point-item/collection-point-item.component';
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { OccConfig, BaseSiteService } from '@spartacus/core';
import { MockBaseSiteService, MockCustomCartService } from 'src/app/testing/mock.services';
import { CustomCartService } from 'src/app/services/cart/facade/cart.service';
import { MockOccService } from 'src/app/testing/occ/occ.service.mock';

xdescribe('MapInfoWindowComponent', () => {
  let component: MapInfoWindowComponent;
  let fixture: ComponentFixture<MapInfoWindowComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [MockCxTranslatePipe, MapInfoWindowComponent, MockCxIconComponent, CollectionPointItemComponent],
      providers: [
        { provide: OccConfig, useClass: MockOccService },
        { provide: BaseSiteService, useClass: MockBaseSiteService },
        { provide: CustomCartService, useClass: MockCustomCartService }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MapInfoWindowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
