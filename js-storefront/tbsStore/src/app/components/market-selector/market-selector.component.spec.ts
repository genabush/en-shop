import { MockTranslationService } from './../../testing/mock.services';
import { Component, Input } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { MatSelectModule } from '@angular/material';
import { By } from '@angular/platform-browser';
import { Router } from '@angular/router';

import {
  BaseSiteService,
  AuthService,
  CurrencyService,
  LanguageService,
  SiteContextConfig,
  RoutingConfig,
  TranslationService
} from '@spartacus/core';
import { CmsComponentData, ICON_TYPE } from '@spartacus/storefront';

// vendor
import { of, Subscription } from 'rxjs';
import { Store } from '@ngrx/store';

// components
import { MarketSelectorComponent } from './market-selector.component';
import { MockStore, MockCmsComponentData, MockRoutedComponent } from 'src/app/testing/mock.components';

// services
import { MockRouterService, MockAuthService, MockBaseSiteService } from 'src/app/testing/mock.services';

// constants
import { MARKET_SELECTOR_CMS_DATA } from 'src/app/testing/mock.constants';

// pipes
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';

const routerMock = {
  navigateByUrl: jasmine.createSpy('navigateByUrl') // to spy on the url that has been routed
};
@Component({
  selector: 'cx-icon',
  template: ''
})
class MockCxIconComponent {
  @Input() type: ICON_TYPE;
}

describe('MarketSelectorComponent', () => {
  let component: MarketSelectorComponent;
  let fixture: ComponentFixture<MarketSelectorComponent>;
  let router: Router;
  let routerSpy: jasmine.Spy;
  let baseSiteSpy: jasmine.Spy;
  let currencySpy: jasmine.Spy;
  let languageSpy: jasmine.Spy;
  let marketDataSpy: jasmine.Spy;
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [MatSelectModule],
      declarations: [MarketSelectorComponent, MockRoutedComponent, MockCxTranslatePipe, MockCxIconComponent],
      providers: [
        { provide: Store, useClass: MockStore },
        { provide: CmsComponentData, useClass: MockCmsComponentData },
        { provide: BaseSiteService, useClass: MockBaseSiteService },
        { provide: AuthService, useClass: MockAuthService },
        { provide: Router, useClass: MockRouterService },
        { provide: TranslationService, useClass: MockTranslationService },
        LanguageService,
        CurrencyService,
        SiteContextConfig,
        RoutingConfig
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MarketSelectorComponent);
    component = fixture.componentInstance;
    router = fixture.componentRef.injector.get(Router);
    marketDataSpy = spyOn(component, 'getMarketSelectorData').and.returnValue(of(MARKET_SELECTOR_CMS_DATA));
    baseSiteSpy = spyOn(component.baseSiteService, 'getActive').and.returnValue(of('thebodyshop-uk'));
    currencySpy = spyOn(component.currencyService, 'setActive').and.callFake(() => {
      return new Subscription();
    });
    languageSpy = spyOn(component.languageService, 'setActive').and.callFake(() => {
      return new Subscription();
    });
    routerSpy = spyOn(component, 'doNavigationBySelect').and.callFake(() => {});
    fixture.detectChanges();
  });

  it('should CREATE', () => {
    expect(component).toBeTruthy();
  });
  it('should NAVIGATE to a new URL when the MARKET is changed', async(() => {
    const selectEl = fixture.debugElement.query(By.css('.custom-market-selector'));
    selectEl.nativeElement.value = selectEl.nativeElement.options[3].value;
    selectEl.nativeElement.dispatchEvent(new Event('change'));
    component.activeMarket$.next('thebodyshop-ca');
    fixture.detectChanges();
    fixture.whenStable().then(() => {
      fixture.detectChanges();
      expect(routerSpy).toHaveBeenCalledWith('/thebodyshop-ca/en_CA/CAD/');
    });
  }));
  afterEach(() => {
    component.ngOnDestroy();
    fixture.destroy();
  });
});
