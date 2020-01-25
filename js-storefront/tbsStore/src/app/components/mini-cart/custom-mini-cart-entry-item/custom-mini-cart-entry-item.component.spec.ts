import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { RouterTestingModule } from '@angular/router/testing';

// components
import { MockCxIconComponent, MockCxMediaComponent } from 'src/app/testing/mock.components';
import { MockCxUrlPipe, MockCxTranslatePipe } from 'src/app/testing/mock.pipes';
import { CustomMiniCartEntryVariantsComponent } from '../custom-mini-cart-entry-variants/custom-mini-cart-entry-variants.component';
import { CustomMiniCartEntryItemComponent } from './custom-mini-cart-entry-item.component';

// constants
import { DUMMY_CART_ENTRIES } from 'src/app/testing/mock.constants';

describe('CustomMiniCartEntryItemComponent', () => {
  let component: CustomMiniCartEntryItemComponent;
  let fixture: ComponentFixture<CustomMiniCartEntryItemComponent>;
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([])],
      declarations: [
        CustomMiniCartEntryItemComponent,
        CustomMiniCartEntryVariantsComponent,
        MockCxIconComponent,
        MockCxMediaComponent,
        MockCxUrlPipe,
        MockCxTranslatePipe
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomMiniCartEntryItemComponent);
    component = fixture.componentInstance;
    component.cartEntry = DUMMY_CART_ENTRIES[1];
  });

  it('should CREATE', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should SHOW an ENTRY that contains ENTRY DETAILS', async(() => {
    fixture.detectChanges();
    fixture.whenStable().then(() => {
      fixture.detectChanges();
      const colorSwatchEl = fixture.debugElement.query(By.css('.mini-cart-variant__colour-swatch'));
      const colorNameEl = fixture.debugElement.query(By.css('.mini-cart-variant__colour-title'));
      const entryPriceEl = fixture.debugElement.query(By.css('.cx-entry-price'));
      expect(colorSwatchEl).toBeTruthy();
      expect(colorNameEl.nativeElement.textContent).toContain(DUMMY_CART_ENTRIES[1].product.colour.name);
      expect(entryPriceEl.nativeElement.textContent).toContain(DUMMY_CART_ENTRIES[1].totalPrice.formattedValue);
    });
  }));

  afterEach(() => {
    fixture.destroy();
    component = undefined;
  });
});
