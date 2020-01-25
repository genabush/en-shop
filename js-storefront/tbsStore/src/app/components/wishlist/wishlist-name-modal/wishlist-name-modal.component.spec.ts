import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { WishlistNameModalComponent } from './wishlist-name-modal.component';
import { MockCxTranslatePipe } from '../../../testing/mock.pipes';
import { Store } from '@ngrx/store';
import { BehaviorSubject, Observable } from 'rxjs';

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

xdescribe('WishlistNameModalComponent', () => {
  let component: WishlistNameModalComponent;
  let fixture: ComponentFixture<WishlistNameModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [WishlistNameModalComponent, MockCxTranslatePipe],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      providers: [{ provide: Store, useClass: MockStore }]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WishlistNameModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
