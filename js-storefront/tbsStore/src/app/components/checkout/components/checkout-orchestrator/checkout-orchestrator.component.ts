/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
import { Component, HostBinding, OnInit, OnDestroy } from '@angular/core';

// spartacus
import { RoutingService, RouterState } from '@spartacus/core';

// vendor
import { Observable, Subscription } from 'rxjs';
import { isUndefined } from 'lodash';

// services
import { CustomCartService } from 'src/app/services/cart/facade/cart.service';
import { CollectionPointsService } from 'src/app/services/checkout/collection-points/collection-points.service';
import { CheckoutCollectInStoreService } from 'src/app/services/checkout/collect-in-store/collect-in-store.service';
import { CustomCheckoutService } from 'src/app/services/checkout/custom-checkout.service';
import { CartGiftWrapService } from 'src/app/services/cart-gift-wrap-service/cart-gift-wrap.service';
// interfaces
import {
  IDeliveryRestrictedState,
  IDeliveryModesRestrictedState,
  ICheckoutOrchestratorState,
  CheckoutFulfillmentTabs
} from 'src/app/interfaces/custom-checkout.interface';
import { CustomCart, IFullfillmentEligibilityState } from 'src/app/interfaces/custom-cart.interface';

@Component({
  selector: 'cx-checkout-orchestrator',
  templateUrl: './checkout-orchestrator.component.html'
})
export class CustomCheckoutOrchestratorComponent implements OnInit, OnDestroy {
  @HostBinding('class') class$ = 'd-flex flex-row flex-wrap mb-5';
  checkoutFulfillmentTabs = CheckoutFulfillmentTabs;
  checkoutJourneyType = this.checkoutFulfillmentTabs.DELIVERY;
  checkoutStates: ICheckoutOrchestratorState = {
    fullFillmentState: false,
    deliveryModesState: false,
    paymentDetailsState: false,
    whoWillCollectState: false
  };
  deliveryRestricted: IDeliveryRestrictedState = {
    isDeliveryRestricted: false
  };
  deliveryModesRestricted: IDeliveryModesRestrictedState = {
    isDeliveryModesRestricted: false
  };
  fulfillmentEligibilityState: IFullfillmentEligibilityState = {
    eligibleForCollectInStore: false,
    eligibleForCollectionPoint: false
  };

  collectionPoint$: Observable<any>;
  cis$: Observable<any>;
  subscription = new Subscription();
  constructor(
    private customCheckoutService: CustomCheckoutService,
    private customCartService: CustomCartService,
    private collectionPointsService: CollectionPointsService,
    private collectInStoreService: CheckoutCollectInStoreService,
    private cartGiftWrapService: CartGiftWrapService,
    private routingService: RoutingService
  ) {}
  ngOnInit() {
    this.setActiveCartSub();
    this.setRoutingSub();
    this.collectionPoint$ = this.collectionPointsService.getSelectedCollectionPoint();
    this.cis$ = this.collectInStoreService.getSelectedCollectionStore();
  }
  setFulfillmentState(state: boolean): void {
    this.checkoutStates.fullFillmentState = state;
    if (state === false) {
      this.checkoutStates.deliveryModesState = false;
    }
  }

  setDeliveryModesState(state: boolean): void {
    this.checkoutStates.deliveryModesState = state;
  }

  setPaymentDetailsState(state: boolean): void {
    this.checkoutStates.paymentDetailsState = state;
  }

  setCheckoutJourneyType(journeyType: number): void {
    this.resetStoreState();
    this.checkoutJourneyType = journeyType;
  }

  resetStoreState(): void {
    this.deliveryRestricted = {
      isDeliveryRestricted: false
    };
    this.deliveryModesRestricted = {
      isDeliveryModesRestricted: false
    };
    this.checkoutStates = {
      fullFillmentState: false,
      deliveryModesState: false,
      paymentDetailsState: false,
      whoWillCollectState: false
    };
  }

  setDeliveryRestricted(deliveryRestrictedState: IDeliveryRestrictedState): void {
    this.deliveryRestricted = deliveryRestrictedState;
  }

  setDeliveryModesRestricted(deliveryModesRestrictedState: IDeliveryModesRestrictedState): void {
    this.deliveryModesRestricted = deliveryModesRestrictedState;
  }

  setActiveCartSub(): void {
    this.subscription.add(
      this.customCartService.getActive().subscribe((data: CustomCart) => {
        // set the tabbed eligibility from cart
        // intercept and turn off unavailable fulfillment
        this.fulfillmentEligibilityState = {
          eligibleForCollectInStore: data.eligibleForCollectInStore,
          eligibleForCollectionPoint: data.eligibleForCollectionPoint
        };
        if (!isUndefined(data.collectionPoint) && data.eligibleForCollectionPoint) {
          this.collectionPointsService.setCollectionPoint(data.collectionPoint);
        } else {
          this.collectionPointsService.unsetCollectionPoint();
        }
        if (!isUndefined(data.deliveryPointOfService) && data.deliveryPointOfService.available) {
          this.collectInStoreService.setCollectionStore(data.deliveryPointOfService);
        } else {
          this.collectInStoreService.unsetCollectionStore();
        }
        // unset the personalised message if not present
        if (isUndefined(data.giftMessage)) {
          this.cartGiftWrapService.unsetStoreGiftWrapMessage();
        }
      })
    );
  }

  setRoutingSub() {
    this.subscription.add(
      this.routingService.getRouterState().subscribe((state: RouterState) => {
        if (!isUndefined(state.nextState)) {
          // if not navigating to confirmation page
          if (state.nextState.url.split('/')[3] !== 'order-confirmation') {
            // clear the checkout flow when navigating away
            // TODO call to custom endpopint
            this.customCheckoutService.clearServerSideCheckout();
          }
        }
      })
    );
  }

  chooseWhoWillCollect(state: boolean): void {
    this.checkoutStates.whoWillCollectState = state;
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
