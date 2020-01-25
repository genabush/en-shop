import { TestBed } from '@angular/core/testing';

import { CartGiftWrapService } from './cart-gift-wrap.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { OccConfig, BaseSiteService } from '@spartacus/core';
import { MockOccConfig } from 'src/app/testing/occ/occ.config.mock';
import { MockBaseSiteService, MockCustomCartService } from 'src/app/testing/mock.services';
import { CustomCartService } from '../cart/facade/cart.service';
import { Store } from '@ngrx/store';
import { MockStore } from 'src/app/testing/mock.components';

describe('CartGiftWrapService', () => {
  beforeEach(() =>
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        { provide: Store, useClass: MockStore },
        { provide: CustomCartService, useClass: MockCustomCartService },
        { provide: OccConfig, useClass: MockOccConfig },
        { provide: BaseSiteService, useClass: MockBaseSiteService }
      ]
    })
  );

  it('should be CREATED', () => {
    const service: CartGiftWrapService = TestBed.get(CartGiftWrapService);
    expect(service).toBeTruthy();
  });
});
