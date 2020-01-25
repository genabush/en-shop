import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LybcMessageComponent } from './lybc-message.component';
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';

describe('Lybc Message Component', () => {
  let component: LybcMessageComponent;
  let fixture: ComponentFixture<LybcMessageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [LybcMessageComponent, MockCxTranslatePipe]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LybcMessageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
