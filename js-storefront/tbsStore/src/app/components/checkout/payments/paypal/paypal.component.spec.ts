import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';

// vendor
import { Store } from '@ngrx/store';

// spartacus
import { AuthService, BaseSiteService } from '@spartacus/core';
import { SpinnerModule, SpinnerComponent } from '@spartacus/storefront';

// services
import { CustomCartService } from 'src/app/services/cart/facade/cart.service';
import { OccService } from 'src/app/services/occ/occ.service';
import { CustomCheckoutService } from '../../../../services/checkout/custom-checkout.service';
import {
  MockRouterService,
  MockBaseSiteService,
  MockCustomCartService,
  MockCustomCheckoutService,
  MockAuthService
} from 'src/app/testing/mock.services';

// components
import { MockStore } from 'src/app/testing/mock.components';
import { PaypalComponent } from './paypal.component';

// pipes
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';
import { MockComponent } from 'ng-mocks';
import { NgxPaypalComponent } from 'ngx-paypal';
import { MockOccService } from 'src/app/testing/occ/occ.service.mock';

describe('PaypalComponent', () => {
  let component: PaypalComponent;
  let fixture: ComponentFixture<PaypalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule],
      declarations: [
        PaypalComponent,
        MockCxTranslatePipe,
        MockComponent(NgxPaypalComponent),
        MockComponent(SpinnerComponent)
      ],
      providers: [
        { provide: CustomCheckoutService, useClass: MockCustomCheckoutService },
        { provide: OccService, useClass: MockOccService },
        { provide: CustomCartService, useClass: MockCustomCartService },
        { provide: Store, useClass: MockStore },
        { provide: Router, useClass: MockRouterService },
        { provide: AuthService, useClass: MockAuthService },
        { provide: BaseSiteService, useClass: MockBaseSiteService }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PaypalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should CREATE', () => {
    expect(component).toBeTruthy();
  });
});
