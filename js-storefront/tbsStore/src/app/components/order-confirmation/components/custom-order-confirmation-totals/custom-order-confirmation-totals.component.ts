import { ChangeDetectionStrategy, Component, OnDestroy, OnInit } from '@angular/core';
import { CheckoutService, Order } from '@spartacus/core';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-order-confirmation-totals',
  templateUrl: './custom-order-confirmation-totals.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CustomOrderConfirmationTotalsComponent implements OnInit, OnDestroy {
  order$: Observable<Order>;

  constructor(protected checkoutService: CheckoutService) {}

  ngOnInit() {
    this.order$ = this.checkoutService.getOrderDetails();
  }

  ngOnDestroy() {
    this.checkoutService.clearCheckoutData();
  }
}
