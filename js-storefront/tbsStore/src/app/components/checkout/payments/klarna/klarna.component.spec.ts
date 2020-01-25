import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { KlarnaComponent } from './klarna.component';
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';

describe('KlarnaComponent', () => {
  let component: KlarnaComponent;
  let fixture: ComponentFixture<KlarnaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [KlarnaComponent, MockCxTranslatePipe]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(KlarnaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
