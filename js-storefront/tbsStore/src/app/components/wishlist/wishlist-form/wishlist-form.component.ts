import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { FormGroup, Validators, FormBuilder } from '@angular/forms';
import { WishlistService } from 'src/app/services/wishlist/wishlist.service';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';
import { WishlistsStoreActions, WishlistsStoreState } from '../../../root-store/wishlists';
import { Store } from '@ngrx/store';

@Component({
  selector: 'app-wishlist-form',
  templateUrl: './wishlist-form.component.html'
})
export class WishlistFormComponent implements OnInit {
  createWishlistForm: FormGroup = this.getWishlistNameFormGroup();
  enableSubmit = false;
  createWishSub: Subscription;
  updateWishSub: Subscription;
  wishlistName: string;
  wishlistId: string;

  @Input() isEdit: boolean = false;
  @Input() productId: string | null = null;
  @Input() nameFormModal: any = false;
  @Input() hideCancel: boolean = false;
  @Output() triggerClosingParentModal: EventEmitter<boolean> = new EventEmitter<boolean>();
  @Output() triggerForm: EventEmitter<any> = new EventEmitter<any>();

  constructor(
    public store: Store<WishlistsStoreState.State>,
    private router: Router,
    private fb: FormBuilder,
    private wishlistService: WishlistService
  ) {}

  ngOnInit() {
    this.createWishlistForm.valueChanges.subscribe(() => {
      this.enableSubmit = this.createWishlistForm.valid;
    });

    if (this.isEdit) {
      let urlArray = this.router.routerState.snapshot.url.split('/');
      this.wishlistId = urlArray[urlArray.length - 1];
      this.wishlistService.getWishlistById(this.wishlistId).subscribe(data => {
        this.wishlistName = data.wishlistName;
        this.createWishlistForm.patchValue({
          wishlistName: data.wishlistName
        });
      });
    }
  }

  getWishlistNameFormGroup() {
    return this.fb.group({
      wishlistName: ['', Validators.compose([Validators.required, Validators.maxLength(60), Validators.minLength(3)])]
    });
  }

  triggerCreateNewWishlist() {
    const listName = this.createWishlistForm.get(['wishlistName']).value;

    if (!this.isEdit) {
      this.createWishSub = this.wishlistService.createNew(listName).subscribe(success => {
        if (!this.productId) {
          // No Product to add to new wishlist
          this.router.navigate(['my-account', 'wishlist', success.wishlistId], {});
          this.store.dispatch(
            new WishlistsStoreActions.CloseWishlistNameModalAction({ nameModal: { isModalOpen: false } })
          );
        } else {
          // Add product to wishlist
          this.wishlistService.addToListId(success.wishlistId, this.productId).subscribe(success => {
            this.router.navigate(['my-account', 'wishlist', success.wishlistId], {});
            this.productId = null;
            this.store.dispatch(
              new WishlistsStoreActions.CloseWishlistNameModalAction({ nameModal: { isModalOpen: false } })
            );
          });
        }
      });
    } else {
      this.updateWishSub = this.wishlistService.renameWishlist(this.wishlistId, listName).subscribe((success: any) => {
        this.store.dispatch(
          new WishlistsStoreActions.CloseWishlistNameModalAction({ nameModal: { isModalOpen: false } })
        );
        this.store.dispatch(new WishlistsStoreActions.UpdateWishlistName({ newWishlistName: success.wishlistName }));
      });
    }
  }

  triggerCancel() {
    this.triggerClosingParentModal.emit(true);
  }
}
