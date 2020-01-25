import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OpeningHoursComponent } from './opening-hours.component';
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';

describe('Opening Hours Component', () => {
  let component: OpeningHoursComponent;
  let fixture: ComponentFixture<OpeningHoursComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [OpeningHoursComponent, MockCxTranslatePipe]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OpeningHoursComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
