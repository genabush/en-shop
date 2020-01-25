import { Injectable, OnDestroy } from '@angular/core';
import { HttpClient } from '@angular/common/http';

// spartacus
import {
  CheckoutDeliveryService,
  StateWithCheckout,
  CartDataService,
  BaseSiteService,
  CheckoutActions
} from '@spartacus/core';

// vendor
import { isEmpty, isUndefined } from 'lodash';

// vendor
import { Store } from '@ngrx/store';
import { Observable, Subscription, throwError, BehaviorSubject } from 'rxjs';

// services
import { OccService } from '../../occ/occ.service';

// interfaces
import { IDeliveryModesResponse, IDeliveryAddressResponse } from 'src/app/interfaces/custom-checkout.interface';

@Injectable({
  providedIn: 'root'
})
export class CustomCheckoutDeliveryService extends CheckoutDeliveryService implements OnDestroy {
  baseSiteSub: Subscription;
  baseSite: string;
  baseUrl: string;
  isLoadingDeliveryModes: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  constructor(
    checkoutStore: Store<StateWithCheckout>,
    cartData: CartDataService,
    private baseSiteService: BaseSiteService,
    private http: HttpClient,
    private occService: OccService
  ) {
    super(checkoutStore, cartData);
    this.baseUrl = this.occService.getBaseUrl();
    this.setBaseSiteSub();
  }
  /**
   * Clear selected delivery modes from API and State
   */
  clearSetDeliveryMode(): void {
    if (!isUndefined(this.cartData.cartId) && !isEmpty(this.cartData.cartId)) {
      const url = this.getApiEndpoint('/deliverymode');
      this.http.delete<any>(url).subscribe(() => {
        if (this.actionAllowed()) {
          this.checkoutStore.dispatch(new CheckoutActions.ClearCheckoutStep(2));
        }
      });
    }
  }
  /**
   * Clear selected delivery address tab
   */
  clearDeliveryAddressTab(): void {
    if (this.actionAllowed()) {
      this.checkoutStore.dispatch(new CheckoutActions.ClearCheckoutStep(1));
    }
  }

  /**
   * Get delivery modes from API
   */
  getApiSupportedDeliveryModes(): Observable<IDeliveryModesResponse> {
    if (!isUndefined(this.cartData.cartId) && !isEmpty(this.cartData.cartId)) {
      const url = this.getApiEndpoint('/deliverymodes');
      return this.http.get<IDeliveryModesResponse>(url);
    }
    return throwError('');
  }
  /**
   * Get delivery address restrictions from API
   */
  getApiProductDeliveryRestriction() {
    if (!isUndefined(this.cartData.cartId) && !isEmpty(this.cartData.cartId)) {
      const url = this.getApiEndpoint('/productdeliveryrestriction');
      return this.http.get<IDeliveryAddressResponse>(url);
    }
    return throwError('');
  }
  /**
   * Subscribe to the BaseSiteService
   */
  setBaseSiteSub() {
    this.baseSiteSub = this.baseSiteService.getActive().subscribe((baseSite: string) => {
      this.baseSite = baseSite;
    });
  }
  /**
   * Get URL endpoint
   * * @param enpoint : the endpoint to be called
   */
  getApiEndpoint(endpoint: string): string {
    return this.baseUrl + '/rest/v2/' + this.baseSite + '/users/current/carts/' + this.cartData.cartId + endpoint;
  }

  destroyBaseSiteSub() {
    if (this.baseSiteSub) {
      this.baseSiteSub.unsubscribe();
    }
  }

  ngOnDestroy() {
    this.destroyBaseSiteSub();
  }

  getCustomSelectedDeliveryMode(): Observable<any> {
    if (!isUndefined(this.cartData.cartId) && !isEmpty(this.cartData.cartId)) {
      const url = this.getApiEndpoint('/deliverymode');
      return this.http.get<any>(url, {});
    }
    return throwError('');
  }
}
