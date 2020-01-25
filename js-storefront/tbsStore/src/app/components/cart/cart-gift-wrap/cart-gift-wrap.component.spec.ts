import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MatFormFieldModule, MatInputModule } from '@angular/material';

// spartacus
import { OccConfig, BaseSiteService, GlobalMessageService } from '@spartacus/core';
import { CmsComponentData } from '@spartacus/storefront';

// vendor
import { Store } from '@ngrx/store';
import { of } from 'rxjs';
import { MockComponent } from 'ng-mocks';

// components
import { CartGiftWrapComponent } from './cart-gift-wrap.component';
import { CartGiftWrapServiceComponent } from './gift-wrap-service/cart-gift-wrap-service/cart-gift-wrap-service.component';
import { CartGiftWrapMessageComponent } from './gift-wrap-message/cart-gift-wrap-message/cart-gift-wrap-message.component';
import { CartGiftWrapImageComponent } from './cart-gift-wrap-image/cart-gift-wrap-image.component';
import { CartPersonalMessageFormComponent } from './gift-wrap-message/cart-personal-message-form/cart-personal-message-form.component';
import { MockCxIconComponent, MockStore, CartMockCmsComponentData } from 'src/app/testing/mock.components';
import { CartGiftWrapServiceFormComponent } from './gift-wrap-service/cart-gift-wrap-service-form/cart-gift-wrap-service-form.component';

// services
import { MockCustomCartService, MockBaseSiteService, MockGlobalMessagingService } from 'src/app/testing/mock.services';
import { CustomCartService } from 'src/app/services/cart/facade/cart.service';

// pipes
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';

// constants
import { MockOccConfig } from 'src/app/testing/occ/occ.config.mock';
import { DUMMY_ACTIVE_CART } from 'src/app/testing/mock.constants';
import { AppCustomStorageService } from 'src/app/services/product/web-storage.service';

describe('CartGiftWrapComponent', () => {
  let component: CartGiftWrapComponent;
  let fixture: ComponentFixture<CartGiftWrapComponent>;
  let cartService: CustomCartService;
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule],
      declarations: [
        CartGiftWrapComponent,
        MockComponent(CartGiftWrapImageComponent),
        MockComponent(CartGiftWrapServiceComponent),
        MockComponent(CartGiftWrapMessageComponent),
        MockComponent(CartGiftWrapServiceFormComponent),
        MockComponent(CartPersonalMessageFormComponent),
        MockCxTranslatePipe,
        MockCxIconComponent
      ],
      providers: [
        { provide: Store, useClass: MockStore },
        { provide: OccConfig, useClass: MockOccConfig },
        { provide: BaseSiteService, useClass: MockBaseSiteService },
        { provide: CmsComponentData, useClass: CartMockCmsComponentData },
        { provide: CustomCartService, useClass: MockCustomCartService },
        { provide: GlobalMessageService, useClass: MockGlobalMessagingService },
        AppCustomStorageService
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CartGiftWrapComponent);
    component = fixture.componentInstance;
    cartService = fixture.componentRef.injector.get(CustomCartService);
    spyOn(cartService, 'getActive').and.returnValue(of(DUMMY_ACTIVE_CART));
    fixture.detectChanges();
  });

  it('should CREATE', () => {
    expect(component).toBeTruthy();
  });
});
