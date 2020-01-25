import { ChangeDetectionStrategy, Component, OnInit, ViewChild, ElementRef, OnDestroy } from '@angular/core';
// spartacus
import { Product } from '@spartacus/core';
// vendor
import { Observable, of, Subscription } from 'rxjs';
// services
import { CurrentProductService } from '../../current-product.service';
// interfaces
import { ICustomProduct } from 'src/app/interfaces/custom-product-item.interface';
@Component({
  selector: 'cx-product-details-tab',
  templateUrl: './product-details-tab.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ProductDetailsTabComponent implements OnInit, OnDestroy {
  @ViewChild('productDescriptionDiv', { static: true }) productDescriptionDiv: ElementRef;
  product$: Observable<Product | ICustomProduct>;
  subscription = new Subscription();
  constructor(protected currentProductService: CurrentProductService) {}
  ngOnInit() {
    this.subscription.add(
      this.currentProductService.getProduct().subscribe(productResponse => {
        this.product$ = of(productResponse);
        // use insertAdjacentHTML to maintain event driven attributes on HTML tags
        this.productDescriptionDiv.nativeElement.insertAdjacentHTML('beforeend', productResponse.description);
      })
    );
  }
  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
