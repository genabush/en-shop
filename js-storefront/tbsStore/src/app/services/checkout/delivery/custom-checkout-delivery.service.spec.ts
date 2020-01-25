import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

// spartacus
import { CartDataService, BaseSiteService, OccConfig } from '@spartacus/core';

// vendor
import { Store } from '@ngrx/store';

// services
import { CustomCheckoutDeliveryService } from './custom-checkout-delivery.service';
import { MockCartDataService, MockBaseSiteService } from 'src/app/testing/mock.services';
import { OccService } from '../../occ/occ.service';

// components
import { MockStore } from 'src/app/testing/mock.components';
import { MockOccService } from 'src/app/testing/occ/occ.service.mock';
import { MockOccConfig } from 'src/app/testing/occ/occ.config.mock';

describe('CustomCheckoutDeliveryService', () => {
  beforeEach(() =>
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        {
          provide: Store,
          useClasss: MockStore
        },
        { provide: CartDataService, useClass: MockCartDataService },
        { provide: BaseSiteService, useClass: MockBaseSiteService },
        { provide: OccConfig, useClas: MockOccConfig },
        { provide: OccService, useClass: MockOccService }
      ]
    })
  );

  it('should be CREATED', () => {
    const service: CustomCheckoutDeliveryService = TestBed.get(CustomCheckoutDeliveryService);
    expect(service).toBeTruthy();
  });
});
