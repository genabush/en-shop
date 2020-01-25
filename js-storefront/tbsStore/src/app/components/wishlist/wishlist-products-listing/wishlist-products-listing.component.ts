import { Component, Input, Output, EventEmitter, ViewChild, ElementRef } from '@angular/core';
import { ICON_TYPE, ModalService } from '@spartacus/storefront';
import { NgbModalRef, NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
@Component({
  selector: 'app-wishlist-products-listing',
  templateUrl: './wishlist-products-listing.component.html'
})
export class WishlistProductsListingComponent {
  @Input() products: Array<any>;
  @Input() columnClass: string = 'col-6 col-md-4 col-lg-3';
  @Output() productListingEmit = new EventEmitter<string>();

  iconTypes = ICON_TYPE;
  valueToDelete: string;
  @ViewChild('deleteConfirmModal', { static: false }) deleteConfirmModal: ElementRef;
  deleteConfirmModalRef: NgbModalRef;
  modalOptions: NgbModalOptions = {
    windowClass: '',
    ariaLabelledBy: 'modal-delete-confirm',
    size: 'lg'
  };

  constructor(private modalService: ModalService) {}

  triggerRemoveProduct($event) {
    this.valueToDelete = $event;
    this.deleteConfirmModalRef = this.modalService.open(this.deleteConfirmModal, this.modalOptions);
  }

  confirmDeletion() {
    this.closeDeleteConfirmModal();
    this.productListingEmit.emit(this.valueToDelete);
  }

  closeDeleteConfirmModal() {
    if (this.deleteConfirmModalRef) {
      this.deleteConfirmModalRef.close();
    }
  }
  ngOnDestroy() {
    this.closeDeleteConfirmModal();
  }
}
