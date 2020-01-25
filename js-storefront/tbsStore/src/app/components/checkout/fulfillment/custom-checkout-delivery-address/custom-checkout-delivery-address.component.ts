/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
import {
  ChangeDetectionStrategy,
  Component,
  OnDestroy,
  OnInit,
  ViewChild,
  ElementRef,
  ChangeDetectorRef,
  EventEmitter,
  Output
} from '@angular/core';

// spartacus
import { RoutingService, WindowRef } from '@spartacus/core';
import { ModalService, ICON_TYPE } from '@spartacus/storefront';

// vendor
import { NgbModalRef, NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { BehaviorSubject, Observable, Subscription } from 'rxjs';
import { isEmpty } from 'lodash';

// services
import { CustomCheckoutDeliveryService } from 'src/app/services/checkout/delivery/custom-checkout-delivery.service';
import { CustomUserAddressService } from 'src/app/services/checkout/custom-user-address.service';
import { CustomCartService } from 'src/app/services/cart/facade/cart.service';

// interfaces
import {
  ICustomAddress,
  IDeliveryRestrictedState,
  IDeliveryAddressResponse
} from 'src/app/interfaces/custom-checkout.interface';

@Component({
  selector: 'cx-checkout-delivery-address',
  templateUrl: './custom-checkout-delivery-address.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CustomCheckoutDeliveryAddressComponent implements OnInit, OnDestroy {
  @ViewChild('shippingAddressModal', { static: false }) shippingAddressModal: ElementRef;
  @Output() shippingAddressStateEmit: EventEmitter<boolean> = new EventEmitter<boolean>();
  @Output() deliveryRestrictedEmit: EventEmitter<IDeliveryRestrictedState> = new EventEmitter<
    IDeliveryRestrictedState
  >();
  isLoading$: Observable<boolean>;
  existingAddresses$: BehaviorSubject<ICustomAddress[]> = new BehaviorSubject<ICustomAddress[]>(null);
  selectedAddress$: BehaviorSubject<ICustomAddress> = new BehaviorSubject<ICustomAddress>(null);
  selectedAddressCountry: string | null = null;
  selectedAddress: string | null = null;
  editingAddress: ICustomAddress;
  private subscription = new Subscription();
  openModalRef: NgbModalRef;
  private shippingAddressModalOptions: NgbModalOptions = {
    windowClass: 'modal-checkout checkout-shipping-address-modal modal-md d-flex',
    ariaLabelledBy: 'modal-checkout-shipping-address'
  };
  iconTypes = ICON_TYPE;
  isExistingAddress: boolean;
  supportedModesSub: Subscription;
  restrictedProducts: string[];
  isDeliveryRestricted = false;
  isAddingNewAddress = false;
  cartMergeSub: Subscription;
  cartMergeComplete: Observable<boolean>;
  userAddressSub: Subscription;
  constructor(
    protected userAddressService: CustomUserAddressService,
    protected cartService: CustomCartService,
    protected routingService: RoutingService,
    protected checkoutDeliveryService: CustomCheckoutDeliveryService,
    private modalService: ModalService,
    private cd: ChangeDetectorRef,
    private windowRef: WindowRef
  ) {
    this.cartMergeComplete = this.cartService.getCartMergeComplete();
  }

  ngOnInit() {
    this.isLoading$ = this.userAddressService.getAddressesLoading();
    if (this.cartService.cartMergeRequired) {
      this.cartMergeSub = this.cartMergeComplete.subscribe((value: boolean) => {
        if (value) {
          this.setDeliveryAddressSubscription();
        }
      });
    } else {
      this.setDeliveryAddressSubscription();
    }
    this.setExistingAddressesSub();
    this.userAddressService.loadAddresses();
  }

  setExistingAddressesSub() {
    this.userAddressSub = this.userAddressService.getAddresses().subscribe(addresses => {
      this.existingAddresses$.next(addresses);
      this.destroyExistingAddressSub();
      this.checkDefaultAndSelected();
    });
  }

  setDeliveryAddressSubscription() {
    this.subscription.add(
      this.checkoutDeliveryService.getDeliveryAddress().subscribe(address => {
        this.processDeliveryAddress(address);
      })
    );
  }

  processDeliveryAddress(address: ICustomAddress) {
    if (!isEmpty(address)) {
      this.setDeliveryAddress(address);
      this.checkDeliveryRestrictions().then(() => {
        this.destroySupportedModesSub();
        if (!this.isDeliveryRestricted) {
          this.shippingAddressStateEmit.emit(true);
        } else {
          this.shippingAddressStateEmit.emit(false);
        }
      });
    } else {
      this.selectedAddress = undefined;
      this.shippingAddressStateEmit.emit(false);
    }
  }

  setDeliveryAddress(address: ICustomAddress) {
    if (this.isAddingNewAddress) {
      this.isAddingNewAddress = false;
    }
    this.selectedAddress = address.id;
    this.selectedAddressCountry = address.country.isocode;
  }

  checkDefaultAndSelected() {
    let foundAddress: ICustomAddress | null;
    if (this.existingAddresses$.value.length > 1) {
      if (this.selectedAddress) {
        foundAddress = this.userAddressService.getAddressById(this.selectedAddress, this.existingAddresses$.value);
      } else {
        foundAddress = this.userAddressService.getDefaultAddress(this.existingAddresses$.value);
      }
      if (foundAddress) {
        if (this.selectedAddress !== foundAddress.id) {
          this.selectedAddress = foundAddress.id;
          this.selectedAddressCountry = foundAddress.country.isocode;
        }
        this.checkoutDeliveryService.setDeliveryAddress(foundAddress);
      } else {
        this.shippingAddressStateEmit.emit(false);
      }
    } else if (this.existingAddresses$.value.length === 1) {
      const existingFirst = this.existingAddresses$.value[0];
      // select an address that is one of one
      this.selectedAddress = existingFirst.id;
      this.selectedAddressCountry = existingFirst.country.isocode;
      this.checkoutDeliveryService.setDeliveryAddress(existingFirst);
    } else {
      this.shippingAddressStateEmit.emit(false);
    }
  }

  checkDeliveryRestrictions() {
    return new Promise(resolve => {
      this.supportedModesSub = this.checkoutDeliveryService.getApiProductDeliveryRestriction().subscribe(
        (response: IDeliveryAddressResponse) => {
          if (response.restrictedProducts.length > 0) {
            this.setDeliveryRestricted(response);
            this.triggerChanges();
            return resolve();
          } else {
            this.setDeliveryRestricted();
            return resolve();
          }
        },
        err => {
          this.isDeliveryRestricted = true;
          this.setDeliveryRestricted({ isDeliveryRestricted: true });
          return resolve();
        }
      );
    });
  }

  setDeliveryRestricted(response?: any) {
    // process the response
    if (response) {
      if (response.restrictedProducts.length > 0) {
        this.restrictedProducts = response.restrictedProducts;
      } else {
        this.restrictedProducts = undefined;
      }
      this.isDeliveryRestricted = true;
    } else {
      this.isDeliveryRestricted = false;
      this.restrictedProducts = undefined;
    }
    // emit the state to the parent
    this.deliveryRestrictedEmit.emit({
      isDeliveryRestricted: this.isDeliveryRestricted,
      restrictedProducts: this.restrictedProducts
    });

    this.triggerChanges();
  }

  addressSelected(address: ICustomAddress): void {
    this.selectedAddress = address.id;
    this.selectedAddressCountry = address.country.isocode;
    this.checkoutDeliveryService.clearSetDeliveryMode();
    this.checkoutDeliveryService.setDeliveryAddress(address);
  }

  addressEditTrigger(): void {
    this.editingAddress = this.userAddressService.getAddressById(this.selectedAddress, this.existingAddresses$.value);
    this.isExistingAddress = true;
    this.openAddressFormModal();
  }

  editAddress(address: ICustomAddress): void {
    this.closeAddressModal();
    address.shippingAddress = true;
    this.userAddressService.updateUserAddress(address.id, address);
    this.setExistingAddressesSub();
  }

  addNewAddress(address: ICustomAddress): void {
    address.shippingAddress = true;
    this.isAddingNewAddress = true;
    this.checkoutDeliveryService.createAndSetAddress(address);
    this.setExistingAddressesSub();
    this.closeAddressModal();
  }

  showNewAddressForm(isEditing: boolean): void {
    this.isExistingAddress = isEditing;
    this.openAddressFormModal();
  }

  openAddressFormModal(): void {
    this.windowRef.nativeWindow.document.body.classList.add('checkout-modal-open');
    this.openModalRef = this.modalService.open(this.shippingAddressModal, this.shippingAddressModalOptions);
    this.openModalRef.result.then(
      () => {
        this.windowRef.nativeWindow.document.body.classList.remove('checkout-modal-open');
      },
      () => {
        this.windowRef.nativeWindow.document.body.classList.remove('checkout-modal-open');
      }
    );
  }

  closeAddressModal(): void {
    if (this.openModalRef) {
      this.openModalRef.close();
    }
    this.clearEditingAddress();
  }

  clearEditingAddress(): void {
    if (typeof this.editingAddress !== 'undefined') {
      this.editingAddress = undefined;
    }
  }

  triggerChanges() {
    if (!this.cd['destroyed']) {
      this.cd.detectChanges();
    }
  }

  destroyExistingAddressSub() {
    if (this.userAddressSub) {
      this.userAddressSub.unsubscribe();
    }
  }

  destroySupportedModesSub() {
    if (this.supportedModesSub) {
      this.supportedModesSub.unsubscribe();
    }
  }

  destroyCartMergeSub() {
    if (this.cartMergeSub) {
      this.cartMergeSub.unsubscribe();
    }
  }

  ngOnDestroy(): void {
    this.destroyExistingAddressSub();
    this.destroyCartMergeSub();
    this.subscription.unsubscribe();
    this.cd.detach();
  }
}
