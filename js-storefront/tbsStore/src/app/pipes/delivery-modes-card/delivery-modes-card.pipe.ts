import { Pipe, PipeTransform } from '@angular/core';
import { DeliveryMode } from '@spartacus/core';
import { Card } from '@spartacus/storefront';

@Pipe({
  name: 'deliveryModesCard'
})
export class DeliveryModesCardPipe implements PipeTransform {
  transform(value: DeliveryMode, args?: any): Card {
    if (value) {
      return {
        title: value.deliveryCost.formattedValue,
        textBold: value.name,
        text: [value.description]
      };
    }
    return {
      title: 'add options'
    };
  }
}
