import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AccountNavigationComponent } from './account-navigation.component';
import { CmsComponentData } from '@spartacus/storefront';
import { MockCmsComponentData } from 'src/app/testing/mock.components';
import { Store } from '@ngrx/store';
import { BehaviorSubject, Observable } from 'rxjs';
import { RoutingConfig } from '@spartacus/core';
import { MockRoutingConfig } from 'src/app/testing/mock.services';

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

describe('AccountNavigationComponent', () => {
  let component: AccountNavigationComponent;
  let fixture: ComponentFixture<AccountNavigationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      declarations: [AccountNavigationComponent],
      providers: [
        { provide: CmsComponentData, useClass: MockCmsComponentData },
        { provide: Store, useClass: MockStore },
        { provide: RoutingConfig, useClass: MockRoutingConfig }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AccountNavigationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
