import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, Event, NavigationEnd } from '@angular/router';

// spartacus
import { CmsService, BaseSiteService } from '@spartacus/core';

// vendor
import { GooglePlatformService } from './services/google/google-platform.service';
import { Observable, Subscription } from 'rxjs';
import { Store } from '@ngrx/store';

// custom
declare global {
  interface Window {
    gapi: any;
    dataLayer: any;
  }
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, OnDestroy {
  title = 'mystore-beta-test';
  cmsSub: Subscription;
  routerSub: Subscription;
  baseSiteSub: Subscription;
  baseSite$: Observable<any>;
  constructor(
    private router: Router,
    private cms: CmsService,
    private googlePlatform: GooglePlatformService,
    public baseSite: BaseSiteService,
    public store: Store<any>
  ) {
    window.dataLayer = window.dataLayer || [];
    this.setRouterSubsription();
  }

  ngOnInit() {
    this.setBaseSiteSubscription();
  }

  setRouterSubsription() {
    this.routerSub = this.router.events.subscribe((event: Event) => {
      if (event instanceof NavigationEnd) {
        this.cmsSub = this.cms.getCurrentPage().subscribe(
          data => {
            if (typeof data !== 'undefined') {
              this.pushDataLayer(data);
            }
          },
          err => console.error(err)
        );
      }
    });
  }

  setBaseSiteSubscription() {
    this.baseSiteSub = this.baseSite.getActive().subscribe(
      data => {
        if (data.toLowerCase().indexOf('au') !== -1) {
          this.googlePlatform.loadScript(true);
        }
      },
      err => {
        console.error(err);
      }
    );
  }

  buildPageData(data: any) {
    return {
      event: 'pageviewCustomEvent',
      pagePath: document.location.href || '', // absolute
      pageTitle: document.title || '',
      pageUrl: this.router.url || '', //root-relative
      pageType: data.type || ''
    };
  }

  pushDataLayer(data: object) {
    const pageData = this.buildPageData(data);
    window.dataLayer.push(pageData);
  }

  destroySubscriptions() {
    if (this.cmsSub) {
      this.cmsSub.unsubscribe();
    }
    if (this.routerSub) {
      this.routerSub.unsubscribe();
    }
    if (this.baseSiteSub) {
      this.baseSiteSub.unsubscribe();
    }
  }

  ngOnDestroy() {
    this.destroySubscriptions();
  }
}
