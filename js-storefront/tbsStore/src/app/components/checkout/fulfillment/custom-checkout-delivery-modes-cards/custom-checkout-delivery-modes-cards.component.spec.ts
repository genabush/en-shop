import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSelectModule } from '@angular/material';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

// spartacus
import { CardModule, SpinnerModule } from '@spartacus/storefront';

// components
import { CustomCheckoutDeliveryModesCardsComponent } from './custom-checkout-delivery-modes-cards.component';
import { MockCxIconComponent } from 'src/app/testing/mock.components';

// pipes
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';
import { DeliveryModesSelectedCardPipe } from 'src/app/pipes/delivery-modes-card/delivery-modes-selected-card.pipe';
import { DeliveryModesCardPipe } from 'src/app/pipes/delivery-modes-card/delivery-modes-card.pipe';

describe('CustomCheckoutDeliveryModesCardsComponent', () => {
  let component: CustomCheckoutDeliveryModesCardsComponent;
  let fixture: ComponentFixture<CustomCheckoutDeliveryModesCardsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [BrowserAnimationsModule, ReactiveFormsModule, MatSelectModule, CardModule, SpinnerModule],
      declarations: [
        CustomCheckoutDeliveryModesCardsComponent,
        MockCxTranslatePipe,
        DeliveryModesSelectedCardPipe,
        DeliveryModesCardPipe,
        MockCxIconComponent
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomCheckoutDeliveryModesCardsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should CREATE', () => {
    expect(component).toBeTruthy();
  });
});
