import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CartGiftWrapServiceFormComponent } from './cart-gift-wrap-service-form.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule, MatInputModule } from '@angular/material';
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';
import { DUMMY_ACTIVE_CART } from 'src/app/testing/mock.constants';

xdescribe('CartGiftWrapFormComponent', () => {
  let component: CartGiftWrapServiceFormComponent;
  let fixture: ComponentFixture<CartGiftWrapServiceFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, MatFormFieldModule, MatInputModule],
      declarations: [CartGiftWrapServiceFormComponent, MockCxTranslatePipe]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CartGiftWrapServiceFormComponent);
    component = fixture.componentInstance;
    component.cart = DUMMY_ACTIVE_CART;
    fixture.detectChanges();
  });

  it('should CREATE', () => {
    expect(component).toBeTruthy();
  });
});
