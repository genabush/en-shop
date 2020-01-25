import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

// spartacus
import { CartDataService, BaseSiteService, OccConfig } from '@spartacus/core';

// vendor
import { Store } from '@ngrx/store';

// services
import { CollectionPointsService } from './collection-points.service';
import { MockCartDataService, MockBaseSiteService, MockCustomCartService } from 'src/app/testing/mock.services';
import { OccService } from '../../occ/occ.service';

// components
import { MockStore } from 'src/app/testing/mock.components';
import { CustomCartService } from '../../cart/facade/cart.service';
import { MockOccService } from 'src/app/testing/occ/occ.service.mock';
import { MockOccConfig } from 'src/app/testing/occ/occ.config.mock';

describe('CollectionPointsService', () => {
  beforeEach(() =>
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        {
          provide: Store,
          useClasss: MockStore
        },
        { provide: CartDataService, useClass: MockCartDataService },
        { provide: CustomCartService, useClass: MockCustomCartService },
        { provide: BaseSiteService, useClass: MockBaseSiteService },
        { provide: OccConfig, useClas: MockOccConfig },
        { provide: OccService, useClass: MockOccService }
      ]
    })
  );

  it('should be CREATED', () => {
    const service: CollectionPointsService = TestBed.get(CollectionPointsService);
    expect(service).toBeTruthy();
  });
});
