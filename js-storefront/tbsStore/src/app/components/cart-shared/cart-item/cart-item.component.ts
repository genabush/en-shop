import { Component, EventEmitter, Input, Output, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { ICON_TYPE } from '@spartacus/storefront';

export interface Item {
  product?: any;
  quantity?: any;
  basePrice?: any;
  totalPrice?: any;
  updateable?: boolean;
  maxProductOrderQuantity: number;
}

@Component({
  selector: 'cx-cart-item',
  templateUrl: './cart-item.component.html',
  styles: [
    `
      .cx-image-container {
        max-width: 80px;
      }
    `
  ]
})
export class CartItemComponent implements OnInit {
  iconTypes = ICON_TYPE;
  @Input() compact = false;
  @Input() item: Item;
  @Input() potentialProductPromotions: any[];
  @Input() isReadOnly = false;
  @Input() cartIsLoading = false;
  @Input() quantity: number;
  maxQuantity = 0;

  @Output()
  remove = new EventEmitter<any>();
  @Output()
  update = new EventEmitter<any>();
  @Output()
  view = new EventEmitter<any>();

  @Input() parent: FormGroup;

  ngOnInit() {
    this.maxQuantity = this.item.maxProductOrderQuantity;
  }

  isProductOutOfStock(product) {
    // TODO Move stocklevelstatuses across the app to an enum
    return product && product.stock && product.stock.stockLevelStatus === 'outOfStock';
  }

  updateItem(updatedQuantity: number) {
    this.update.emit({ item: this.item, updatedQuantity });
  }

  removeItem() {
    this.remove.emit(this.item);
  }

  viewItem() {
    this.view.emit();
  }
}
