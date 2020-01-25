import { ChangeDetectionStrategy, Component, OnDestroy, OnInit } from '@angular/core';
import { CheckoutService, Order } from '@spartacus/core';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { CustomOrder } from 'src/app/interfaces/custom-order.interface';

@Component({
  selector: 'app-order-confirmation-thank-you-message',
  templateUrl: './custom-order-confirmation-thank-you-message.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CustomOrderConfirmationThankYouMessageComponent implements OnInit, OnDestroy {
  order$: Observable<CustomOrder>;
  isGuestCustomer = false;
  orderGuid: string;

  constructor(protected checkoutService: CheckoutService) {}

  ngOnInit() {
    this.order$ = this.checkoutService.getOrderDetails().pipe(
      tap(order => {
        this.isGuestCustomer = order.guestCustomer;
        this.orderGuid = order.guid;
      })
    );
  }

  ngOnDestroy() {
    this.checkoutService.clearCheckoutData();
  }
}
