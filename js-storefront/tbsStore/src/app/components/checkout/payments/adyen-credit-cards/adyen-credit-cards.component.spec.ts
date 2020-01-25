import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatCheckboxModule } from '@angular/material';

// venfor
import { of } from 'rxjs';

//components
import { AdyenCreditCardsComponent } from './adyen-credit-cards.component';

//services
import { CustomCheckoutService } from '../../../../services/checkout/custom-checkout.service';

// pipes
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';

class MockCustomCheckoutService {
  getAdyenConfiguration() {
    return of({});
  }
}

describe('AdyenCreditCardsComponent', () => {
  let component: AdyenCreditCardsComponent;
  let fixture: ComponentFixture<AdyenCreditCardsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [FormsModule, ReactiveFormsModule, MatCheckboxModule],
      declarations: [AdyenCreditCardsComponent, MockCxTranslatePipe],
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
    fixture = TestBed.createComponent(AdyenCreditCardsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
