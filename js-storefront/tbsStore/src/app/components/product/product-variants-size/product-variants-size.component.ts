import { Component, OnInit, Input } from '@angular/core';
import { ProductVariantSelection, ProductVariant } from 'src/app/models';
@Component({
  selector: 'app-product-variants-size',
  templateUrl: './product-variants-size.component.html',
  styleUrls: ['./product-variants-size.component.scss']
})
export class ProductVariantsSizeComponent implements OnInit {
  @Input()
  variant: ProductVariantSelection;

  constructor() {}

  ngOnInit() {}
}
