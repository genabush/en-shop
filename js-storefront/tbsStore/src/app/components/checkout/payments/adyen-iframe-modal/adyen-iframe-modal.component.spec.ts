import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdyenIframeModalComponent } from './adyen-iframe-modal.component';
import { CustomCheckoutService } from '../../../../services/checkout/custom-checkout.service';
import { ReplaySubject } from 'rxjs';

class MockCustomCheckoutService {
  adyenModalConfig$ = new ReplaySubject(1);
}

describe('AdyenIframeModalComponent', () => {
  let component: AdyenIframeModalComponent;
  let fixture: ComponentFixture<AdyenIframeModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [AdyenIframeModalComponent],
      providers: [{ provide: CustomCheckoutService, useClass: MockCustomCheckoutService }]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdyenIframeModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
