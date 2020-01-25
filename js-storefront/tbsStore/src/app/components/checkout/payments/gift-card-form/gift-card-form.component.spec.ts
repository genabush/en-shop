import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ReactiveFormsModule } from '@angular/forms';
// vendor
import { of } from 'rxjs';
import { MatSelectModule, MatInputModule } from '@angular/material';

// components
import { GiftCardFormComponent } from './gift-card-form.component';

// services
import { CustomCheckoutService } from '../../../../services/checkout/custom-checkout.service';

// pipes
import { Pipe, PipeTransform } from '@angular/core';

class MockCustomCheckoutService {
  addGiftCart(param) {
    return of({});
  }
}

@Pipe({ name: 'cxTranslate' })
class MockCxTranslate implements PipeTransform {
  transform(value: number): number {
    return value;
  }
}
describe('GiftCardFormComponent', () => {
  let component: GiftCardFormComponent;
  let fixture: ComponentFixture<GiftCardFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [BrowserAnimationsModule, ReactiveFormsModule, MatSelectModule, MatInputModule],
      declarations: [GiftCardFormComponent, MockCxTranslate],
      providers: [{ provide: CustomCheckoutService, useClass: MockCustomCheckoutService }]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GiftCardFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
