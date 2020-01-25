import { Pipe, PipeTransform } from '@angular/core';
import { CheckoutFulfillmentTabs } from 'src/app/interfaces/custom-checkout.interface';

@Pipe({
  name: 'payNowEnabled'
})
export class PayNowEnabledPipe implements PipeTransform {
  transform(checkoutJourneyType: any, args?: any): any {
    if ((!args.isReadyToPay && args.hasOutstandingAmount) || !args.isTandCsAccepted) {
      return true;
    } else if (checkoutJourneyType === CheckoutFulfillmentTabs.DELIVERY) {
      return !args.checkoutState.fullFillmentState || !args.checkoutState.deliveryModesState;
    } else if (checkoutJourneyType === CheckoutFulfillmentTabs.COLLECT_IN_STORE) {
      return !args.checkoutState.fullFillmentState || !args.checkoutState.whoWillCollectState;
    } else if (checkoutJourneyType === CheckoutFulfillmentTabs.COLLECTION_POINT) {
      return (
        !args.checkoutState.fullFillmentState ||
        !args.checkoutState.deliveryModesState ||
        !args.checkoutState.whoWillCollectState
      );
    }
    return true;
  }
}
