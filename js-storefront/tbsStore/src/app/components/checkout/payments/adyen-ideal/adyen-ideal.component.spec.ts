import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdyenIdealComponent } from './adyen-ideal.component';

describe('AdyenIdealComponent', () => {
  let component: AdyenIdealComponent;
  let fixture: ComponentFixture<AdyenIdealComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdyenIdealComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdyenIdealComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
