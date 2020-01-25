import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-custom-mini-cart-entry-variants',
  templateUrl: './custom-mini-cart-entry-variants.component.html'
})
export class CustomMiniCartEntryVariantsComponent {
  @Input() product: any;
  constructor() {}
}
