import { Component, Input, OnInit, ViewChild, ElementRef } from '@angular/core';

//spartacus
import { DeliveryMode, PriceType } from '@spartacus/core';
import { ICON_TYPE, ModalService } from '@spartacus/storefront';

// vendor
import { NgbModalRef, NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { Subscription } from 'rxjs';

// services
import { CustomCheckoutDeliveryService } from 'src/app/services/checkout/delivery/custom-checkout-delivery.service';

// interfaces
import { CustomCart } from 'src/app/interfaces/custom-cart.interface';
import { CustomCartService } from 'src/app/services/cart/facade/cart.service';

const DUMMY_DELIVERY_MODES = [
  {
    code: 'uk-click-and-collect',
    deliveryCost: {
      currencyIso: 'GBP',
      formattedValue: '£0.00',
      priceType: PriceType['BUY'],
      value: 0.0
    },
    description: 'Click and Collect detailed description',
    name: 'Click and Collect'
  },
  {
    code: 'UK-1-ECONOMY',
    deliveryCost: {
      currencyIso: 'GBP',
      formattedValue: '£2.49',
      priceType: PriceType['BUY'],
      value: 2.49
    },
    description: 'Economy Delivery detailed description',
    name: 'Economy Delivery'
  }
];

@Component({
  selector: 'cx-order-summary',
  templateUrl: './order-summary.component.html'
})
export class OrderSummaryComponent implements OnInit {
  @ViewChild('orderSummaryDeliveryModal', { static: false }) orderSummaryDeliveryModal: ElementRef;
  @Input() cart: CustomCart;
  @Input() isCheckout = false;
  collectionModalRef: NgbModalRef;
  modalOptions: NgbModalOptions = {
    windowClass: 'cart-delivery-modes-modal d-flex',
    ariaLabelledBy: 'modal-cart-delivery-modes',
    size: 'lg'
  };
  iconTypes = ICON_TYPE;
  supportedModesSub: Subscription;
  deliveryModes: DeliveryMode[];
  constructor(
    protected cartService: CustomCartService,
    private modalService: ModalService,
    private checkoutDeliveryService: CustomCheckoutDeliveryService
  ) {}
  ngOnInit() {
    this.setSupportedModesSub();
  }
  setSupportedModesSub() {
    this.deliveryModes = DUMMY_DELIVERY_MODES;
    // TODO As per comments on RCOM-922- this function will be handled in separate ticßket
    // this.supportedModesSub = this.checkoutDeliveryService.getApiSupportedDeliveryModes().subscribe(
    //   (response: IDeliveryModesResponse) => {
    //     // this.handleDeliveryModeResponse(response);
    //     // this.destroySupportedModesSub();
    //     this.deliveryModes = DUMMY_DELIVERY_MODES;
    //   },
    //   err => {
    //     // ignore store not loaded
    //     // this.isLoading = false;
    //     // this.destroySupportedModesSub();
    //     // this.triggerChanges();
    //     this.deliveryModes = DUMMY_DELIVERY_MODES;
    //   }
    // );
  }

  getAllPromotionsForCart(cart: CustomCart): any[] {
    return this.cartService.getAllPromotions(cart);
  }

  viewCollectionOptions(e: Event) {
    e.preventDefault();
    this.openDeliveryModal();
  }

  openDeliveryModal() {
    this.collectionModalRef = this.modalService.open(this.orderSummaryDeliveryModal, this.modalOptions);
    this.collectionModalRef.result.then(
      () => {
        this.closeDeliveryModal();
      },
      () => {
        this.closeDeliveryModal();
      }
    );
  }

  closeDeliveryModal() {
    if (this.collectionModalRef) {
      this.collectionModalRef.close();
    }
  }
  ngOnDestroy() {
    this.closeDeliveryModal();
  }
}
