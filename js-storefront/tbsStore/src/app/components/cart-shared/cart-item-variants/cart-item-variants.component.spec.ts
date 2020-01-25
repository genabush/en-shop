import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';

// vendor
import { cloneDeep } from 'lodash';

// components
import { CartItemVariantsComponent } from './cart-item-variants.component';

// constants
import { DUMMY_CART_ENTRIES } from 'src/app/testing/mock.constants';

describe('CartItemVariantsComponent', () => {
  let component: CartItemVariantsComponent;
  let fixture: ComponentFixture<CartItemVariantsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CartItemVariantsComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CartItemVariantsComponent);
    component = fixture.componentInstance;
  });

  it('should CREATE', async(() => {
    fixture.detectChanges();
    fixture.whenStable().then(() => {
      fixture.detectChanges();
      expect(component).toBeTruthy();
    });
  }));

  it('should SHOW an available PRODUCT VARIANT COLOUR', async(() => {
    component.product = cloneDeep(DUMMY_CART_ENTRIES[0].product);
    fixture.detectChanges();
    fixture.whenStable().then(() => {
      fixture.detectChanges();
      const variantColorEl = fixture.debugElement.query(By.css('.mini-cart-variant__colour-title'));
      expect(variantColorEl.nativeElement.textContent).toContain(DUMMY_CART_ENTRIES[0].product.colour.name);
    });
  }));

  afterEach(() => {
    component = undefined;
    fixture.destroy();
  });
});
