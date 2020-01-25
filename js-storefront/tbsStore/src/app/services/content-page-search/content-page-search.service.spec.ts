import { TestBed } from '@angular/core/testing';

import { ContentPageSearchService } from './content-page-search.service';
import { Store } from '@ngrx/store';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RoutingService, BaseSiteService } from '@spartacus/core';
import { OccService } from '../occ/occ.service';

class MockStore<T> {
  private state: BehaviorSubject<T> = new BehaviorSubject(undefined);
  setState(data: T) {
    this.state.next(data);
  }
  select(selector?: any): Observable<T> {
    return this.state.asObservable();
  }
  dispatch(action: any) {}
}
class MockRoutingService {
  getRouterState() {
    return of({ state: { params: { query: '' } } });
  }
}
class MockOccService {
  occ = { backend: { occ: { endpoints: { contentSearch: 'test' } } } };
  getBaseUrl() {
    return 'test';
  }
}
class MockBaseSiteService {
  getActive() {
    return of({});
  }
}

describe('ContentPageSearchService', () => {
  beforeEach(() =>
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        { provide: Store, useClass: MockStore },
        { provide: RoutingService, useClass: MockRoutingService },
        { provide: OccService, useClass: MockOccService },
        { provide: BaseSiteService, useClass: MockBaseSiteService }
      ]
    })
  );

  it('should be created', () => {
    const service: ContentPageSearchService = TestBed.get(ContentPageSearchService);
    expect(service).toBeTruthy();
  });
});
