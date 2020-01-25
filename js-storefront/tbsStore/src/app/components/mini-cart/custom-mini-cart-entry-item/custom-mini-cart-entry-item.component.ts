import { Component, OnInit, Input } from '@angular/core';
import { Observable } from 'rxjs';
import { OrderEntry } from '@spartacus/core';

@Component({
  selector: 'app-custom-mini-cart-entry-item',
  templateUrl: './custom-mini-cart-entry-item.component.html'
})
export class CustomMiniCartEntryItemComponent implements OnInit {
  @Input() cartEntry: OrderEntry;
  product: any;
  url;
  url$: Observable<any>;
  baseSite$: Observable<any>;
  route;
  origin;
  baseUrl;
  constructor() {}

  ngOnInit() {
    this.product = this.cartEntry.product;
  }
}
