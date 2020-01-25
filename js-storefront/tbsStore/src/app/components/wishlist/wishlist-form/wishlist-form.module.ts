import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WishlistFormComponent } from './wishlist-form.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material';
import { I18nModule } from '@spartacus/core';

@NgModule({
  declarations: [WishlistFormComponent],
  imports: [CommonModule, MatInputModule, ReactiveFormsModule, I18nModule],
  exports: [WishlistFormComponent]
})
export class WishlistFormModule {}
