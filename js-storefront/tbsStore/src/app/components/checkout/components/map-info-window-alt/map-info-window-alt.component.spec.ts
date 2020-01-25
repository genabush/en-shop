import { async, ComponentFixture, TestBed } from '@angular/core/testing';

// vendor
import { AgmCoreModule } from '@agm/core';

// components
import { MapInfoWindowAltComponent } from './map-info-window-alt.component';
import { MockCxIconComponent } from 'src/app/testing/mock.components';
import { CollectionPointItemComponent } from '../../fulfillment/collection-point-item/collection-point-item.component';

// pipes
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';

xdescribe('MapInfoWindowAltComponent', () => {
  let component: MapInfoWindowAltComponent;
  let fixture: ComponentFixture<MapInfoWindowAltComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AgmCoreModule],
      declarations: [MockCxTranslatePipe, MapInfoWindowAltComponent, MockCxIconComponent, CollectionPointItemComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MapInfoWindowAltComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should CREATE', () => {
    expect(component).toBeTruthy();
  });
});
