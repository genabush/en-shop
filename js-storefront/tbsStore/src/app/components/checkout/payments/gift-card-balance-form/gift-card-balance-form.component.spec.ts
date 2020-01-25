import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GiftCardBalanceFormComponent } from './gift-card-balance-form.component';
import { CustomCheckoutService } from '../../../../services/checkout/custom-checkout.service';
import { ReactiveFormsModule } from '@angular/forms';
import { of } from 'rxjs';
import { Pipe, PipeTransform } from '@angular/core';

class MockCustomCheckoutService {
  checkGiftCartBalance(param) {
    return of({});
  }
}

@Pipe({ name: 'cxTranslate' })
class MockCxTranslate implements PipeTransform {
  transform(value: number): number {
    return value;
  }
}

describe('GiftCardBalanceFormComponent', () => {
  let component: GiftCardBalanceFormComponent;
  let fixture: ComponentFixture<GiftCardBalanceFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule],
      declarations: [GiftCardBalanceFormComponent, MockCxTranslate],
      providers: [{ provide: CustomCheckoutService, useClass: MockCustomCheckoutService }]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GiftCardBalanceFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
