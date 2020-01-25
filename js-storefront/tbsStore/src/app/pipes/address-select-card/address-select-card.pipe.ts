/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
import { Pipe, PipeTransform } from '@angular/core';

// vendor
import { map } from 'rxjs/operators';
import { isUndefined, isNull } from 'lodash';

// spartacus
import { Card } from '@spartacus/storefront';
import { TranslationService } from '@spartacus/core';

// interfaces
import { ICustomAddress } from 'src/app/interfaces/custom-checkout.interface';

@Pipe({
  name: 'addressSelectCard'
})
export class AddressSelectCardPipe implements PipeTransform {
  constructor(private translationService: TranslationService) {
    this.translationService.translate('addressBook.addAddress').pipe(
      map(res => {
        this.emptyAddress = res;
      })
    );
  }
  emptyAddress: string;
  transform(addresses: ICustomAddress[], args?: any): Card {
    if (!isUndefined(args.selectedAddress) && !isNull(args.selectedAddress) && !isUndefined(addresses)) {
      const selectedAddress = args.selectedAddress;
      if (addresses.length > 0) {
        const selectedAddressObject = this.getSelectedAddress(addresses, selectedAddress);
        if (selectedAddressObject) {
          return this.getAddressCard(selectedAddressObject);
        }
      }
    } else {
      if (!isUndefined(addresses)) {
        if (addresses.length === 1) {
          return this.getAddressCard(addresses[0]);
        }
      }
      return {
        text: [this.emptyAddress]
      };
    }
  }

  getSelectedAddress(addresses: ICustomAddress[], selectedAddress: string) {
    for (let i = 0; i <= addresses.length - 1; i++) {
      if (addresses[i].id === selectedAddress) {
        return addresses[i];
      }
    }
  }

  getAddressCard(address: ICustomAddress) {
    const region = this.getCardRegion(address);
    const company = address.companyName ? ' (' + address.companyName + ')' : '';
    return {
      textBold: address.firstName + ' ' + address.lastName + company,
      text: [
        address.line1 + ', ' + (address.line2 ? address.line2 + ', ' : '') + address.town,
        (region ? region + ', ' : '') + address.postalCode + ', ' + address.country.isocode
      ]
    };
  }

  getCardRegion(address: ICustomAddress): string | undefined {
    return !isUndefined(address.region)
      ? !isUndefined(address.region.name)
        ? address.region.name
        : undefined
      : undefined;
  }
}
