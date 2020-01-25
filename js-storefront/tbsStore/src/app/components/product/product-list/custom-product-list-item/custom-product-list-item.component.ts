import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

// spartacus
import { ProductService, BaseSiteService } from '@spartacus/core';

// vendor
import { Store } from '@ngrx/store';
import { take } from 'rxjs/operators';
import { Observable } from 'rxjs';

// services
import { OccService } from 'src/app/services/occ/occ.service';

// interfaces
import { ICustomProductImage } from 'src/app/interfaces/custom-product-item.interface';

@Component({
  selector: 'app-custom-product-list-item',
  templateUrl: './custom-product-list-item.component.html',
  styleUrls: ['./custom-product-list-item.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CustomProductListItemComponent implements OnInit {
  @Input() product: any;
  product$: Observable<ICustomProductImage>;
  url;
  url$: Observable<any>;
  baseSite$: Observable<any>;
  route;
  origin;
  baseUrl;

  constructor(
    public router: Router,
    public http: HttpClient,
    public store: Store<any>,
    public productService: ProductService,
    public baseSite: BaseSiteService,
    public occService: OccService
  ) {
    this.baseSite$ = this.baseSite.getBaseSiteData();
    this.origin = window.location.origin;
    this.baseUrl = this.occService.getBaseUrl();
  }

  ngOnInit() {
    if (this.product !== undefined) {
      const route = this.router.routerState.snapshot.url;
      const cat = 'c/c';
      const catPos = route.indexOf(cat);
      const subStr = route.slice(0, catPos);
      if (this.product.params) {
        this.url = subStr + '/product/' + this.product.params.code + '/' + this.product.params.name;
      }
    }
  }

  handleCta(product, baseSite) {
    if (product.params) {
      const route = this.router.routerState.snapshot.url;
      const cat = 'c/c';
      const catPos = route.indexOf(cat);
      const subStr = route.slice(0, catPos);
      const productCode = product.params.code;
      this.http
        .get(this.baseUrl + '/rest/v2/' + baseSite.uid + '/products/visible/' + productCode + '?fields=DEFAULT')
        .pipe(take(1))
        .subscribe((data: any) => {
          if (data.visible === 'true') {
            this.router.navigateByUrl(subStr + '/product/' + product.params.code + '/' + product.params.name);
          } else {
            this.router.navigateByUrl(data.redirectUrl);
          }
        });
    }
    return false;
  }
}
