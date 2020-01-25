import { TestBed, inject, ComponentFixture } from '@angular/core/testing';
import { ProductVariantsService } from './product-variants.service';
import { RootStoreState } from 'src/app/root-store';
import { Observable, BehaviorSubject } from 'rxjs';
import { Store } from '@ngrx/store';
export class MockStore<T> {
  private state: BehaviorSubject<T> = new BehaviorSubject(undefined);
  setState(data: T) {
    this.state.next(data);
  }
  select(selector?: any): Observable<T> {
    return this.state.asObservable();
  }
  dispatch(action: any) {}
}

describe('ProductVariantsService', () => {
  let service: ProductVariantsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [{ provide: Store, useClass: MockStore }, ProductVariantsService]
    }).compileComponents();

    service = TestBed.get(ProductVariantsService);
  });

  it('should create an instance', () => {
    expect(service).toBeDefined();
  });
});
