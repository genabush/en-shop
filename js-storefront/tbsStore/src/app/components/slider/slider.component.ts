import { Component, OnInit, Input } from '@angular/core';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-slider',
  templateUrl: './slider.component.html',
  styleUrls: ['./slider.component.scss']
})

// Documentation: https://www.npmjs.com/package/ngx-swiper-wrapper
export class SliderComponent implements OnInit {
  constructor() {}

  @Input() slides: Observable<any>[];

  @Input() configs: object;

  config = {};

  ngOnInit() {
    this.config = {
      ...this.config,
      ...this.configs
    };
  }
}
