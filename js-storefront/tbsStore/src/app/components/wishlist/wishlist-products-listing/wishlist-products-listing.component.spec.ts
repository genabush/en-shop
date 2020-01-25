import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WishlistProductsListingComponent } from './wishlist-products-listing.component';
import { WishlistProductItemComponent } from '../wishlist-product-item/wishlist-product-item.component';
import { MockCxTranslatePipe, MockCxUrlPipe } from 'src/app/testing/mock.pipes';
import {
  MockCxIconComponent,
  MockCxMediaComponent,
  MockAppBackInStockComponent
} from 'src/app/testing/mock.components';
import { RouterTestingModule } from '@angular/router/testing';
import { AddToCartComponent } from '../../cart/add-to-cart/add-to-cart.component';
import { NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';
import { FindInStoreComponent } from '../../find-in-store/find-in-store.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatInputModule, MatFormFieldModule, MatSelectModule } from '@angular/material';
import { AgmCoreModule } from '@agm/core';
import { CollectionPointItemModule } from '../../checkout/fulfillment/collection-point-item/collection-point-item.module';
import { WishlistProductsListingRefineComponent } from '../wishlist-products-listing-refine/wishlist-products-listing-refine.component';
import { CustomQuantitySelectorComponent } from '../../shared/custom-quantity-selector/custom-quantity-selector.component';
import { WishlistFormComponent } from '../wishlist-form/wishlist-form.component';
import { MessagingComponent } from '../../shared/messaging/messaging.component';
import { MockDatePipe } from '@spartacus/core';

describe('WishlistProductsListingComponent', () => {
  let component: WishlistProductsListingComponent;
  let fixture: ComponentFixture<WishlistProductsListingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        NgbTooltipModule,
        FormsModule,
        ReactiveFormsModule,
        MatInputModule,
        MatFormFieldModule,
        CollectionPointItemModule,
        AgmCoreModule,
        MatSelectModule
      ],
      declarations: [
        WishlistProductsListingComponent,
        WishlistProductItemComponent,
        MockCxUrlPipe,
        MockCxMediaComponent,
        WishlistProductItemComponent,
        AddToCartComponent,
        MockCxTranslatePipe,
        MockAppBackInStockComponent,
        MessagingComponent,
        MockCxIconComponent,
        WishlistFormComponent,
        FindInStoreComponent,
        WishlistProductsListingRefineComponent,
        CustomQuantitySelectorComponent,
        MockDatePipe
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WishlistProductsListingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
