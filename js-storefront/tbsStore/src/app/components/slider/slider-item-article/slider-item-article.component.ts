import { Component, OnInit, Input } from '@angular/core';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-slider-item-article',
  templateUrl: './slider-item-article.component.html',
  styleUrls: ['./slider-item-article.component.scss']
})
export class SliderItemArticleComponent implements OnInit {
  @Input() item: Observable<any>[];

  constructor() {}

  ngOnInit() {}
}
