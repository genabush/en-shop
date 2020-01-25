import { async, ComponentFixture, TestBed } from '@angular/core/testing';

// spartacus
import { OccConfig, BaseSiteService } from '@spartacus/core';

// components
import { CartGiftWrapImageComponent } from './cart-gift-wrap-image.component';

// static
import { MockOccConfig } from 'src/app/testing/occ/occ.config.mock';
import { MockBaseSiteService } from 'src/app/testing/mock.services';
import { DUMMMY_CART_CMS_DATA } from 'src/app/testing/mock.constants';

describe('CartGiftWrapImageComponent', () => {
  let component: CartGiftWrapImageComponent;
  let fixture: ComponentFixture<CartGiftWrapImageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CartGiftWrapImageComponent],
      providers: [
        { provide: OccConfig, useClass: MockOccConfig },
        { provide: BaseSiteService, useClass: MockBaseSiteService }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CartGiftWrapImageComponent);
    component = fixture.componentInstance;
    component.giftWrapImageUrl = '';
    fixture.detectChanges();
  });

  it('should CREATE', () => {
    expect(component).toBeTruthy();
  });
});
