import { MockStore } from './../product-variants/product-variants.service.spec';
import { MockBaseSiteService, MockAuthService } from './../../testing/mock.services';
import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { WishlistService } from './wishlist.service';
import { OccConfig, BaseSiteService, AuthService } from '@spartacus/core';
import { Store } from '@ngrx/store';
import { MockOccConfig } from 'src/app/testing/occ/occ.config.mock';

describe('WishlistService', () => {
  beforeEach(() =>
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        { provide: OccConfig, useClass: MockOccConfig },
        { provide: BaseSiteService, useClass: MockBaseSiteService },
        { provide: Store, useClass: MockStore },
        { provide: AuthService, useClass: MockAuthService }
      ]
    })
  );

  it('should be created', () => {
    const service: WishlistService = TestBed.get(WishlistService);
    expect(service).toBeTruthy();
  });
});
