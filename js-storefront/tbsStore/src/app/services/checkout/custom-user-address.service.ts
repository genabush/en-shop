import { Injectable } from '@angular/core';

import { UserAddressService, StateWithUser, StateWithProcess, CartDataService } from '@spartacus/core';

// vendor
import { Store } from '@ngrx/store';
import { findIndex, isUndefined } from 'lodash';

// interfaces
import { ICustomAddress } from 'src/app/interfaces/custom-checkout.interface';
import { HttpClient } from '@angular/common/http';
import { OccService } from '../occ/occ.service';
import { CustomCartService } from '../cart/facade/cart.service';

@Injectable({
  providedIn: 'root'
})
export class CustomUserAddressService extends UserAddressService {
  constructor(
    protected store: Store<StateWithUser | StateWithProcess<void>>,
    private http: HttpClient,
    private occService: OccService,
    private cartDataService: CartDataService
  ) {
    super(store);
  }
  /**
   * Get a list of billing addresses from API endpoint
   */
  getBillingAddresses() {
    return this.http.get(
      this.occService.getApiUrl() +
        '/users/' +
        (this.cartDataService.userId ? this.cartDataService.userId : 'anonymous') +
        '/addresses?addressType=BILLING'
    );
  }
  /**
   * Get a list of billing addresses from API endpoint
   */
  addNewUserAddress(newAddress: ICustomAddress) {
    return this.http.post(
      this.occService.getApiUrl() +
        '/users/' +
        (this.cartDataService.userId ? this.cartDataService.userId : 'anonymous') +
        '/addresses',
      newAddress
    );
  }
  /**
   * Get a list of address id's as a array
   */
  getUserAddressIdList(existingAddresses: ICustomAddress[]): string[] {
    if (!isUndefined(existingAddresses)) {
      if (existingAddresses.length > 0) {
        return existingAddresses.map(addressItem => {
          return addressItem.id;
        });
      }
    }
    return [];
  }
  /**
   * Find the index of an address by Id in checkout delivery
   */
  getAddressById(addressId: string, existingAddresses: ICustomAddress[]) {
    let foundAddress: ICustomAddress | null = null;
    const existingIndex = findIndex(existingAddresses, (addressItem: ICustomAddress) => {
      return addressItem.id === addressId;
    });
    if (existingIndex > -1) {
      foundAddress = existingAddresses[existingIndex];
    }
    return foundAddress;
  }
  /**
   * Find the index of a the default address in checkout delivery
   */
  getDefaultAddress(existingAddresses: ICustomAddress[]): ICustomAddress | null {
    let defaultAddress: ICustomAddress | null = null;
    const defaultIndex = findIndex(existingAddresses, (addressItem: ICustomAddress) => {
      return addressItem.defaultAddress;
    });
    if (defaultIndex > -1) {
      defaultAddress = existingAddresses[defaultIndex];
    }

    return defaultAddress;
  }
  /**
   * Find the index of an added address in checkout delivery
   */
  findNewAddress(builtAddressList: string[], addresses: ICustomAddress[]): ICustomAddress | null {
    let missingAddress: ICustomAddress | null = null;
    addresses.forEach(addressItem => {
      if (builtAddressList.indexOf(addressItem.id) === -1) {
        missingAddress = addressItem;
      }
    });
    return missingAddress;
  }
}
