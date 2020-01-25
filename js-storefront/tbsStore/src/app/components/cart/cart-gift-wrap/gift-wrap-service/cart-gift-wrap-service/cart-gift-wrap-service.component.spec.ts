import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CartGiftWrapServiceComponent } from './cart-gift-wrap-service.component';
import { DUMMY_ACTIVE_CART } from 'src/app/testing/mock.constants';

xdescribe('CartGiftWrapItemComponent', () => {
  let component: CartGiftWrapServiceComponent;
  let fixture: ComponentFixture<CartGiftWrapServiceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CartGiftWrapServiceComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CartGiftWrapServiceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should CREATE', () => {
    expect(component).toBeTruthy();
  });
});
