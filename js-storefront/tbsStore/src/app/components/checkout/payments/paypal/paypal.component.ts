/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

import { ChangeDetectionStrategy, ChangeDetectorRef, Component, EventEmitter, OnInit, Output } from '@angular/core';
import { CustomCheckoutService } from '../../../../services/checkout/custom-checkout.service';
import { OccService } from '../../../../services/occ/occ.service';
import { CustomCartService } from '../../../../services/cart/facade/cart.service';
import { IPayPalConfig } from 'ngx-paypal';
import { AuthService, CheckoutActions, UserToken } from '@spartacus/core';
import { Store } from '@ngrx/store';
import { RootStoreState } from '../../../../root-store';
import { Router } from '@angular/router';
import { take } from 'rxjs/operators';
import { StoreConfigService } from '../../../../services/config/store-config.service';
import { PaypalConfig } from '../../../../../environments/environment.interface';

@Component({
  selector: 'app-paypal',
  templateUrl: './paypal.component.html',
  styleUrls: ['./paypal.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PaypalComponent implements OnInit {
  private apiUrl: string;
  private userId: string;
  private cartCode: string;

  private authToken: UserToken;
  isLoading = false;
  hasError = false;
  errorMsg: string;

  private paypalConfigurationForStore: PaypalConfig;

  public payPalConfig?: IPayPalConfig;

  @Output() loadSpinner = new EventEmitter<boolean>();

  @Output() errorEmitter = new EventEmitter<string>();

  constructor(
    private customCheckoutService: CustomCheckoutService,
    private occService: OccService,
    private customCartService: CustomCartService,
    private store: Store<RootStoreState.State>,
    private router: Router,
    private authService: AuthService,
    private cd: ChangeDetectorRef,
    private paymentConfigService: StoreConfigService
  ) {
    this.apiUrl = this.occService.getApiUrl();
    this.userId = this.customCartService.getUserId();
    this.cartCode = this.customCartService.getActiveCartCode();
    this.paypalConfigurationForStore = this.paymentConfigService.getPaypalConfiguration();
    this.authService
      .getUserToken()
      .pipe(take(1))
      .subscribe(val => (this.authToken = val));
    this.loadSpinner.subscribe(state => {
      this.isLoading = state;
      this.cd.detectChanges();
    });
    this.errorEmitter.subscribe(state => {
      this.errorMsg = state;
      this.hasError = !!state;
      this.cd.detectChanges();
    });
  }

  ngOnInit(): void {
    this.initConfig();
  }

  private initConfig(): void {
    this.payPalConfig = {
      clientId: this.paypalConfigurationForStore.clientId,
      advanced: {
        extraQueryParams: [
          {
            name: 'disable-card',
            value: 'amex,jcb,visa,discover,mastercard'
          },
          {
            name: 'currency',
            value: this.paypalConfigurationForStore.currency
          }
        ]
      },
      createOrderOnServer: data => {
        return fetch(`${this.apiUrl}/users/${this.userId}/paypal/order/${this.cartCode}/create`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `${this.authToken.token_type} ${this.authToken.access_token}`
          }
        })
          .then(createOrderResponse => {
            return createOrderResponse.json();
          })
          .then(createOrderResponse => {
            return createOrderResponse.orderId;
          });
      },
      onApprove: approveData => {
        this.setLoader(true);
        fetch(
          `${this.apiUrl}/users/${this.userId}/paypal/order/${this.cartCode}/authorize?orderId=${approveData.orderID}`,
          {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              Authorization: `${this.authToken.token_type} ${this.authToken.access_token}`
            }
          }
        )
          .then(res => {
            return res.json();
          })
          .then(details => {
            if (!!details && !!details.code) {
              this.store.dispatch(new CheckoutActions.PlaceOrderSuccess(details));
              this.router.navigate(['/order-confirmation']);
            } else {
              this.setLoader(false);
              this.setErrors(JSON.stringify(details.errors));
            }
          });
      },
      onClick: () => {
        this.setErrors(null);
      },
      onCancel: (data, actions) => {
        this.setLoader(false);
      },
      onError: err => {
        this.setErrors(JSON.stringify(err));
        this.setLoader(false);
      }
    };
  }

  setLoader(load: boolean) {
    this.loadSpinner.emit(load);
  }

  setErrors(errors: string) {
    this.errorEmitter.emit(errors);
  }
}
