import { ChangeDetectionStrategy, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ICON_TYPE, ModalRef, ModalService } from '@spartacus/storefront';
import { Store } from '@ngrx/store';
import { WishlistsStoreActions, WishlistsStoreSelectors, WishlistsStoreState } from '../../../root-store/wishlists';
import { NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { WishlistNameModal } from '../../../interfaces/wishlists.interface';

@Component({
  selector: 'app-wishlist-name-modal',
  templateUrl: './wishlist-name-modal.component.html'
})
export class WishlistNameModalComponent implements OnInit {
  modalRef: ModalRef;
  iconTypes = ICON_TYPE;
  isEditing: boolean;
  nameFormModal: WishlistNameModal;

  @ViewChild('wishlistModal', { static: true }) wishListModalContent: ElementRef;

  constructor(private modalService: ModalService, public store: Store<WishlistsStoreState.State>) {}

  ngOnInit() {
    this.store.select(WishlistsStoreSelectors.getWishlistsIsOpenState).subscribe((nameModal: WishlistNameModal) => {
      if (nameModal.isModalOpen) {
        this.openWishlistModal();
        this.isEditing = nameModal.isModalNameEdit;
        this.nameFormModal = nameModal;
      } else {
        this.modalService.closeActiveModal('close');
      }
    });
  }

  openWishlistModal() {
    const ngbModalOptions: NgbModalOptions = {
      backdrop: 'static',
      keyboard: false,
      ariaLabelledBy: 'modal-basic-title'
    };
    this.modalRef = this.modalService.open(this.wishListModalContent, ngbModalOptions);
  }

  closeModal() {
    this.store.dispatch(new WishlistsStoreActions.CloseWishlistNameModalAction({ nameModal: { isModalOpen: false } }));
    this.modalService.closeActiveModal('close');
  }
}
