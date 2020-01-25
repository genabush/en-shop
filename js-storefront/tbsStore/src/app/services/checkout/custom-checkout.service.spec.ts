import { TestBed } from '@angular/core/testing';

import { CustomCheckoutService } from './custom-checkout.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { OccService } from '../occ/occ.service';
import { CustomCartService } from '../cart/facade/cart.service';
import { Store } from '@ngrx/store';
import { BehaviorSubject, Observable } from 'rxjs';
import { CartDataService } from '@spartacus/core';
import { MockCartDataService } from 'src/app/testing/mock.services';

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

class MocksOccService {
  getApiUrl() {
    return 'test';
  }
}
class MockCustomCartService {
  getUserId() {
    return '123';
  }
  getActiveCartCode() {
    return '345';
  }
}

describe('CustomCheckoutService', () => {
  beforeEach(() =>
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        { provide: OccService, useClass: MocksOccService },
        { provide: CustomCartService, useClass: MockCustomCartService },
        { provide: Store, useClass: MockStore },
        { provide: CartDataService, useClass: MockCartDataService }
      ]
    })
  );

  it('should be created', () => {
    const service: CustomCheckoutService = TestBed.get(CustomCheckoutService);
    expect(service).toBeTruthy();
  });
});
