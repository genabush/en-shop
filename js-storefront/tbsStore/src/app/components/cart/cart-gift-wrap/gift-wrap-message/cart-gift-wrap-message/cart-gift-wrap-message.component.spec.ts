import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CartGiftWrapMessageComponent } from './cart-gift-wrap-message.component';
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule, MatFormFieldModule } from '@angular/material';
import { MockCxIconComponent, MockStore } from 'src/app/testing/mock.components';
import { CartPersonalMessageFormComponent } from '../cart-personal-message-form/cart-personal-message-form.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { OccConfig, BaseSiteService } from '@spartacus/core';
import { MockOccConfig } from 'src/app/testing/occ/occ.config.mock';
import { MockBaseSiteService, MockCustomCartService } from 'src/app/testing/mock.services';
import { CustomCartService } from 'src/app/services/cart/facade/cart.service';
import { Store } from '@ngrx/store';
import { MockComponent } from 'ng-mocks';
import { DUMMY_ACTIVE_CART } from 'src/app/testing/mock.constants';
import { of } from 'rxjs';

describe('CartGiftWrapMessageComponent', () => {
  let component: CartGiftWrapMessageComponent;
  let fixture: ComponentFixture<CartGiftWrapMessageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule],
      declarations: [
        CartGiftWrapMessageComponent,
        MockComponent(CartPersonalMessageFormComponent),
        MockCxTranslatePipe,
        MockCxIconComponent
      ],
      providers: [
        { provide: Store, useClass: MockStore },
        { provide: OccConfig, useClass: MockOccConfig },
        { provide: BaseSiteService, useClass: MockBaseSiteService },
        { provide: CustomCartService, useClass: MockCustomCartService }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CartGiftWrapMessageComponent);
    component = fixture.componentInstance;
    component.cart = of(DUMMY_ACTIVE_CART);
    fixture.detectChanges();
  });

  it('should CREATE', () => {
    expect(component).toBeTruthy();
  });
});
