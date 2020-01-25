import { TestBed, async } from '@angular/core/testing';

import { CustomProductListComponentService } from './custom-product-list-component.service';
import { ProductSearchService, CurrencyService, LanguageService, RoutingConfig, RoutingService } from '@spartacus/core';
import { of, BehaviorSubject, Observable } from 'rxjs';
import { Store } from '@ngrx/store';
import { RouterTestingModule } from '@angular/router/testing';
import { ActivatedRoute } from '@angular/router';

class MockProductSearchService {
  getResults() {
    return of({});
  }
  clearResults() {
    return;
  }
  search(param1, param2) {
    return;
  }
}

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
class MockCurrencyService {
  getActive() {
    return of('test');
  }
}
class MockLanguageService {
  getActive() {
    return of('test');
  }
}
class MockRoutingService {
  getRouterState() {
    return of({});
  }
}

describe('CustomProductListComponentServiceService', () => {
  let service: CustomProductListComponentService;
  beforeEach(async(() =>
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      providers: [
        { provide: ProductSearchService, useClass: MockProductSearchService },
        {
          provide: Store,
          useClass: MockStore
        },
        { provide: CurrencyService, useClass: MockCurrencyService },
        { provide: LanguageService, useClass: MockLanguageService },
        { provide: RoutingService, useClass: MockRoutingService }
      ]
    })));
  beforeEach(() => {
    service = TestBed.get(CustomProductListComponentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
