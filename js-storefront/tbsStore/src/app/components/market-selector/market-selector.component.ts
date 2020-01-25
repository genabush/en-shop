import { Component, OnInit, OnDestroy, HostListener, Inject } from '@angular/core';

// spartacus
import { CmsComponentData } from '@spartacus/storefront';
import {
  BaseSiteService,
  AuthService,
  AuthSelectors,
  AuthState,
  CurrencyService,
  LanguageService
} from '@spartacus/core';

// vendor
import { Observable, Subscription, BehaviorSubject } from 'rxjs';
import { map, take } from 'rxjs/operators';
import { Store } from '@ngrx/store';
import { Router } from '@angular/router';
import { DOCUMENT } from '@angular/common';

@Component({
  selector: 'app-market-selector',
  templateUrl: './market-selector.component.html',
  styleUrls: ['./market-selector.component.scss']
})
export class MarketSelectorComponent implements OnInit, OnDestroy {
  marketData$: Observable<any>;
  activeMarket$: BehaviorSubject<any> = new BehaviorSubject<any>(null);
  authState$: Observable<AuthState>;
  tempMarketSub: Subscription;

  @HostListener('window:popstate', ['$event'])
  popState(e: Event) {
    const state = (e as any).state;
    if (state !== null && this.activeMarket$.getValue() !== null && typeof state !== 'object') {
      // compare the active market to the popstate window variable
      if (window.location.href.split('/')[1] !== this.activeMarket$.getValue()) {
        // this.goToMarket(state);
        this.goToMarket(state, true);
      }
    } else if (state === null && window.location.pathname.split('/')[1] !== this.activeMarket$.getValue()) {
      this.goToMarket(window.location.pathname);
    }
  }

  constructor(
    public store: Store<any>,
    public cmsData: CmsComponentData<any>,
    public baseSiteService: BaseSiteService,
    public currencyService: CurrencyService,
    public languageService: LanguageService,
    public auth: AuthService,
    public router: Router,
    @Inject(DOCUMENT) private document: Document
  ) {}

  ngOnInit() {
    this.authState$ = this.store.select(AuthSelectors.getAuthState);
    this.getSelectorData();
    this.getActiveMarket();
  }

  getSelectorData() {
    this.marketData$ = this.getMarketSelectorData().pipe(
      map(data => {
        const marketStr = data.markets;
        const replaceComma = marketStr.replace(/'/g, '"');
        const parsedData = JSON.parse(replaceComma);
        return parsedData;
      })
    );
  }

  getMarketSelectorData(): Observable<any> {
    return this.cmsData.data$;
  }

  getActiveMarket() {
    this.baseSiteService.getActive().subscribe((activeMarket: string) => {
      this.activeMarket$.next(activeMarket);
    });
  }

  goToMarketEvent($event: Event) {
    const val = ($event.target as HTMLInputElement).value;
    let isExternal = val.includes('http');
    if (isExternal) {
      this.document.location.href = val;
    }
    if (val !== undefined) {
      this.goToMarket(val, false);
    }
  }

  goToMarket(val: string, viaBrowser?: boolean) {
    this.auth.logout();
    const valueArray = val.split('/');
    const currentPath = window.location.pathname + window.location.search;
    this.baseSiteService.setActive(valueArray[1]);
    this.setTempMarketSub(val, viaBrowser, valueArray, currentPath);
  }

  setTempMarketSub(val: string, viaBrowser: boolean, valueArray: string[], currentPath: string) {
    this.tempMarketSub = this.activeMarket$.pipe(take(1)).subscribe(() => {
      this.setSiteDependencies(val);
      this.fillInitialHistoryState(currentPath);
      // destroy this subscription
      this.destroyTempMarketSub();
      // engage the navigation
      if (!viaBrowser) {
        this.doNavigationBySelect(val);
      } else {
        this.doNavigationByBrowser(val);
      }
    });
    this.activeMarket$.next(valueArray[1]);
  }

  fillInitialHistoryState(currentPath: string) {
    if (window.history.state === null) {
      window.history.replaceState(currentPath, null, currentPath);
    } else if (typeof window.history.state.navigationId === 'number') {
      window.history.replaceState(currentPath, null, currentPath);
    }
  }

  setSiteDependencies(val: string) {
    this.currencyService.setActive(val.split('/')[3]);
    this.languageService.setActive(val.split('/')[2]);
  }

  doNavigationBySelect(val: string) {
    this.router.navigateByUrl(val, { skipLocationChange: true }).then(() => {
      window.location.pathname = val;
    });
  }

  doNavigationByBrowser(val: string) {
    this.router.navigateByUrl(val, { skipLocationChange: false });
  }

  destroyTempMarketSub() {
    if (this.tempMarketSub) {
      this.tempMarketSub.unsubscribe();
    }
  }

  ngOnDestroy() {
    this.destroyTempMarketSub();
  }
}
