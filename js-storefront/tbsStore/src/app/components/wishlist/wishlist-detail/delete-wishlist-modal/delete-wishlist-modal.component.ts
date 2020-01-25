import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ICON_TYPE, ModalRef, ModalService } from '@spartacus/storefront';
import { WishlistDeleteModal } from '../../../../interfaces/wishlists.interface';
import { Store } from '@ngrx/store';
import { WishlistsStoreActions, WishlistsStoreSelectors, WishlistsStoreState } from '../../../../root-store/wishlists';
import { NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { WishlistService } from '../../../../services/wishlist/wishlist.service';
import { StateWithGlobalMessage } from '@spartacus/core/src/global-message/store/global-message-state';
import { GlobalMessageActions, GlobalMessageType, TranslatePipe } from '@spartacus/core';

@Component({
  selector: 'app-delete-wishlist-modal',
  templateUrl: './delete-wishlist-modal.component.html'
})
export class DeleteWishlistModalComponent implements OnInit {
  modalRef: ModalRef;
  iconTypes = ICON_TYPE;
  wishlistId: string;

  @ViewChild('wishlistDeleteModal', { static: true }) wishListModalContent: ElementRef;

  constructor(
    private router: Router,
    private modalService: ModalService,
    private wishlistService: WishlistService,
    public store: Store<WishlistsStoreState.State>,
    private messageStore: Store<StateWithGlobalMessage>
  ) {}

  ngOnInit() {
    this.store
      .select(WishlistsStoreSelectors.getWishlistsDeleteModalState)
      .subscribe((deleteModal: WishlistDeleteModal) => {
        if (deleteModal.isModalOpen) {
          this.openDeleteWishlistModal();
          this.wishlistId = deleteModal.wishlistId;
        } else {
          this.modalService.closeActiveModal('close');
        }
      });
  }

  openDeleteWishlistModal() {
    const ngbModalOptions: NgbModalOptions = {
      keyboard: false,
      ariaLabelledBy: 'modal-basic-title'
    };
    this.modalRef = this.modalService.open(this.wishListModalContent, ngbModalOptions);
  }

  confirmDeletion() {
    if (this.wishlistId) {
      this.wishlistService.deleteWishlist(this.wishlistId).subscribe((success: boolean) => {
        if (success) {
          this.closeDeleteModal();
          this.router.navigate(['my-account', 'wishlist']);
          this.messageStore.dispatch(
            new GlobalMessageActions.AddMessage({
              text: { key: 'wishlists.deleteWishlistModal.globalMessage' },
              type: GlobalMessageType.MSG_TYPE_CONFIRMATION
            })
          );
        }
      });
    }
  }

  closeDeleteModal() {
    this.store.dispatch(
      new WishlistsStoreActions.CloseWishlistDeleteModalAction({ deleteModal: { isModalOpen: false } })
    );
    this.modalService.closeActiveModal('close');
  }
}
