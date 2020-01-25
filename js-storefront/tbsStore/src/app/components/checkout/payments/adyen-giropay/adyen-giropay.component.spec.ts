import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdyenGiropayComponent } from './adyen-giropay.component';

describe('AdyenGiropayComponent', () => {
  let component: AdyenGiropayComponent;
  let fixture: ComponentFixture<AdyenGiropayComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdyenGiropayComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdyenGiropayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
