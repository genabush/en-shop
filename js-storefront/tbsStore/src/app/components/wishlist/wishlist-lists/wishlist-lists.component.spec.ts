import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WishlistListsComponent } from './wishlist-lists.component';
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';

import { DUMMY_WISHLIST_PRODUCT_LISTS, DUMMY_WISHLIST } from 'src/app/testing/mock.constants';
import { By } from '@angular/platform-browser';

describe('WishlistListsComponent', () => {
  let component: WishlistListsComponent;
  let fixture: ComponentFixture<WishlistListsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [WishlistListsComponent, MockCxTranslatePipe]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WishlistListsComponent);
    component = fixture.componentInstance;
    component.lists = DUMMY_WISHLIST;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display the wishlist lists elements', () => {
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('.wishlists__name')).nativeElement.innerText).toEqual(
      DUMMY_WISHLIST[0].wishlistName
    );
    expect(fixture.debugElement.query(By.css('.wishlists__timestamp')).nativeElement.innerText).toEqual(
      DUMMY_WISHLIST[0].created
    );
    expect(fixture.debugElement.query(By.css('.wishlists__counter-value')).nativeElement.innerText).toEqual(
      DUMMY_WISHLIST[0].products.length.toString()
    );
  });
});
