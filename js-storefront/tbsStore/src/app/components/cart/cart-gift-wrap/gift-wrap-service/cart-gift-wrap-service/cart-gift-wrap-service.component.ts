/*
 * Copyright (c)
 * 2020 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
import { Component, Input, ViewChild, ElementRef, OnDestroy, OnInit, EventEmitter, Output } from '@angular/core';

// spartacus
import { ModalService, ICON_TYPE } from '@spartacus/storefront';

// vendor
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Subscription, Observable } from 'rxjs';

import { isUndefined } from 'lodash';

// services
import { CartGiftWrapService } from 'src/app/services/cart-gift-wrap-service/cart-gift-wrap.service';

// interfaces
import {
  CustomCart,
  CustomGiftCMSBannerComponentData,
  IGiftWrapMessage
} from 'src/app/interfaces/custom-cart.interface';

@Component({
  selector: 'cx-cart-gift-wrap-service',
  templateUrl: './cart-gift-wrap-service.component.html'
})
export class CartGiftWrapServiceComponent implements OnInit, OnDestroy {
  @ViewChild('giftWrapModal', { static: false }) giftWrapModal: ElementRef;
  @Input() cart: CustomCart;
  @Input() cmsData: CustomGiftCMSBannerComponentData;
  @Output() giftWrapAppliedEmit: EventEmitter<undefined> = new EventEmitter<undefined>();
  @Output() giftWrapRemovedEmit: EventEmitter<undefined> = new EventEmitter<undefined>();
  isRemoving = false;
  giftWrapModalRef: NgbModalRef;
  iconTypes = ICON_TYPE;
  addGiftWrapSub: Subscription;
  removeGiftWrapSub: Subscription;
  giftWrapService$: Observable<IGiftWrapMessage>;
  constructor(private modalService: ModalService, private cartGiftWrapService: CartGiftWrapService) {
    this.giftWrapService$ = this.cartGiftWrapService.getGiftWrapServiceFromState();
  }
  ngOnInit() {
    if (!isUndefined(this.cart.giftWrapApplied)) {
      this.setStoreGiftService();
    }
  }
  handleSubmit() {
    if (!this.isRemoving) {
      this.addGiftWrap();
    } else {
      this.removeGiftWrap();
    }
  }

  setStoreGiftService() {
    if (this.cart.giftWrapApplied) {
      this.cartGiftWrapService.setStoreGiftWrapService(this.getGiftServiceData(true));
    } else {
      this.cartGiftWrapService.unsetStoreGiftWrapService(this.getGiftServiceData(false));
    }
  }

  getGiftServiceData(isApplied: boolean): IGiftWrapMessage {
    return {
      giftWrapApplied: isApplied,
      giftWrapServiceImage: this.cmsData.giftWrapServiceImage,
      giftWrapServiceMessage: this.cmsData.giftWrapServiceMessage
    };
  }

  addGiftWrap() {
    this.addGiftWrapSub = this.cartGiftWrapService.addGiftWrapService().subscribe(() => {
      this.cartGiftWrapService.setStoreGiftWrapService(this.getGiftServiceData(true));
      this.destroyAddGiftWrapSub();
      this.closeGiftWrapModal();
      this.giftWrapAppliedEmit.emit();
    });
  }

  removeGiftWrap() {
    this.removeGiftWrapSub = this.cartGiftWrapService.removeGiftWrapService().subscribe(() => {
      this.cartGiftWrapService.unsetStoreGiftWrapService(this.getGiftServiceData(false));
      this.destroyRemoveGiftWrapSub();
      this.closeGiftWrapModal();
      this.giftWrapRemovedEmit.emit();
    });
  }

  openGiftWrapModal() {
    this.isRemoving = false;
    this.giftWrapModalRef = this.modalService.open(this.giftWrapModal, { size: 'lg' });
  }

  openGiftWrapRemoveModal() {
    this.isRemoving = true;
    this.giftWrapModalRef = this.modalService.open(this.giftWrapModal, { size: 'lg' });
  }

  closeGiftWrapModal() {
    if (this.giftWrapModalRef) {
      this.giftWrapModalRef.close();
    }
  }
  destroyAddGiftWrapSub() {
    if (this.addGiftWrapSub) {
      this.addGiftWrapSub.unsubscribe();
    }
  }
  destroyRemoveGiftWrapSub() {
    if (this.removeGiftWrapSub) {
      this.removeGiftWrapSub.unsubscribe();
    }
  }

  ngOnDestroy() {
    this.destroyAddGiftWrapSub();
    this.destroyRemoveGiftWrapSub();
  }
}
