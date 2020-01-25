import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdyenSavedCreditCardsComponent } from './adyen-saved-credit-cards.component';
import { of } from 'rxjs';
import { CustomCheckoutService } from '../../../../services/checkout/custom-checkout.service';

// pipes
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';

class MockCustomCheckoutService {
  getAdyenConfiguration() {
    return of({});
  }
}

describe('AdyenSavedCreditCardsComponent', () => {
  let component: AdyenSavedCreditCardsComponent;
  let fixture: ComponentFixture<AdyenSavedCreditCardsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [AdyenSavedCreditCardsComponent, MockCxTranslatePipe],
      providers: [
        { provide: CustomCheckoutService, useClass: MockCustomCheckoutService },
        { provide: 'Window', useValue: window }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    window['AdyenCheckout'] = class AdyenCheckout {
      create(param, param2) {
        return { mount: param => true };
      }
    };
    fixture = TestBed.createComponent(AdyenSavedCreditCardsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
