import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WishlistProductItemComponent } from './wishlist-product-item.component';
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';
import {
  MockCxIconComponent,
  MockAppBackInStockComponent,
  MockCxMediaComponent,
  MockStore
} from 'src/app/testing/mock.components';
import { PipeTransform, Pipe, Component, Input } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';
import { AddToCartComponent } from '../../cart/add-to-cart/add-to-cart.component';
import { NgbTooltipModule, NgbModalModule } from '@ng-bootstrap/ng-bootstrap';
import { FindInStoreComponent } from '../../find-in-store/find-in-store.component';
import { FormsModule, ReactiveFormsModule, FormGroup } from '@angular/forms';
import { MatInputModule, MatFormFieldModule, MatSelectModule } from '@angular/material';
import { CollectionPointItemModule } from '../../checkout/fulfillment/collection-point-item/collection-point-item.module';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { AgmCoreModule } from '@agm/core';
import { AddedToCartDialogComponent } from '../../cart/add-to-cart/added-to-cart-dialog/added-to-cart-dialog.component';
import { Observable } from 'rxjs';
import {
  OrderEntry,
  PromotionResult,
  OccConfig,
  BaseSiteService,
  RoutingConfig,
  TranslationService,
  MockDatePipe
} from '@spartacus/core';
import { SpinnerModule } from '@spartacus/storefront';
import { MockOccConfig } from 'src/app/testing/occ/occ.config.mock';
import {
  MockBaseSiteService,
  MockWishlistsService,
  MockCustomCartService,
  MockRoutingConfig,
  MockCurrentProductService,
  MockTranslationService
} from 'src/app/testing/mock.services';
import { DUMMY_WISHLIST_PRODUCT, DUMMY_WISHLIST_PRODUCT_NOSTOCK } from 'src/app/testing/mock.constants';
import { Store } from '@ngrx/store';
import { WishlistService } from 'src/app/services/wishlist/wishlist.service';
import { CustomCartService } from 'src/app/services/cart/facade/cart.service';
import { CurrentProductService } from '../../product/current-product.service';
import { By } from '@angular/platform-browser';
import { CustomQuantitySelectorComponent } from '../../shared/custom-quantity-selector/custom-quantity-selector.component';
import { WishlistFormComponent } from '../wishlist-form/wishlist-form.component';
import { MessagingComponent } from '../../shared/messaging/messaging.component';

@Pipe({ name: 'cxUrl' })
class MockCxUrlPipe implements PipeTransform {
  transform(value: any): string {
    return '/';
  }
}

@Component({
  selector: 'cx-cart-item',
  template: ''
})
class MockCartItemComponent {
  @Input()
  compact = false;
  @Input()
  item: Observable<OrderEntry>;
  @Input()
  potentialProductPromotions: PromotionResult[];
  @Input()
  isReadOnly = false;
  @Input()
  cartIsLoading = false;
  @Input()
  parent: FormGroup;
  @Input()
  quantity: number;
}

xdescribe('WishlistProductItemComponent', () => {
  let component: WishlistProductItemComponent;
  let fixture: ComponentFixture<WishlistProductItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientTestingModule,
        FormsModule,
        ReactiveFormsModule,
        MatInputModule,
        MatFormFieldModule,
        CollectionPointItemModule,
        AgmCoreModule,
        ReactiveFormsModule,
        HttpClientTestingModule,
        NgbModalModule,
        NgbTooltipModule,
        SpinnerModule,
        MatSelectModule
      ],
      declarations: [
        MockCxUrlPipe,
        MockCxMediaComponent,
        WishlistProductItemComponent,
        AddToCartComponent,
        MockCxTranslatePipe,
        AddedToCartDialogComponent,
        MockAppBackInStockComponent,
        MessagingComponent,
        MockCxIconComponent,
        WishlistFormComponent,
        FindInStoreComponent,
        MockCartItemComponent,
        CustomQuantitySelectorComponent,
        MockDatePipe
      ],
      providers: [
        { provide: OccConfig, useClass: MockOccConfig },
        { provide: BaseSiteService, useClass: MockBaseSiteService },
        { provide: Store, useClass: MockStore },
        { provide: WishlistService, useClass: MockWishlistsService },
        { provide: CustomCartService, useClass: MockCustomCartService },
        { provide: RoutingConfig, useClass: MockRoutingConfig },
        { provide: CurrentProductService, useClass: MockCurrentProductService },
        { provide: TranslationService, useClass: MockTranslationService }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WishlistProductItemComponent);
    component = fixture.componentInstance;
    component.product = DUMMY_WISHLIST_PRODUCT;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should show product cards elements', () => {
    // Remove button
    expect(
      fixture.debugElement.query(By.css('.product-tile a.product-tile__remove')).nativeElement.innerText
    ).toBeTruthy();
    // Product name
    expect(fixture.debugElement.query(By.css('.product-tile .product-tile__name')).nativeElement.innerText).toEqual(
      DUMMY_WISHLIST_PRODUCT.name
    );
    // Product size
    expect(
      fixture.debugElement.query(By.css('.product-tile .product-tile__size')).nativeElement.innerText.trim()
    ).toEqual(DUMMY_WISHLIST_PRODUCT.variantOptions[0].size);
    // Product price
    expect(fixture.debugElement.query(By.css('.product-tile .product-tile__price')).nativeElement.innerText).toEqual(
      DUMMY_WISHLIST_PRODUCT.price.formattedValue
    );
  });

  describe('Given that the product has no stock', () => {
    beforeEach(() => {
      fixture = TestBed.createComponent(WishlistProductItemComponent);
      component = fixture.componentInstance;
      component.product = DUMMY_WISHLIST_PRODUCT_NOSTOCK;
      fixture.detectChanges();
    });

    it('should show the out of stock message', () => {
      expect(
        fixture.debugElement.query(By.css('.product-tile .add-to-bag-set .btn--secondary')).nativeElement
      ).toBeTruthy();
    });
  });
});
