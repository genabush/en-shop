import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';

// vendor
import { cloneDeep } from 'lodash';

// components
import { CustomMiniCartEntryVariantsComponent } from './custom-mini-cart-entry-variants.component';

// constants
import { DUMMY_CART_ENTRIES } from 'src/app/testing/mock.constants';

describe('CustomMiniCartEntryVariantsComponent', () => {
  let component: CustomMiniCartEntryVariantsComponent;
  let fixture: ComponentFixture<CustomMiniCartEntryVariantsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CustomMiniCartEntryVariantsComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomMiniCartEntryVariantsComponent);
    component = fixture.componentInstance;
  });

  it('should CREATE', async(() => {
    fixture.detectChanges();
    fixture.whenStable().then(() => {
      fixture.detectChanges();
      expect(component).toBeTruthy();
    });
  }));

  it('should SHOW an available PRODUCT VARIANT SIZE', async(() => {
    component.product = cloneDeep(DUMMY_CART_ENTRIES[1].product);
    fixture.detectChanges();
    fixture.whenStable().then(() => {
      fixture.detectChanges();
      const variantSizeEl = fixture.debugElement.query(By.css('.mini-cart-variant__size'));
      expect(variantSizeEl.nativeElement.textContent).toContain(DUMMY_CART_ENTRIES[1].product.size);
    });
  }));

  it('should SHOW an available PRODUCT VARIANT COLOUR', async(() => {
    component.product = cloneDeep(DUMMY_CART_ENTRIES[1].product);
    fixture.detectChanges();
    fixture.whenStable().then(() => {
      fixture.detectChanges();
      const variantColorEl = fixture.debugElement.query(By.css('.mini-cart-variant__colour-title'));
      expect(variantColorEl.nativeElement.textContent).toContain(DUMMY_CART_ENTRIES[1].product.colour.name);
    });
  }));

  afterEach(() => {
    component = undefined;
    fixture.destroy();
  });
});
