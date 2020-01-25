import { Component, OnInit, Input } from '@angular/core';

// interfaces
import { CustomCart } from 'src/app/interfaces/custom-cart.interface';

@Component({
  selector: 'cx-cart-loyalty-points',
  templateUrl: './cart-loyalty-points.component.html'
})
export class CartLoyaltyPointsComponent implements OnInit {
  @Input() cart: CustomCart;
  constructor() {}

  ngOnInit() {}
}
