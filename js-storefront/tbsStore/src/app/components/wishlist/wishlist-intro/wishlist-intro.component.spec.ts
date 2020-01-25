import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WishlistIntroComponent } from './wishlist-intro.component';
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';
import { By } from '@angular/platform-browser';

describe('WishlistIntroComponent', () => {
  let component: WishlistIntroComponent;
  let fixture: ComponentFixture<WishlistIntroComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [WishlistIntroComponent, MockCxTranslatePipe]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WishlistIntroComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
