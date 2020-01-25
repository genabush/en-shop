import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteWishlistModalComponent } from './delete-wishlist-modal.component';
import { RouterTestingModule } from '@angular/router/testing';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Store } from '@ngrx/store';
import { MockCxTranslatePipe } from '../../../../testing/mock.pipes';
import { BaseSiteService, OccConfig } from '@spartacus/core';
import { MockOccConfig } from '../../../../testing/occ/occ.config.mock';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MockBaseSiteService, MockWishlistsService } from '../../../../testing/mock.services';
import { WishlistService } from '../../../../services/wishlist/wishlist.service';

class MockStore<T> {
  private state: BehaviorSubject<T> = new BehaviorSubject(undefined);
  setState(data: T) {
    this.state.next(data);
  }
  select(selector?: any): Observable<T> {
    return this.state.asObservable();
  }
  pipe(operator: any) {}
  dispatch(action: any) {}
}

xdescribe('DeleteWishlistModalComponent', () => {
  let component: DeleteWishlistModalComponent;
  let fixture: ComponentFixture<DeleteWishlistModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule, HttpClientTestingModule],
      declarations: [DeleteWishlistModalComponent, MockCxTranslatePipe],
      providers: [
        { provide: Store, useClass: MockStore },
        { provide: OccConfig, useClass: MockOccConfig },
        { provide: BaseSiteService, useClass: MockBaseSiteService },
        { provide: WishlistService, useClass: MockWishlistsService }
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DeleteWishlistModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
