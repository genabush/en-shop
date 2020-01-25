import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

// spartacus
import { CartActions, CartDataService, StateWithCheckout, CheckoutActions } from '@spartacus/core';
import { Store } from '@ngrx/store';

// vendor
import { Observable, ReplaySubject, Subject } from 'rxjs';
import { isUndefined } from 'lodash';

// services
import { RootStoreState } from '../../root-store';
import { OccService } from '../occ/occ.service';
import { CustomCartService } from '../cart/facade/cart.service';

// interfaces
import { PaymentDetailsResponse, PaymentRedirect } from '../../interfaces/payment-details-response.interface';
import { PaymentMethodsResponse, AddGiftCardResponse } from '../../interfaces/payment-methods-response.interface';
import { LoyaltyVouchers, LoyaltyVoucher } from 'src/app/interfaces/custom-checkout.interface';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class CustomCheckoutService {
  private apiUrl: string;
  private userId: string;
  private cartCode: string;

  adyenModalConfig$ = new ReplaySubject<PaymentRedirect>(1);
  closeModal$ = new Subject<number>();

  constructor(
    private http: HttpClient,
    private occService: OccService,
    private customCartService: CustomCartService,
    private store: Store<RootStoreState.State>,
    private checkoutStore: Store<StateWithCheckout>,
    private cartDataService: CartDataService
  ) {
    this.setCurrentConfig();
  }

  /**
   * Clear all checkout data
   */
  clearServerSideCheckout() {
    this.http
      .post(`${this.apiUrl}/users/${this.userId}/carts/${this.cartDataService.cartId}/reset`, {})
      .subscribe(() => {
        this.checkoutStore.dispatch(new CheckoutActions.ClearCheckoutData());
      });
  }

  getAdyenConfiguration(): Observable<PaymentMethodsResponse> {
    this.setCurrentConfig();
    return this.http.get<PaymentMethodsResponse>(
      `${this.apiUrl}/users/${this.userId}/carts/${this.cartDataService.cartId}/paymentmodes`,
      {
        params: {
          fields: 'DEFAULT'
        }
      }
    );
  }

  makeAdyenCreditCardPayment(cardDetails): Observable<PaymentDetailsResponse> {
    this.setCurrentConfig();
    return this.http.post<PaymentDetailsResponse>(
      `${this.apiUrl}/users/${this.userId}/paymentdetails/carts/${this.cartDataService.cartId}`,
      cardDetails
    );
  }

  addGiftCart(
    giftCard: { giftCardNumber: string; giftCardPin: string },
    pinRequired = true
  ): Observable<AddGiftCardResponse> {
    this.setCurrentConfig();
    return this.http.post<AddGiftCardResponse>(
      `${this.apiUrl}/users/${this.userId}/carts/${this.cartDataService.cartId}/giftCard/addGiftCard`,
      {
        ...giftCard,
        pinRequired
      }
    );
  }

  removeGiftCart(giftCard: { giftCardNumber: string }, pinRequired = false): Observable<AddGiftCardResponse> {
    this.setCurrentConfig();
    const httpHeaders = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');
    const options = {
      headers: httpHeaders,
      body: {
        ...giftCard,
        pinRequired
      }
    };
    return this.http.delete<AddGiftCardResponse>(
      `${this.apiUrl}/users/${this.userId}/carts/${this.cartDataService.cartId}/giftCard/removeGiftCard`,
      options
    );
  }

  checkGiftCartBalance(
    giftCard: { giftCardNumber: string; giftCardPin: string },
    pinRequired = true
  ): Observable<AddGiftCardResponse> {
    this.setCurrentConfig();
    return this.http.post<AddGiftCardResponse>(`${this.apiUrl}/giftCard/checkbalance`, {
      ...giftCard,
      pinRequired
    });
  }

  refreshCheckout(): void {
    const selectedCartCode = !isUndefined(this.cartDataService.cartId) ? this.cartDataService.cartId : 'current';
    return this.store.dispatch(
      new CartActions.LoadCart({
        userId: this.userId,
        cartId: selectedCartCode
      })
    );
  }

  postAdyen3dSecureRequest(config: PaymentRedirect): void {
    this.adyenModalConfig$.next(config);
  }

  placeOrderAfter3dSecure(): Observable<PaymentDetailsResponse> {
    this.setCurrentConfig();
    return this.http.post<any>(
      `${this.apiUrl}/users/${this.userId}/paymentdetails/carts/${this.cartDataService.cartId}/placeRedirectOrder`,
      {}
    );
  }

  makeAdyenIdealPayment(cardDetails): Observable<PaymentDetailsResponse> {
    this.setCurrentConfig();
    return this.http.post<PaymentDetailsResponse>(
      `${this.apiUrl}/users/${this.userId}/paymentdetails/carts/${this.cartDataService.cartId}`,
      cardDetails
    );
  }

  getLoyaltyVouchers(): Observable<LoyaltyVouchers> {
    this.setCurrentConfig();
    return this.http.get<LoyaltyVouchers>(
      `${this.apiUrl}/users/${this.userId}/carts/${this.cartDataService.cartId}/loyalty/vouchers`
    );
  }

  addVoucherToBasket(voucherCode: string): Observable<LoyaltyVouchers> {
    this.setCurrentConfig();
    return this.http.put<LoyaltyVouchers>(
      `${this.apiUrl}/users/${this.userId}/carts/${this.cartDataService.cartId}/loyalty/payment?voucherCode=${voucherCode}`,
      { voucherCode }
    );
  }

  removeVoucherFromBasket(voucherCode: string): Observable<LoyaltyVouchers> {
    this.setCurrentConfig();
    return this.http.delete<LoyaltyVouchers>(
      `${this.apiUrl}/users/${this.userId}/carts/${this.cartDataService.cartId}/loyalty/payment?voucherCode=${voucherCode}`
    );
  }

  private setCurrentConfig(): void {
    this.apiUrl = this.occService.getApiUrl();
    this.userId = this.customCartService.getUserId();
  }
}
