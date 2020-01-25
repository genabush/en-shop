import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

// mock components
import {
  MockCxMediaComponent,
  MockMapInfoWindowComponent,
  MockCxBillingAddressFormComponent,
  MockMapInfoAltWindowComponent,
  MockGiftCardsComponent,
  MockCAdyenSavedCreditCardsComponent,
  MockCAdyenCreditCardsComponent,
  AddedToCartDialogComponent,
  MockItemCounterComponent,
  MockAppBackInStockComponent,
  MockRoutedComponent,
  MockSliderComponent,
  MockResultCountComponent,
  MockCxProductViewComponent,
  MockCxPaginationComponent,
  MockCxAddToCartComponent,
  MockCxStartRatingComponent,
  MockCxGenericLinkComponent,
  MockCxSpinnerComponent,
  MockCxIconComponent,
  MockAmplienceComponent,
  MockProductGridItemComponent
} from './mock.components';

@NgModule({
  imports: [CommonModule, ReactiveFormsModule, BrowserAnimationsModule],
  declarations: [
    MockProductGridItemComponent,
    MockAmplienceComponent,
    MockCxIconComponent,
    MockCxSpinnerComponent,
    MockCxGenericLinkComponent,
    MockCxStartRatingComponent,
    MockCxAddToCartComponent,
    MockCxPaginationComponent,
    MockCxProductViewComponent,
    MockResultCountComponent,
    MockItemCounterComponent,
    MockCxMediaComponent,
    MockSliderComponent,
    MockRoutedComponent,
    MockAppBackInStockComponent,
    MockMapInfoWindowComponent,
    MockMapInfoAltWindowComponent,
    MockCxBillingAddressFormComponent,
    MockGiftCardsComponent,
    MockCAdyenSavedCreditCardsComponent,
    MockCAdyenCreditCardsComponent,
    AddedToCartDialogComponent
  ],
  entryComponents: []
})
export class AppTestingModule {}
