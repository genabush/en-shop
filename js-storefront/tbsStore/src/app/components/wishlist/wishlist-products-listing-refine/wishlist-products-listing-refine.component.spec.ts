import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WishlistProductsListingRefineComponent } from './wishlist-products-listing-refine.component';
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';

describe('WishlistProductsListingRefineComponent', () => {
  let component: WishlistProductsListingRefineComponent;
  let fixture: ComponentFixture<WishlistProductsListingRefineComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [WishlistProductsListingRefineComponent, MockCxTranslatePipe]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WishlistProductsListingRefineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
