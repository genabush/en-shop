import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

// vendor
import { Store } from '@ngrx/store';

// services
import { CustomCartVoucherService } from './custom-cart-voucher.service';
import { CustomCartService } from '../cart/facade/cart.service';
import { MockCustomCartService } from 'src/app/testing/mock.services';
import { MockOccService } from 'src/app/testing/occ/occ.service.mock';

// components
import { MockStore } from 'src/app/testing/mock.components';
import { OccService } from '../occ/occ.service';

describe('CustomCartVoucherService', () => {
  beforeEach(() =>
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        { provide: Store, useClass: MockStore },
        { provide: OccService, useClas: MockOccService },
        { provide: CustomCartService, useClass: MockCustomCartService }
      ]
    })
  );

  it('should be CREATED', () => {
    const service: CustomCartVoucherService = TestBed.get(CustomCartVoucherService);
    expect(service).toBeTruthy();
  });
});
