import { Injectable, OnDestroy } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, Subscription } from 'rxjs';
import { Store } from '@ngrx/store';
import { tap, map, take } from 'rxjs/operators';
import { RoutingService, BaseSiteService } from '@spartacus/core';
import { OccService } from '../occ/occ.service';

@Injectable({
  providedIn: 'root'
})
export class ContentPageSearchService implements OnDestroy {
  querySearchSub: Subscription;
  baseSiteSub: Subscription;
  baseSite$: Observable<any>;
  pageMatches: Observable<any>;
  querySearch$: Observable<any>;
  querySearchString$: string;
  contentSearchUrl: string;
  base: string;
  baseUrl: string;
  origin;
  constructor(
    public store: Store<any>,
    public http: HttpClient,
    protected routingService: RoutingService,
    public occService: OccService,
    public baseSiteService: BaseSiteService
  ) {
    // TODO: content search url to reside with other endpoints in app.module | TbsProductOccModule
    this.contentSearchUrl = 'content/search?currentPage=0&fields=FULL&pageSize=20&query=';
    this.baseUrl = this.getOCCServiceBaseUrl();

    this.setSearchSiteSubscription();
    this.setBaseSiteSubscription();
  }

  setSearchSiteSubscription() {
    this.querySearchSub = this.routingService.getRouterState().subscribe(routingData => {
      this.querySearchString$ = '';
      if (routingData.state.params.query) {
        this.querySearchString$ = routingData.state.params.query;
      }
    });
  }

  setBaseSiteSubscription() {
    this.baseSiteSub = this.baseSiteService.getActive().subscribe(data => {
      this.base = data;
    });
  }

  getOCCServiceBaseUrl(): string {
    return this.occService.getBaseUrl();
  }

  searchContentPages(): Observable<any> {
    if (this.querySearchString$ && this.querySearchString$ !== '') {
      return this.http.get(this.getQuerySearchUrlParam()).pipe(
        map((data: any) => {
          return data;
        })
      );
    } else {
      return of({});
    }
  }

  getQuerySearchUrlParam() {
    const urlBase = this.baseUrl + '/rest/v2/' + this.base + '/';
    let endpoint =
      urlBase + this.querySearchString$ && this.querySearchString$ !== ''
        ? this.contentSearchUrl.replace(/\$\{query\}/g, '') + this.querySearchString$
        : this.contentSearchUrl.replace(/\&query\=\$\{query\}/g, '');
    let fullUrl = urlBase + endpoint;
    return fullUrl;
  }

  destroySubsriptions() {
    if (this.querySearchSub) {
      this.querySearchSub.unsubscribe();
    }
    if (this.baseSiteSub) {
      this.baseSiteSub.unsubscribe();
    }
  }

  ngOnDestroy() {
    this.destroySubsriptions();
  }
}
