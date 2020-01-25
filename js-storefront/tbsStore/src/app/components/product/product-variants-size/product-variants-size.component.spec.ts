import { ICustomVariantOption } from './../../../interfaces/custom-product-item.interface';
import { Product } from '@spartacus/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductVariantsSizeComponent } from './product-variants-size.component';
import { ProductVariantSelection } from '../../../models';
import { By } from '@angular/platform-browser';
import { Component, Input, Pipe, PipeTransform } from '@angular/core';
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';

const mockProduct = {
  name: 'mockProduct',
  code: 'code1',
  stock: { stockLevelStatus: 'inStock', stockLevel: 20 },
  maxOrderProductQuantity: 0,
  priceData: {
    currencyIso: 'GBP',
    formattedValue: '£6.00',
    loyaltyPoints: 6,
    pricePerMetric: '£12.00/100 ML',
    priceType: 'BUY',
    value: 6.0
  }
};

const mockProductNoLoyalty = {
  name: 'mockProduct',
  code: 'code1',
  stock: { stockLevelStatus: 'inStock', stockLevel: 20 },
  maxOrderProductQuantity: 0,
  priceData: {
    currencyIso: 'GBP',
    formattedValue: '£6.00',
    loyaltyPoints: 0,
    pricePerMetric: '£12.00/100 ML',
    priceType: 'BUY',
    value: 6.0
  }
};

describe('ProductVariantsSizeComponent', () => {
  let component: ProductVariantsSizeComponent;
  let fixture: ComponentFixture<ProductVariantsSizeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ProductVariantsSizeComponent, MockCxTranslatePipe]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProductVariantsSizeComponent);
    component = fixture.componentInstance;
    component.variant = { code: '123' } as ProductVariantSelection;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('Given that I have loyalty point on a variant level', () => {
    it('the loyalty count should display', () => {
      component.ngOnInit();
      component.variant = mockProduct as ProductVariantSelection;
      fixture.detectChanges();
      expect(fixture.debugElement.query(By.css('.price__element--rewards')).nativeElement.textContent).toBeTruthy();
    });
  });

  describe('Given that I have NO loyalty point on a variant level', () => {
    it('the loyalty count should NOT display', () => {
      component.ngOnInit();
      component.variant = mockProductNoLoyalty as ProductVariantSelection;
      fixture.detectChanges();
      expect(fixture.debugElement.query(By.css('.price__element--rewards'))).toBeNull();
    });
  });

  afterEach(() => {
    component.variant = {};
  });
});
