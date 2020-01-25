/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

import { BaseSiteService } from '@spartacus/core';
import { GigyaRaasConfig, PaypalConfig, StoreConfig } from '../../../environments/environment.interface';
import { environment } from '../../../environments/environment';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PaymentMode, PaymentModeList } from '../../interfaces/payment-methods-response.interface';
import { HttpClient } from '@angular/common/http';
import { OccService } from '../occ/occ.service';
import { map, pluck } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class StoreConfigService {
  private shopName: string;

  private apiUrl: string;

  private paymentModesResponse: PaymentMode[];

  constructor(private baseSiteService: BaseSiteService, private http: HttpClient, private occService: OccService) {
    this.baseSiteService.getActive().subscribe((data: string) => {
      this.shopName = data;
    });
    this.apiUrl = this.occService.getApiUrl();
    this.getPaymentModes()
      .pipe(
        map(data => {
          return data;
        })
      )
      .subscribe(data => (this.paymentModesResponse = data));
  }

  getPaypalConfiguration(): PaypalConfig {
    const storeConfig = this.getStoreConfigOrThrowError();
    return storeConfig.paypalConfig;
  }

  isPaymentVisible(paymentMode: string): boolean {
    return (
      !!this.paymentModesResponse &&
      this.paymentModesResponse.findIndex((data: PaymentMode) => data.code === paymentMode) > -1
    );
  }

  getGigyaConfiguration(): GigyaRaasConfig {
    const storeConfig = this.getStoreConfigOrThrowError();
    return storeConfig.gigyaConfig;
  }

  private getStoreConfigOrThrowError(): StoreConfig {
    const allStoreConfig = environment.storeConfig;
    const storeConfig = allStoreConfig.find((config: StoreConfig) => {
      return config.shopName === this.shopName;
    });
    if (!storeConfig) {
      throw new Error(`No Configuration found for store name ${this.shopName}`);
    }
    return storeConfig;
  }

  private getPaymentModes(): Observable<PaymentMode[]> {
    return this.http
      .get<PaymentModeList>(`${this.apiUrl}/paymentmodes`, {
        params: {
          fields: 'DEFAULT'
        }
      })
      .pipe(pluck('paymentModes'));
  }
}
