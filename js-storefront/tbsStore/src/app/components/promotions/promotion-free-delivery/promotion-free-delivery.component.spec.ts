import { Promotion } from '@spartacus/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { PromotionFreeDeliveryComponent } from './promotion-free-delivery.component';
import {
  DUMMY_PROMO_FREE_DELIVERY,
  DUMMY_PROMO,
  DUMMY_PROMO_FREE_DELIVERY_SPLIT
} from 'src/app/testing/mock.constants';

describe('PromotionFreeDeliveryComponent', () => {
  let component: PromotionFreeDeliveryComponent;
  let fixture: ComponentFixture<PromotionFreeDeliveryComponent>;
  const mockSplitResult = ['Free delivery on orders over £30.00 ', ' Spend £8.00 more to qualify'];

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [PromotionFreeDeliveryComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PromotionFreeDeliveryComponent);
    component = fixture.componentInstance;
    component.promotions = DUMMY_PROMO_FREE_DELIVERY;
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });

  describe('Given that the component is used on the basket page - top section', () => {
    it('should show the promotion basket (top) free delivery promotion', () => {
      component.ngOnInit();
      fixture.detectChanges();
      expect(fixture.debugElement.query(By.css('.promotions__inner')).nativeElement).toBeTruthy();
    });
    it('should show the dismiss button for the promotion', () => {
      component.ngOnInit();
      fixture.detectChanges();
      expect(fixture.debugElement.query(By.css('.promotions__dismiss'))).toBeTruthy();
    });
  });

  describe('Given that the component is used on the mini basket', () => {
    it('should show the mini basket free delivery promotion', () => {
      component.ngOnInit();
      component.minibasket = true;
      fixture.detectChanges();
      const promo = fixture.debugElement.query(By.css('.promotions__inner')).nativeElement;
      expect(promo.classList).toContain('promotions__inner--wrap');
    });
  });

  describe('Given that the component is used on the basket totals', () => {
    it('should show the basket totals free delivery promotion', () => {
      component.ngOnInit();
      component.totals = true;
      fixture.detectChanges();
      const promo = fixture.debugElement.query(By.css('.promotions')).nativeElement;
      expect(promo.classList).toContain('promotions--totals');
    });
  });

  describe('Given that promotion message has the split keyword', () => {
    beforeEach(() => {
      fixture = TestBed.createComponent(PromotionFreeDeliveryComponent);
      component = fixture.componentInstance;
      component.promotions = DUMMY_PROMO_FREE_DELIVERY_SPLIT;
    });
    it('should on the basket page, split the promo description', () => {
      component.ngOnInit();
      fixture.detectChanges();
      expect(component.freeDeliveryPromotionDesc).toEqual(mockSplitResult);
    });
    it('should on the basket page, have an hyphen between desc', () => {
      component.ngOnInit();
      fixture.detectChanges();
      expect(fixture.debugElement.query(By.css('.promotions__desc')).nativeElement.textContent).toEqual(
        mockSplitResult[0] + '-' + mockSplitResult[1]
      );
    });
    it('should on the mini basket, be different lines', () => {
      component.ngOnInit();
      component.minibasket = true;
      fixture.detectChanges();
      expect(document.querySelectorAll('.promotions__desc').length).toEqual(2);
    });
  });

  describe('Given there is no free delivery potential promotion', () => {
    beforeEach(() => {
      fixture = TestBed.createComponent(PromotionFreeDeliveryComponent);
      component = fixture.componentInstance;
      component.promotions = DUMMY_PROMO;
    });
    it('should not show the free delivery promotion', () => {
      component.ngOnInit();
      fixture.detectChanges();
      expect(fixture.debugElement.query(By.css('.promotions__inner')).nativeElement).toBeTruthy();
    });
  });
});
