import { ChangeDetectionStrategy, Component, OnDestroy, OnInit, Output, EventEmitter } from '@angular/core';
import { CheckoutService, Order } from '@spartacus/core';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-order-confirmation-items',
  templateUrl: './custom-order-confirmation-items.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CustomOrderConfirmationItemsComponent implements OnInit, OnDestroy {
  order$: Observable<Order>;
  @Output()
  view = new EventEmitter<any>();
  constructor(protected checkoutService: CheckoutService) {}

  ngOnInit() {
    this.order$ = this.checkoutService.getOrderDetails();
  }

  ngOnDestroy() {
    this.checkoutService.clearCheckoutData();
  }
  viewItem() {
    this.view.emit();
  }
}
