import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SaveToWishlistModalComponent } from './save-to-wishlist-modal.component';
import { Component, CUSTOM_ELEMENTS_SCHEMA, Input, Pipe, PipeTransform } from '@angular/core';
import { ICON_TYPE } from '@spartacus/storefront';
import { RouterTestingModule } from '@angular/router/testing';
import { Store } from '@ngrx/store';
import { BehaviorSubject, of } from 'rxjs';

@Component({
  selector: 'cx-icon',
  template: ''
})
class MockCxIconComponent {
  @Input() type: ICON_TYPE;
}

class MockStore<T> {
  private state = new BehaviorSubject(undefined);
  setState(data) {
    this.state.next(data);
  }
  select(selector?: any) {
    return of({ selected: { code: 123 } });
  }
  dispatch(action: any) {}
}

@Pipe({ name: 'cxTranslate' })
class MockCxTranslate implements PipeTransform {
  transform(value: number): number {
    return value;
  }
}

describe('SaveToWishlistModalComponent', () => {
  let component: SaveToWishlistModalComponent;
  let fixture: ComponentFixture<SaveToWishlistModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      declarations: [SaveToWishlistModalComponent, MockCxIconComponent, MockCxTranslate],
      providers: [{ provide: Store, useClass: MockStore }],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SaveToWishlistModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
