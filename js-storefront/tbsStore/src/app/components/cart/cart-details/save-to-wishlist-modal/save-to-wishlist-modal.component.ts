import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ICON_TYPE, ModalRef, ModalService } from '@spartacus/storefront';
import { Store } from '@ngrx/store';
import { NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { WishlistsStoreActions, WishlistsStoreSelectors, WishlistsStoreState } from '../../../../root-store/wishlists';
import { CartToWishlistModal } from '../../../../interfaces/wishlists.interface';

@Component({
  selector: 'app-save-to-wishlist-modal',
  templateUrl: './save-to-wishlist-modal.component.html'
})
export class SaveToWishlistModalComponent implements OnInit {
  modalRef: ModalRef;
  iconTypes = ICON_TYPE;

  @ViewChild('saveToWishlistModal', { static: true }) wishListModalContent: ElementRef;

  constructor(private modalService: ModalService, public store: Store<WishlistsStoreState.State>) {}

  ngOnInit() {
    this.store.select(WishlistsStoreSelectors.getCartToWishlistState).subscribe((success: CartToWishlistModal) => {
      if (success) {
        if (!success.isOpen) {
          this.modalService.closeActiveModal('close');
        } else {
          this.openWishlistModal();
        }
      }
    });
  }

  openWishlistModal() {
    const ngbModalOptions: NgbModalOptions = {
      keyboard: false,
      ariaLabelledBy: 'modal-basic-title'
    };
    this.modalRef = this.modalService.open(this.wishListModalContent, ngbModalOptions);
  }

  closeModal() {
    this.store.dispatch(new WishlistsStoreActions.CloseAddFromCartModalAction());
    this.modalService.closeActiveModal('close');
  }
}
