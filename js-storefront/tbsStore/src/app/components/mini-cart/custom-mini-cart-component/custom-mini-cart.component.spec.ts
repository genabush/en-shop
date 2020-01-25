import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';

// spartacus
import { ModalService, CmsComponentData } from '@spartacus/storefront';
import { CartService, BaseSiteService } from '@spartacus/core';

// vendor
import { of } from 'rxjs';
import { Store } from '@ngrx/store';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

// components
import { CustomMiniCartComponent } from './custom-mini-cart.component';
import { CustomMiniCartEntryItemComponent } from '../custom-mini-cart-entry-item/custom-mini-cart-entry-item.component';
import { CustomMiniCartEntryVariantsComponent } from '../custom-mini-cart-entry-variants/custom-mini-cart-entry-variants.component';
import {
  MockCxIconComponent,
  MockCxMediaComponent,
  MockStore,
  MockCmsComponentData,
  MockPromotionFreeDeliveryComponent
} from 'src/app/testing/mock.components';

// services
import { CustomCartService } from 'src/app/services/cart/facade/cart.service';
import { OccService } from 'src/app/services/occ/occ.service';
import { MockBaseSiteService, MockCustomCartService } from 'src/app/testing/mock.services';

// pipes
import { MockCxUrlPipe, MockCxTranslatePipe } from 'src/app/testing/mock.pipes';

// constants
import { DUMMY_ACTIVE_CART_EMPTY } from 'src/app/testing/mock.constants';
import { MockComponent } from 'ng-mocks';
import { MockOccService } from 'src/app/testing/occ/occ.service.mock';

describe('CustomMiniCartComponent', () => {
  let component: CustomMiniCartComponent;
  let fixture: ComponentFixture<CustomMiniCartComponent>;
  let cartService: CustomCartService;
  let activeCartSpy: any;
  let cartEntriesSpy: any;
  let routerSpy: any;
  let router: Router;
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        BrowserAnimationsModule,
        NgbModule,
        RouterTestingModule.withRoutes([
          {
            path: 'cart',
            redirectTo: '/'
          }
        ])
      ],
      declarations: [
        CustomMiniCartComponent,
        MockComponent(CustomMiniCartEntryItemComponent),
        MockComponent(CustomMiniCartEntryVariantsComponent),
        MockCxIconComponent,
        MockCxMediaComponent,
        MockPromotionFreeDeliveryComponent,
        MockCxUrlPipe,
        MockCxTranslatePipe
      ],
      providers: [
        { provide: Store, useClass: MockStore },
        {
          provide: ModalService,
          useClass: ModalService
        },
        {
          provide: CmsComponentData,
          useClass: MockCmsComponentData
        },
        { provide: BaseSiteService, useClass: MockBaseSiteService },
        { provide: OccService, useClass: MockOccService },
        { provide: CartService, useClass: MockCustomCartService },
        { provide: CustomCartService, useClass: MockCustomCartService }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomMiniCartComponent);
    component = fixture.componentInstance;

    cartService = fixture.componentRef.injector.get(CustomCartService);
    router = fixture.componentRef.injector.get(Router);

    routerSpy = spyOn(router, 'navigateByUrl').and.returnValue(Promise.resolve(true));
  });

  it('should CREATE', async(() => {
    fixture.detectChanges();
    fixture.whenStable().then(() => {
      fixture.detectChanges();
      expect(component).toBeTruthy();
    });
  }));

  it('should SHOW the MiniCart modal on DESKTOP HOVER when there is at least 1 entry', async(() => {
    fixture.detectChanges();
    fixture.whenStable().then(() => {
      fixture.detectChanges();
      const desktopTrigger = fixture.debugElement.query(By.css('.mini-cart--desktop'));
      desktopTrigger.nativeElement.dispatchEvent(new MouseEvent('mouseenter'));
      fixture.whenStable().then(() => {
        fixture.detectChanges();
        const modalWindow = document.querySelector('.mini-cart-modal');
        expect(modalWindow.classList).toContain('show');
        expect(document.querySelector('.mini-cart__header')).toBeTruthy();
        expect(document.querySelector('.mini-cart__msg')).toBeTruthy();
      });
    });
  }));

  it('should NOT SHOW the MiniCart modal on DESKTOP HOVER when there is NOT at least 1 entry', async(() => {
    activeCartSpy = spyOn(cartService, 'getActive').and.returnValue(of(DUMMY_ACTIVE_CART_EMPTY));
    cartEntriesSpy = spyOn(cartService, 'getEntries').and.returnValue(of([]));
    component.appCustomCartDirective.setActiveCartSub();

    fixture.detectChanges();
    fixture.whenStable().then(() => {
      fixture.detectChanges();
      const desktopTrigger = fixture.debugElement.query(By.css('.mini-cart--desktop'));
      desktopTrigger.nativeElement.dispatchEvent(new MouseEvent('mouseenter'));
      fixture.whenStable().then(() => {
        fixture.detectChanges();
        const modalWindow = document.querySelector('.mini-cart-modal');
        expect(modalWindow).toBeFalsy();
      });
    });
  }));

  it('should HIDE the MiniCart modal on CROSS CLICK', async(() => {
    fixture.whenStable().then(() => {
      fixture.detectChanges();
      const desktopTrigger = fixture.debugElement.query(By.css('.mini-cart--desktop'));
      desktopTrigger.nativeElement.dispatchEvent(new MouseEvent('mouseenter'));
      fixture.whenStable().then(() => {
        fixture.detectChanges();
        (document.querySelector('button.close') as HTMLElement).dispatchEvent(new MouseEvent('click', null));
        fixture.whenStable().then(() => {
          fixture.detectChanges();
          const modalWindow = document.querySelector('.mini-cart-modal');
          expect(modalWindow).toBeFalsy();
        });
      });
    });
  }));

  xit('should HIDE the MiniCart modal on DESKTOP when the CONTINUE SHOPPING link is CLICKED', async(() => {
    fixture.whenStable().then(() => {
      fixture.detectChanges();
      const desktopTrigger = fixture.debugElement.query(By.css('.mini-cart--desktop'));
      desktopTrigger.nativeElement.dispatchEvent(new MouseEvent('mouseenter'));
      fixture.whenStable().then(() => {
        fixture.detectChanges();
        (document.querySelector('.mini-cart-modal__link-continue') as HTMLElement).dispatchEvent(
          new MouseEvent('click', null)
        );
        fixture.whenStable().then(() => {
          fixture.detectChanges();
          const modalWindow = document.querySelector('.mini-cart-modal');
          expect(modalWindow).toBeFalsy();
        });
      });
    });
  }));

  it('should NAVIGATE to the Cart Page on DESKTOP when there is at least 1 entry and the BASKET link is CLICKED', async(() => {
    fixture.whenStable().then(() => {
      fixture.detectChanges();
      const desktopTrigger = fixture.debugElement.query(By.css('.mini-cart--desktop'));
      desktopTrigger.nativeElement.dispatchEvent(new MouseEvent('mouseenter', null));
      fixture.whenStable().then(() => {
        fixture.detectChanges();
        (document.querySelector('.mini-cart-modal__link-basket') as HTMLElement).dispatchEvent(
          new MouseEvent('click', null)
        );
        fixture.whenStable().then(() => {
          fixture.detectChanges();
          expect(routerSpy).toHaveBeenCalledWith('/cart');
        });
      });
    });
  }));
  it('should NAVIGATE to the Cart Page on MOBILE when there is at least 1 entry', async(() => {
    component.appCustomCartDirective.setActiveCartSub();
    fixture.detectChanges();
    fixture.whenStable().then(() => {
      fixture.detectChanges();
      const desktopTrigger = fixture.debugElement.query(By.css('.mini-cart--mobile'));
      desktopTrigger.nativeElement.dispatchEvent(new MouseEvent('click', null));
      fixture.whenStable().then(() => {
        fixture.detectChanges();
        expect(routerSpy).toHaveBeenCalledWith('/cart');
      });
    });
  }));

  it('should NOT NAVIGATE to the Cart Page on MOBILE when there is NOT at least 1 entry', async(() => {
    fixture.whenStable().then(() => {
      fixture.detectChanges();
      const desktopTrigger = fixture.debugElement.query(By.css('.mini-cart--mobile'));
      desktopTrigger.nativeElement.dispatchEvent(new MouseEvent('mouseenter'));
      fixture.whenStable().then(() => {
        fixture.detectChanges();
        expect(routerSpy).not.toHaveBeenCalled();
      });
    });
  }));

  afterEach(() => {
    activeCartSpy = undefined;
    cartEntriesSpy = undefined;
    routerSpy = undefined;
    component.appCustomCartDirective.checkMiniCartClosed();
    component.appCustomCartDirective.ngOnDestroy();
    cartService = null;
    router = null;
    component = undefined;
    fixture.destroy();
  });
});
