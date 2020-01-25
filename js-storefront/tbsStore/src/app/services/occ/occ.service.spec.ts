import { TestBed } from '@angular/core/testing';

import { OccService } from './occ.service';
import { OccConfig, BaseSiteService } from '@spartacus/core';
import { of } from 'rxjs';

class MockOccConfig {
  backend = { occ: { baseUrl: 'test1' } };
}

class MockBaseSiteService {
  getActive() {
    return of('test');
  }
}

describe('OccService', () => {
  beforeEach(() =>
    TestBed.configureTestingModule({
      providers: [
        { provide: OccConfig, useClass: MockOccConfig },
        { provide: BaseSiteService, useClass: MockBaseSiteService }
      ]
    })
  );

  it('should be created', () => {
    const service: OccService = TestBed.get(OccService);
    expect(service).toBeTruthy();
  });

  it('should return base url', () => {
    const service: OccService = TestBed.get(OccService);
    const resp = service.getBaseUrl();
    expect(resp).toEqual('test1');
  });
});
