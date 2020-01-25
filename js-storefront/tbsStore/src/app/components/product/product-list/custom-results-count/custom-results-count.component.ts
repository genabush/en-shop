import { ChangeDetectionStrategy, Component, Input, OnChanges } from '@angular/core';

@Component({
  selector: 'app-results-count',
  templateUrl: './custom-results-count.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CustomResultsCountComponent implements OnChanges {
  constructor() {}
  @Input() paginationData: any;
  pageSize: number;
  totalResults: number;

  ngOnChanges() {
    this.calculateResults(this.paginationData);
  }
  calculateResults(pagination) {
    const calc = pagination.totalResults - pagination.currentPage * pagination.pageSize;
    this.totalResults = pagination.totalResults;
    this.pageSize = calc > pagination.pageSize ? pagination.pageSize : calc;
  }
}
