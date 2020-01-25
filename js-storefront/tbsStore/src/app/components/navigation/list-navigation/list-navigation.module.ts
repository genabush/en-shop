import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgSelectModule } from '@ng-select/ng-select';
import { PaginationComponent } from './pagination/pagination.component';
import { SortingComponent } from './sorting/sorting.component';
import { I18nModule } from '@spartacus/core';

@NgModule({
  imports: [CommonModule, NgSelectModule, FormsModule, I18nModule],
  declarations: [PaginationComponent, SortingComponent],
  exports: [PaginationComponent, SortingComponent]
})
export class ListNavigationModule {}
