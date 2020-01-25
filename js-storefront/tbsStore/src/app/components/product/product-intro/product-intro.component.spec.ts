import { Component } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { Product, TranslationService, WindowRef, RoutingConfig, ProductService, RoutingService } from '@spartacus/core';
import { Observable, of, BehaviorSubject } from 'rxjs';
import { CurrentProductService } from '@spartacus/storefront';
import { ProductIntroComponent } from './product-intro.component';
import { Store } from '@ngrx/store';
import { RouterTestingModule } from '@angular/router/testing';

@Component({
  selector: 'cx-star-rating',
  template: ''
})
class MockCurrentProductService {
  getProduct(): Observable<Product> {
    return of({ state: { params: { productCode: '123' } } } as Product);
  }
}
class MockStore<T> {
  private state: BehaviorSubject<T> = new BehaviorSubject(undefined);
  setState(data: T) {
    this.state.next(data);
  }
  select(selector?: any): Observable<T> {
    return this.state.asObservable();
  }
  dispatch(action: any) {}
}
class MockTranslationService {
  translate() {
    return of({});
  }
}
class MockWindowRef {
  document = { querySelector: () => true };
}
class MockRoutingConfig {}
class MockProductService {
  get() {
    return of({});
  }
}
class MockRoutingService {
  getRouterState() {
    return of({ state: { params: { productCode: '123' } } } as Product);
  }
}

describe('ProductIntroComponent in product', () => {
  let component: ProductIntroComponent;
  let fixture: ComponentFixture<ProductIntroComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      declarations: [ProductIntroComponent],
      providers: [
        { provide: CurrentProductService, useClass: MockCurrentProductService },
        { provide: TranslationService, useClass: MockTranslationService },
        { provide: WindowRef, useClass: MockWindowRef },
        { provide: ProductService, useClass: MockProductService },
        { provide: Store, useClass: MockStore },
        { provide: RoutingConfig, useClass: MockRoutingConfig },
        { provide: RoutingService, useClass: MockRoutingService }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProductIntroComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });

  describe('clickTabIfInactive to click tabs indicated as inactive', () => {
    it('should click tab with no classes', () => {
      const tabElement: HTMLElement = document.createElement('div');
      spyOn(tabElement, 'click');
      (component as any).clickTabIfInactive(tabElement);
      expect(tabElement.click).toHaveBeenCalled();
    });

    it('should not click tab with active class', () => {
      const tabElement: HTMLElement = document.createElement('div');
      tabElement.classList.add('active');
      spyOn(tabElement, 'click');
      (component as any).clickTabIfInactive(tabElement);
      expect(tabElement.click).not.toHaveBeenCalled();
    });

    it('should click tab with toggled classes', () => {
      const tabElement: HTMLElement = document.createElement('div');
      tabElement.classList.add('toggled');
      spyOn(tabElement, 'click');
      (component as any).clickTabIfInactive(tabElement);
      expect(tabElement.click).toHaveBeenCalled();
    });

    it('should click tab with active and toggled classes', () => {
      const tab: HTMLElement = document.createElement('div');
      tab.classList.add('active');
      tab.classList.add('toggled');
      spyOn(tab, 'click');
      (component as any).clickTabIfInactive(tab);
      expect(tab.click).toHaveBeenCalled();
    });
  });

  describe('getTabByLabel to get tab from tabs component', () => {
    it('should return correct tab', () => {
      const tabsComponent: HTMLElement = document.createElement('div');
      const tab1: HTMLElement = document.createElement('h3');
      const tab2: HTMLElement = document.createElement('h3');
      const tab3: HTMLElement = document.createElement('h3');

      tab1.innerText = 'Tab 1';
      tab2.innerText = 'Tab 2';
      tab3.innerText = 'Tab 3';

      tabsComponent.appendChild(tab1);
      tabsComponent.appendChild(tab2);
      tabsComponent.appendChild(tab3);

      const result = (component as any).getTabByLabel('Tab 2', tabsComponent);

      expect(result).toBe(tab2);
    });
  });
});
