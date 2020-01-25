import { Component, OnInit, Input } from '@angular/core';
import { formatDate } from '@angular/common';
import { isUndefined, isEmpty } from 'lodash';

@Component({
  selector: 'app-notice-banner',
  templateUrl: './notice-banner.component.html'
})
export class NoticeBannerComponent implements OnInit {
  @Input() data: any;
  showBanner = false;
  constructor() {}

  ngOnInit() {
    this.toggleNoticeBanner();
  }
  private toggleNoticeBanner() {
    if (isUndefined(this.data)) {
      return;
    }
    if (!isEmpty(this.data.temporaryClosedFromDate) && !isEmpty(this.data.temporaryClosedToDate)) {
      const endDate = new Date(this.data.temporaryClosedToDate);
      const todaysDate = new Date();

      if (endDate.getTime() !== todaysDate.getTime()) {
        this.showBanner = true;
      }
    }
  }
}
