import { Component, OnInit, Input } from '@angular/core';
import { clone } from 'lodash';
import { OpenigHours } from 'src/app/interfaces/opening-hours.interface';

@Component({
  selector: 'app-opening-hours',
  templateUrl: './opening-hours.component.html'
})
export class OpeningHoursComponent implements OnInit {
  @Input() data: [OpenigHours];
  openingHrs: [OpenigHours];
  constructor() {}

  ngOnInit() {
    if (!!this.data) {
      this.openingHrs = clone(this.data);
      this.cleanUpDates(this.openingHrs);
    }
  }
  cleanUpDates(dates) {
    let weekDay = '';
    return dates.map(date => {
      weekDay = weekDay === date.weekDay ? '' : date.weekDay;
      date.weekDay = weekDay;
    });
  }
}
