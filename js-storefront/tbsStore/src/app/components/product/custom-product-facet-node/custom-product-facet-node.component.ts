import { Component, Input } from '@angular/core';
import { CustomProductListComponentService } from '../product-list/container/custom-product-list-component.service';
import { HttpUrlEncodingCodec } from '@angular/common/http';
@Component({
  selector: 'app-facet-node',
  templateUrl: './custom-product-facet-node.component.html'
})
export class CustomProductFacetNodeComponent {
  protected queryCodec: HttpUrlEncodingCodec;
  constructor(private productListComponentService: CustomProductListComponentService) {
    this.queryCodec = new HttpUrlEncodingCodec();
  }
  @Input() facet: any;
  toggleValue(query: string): void {
    this.productListComponentService.setQuery(this.queryCodec.decodeValue(query));
  }
  toggleDropdown($event): void {
    if ($event.target.value) {
      this.productListComponentService.setQuery($event.target.value);
    }
  }
}
