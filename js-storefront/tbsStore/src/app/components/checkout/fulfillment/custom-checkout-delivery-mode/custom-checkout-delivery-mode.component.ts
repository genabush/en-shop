import {
  Component,
  OnDestroy,
  OnInit,
  ChangeDetectorRef,
  Output,
  EventEmitter,
  ElementRef,
  ViewChild,
  Input,
  ViewRef
} from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

// spartacus
import { DeliveryMode, Cart, WindowRef } from '@spartacus/core';
import { ICON_TYPE, ModalService } from '@spartacus/storefront';

// vendor
import { NgbModalRef, NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { Subscription, BehaviorSubject, Observable } from 'rxjs';
import { isEqual, isEmpty, isUndefined } from 'lodash';

// services
import { CustomCheckoutDeliveryService } from 'src/app/services/checkout/delivery/custom-checkout-delivery.service';
import { CustomCartService } from 'src/app/services/cart/facade/cart.service';
import { CollectionPointsService } from 'src/app/services/checkout/collection-points/collection-points.service';
import { CheckoutCollectInStoreService } from 'src/app/services/checkout/collect-in-store/collect-in-store.service';

// interfaces
import {
  IDeliveryModesResponse,
  IDeliveryModesRestrictedState,
  ICustomAddress,
  IDeliveryModesChoice,
  CheckoutFulfillmentTabs
} from 'src/app/interfaces/custom-checkout.interface';
import { CollectPoint } from 'src/app/interfaces/collection-point.interface';

@Component({
  selector: 'cx-checkout-delivery-modes',
  templateUrl: './custom-checkout-delivery-mode.component.html'
})
export class CustomDeliveryModeComponent implements OnInit, OnDestroy {
  @ViewChild('deliveryModesModal', { static: false }) deliveryModesModal: ElementRef;
  @Output() deliveryModesStateEmit: EventEmitter<boolean> = new EventEmitter<boolean>();
  @Output() deliveryModesRestrictedEmit: EventEmitter<IDeliveryModesRestrictedState> = new EventEmitter<
    IDeliveryModesRestrictedState
  >();
  @Input() checkoutJourneyType: number;
  collectionPoint$: Observable<any>;
  checkoutFulfillmentTabs = CheckoutFulfillmentTabs;
  iconTypes = ICON_TYPE;
  supportedDeliveryModes$: BehaviorSubject<DeliveryMode[]> = new BehaviorSubject<DeliveryMode[]>([]);
  selectedDeliveryMode$: BehaviorSubject<DeliveryMode> = new BehaviorSubject<DeliveryMode>(null);
  selectedDeliveryMode: number | null = null;
  currentDeliveryModeId: string;
  supportedModesSub: Subscription;
  subscription: Subscription = new Subscription();
  isLoading = true;
  productErrorList: any[];
  openModalRef: NgbModalRef;
  modalOptions: NgbModalOptions = {
    windowClass: 'modal-checkout checkout-delivery-modes-modal d-flex',
    ariaLabelledBy: 'modal-checkout-delivery-modes'
  };
  mode: FormGroup = this.fb.group({
    deliveryModeId: ['', Validators.required]
  });
  activeCart: Cart;
  hasDeliveryModeErrors = false;
  errors$: string[];
  currentAddress: ICustomAddress;
  collectionPoint: CollectPoint;
  deliveryAddressSub: Subscription;
  collectionPointSub: Subscription;
  deliveryModesLoadingSub: Subscription;
  constructor(
    private fb: FormBuilder,
    private checkoutDeliveryService: CustomCheckoutDeliveryService,
    private cd: ChangeDetectorRef,
    private cartService: CustomCartService,
    private modalService: ModalService,
    private windowRef: WindowRef,
    public collectionPointsService: CollectionPointsService,
    public collectionInStoreService: CheckoutCollectInStoreService
  ) {}

  ngOnInit() {
    this.cartService.getActive().subscribe(activeCart => {
      this.activeCart = activeCart;
      this.setDeliveryModeSub();
      this.triggerChanges();
    });
    this.applyJourneyType();
  }

  triggerChanges() {
    if (this.cd && !(this.cd as ViewRef).destroyed) {
      this.cd.detectChanges();
    }
  }

  applyJourneyType() {
    this.clearFulfillmentSubs();
    if (this.checkoutJourneyType !== this.checkoutFulfillmentTabs.DELIVERY) {
      this.setCheckoutCollectionPointSub();
    } else if (this.checkoutJourneyType === this.checkoutFulfillmentTabs.DELIVERY) {
      this.setCheckoutDeliveryAddressSub();
    }
  }

  clearFulfillmentSubs() {
    this.destroyCheckoutDeliveryAddressSub();
    this.destroyCheckoutCollectionPointSub();
    this.currentAddress = undefined;
    this.collectionPoint = undefined;
  }

  setCheckoutDeliveryAddressSub() {
    this.deliveryAddressSub = this.checkoutDeliveryService
      .getDeliveryAddress()
      .subscribe((currentAddress: ICustomAddress) => {
        if (!isEmpty(currentAddress) && !isUndefined(this.currentAddress)) {
          if (!isEqual(this.currentAddress, currentAddress)) {
            this.currentAddress = currentAddress;
            // reset the delivery tab
            this.selectedDeliveryMode = null;
            this.checkoutDeliveryService.clearSetDeliveryMode();
            this.collectionInStoreService.unsetCollectionStore();
            this.collectionPointsService.unsetCollectionPoint();
          }
        }
        this.setSupportedModesSub();
      });
  }

  destroyCheckoutDeliveryAddressSub() {
    if (this.deliveryAddressSub) {
      this.deliveryAddressSub.unsubscribe();
    }
  }

  setCheckoutCollectionPointSub() {
    this.collectionPointSub = this.collectionPointsService
      .getSelectedCollectionPoint()
      .subscribe((collectionPoint: CollectPoint) => {
        if (!isEmpty(collectionPoint)) {
          if (!isEqual(this.collectionPoint, collectionPoint)) {
            // reset the delivery Modes
            this.collectionPoint = collectionPoint;
            this.selectedDeliveryMode = null;
            this.checkoutDeliveryService.clearDeliveryAddressTab();
          }
          this.setSupportedModesSub();
        }
      });
  }

  destroyCheckoutCollectionPointSub() {
    if (this.collectionPointSub) {
      this.collectionPointSub.unsubscribe();
    }
  }

  setSupportedModesSub() {
    this.checkoutDeliveryService.isLoadingDeliveryModes.next(true);
    this.supportedModesSub = this.checkoutDeliveryService.getApiSupportedDeliveryModes().subscribe(
      (response: IDeliveryModesResponse) => {
        this.handleDeliveryModeResponse(response);
        this.destroySupportedModesSub();
      },
      err => {
        // ignore store not loaded
        this.checkoutDeliveryService.isLoadingDeliveryModes.next(true);
        this.isLoading = false;
        this.destroySupportedModesSub();
        this.triggerChanges();
      }
    );
  }

  handleDeliveryModeResponse(response: IDeliveryModesResponse) {
    if (response.deliveryModes.length === 0) {
      // handle delivery modes errors
      this.errors$ = response.deliveryModeRestrictionError.errorList.map((errorItem: string) => {
        return errorItem.split('.')[2];
      });
      this.supportedDeliveryModes$.next([]);
      this.emitDeliveryRestricted(true, this.errors$);
      this.checkoutDeliveryService.isLoadingDeliveryModes.next(false);
    } else if (response.deliveryModes.length > 0) {
      // handle a list of delivery modes
      this.supportedDeliveryModes$.next(response.deliveryModes);
      this.emitDeliveryRestricted(false);
      this.checkoutDeliveryService.isLoadingDeliveryModes.next(false);
      // select if only one(1) delivery mode
      if (response.deliveryModes.length === 1 && this.currentDeliveryModeId !== response.deliveryModes[0].code) {
        this.currentDeliveryModeId = response.deliveryModes[0].code;
        this.changeDeliveryMode();
      }
    }
  }

  emitDeliveryRestricted(isDeliveryRestricted: boolean, errors$?: string[]) {
    this.deliveryModesRestrictedEmit.emit({
      isDeliveryModesRestricted: isDeliveryRestricted,
      errorsList: errors$
    });
    this.isLoading = false;
    this.triggerChanges();
  }

  setDeliveryModeSub() {
    this.subscription.add(
      this.checkoutDeliveryService.getCustomSelectedDeliveryMode().subscribe(deliveryMode => {
        if (!isEmpty(deliveryMode)) {
          this.handleSelectedDeliveryMode(deliveryMode);
        } else {
          this.handleSelectedDeliveryMode();
        }
      })
    );
  }

  handleSelectedDeliveryMode(deliveryMode?: DeliveryMode) {
    if (deliveryMode) {
      const code = deliveryMode && deliveryMode.code ? deliveryMode.code : undefined;
      if (!isUndefined(code)) {
        this.mode.controls['deliveryModeId'].setValue(code);
        this.selectedDeliveryMode$.next(deliveryMode);
        this.setModeIndex(deliveryMode);
        this.deliveryModesStateEmit.emit(true);
      } else {
        this.setModeIndex();
      }
    } else {
      this.currentDeliveryModeId = undefined;
      this.mode.controls['deliveryModeId'].setValue(undefined);
      this.setModeIndex();
      this.deliveryModesStateEmit.emit(false);
    }
  }

  setModeIndex(deliveryModeSelected?: DeliveryMode) {
    if (deliveryModeSelected) {
      const deliveryModes = this.supportedDeliveryModes$.value;
      if (deliveryModes.length > 0) {
        for (let cnt = 0; cnt <= deliveryModes.length - 1; cnt++) {
          if (deliveryModeSelected.code === deliveryModes[cnt].code) {
            this.selectedDeliveryMode = cnt + 1;
          }
        }
      } else if (this.checkoutDeliveryService.isLoadingDeliveryModes.value) {
        this.setDeliveryModesLoadingSub(deliveryModeSelected);
      }
    } else {
      this.selectedDeliveryMode = null;
    }
    this.triggerChanges();
  }

  setDeliveryModesLoadingSub(deliveryModeSelected: DeliveryMode) {
    // subscribe once to the loading state, and recursively call it when the delivery modes are loaded
    this.deliveryModesLoadingSub = this.checkoutDeliveryService.isLoadingDeliveryModes.subscribe(isLoading => {
      if (!isLoading) {
        this.setModeIndex(deliveryModeSelected);
        this.destroyDeliveryModesLoadingSub();
      }
    });
  }

  destroyDeliveryModesLoadingSub() {
    if (this.deliveryModesLoadingSub) {
      this.deliveryModesLoadingSub.unsubscribe();
    }
  }

  changeMode(ev: IDeliveryModesChoice): void {
    if (ev.deliveryMode.code !== this.currentDeliveryModeId) {
      this.currentDeliveryModeId = ev.deliveryMode.code;
      this.triggerChanges();
    }
  }

  changeDeliveryMode() {
    this.checkoutDeliveryService.setDeliveryMode(this.currentDeliveryModeId);
    this.closeModesModal();
  }

  chooseDeliveryMode() {
    this.openModesModal();
  }

  openModesModal() {
    this.windowRef.nativeWindow.document.body.classList.add('checkout-modal-open');
    this.openModalRef = this.modalService.open(this.deliveryModesModal, this.modalOptions);
    this.openModalRef.result.then(
      () => {
        this.windowRef.nativeWindow.document.body.classList.remove('checkout-modal-open');
        this.closeModesModal();
      },
      () => {
        this.windowRef.nativeWindow.document.body.classList.remove('checkout-modal-open');
        this.closeModesModal();
      }
    );
  }

  closeModesModal() {
    if (this.openModalRef) {
      this.openModalRef.close();
    }
  }

  get deliveryModeInvalid(): boolean {
    return this.mode.controls['deliveryModeId'].invalid;
  }

  destroySupportedModesSub() {
    if (this.supportedModesSub) {
      this.supportedModesSub.unsubscribe();
    }
  }
  ngOnDestroy(): void {
    this.cd.detach();
    this.closeModesModal();
    this.destroySupportedModesSub();
    this.destroyCheckoutDeliveryAddressSub();
    this.destroyCheckoutCollectionPointSub();
    this.subscription.unsubscribe();
  }
}
