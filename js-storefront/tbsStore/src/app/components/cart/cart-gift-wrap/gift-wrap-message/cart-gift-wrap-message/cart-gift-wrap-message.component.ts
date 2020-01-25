/*
 * Copyright (c)
 * 2020 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
import { Component, Input, EventEmitter, Output, ViewChild, ElementRef, OnInit } from '@angular/core';

// spartacus
import { ModalService, ICON_TYPE } from '@spartacus/storefront';

//vendor
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Observable, Subscription } from 'rxjs';

// services
import { CartGiftWrapService } from 'src/app/services/cart-gift-wrap-service/cart-gift-wrap.service';

// interfaces
import {
  CustomCart,
  IGiftWrapMessage,
  EMOJI_RANGE_EXP,
  TRAILING_SPACES_EXP
} from 'src/app/interfaces/custom-cart.interface';

@Component({
  selector: 'cx-cart-gift-wrap-message',
  templateUrl: './cart-gift-wrap-message.component.html'
})
export class CartGiftWrapMessageComponent implements OnInit {
  @ViewChild('giftMessageModal', { static: false }) giftMessageModal: ElementRef;
  messageModalRef: NgbModalRef;
  @Input() cart: Observable<CustomCart>;
  @Output() personalMessageEmit: EventEmitter<undefined> = new EventEmitter<undefined>();
  giftWrapMessage$: Observable<IGiftWrapMessage>;
  iconTypes = ICON_TYPE;
  addMessageSub: Subscription;
  removeMessageSub: Subscription;
  isRemoval = false;
  constructor(protected modalService: ModalService, protected cartGiftWrapService: CartGiftWrapService) {
    this.giftWrapMessage$ = this.cartGiftWrapService.getGiftWrapFromState();
  }
  ngOnInit() {
    this.cart.subscribe((activeCart: CustomCart) => this.setStoreGiftMessage(activeCart));
  }

  setStoreGiftMessage(cart: CustomCart) {
    if (cart.giftMessage && cart.giftMessageSenderName && cart.giftMessageName) {
      this.cartGiftWrapService.setStoreGiftWrapMessage({
        giftMessage: cart.giftMessage,
        giftMessageSenderName: cart.giftMessageSenderName,
        giftMessageName: cart.giftMessageName
      });
    } else {
      this.cartGiftWrapService.unsetStoreGiftWrapMessage();
    }
  }

  handleSubmit(giftMessage?: IGiftWrapMessage) {
    if (!this.isRemoval) {
      this.addPersonalMessage(giftMessage);
    } else {
      this.removePersonalMessage();
    }
  }

  addPersonalMessage(giftMessage: IGiftWrapMessage) {
    const processedMessage: IGiftWrapMessage = this.getProcessedGiftMessage(giftMessage);

    this.addMessageSub = this.cartGiftWrapService.addGiftWrapMessage(processedMessage).subscribe(() => {
      // this.cartGiftWrapService.setStoreGiftWrapMessage(processedMessage);
      this.destroyAddSub();
      this.closeMessageModal();
      this.personalMessageEmit.emit();
    });
  }

  private getProcessedGiftMessage(giftMessage: IGiftWrapMessage): IGiftWrapMessage {
    // replaces all items in the emoji range with empty spaces, and then replaces trailing spaces
    // Tested emojis found at https://getemoji.com/
    return {
      giftMessage: giftMessage.giftMessage.replace(EMOJI_RANGE_EXP, '').replace(TRAILING_SPACES_EXP, ''),
      giftMessageSenderName: giftMessage.giftMessageSenderName
        .replace(EMOJI_RANGE_EXP, '')
        .replace(TRAILING_SPACES_EXP, ''),
      giftMessageName: giftMessage.giftMessageName.replace(EMOJI_RANGE_EXP, '').replace(TRAILING_SPACES_EXP, '')
    };
  }

  removePersonalMessage() {
    this.removeMessageSub = this.cartGiftWrapService.removeGiftWrapMessage().subscribe(() => {
      // this.cartGiftWrapService.unsetStoreGiftWrapMessage();
      this.destroyRemoveSub();
      this.closeMessageModal();
      this.personalMessageEmit.emit();
    });
  }

  openRemoveMessageModal() {
    this.isRemoval = true;
    this.messageModalRef = this.modalService.open(this.giftMessageModal);
  }

  closeRemoveMessageModal() {
    if (this.messageModalRef) {
      this.messageModalRef.close();
    }
  }
  openMessageModal() {
    this.isRemoval = false;
    this.messageModalRef = this.modalService.open(this.giftMessageModal);
  }

  closeMessageModal() {
    if (this.messageModalRef) {
      this.messageModalRef.close();
    }
  }

  destroyAddSub() {
    if (this.addMessageSub) {
      this.addMessageSub.unsubscribe();
    }
  }
  destroyRemoveSub() {
    if (this.removeMessageSub) {
      this.removeMessageSub.unsubscribe();
    }
  }
  ngOnDestroy() {
    this.destroyAddSub();
    this.destroyRemoveSub();
  }
}
