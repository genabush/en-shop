/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
import {
  Component,
  OnInit,
  OnDestroy,
  ChangeDetectorRef,
  ChangeDetectionStrategy,
  Input,
  OnChanges
} from '@angular/core';
import { ViewChild, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatCheckboxChange } from '@angular/material';

// spartacus
import {
  Address,
  CheckoutDeliveryService,
  Country,
  RoutingService,
  CheckoutActions,
  Order,
  WindowRef,
  CurrencyService
} from '@spartacus/core';
import { ICON_TYPE, ModalService } from '@spartacus/storefront';

// vendor
import { Observable, Subscription, ReplaySubject, BehaviorSubject } from 'rxjs';
import { filter, shareReplay } from 'rxjs/operators';
import { Store } from '@ngrx/store';
import { NgbModalRef, NgbModalOptions, NgbTabChangeEvent } from '@ng-bootstrap/ng-bootstrap';
import { isUndefined, isEqual, isEmpty, isNull } from 'lodash';

// components
import { AdyenIframeModalComponent } from '../adyen-iframe-modal/adyen-iframe-modal.component';

// services
import { RootStoreState } from '../../../../root-store';
import { CustomUserAddressService } from 'src/app/services/checkout/custom-user-address.service';
import { CustomCheckoutService } from '../../../../services/checkout/custom-checkout.service';
import { CustomCartService } from 'src/app/services/cart/facade/cart.service';

// interfaces
import { PaymentMethodsResponse, SavedPaymentsItem } from '../../../../interfaces/payment-methods-response.interface';
import { CustomCart } from '../../../../interfaces/custom-cart.interface';
import { PaymentDetailsResponse } from '../../../../interfaces/payment-details-response.interface';
import {
  ICustomAddress,
  IBillingAddressResponse,
  ICheckoutOrchestratorState,
  CheckoutFulfillmentTabs
} from 'src/app/interfaces/custom-checkout.interface';
import { StoreConfigService } from '../../../../services/config/store-config.service';

@Component({
  selector: 'cx-checkout-payment-details',
  templateUrl: './custom-checkout-payment-details.component.html',
  changeDetection: ChangeDetectionStrategy.Default
})
export class CustomCheckoutPaymentDetailsComponent implements OnInit, OnChanges, OnDestroy {
  @ViewChild('billingAddressModal', { static: false }) billingAddressModal: ElementRef;
  @Input() checkoutState: ICheckoutOrchestratorState;
  @Input() deliveryRestricted: boolean;
  @Input() deliveryModesRestricted: boolean;
  @Input() checkoutJourneyType: number;
  @Input() showList: boolean;
  sameAsShippingAddress = false;
  currentShippingAddress: ICustomAddress;
  shippingAddress$: Observable<ICustomAddress>;
  countries$: Observable<Country[]>;
  cart$ = new ReplaySubject<CustomCart>();
  billingAddress: Address | ICustomAddress;
  editingAddress: Address | ICustomAddress;
  existingAddresses$: BehaviorSubject<ICustomAddress[]> = new BehaviorSubject<ICustomAddress[]>(undefined);
  userAddressesSub: Subscription;
  paymentConfig = null;
  paymentMethodConfig: PaymentMethodsResponse | null = null;
  selectedBillingAddress: string | null = null;
  selectedBillingAddressCountry: string | null = null;
  hasSavedPaymentCards = false;
  savedPaymentCards: SavedPaymentsItem[];
  savedCardSelectedCode;
  savedPaymentData: SavedPaymentsItem[];
  paymentByNewCard = false;
  saveCardData = false;
  private checkoutStepUrlPrevious = 'cart';
  private shippingAddress: Address;
  private subscription = new Subscription();
  openModalRef: NgbModalRef;
  billingAddressModalOptions: NgbModalOptions = {
    windowClass: 'modal-checkout checkout-payment-details-modal modal-md d-flex',
    ariaLabelledBy: 'modal-checkout-billing-address'
  };
  isExistingAddress: boolean;
  isAddingAddress = false;
  iconTypes = ICON_TYPE;
  buildAddressList: string[];
  paymentTandCs: FormGroup = this.getConditionsFormGroup();
  isTandCsAccepted = false;
  checkoutFulfillmentTabs = CheckoutFulfillmentTabs;
  cartMergeComplete: Observable<boolean>;
  cartMergeSub: Subscription;
  addNewAddressSub: Subscription;
  voucherList$ = this.customCheckoutService.getLoyaltyVouchers().pipe(shareReplay());
  currency$ = this.currencyService.getActive();

  constructor(
    private checkoutDeliveryService: CheckoutDeliveryService,
    private customCheckoutService: CustomCheckoutService,
    private routingService: RoutingService,
    private store: Store<RootStoreState.State>,
    private cdRef: ChangeDetectorRef,
    private cartService: CustomCartService,
    private modalService: ModalService,
    private activatedRoute: ActivatedRoute,
    private userAddressService: CustomUserAddressService,
    private windowRef: WindowRef,
    private fb: FormBuilder,
    private paymentConfigService: StoreConfigService,
    private currencyService: CurrencyService
  ) {
    this.cartMergeComplete = this.cartService.getCartMergeComplete();
  }

  ngOnInit(): void {
    this.cartSub();
    this.watchTriggerToCloseModal();
    this.watchOrderDetails();
  }

  cartSub() {
    if (this.cartService.cartMergeRequired) {
      this.cartMergeSub = this.cartMergeComplete.subscribe((value: boolean) => {
        if (value) {
          this.getPaymentMethods();
          this.setShippingAddress();
        }
      });
    } else {
      this.getPaymentMethods();
      this.setShippingAddress();
    }
  }

  ngOnChanges() {
    this.sameAsShippingAddress = this.checkoutJourneyType === this.checkoutFulfillmentTabs.DELIVERY;
    this.checkCheckoutStateForBilling();
  }

  ngOnDestroy(): void {
    if (this.openModalRef) {
      this.openModalRef.close();
    }
    this.subscription.unsubscribe();
    this.destroyCartMergeSub();
    this.destroyNewAddressSub();
    this.destroyUserAddressesSub();
    this.cdRef.detach();
  }

  initPayments() {
    this.getPaymentMethods();
    this.setShippingAddress();
    this.destroyCartMergeSub();
  }

  checkCheckoutStateForBilling() {
    if (!this.sameAsShippingAddress) {
      if (this.isBillingAddressRequiredCIS() || this.isBillingAddressRequiredCP()) {
        this.setUserAddressesSub();
      }
    }
  }

  toggleSameAsShippingAddress(): void {
    this.sameAsShippingAddress = !this.sameAsShippingAddress;
    if (this.sameAsShippingAddress) {
      this.destroyUserAddressesSub();
    } else {
      this.selectedBillingAddress = null;
      this.selectedBillingAddressCountry = null;
      this.billingAddress = undefined;
      this.setUserAddressesSub();
    }
  }

  addressSelect(selectedAddress: ICustomAddress) {
    this.selectedBillingAddress = selectedAddress.id;
    this.selectedBillingAddressCountry = selectedAddress.country.isocode;
    this.billingAddress = selectedAddress;
  }

  addressEdit() {
    this.editingAddress = this.userAddressService.getAddressById(
      this.selectedBillingAddress,
      this.existingAddresses$.value
    );
    this.showAddressForm(true);
    this.triggerChanges();
  }

  setUserAddressesSub() {
    this.userAddressesSub = this.userAddressService
      .getBillingAddresses()
      .subscribe((response: IBillingAddressResponse) => {
        if (this.isAddingAddress) {
          this.selectAddedAddress(response);
        }
        this.existingAddresses$.next(response.addresses);
      });
  }

  selectAddedAddress(response: IBillingAddressResponse) {
    // find the address being added and select it
    const addedAddress = this.userAddressService.findNewAddress(this.buildAddressList, response.addresses);
    if (!isNull(addedAddress)) {
      this.selectedBillingAddress = addedAddress.id;
      this.selectedBillingAddressCountry = addedAddress.country.isocode;
      this.billingAddress = addedAddress;
    }
    this.isAddingAddress = false;
    this.triggerChanges();
  }

  showAddressForm(isExisting: boolean): void {
    this.isExistingAddress = isExisting;
    this.openAddressFormModal();
  }

  openAddressFormModal() {
    this.openModalRef = this.modalService.open(this.billingAddressModal, this.billingAddressModalOptions);
    this.openModalRef.result.then(
      () => {
        this.windowRef.nativeWindow.document.body.classList.remove('checkout-modal-open');
      },
      () => {
        this.windowRef.nativeWindow.document.body.classList.remove('checkout-modal-open');
      }
    );
  }

  closeAddressFormModal() {
    if (this.openModalRef) {
      this.openModalRef.close();
    }
    this.clearEditingAddress();
  }

  clearEditingAddress() {
    if (!isUndefined(this.editingAddress)) {
      this.editingAddress = undefined;
    }
  }

  addNewAddress(newAddress: ICustomAddress) {
    newAddress.billingAddress = true;
    this.isAddingAddress = true;
    this.buildAddressList = this.userAddressService.getUserAddressIdList(this.existingAddresses$.value);
    this.addNewAddressSub = this.userAddressService.addNewUserAddress(newAddress).subscribe(() => {
      this.closeAddressFormModal();
      this.destroyNewAddressSub();
      this.setUserAddressesSub();
    });
  }

  destroyNewAddressSub() {
    if (this.addNewAddressSub) {
      this.addNewAddressSub.unsubscribe();
    }
  }

  editSelectedAddress(address: ICustomAddress): void {
    address.billingAddress = true;
    this.userAddressService.updateUserAddress(address.id, address);
    this.closeAddressFormModal();
  }

  makePayments(): void {
    const billingAddress = this.setBillingAddress();
    const cardPaymentRequired = !!(this.paymentConfig && this.paymentConfig.data);
    const CardsData = cardPaymentRequired ? this.getCardsData(billingAddress) : {};
    const data = {
      cardPaymentRequired,
      billingAddress,
      sameAsShippingAddress: this.sameAsShippingAddress,
      ...CardsData
    };

    if (this.paymentConfig && this.paymentConfig.data) {
      const paymentMethodHandler = this.paymentConfig.data.paymentMethod;

      if (paymentMethodHandler && paymentMethodHandler.type === 'ideal') {
        data.cardPaymentRequired = false;
        data['adyenPaymentMethod'] = 'ideal';
        data['issueNumber'] = paymentMethodHandler.issuer;
      }

      if (paymentMethodHandler && paymentMethodHandler.type === 'giropay') {
        data.cardPaymentRequired = false;
        data['adyenPaymentMethod'] = 'giropay';
        data['issueNumber'] = paymentMethodHandler.bic;
      }

      if (
        paymentMethodHandler &&
        (paymentMethodHandler.type === 'klarna' ||
          paymentMethodHandler.type === 'klarna_paynow' ||
          paymentMethodHandler.type === 'klarna_account')
      ) {
        data.cardPaymentRequired = false;
        data['adyenPaymentMethod'] = paymentMethodHandler.type;
      }
    }
    this.customCheckoutService.makeAdyenCreditCardPayment(data).subscribe((val: PaymentDetailsResponse) => {
      this.checkPaymentMethod(val);
    });
  }

  getCardsData(billingAddress: ICustomAddress) {
    // CARDS
    if (this.paymentByNewCard || !this.hasSavedPaymentCards || !this.savedPaymentData || !this.savedPaymentData[0]) {
      // New
      const paymentData = this.paymentConfig.data.paymentMethod;
      return {
        accountHolderName: paymentData.holderName,
        adyenPaymentMethod: 'adyen_cc',
        encryptedCardNumber: paymentData.encryptedCardNumber,
        encryptedExpiryMonth: paymentData.encryptedExpiryMonth,
        encryptedExpiryYear: paymentData.encryptedExpiryYear,
        encryptedSecurityCode: paymentData.encryptedSecurityCode,
        saveCardData: this.saveCardData
      };
    } else {
      // Saved
      return {
        accountHolderName: this.savedPaymentData[0].accountHolderName,
        adyenPaymentMethod: 'adyen_oneclick_',
        cardPaymentRequired: true,
        adyenSelectedReference: this.savedPaymentData[0].adyenSelectedReference,
        encryptedSecurityCode: this.paymentConfig.data.paymentMethod.encryptedSecurityCode,
        billingAddress,
        sameAsShippingAddress: this.sameAsShippingAddress
      };
    }
  }

  setPaymentDetail(val: any): void {
    this.paymentConfig = val;
    this.triggerChanges();
  }

  setSavedPaymentData(savedPaymentData: SavedPaymentsItem[]) {
    this.savedPaymentData = savedPaymentData;
  }

  setSavedCard(val): void {
    this.saveCardData = val;
    this.triggerChanges();
  }

  triggerChanges() {
    if (!this.cdRef['destroyed']) {
      this.cdRef.detectChanges();
    }
  }

  back(): void {
    this.routingService.go(this.checkoutStepUrlPrevious);
  }

  destroyCartMergeSub() {
    if (this.cartMergeSub) {
      this.cartMergeSub.unsubscribe();
    }
  }

  destroyUserAddressesSub() {
    if (this.userAddressesSub) {
      this.userAddressesSub.unsubscribe();
    }
  }

  isPaymentModeVisible(paymentMode: string): boolean {
    return this.paymentConfigService.isPaymentVisible(paymentMode);
  }

  get isReadyToPay(): boolean {
    const paymentIsValid = this.paymentConfig && this.paymentConfig.isValid;
    if (!this.sameAsShippingAddress) {
      return paymentIsValid && typeof this.billingAddress !== 'undefined';
    }
    return paymentIsValid;
  }

  private setShippingAddress(): void {
    this.shippingAddress$ = this.checkoutDeliveryService.getDeliveryAddress();
    this.subscription.add(
      this.shippingAddress$.subscribe(address => {
        if (!this.sameAsShippingAddress) {
          if (!isEmpty(address)) {
            this.resetBillingCheck(address);
          } else {
            if (this.checkoutJourneyType !== this.checkoutFulfillmentTabs.DELIVERY) {
              this.resetBillingCheck({});
            }
          }
        } else {
          // simply apply the shipping address
          this.shippingAddress = this.currentShippingAddress = address;
        }
      })
    );
  }

  private resetBillingCheck(address: ICustomAddress) {
    if (!isEqual(address, this.currentShippingAddress)) {
      // the shipping address has changed...clear the billing address
      this.buildAddressList = [];
      this.currentShippingAddress = address;
      this.selectedBillingAddress = null;
      this.billingAddress = undefined;
    }
  }

  private setBillingAddress(): ICustomAddress {
    let billingAddress = {
      region: {},
      ...this.shippingAddress
    };
    if (this.checkoutJourneyType === this.checkoutFulfillmentTabs.DELIVERY) {
      if (!this.sameAsShippingAddress && !isUndefined(this.billingAddress)) {
        billingAddress = {
          region: {},
          ...this.billingAddress
        };
      }
      return billingAddress;
    } else {
      if (!isUndefined(this.billingAddress)) {
        return {
          region: {},
          ...this.billingAddress
        };
      }
    }
  }

  private getPaymentMethods(): void {
    setTimeout(() => {
      this.subscription.add(
        this.customCheckoutService
          .getAdyenConfiguration()
          .subscribe((resp: PaymentMethodsResponse) => this.handlePaymentMethodsResponse(resp))
      );
    }, 500);
  }

  private handlePaymentMethodsResponse(resp: PaymentMethodsResponse) {
    this.paymentMethodConfig = resp;
    if (!isUndefined(resp.savedPayments)) {
      if (!isUndefined(resp.savedPayments.payments)) {
        if (resp.savedPayments.payments.length >= 1) {
          this.hasSavedPaymentCards = true;
          this.savedPaymentCards = resp.savedPayments.payments;
        }
      }
    }

    if (!this.hasSavedPaymentCards) {
      this.paymentByNewCard = true;
    }
    this.triggerChanges();
  }

  tabChanged(event: NgbTabChangeEvent) {
    if (event.nextId === 'ngb-tab-0') {
      this.paymentByNewCard = false;
    } else {
      this.paymentByNewCard = true;
    }
  }

  getConditionsFormGroup() {
    return this.fb.group({
      acceptedTandCs: ['']
      // newsletter to go here
    });
  }

  tandCsSelection(event: MatCheckboxChange) {
    this.paymentTandCs.patchValue({
      acceptedTandCs: event.checked
    });
    this.isTandCsAccepted = this.paymentTandCs.get(['acceptedTandCs']).value;
  }

  private isBillingAddressRequiredCIS(): boolean {
    if (this.checkoutJourneyType === this.checkoutFulfillmentTabs.COLLECT_IN_STORE) {
      if (this.checkoutState.fullFillmentState && this.checkoutState.whoWillCollectState) {
        return true;
      }
    }
    return false;
  }

  private isBillingAddressRequiredCP(): boolean {
    if (this.checkoutJourneyType === this.checkoutFulfillmentTabs.COLLECTION_POINT) {
      if (
        this.checkoutState.fullFillmentState &&
        this.checkoutState.deliveryModesState &&
        this.checkoutState.whoWillCollectState
      ) {
        return true;
      }
    }
    return false;
  }

  private watchOrderDetails(): void {
    this.subscription.add(this.cartService.getActive().subscribe((resp: CustomCart) => this.cart$.next(resp)));
  }

  private checkPaymentMethod(value: PaymentDetailsResponse): void {
    if (!!value.paymentRedirect) {
      this.paymentRedirect(value);
      return;
    }
    this.store.dispatch(new CheckoutActions.PlaceOrderSuccess((value as any) as Order));
    this.routingService.go(['/order-confirmation']);
  }

  private paymentRedirect(value: PaymentDetailsResponse): void {
    const data = value.paymentRedirect;
    if (data.method === 'POST') {
      this.open3DSecureModal();
      this.customCheckoutService.postAdyen3dSecureRequest(data);
    }
    if (data.method === 'GET') {
      window.location.href = data.redirectUrl;
    }
  }

  private open3DSecureModal(): void {
    this.modalService.open(AdyenIframeModalComponent, {
      centered: true,
      size: 'lg'
    });
  }

  private closeModal(): void {
    this.modalService.closeActiveModal();
  }

  watchTriggerToCloseModal(): void {
    this.subscription.add(
      this.activatedRoute.queryParams.pipe(filter(params => params['redirectPayment'])).subscribe(params => {
        window.parent.postMessage(
          {
            for: 'auth',
            authData: params
          },
          `${document.location.protocol}//${document.location.hostname}${location.port ? ':' + location.port : ''}`
        );
        this.confirmAdyen3dSecureAuthorization();
      })
    );

    this.subscription.add(
      this.customCheckoutService.closeModal$.subscribe(() => {
        this.confirmAdyen3dSecureAuthorization();
        this.closeModal();
      })
    );
  }

  private confirmAdyen3dSecureAuthorization(): void {
    this.customCheckoutService.placeOrderAfter3dSecure().subscribe((value: PaymentDetailsResponse) => {
      this.checkPaymentMethod(value);
    });
  }
}
