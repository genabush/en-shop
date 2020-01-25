import { TestBed } from '@angular/core/testing';

// vendor
import { Store } from '@ngrx/store';

// components
import { MockStore } from 'src/app/testing/mock.components';

// services
import { CustomUserAddressService } from './custom-user-address.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { OccService } from '../occ/occ.service';
import { MockOccService } from 'src/app/testing/occ/occ.service.mock';
import { CartDataService } from '@spartacus/core';
import { MockCartDataService, MockCustomCartService } from 'src/app/testing/mock.services';
import { CustomCartService } from '../cart/facade/cart.service';

describe('CustomUserAddressService', () => {
  beforeEach(() =>
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        { provide: Store, useClass: MockStore },
        { provide: OccService, useClass: MockOccService },
        { provide: CartDataService, useClass: MockCartDataService }
      ]
    })
  );

  it('should CREATE', () => {
    const service: CustomUserAddressService = TestBed.get(CustomUserAddressService);
    expect(service).toBeTruthy();
  });
});
