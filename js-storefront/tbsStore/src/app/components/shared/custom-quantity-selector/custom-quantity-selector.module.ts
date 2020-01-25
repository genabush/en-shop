import { I18nModule } from '@spartacus/core';
import { CommonModule } from '@angular/common';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { CustomQuantitySelectorComponent } from './custom-quantity-selector.component';
import { OnlyNumberDirectiveModule } from '../../../directives/only-number/only-number.directive.module';
import { MatSelectModule } from '@angular/material';

@NgModule({
  imports: [CommonModule, FormsModule, ReactiveFormsModule, OnlyNumberDirectiveModule, MatSelectModule, I18nModule],
  declarations: [CustomQuantitySelectorComponent],
  exports: [CustomQuantitySelectorComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ItemCounterModule {}
