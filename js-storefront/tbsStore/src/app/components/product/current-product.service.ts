import { Injectable } from '@angular/core';

// spartacus
import { Product, ProductService, RoutingService } from '@spartacus/core';

// vendor
import { Observable } from 'rxjs';
import { filter, map, switchMap } from 'rxjs/operators';
import { Store } from '@ngrx/store';

// custom
import { RootStoreState } from 'src/app/root-store';

// interfaces
import { ICustomProduct } from 'src/app/interfaces/custom-product-item.interface';

@Injectable({
  providedIn: 'root'
})
export class CurrentProductService {
  constructor(
    private routingService: RoutingService,
    private productService: ProductService,
    public store: Store<RootStoreState.State>
  ) {}

  getProduct(): Observable<ICustomProduct> {
    return this.routingService.getRouterState().pipe(
      map(state => state.state.params['productCode']),
      filter(Boolean),
      switchMap((productCode: string) => this.productService.get(productCode))
    );
  }

  getProductDetails(code?: string): Observable<ICustomProduct> {
    if (code) {
      return this.productService.get(code);
    } else {
      return this.routingService.getRouterState().pipe(
        map(state => {
          return state.state.params['productCode'];
        }),
        filter(Boolean),
        switchMap((productCode: string) => this.productService.get(productCode))
      );
    }
  }
}
