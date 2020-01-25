import { Pipe, PipeTransform } from '@angular/core';
import { DeliveryMode } from '@spartacus/core';

@Pipe({
  name: 'deliveryModesSelectedCard'
})
export class DeliveryModesSelectedCardPipe implements PipeTransform {
  transform(value: number, deliveryModes?: DeliveryMode[]): any {
    if (value) {
      // fix for mat-select value 0 not populating
      const actualCardMode = deliveryModes[value - 1];
      return {
        title: actualCardMode.deliveryCost.formattedValue,
        textBold: actualCardMode.name,
        text: [actualCardMode.description]
      };
    }
    return null;
  }
}
