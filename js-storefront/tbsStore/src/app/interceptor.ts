import { Injectable, OnDestroy } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpClient } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';
import { environment } from '../environments/environment';
import { Store } from '@ngrx/store';
import { GlobalMessageService, GlobalMessageType, BaseSiteService, RoutingService } from '@spartacus/core';
import { take } from 'rxjs/operators';
import { Router } from '@angular/router';
import { CurrentProductService } from '@spartacus/storefront';
import { OccService } from './services/occ/occ.service';

@Injectable()
export class Interceptor implements HttpInterceptor, OnDestroy {
  baseUrl: string;
  baseSiteSub: Subscription;
  httpSub: Subscription;
  baseSite: string;
  tbsStr = 'thebodyshop-';
  codeStr = 'code=';
  prodStr = '/product/';
  pageTypeString = 'pageType=ProductPage&code=';
  constructor(
    public store: Store<any>,
    public http: HttpClient,
    public router: Router,
    public msgService: GlobalMessageService,
    public currentProduct: CurrentProductService,
    public baseSiteService: BaseSiteService,
    public occService: OccService
  ) {
    this.baseUrl = occService.getBaseUrl();
    this.setBaseSiteSubScription();
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const url = req.url;
    const subStrType = url.indexOf(this.pageTypeString);
    const subStrTbs = url.lastIndexOf(this.tbsStr);

    if (subStrType !== -1 && subStrTbs !== -1) {
      return this.requestHandler(req, url, next);
    } else {
      return this.deferredHandler(req, next);
    }
  }

  setBaseSiteSubScription() {
    this.baseSiteSub = this.baseSiteService.getActive().subscribe((baseSite: string) => (this.baseSite = baseSite));
  }

  requestHandler(req: HttpRequest<any>, url: string, next: HttpHandler) {
    this.setHttpVisibilitySub(url);
    return next.handle(req);
  }

  setHttpVisibilitySub(url: string) {
    const codePos = url.indexOf(this.codeStr);
    const productCode = url.slice(codePos + this.codeStr.length, url.length);

    this.httpSub = this.http
      .get(this.baseUrl + '/rest/v2/' + this.baseSite + '/products/visible/' + productCode + '?fields=DEFAULT')
      .pipe(take(1))
      .subscribe(
        (data: any) => {
          this.destroyBaseSiteSub();
          if (data.visible === 'true') {
            const currentUrl = window.location.pathname;
            const subStr = this.prodStr + productCode + '/';
            const pos = currentUrl.indexOf(subStr) + subStr.length;
            const name = currentUrl.slice(pos, currentUrl.length);

            if (currentUrl.indexOf(this.prodStr + productCode) !== -1) {
              this.router.navigateByUrl(this.baseSite + this.prodStr + productCode + '/' + name);
            }
          } else {
            this.msgService.remove(GlobalMessageType.MSG_TYPE_ERROR);
            this.router.navigateByUrl(data.redirectUrl);
          }
        },
        err => {
          this.destroyBaseSiteSub();
        }
      );
  }

  deferredHandler(req: HttpRequest<any>, next: HttpHandler) {
    switch (req.method) {
      case 'GET':
        if (environment.enableMockData) {
          if (
            req.url.indexOf(environment.mockEndpoints.pages.homePage.amplience.pathname) !== -1 &&
            environment.mockEndpoints.pages.homePage.amplience.enabled
          ) {
            return next.handle(req.clone({ url: environment.mockEndpoints.pages.homePage.amplience.endpoint }));
          } else if (
            req.url.indexOf(environment.mockEndpoints.pages.homePage.marketSelector.pathname) !== -1 &&
            environment.mockEndpoints.pages.homePage.marketSelector.enabled
          ) {
            return next.handle(req.clone({ url: environment.mockEndpoints.pages.homePage.marketSelector.endpoint }));
          } else if (
            req.url.indexOf(environment.mockEndpoints.components.amplience.pathname) !== -1 &&
            environment.mockEndpoints.components.amplience.enabled
          ) {
            return next.handle(req.clone({ url: environment.mockEndpoints.components.amplience.endpoint }));
          } else if (
            req.url.indexOf(environment.mockEndpoints.pages.homePage.olapic.pathname) > -1 &&
            environment.mockEndpoints.pages.homePage.olapic.enabled
          ) {
            return next.handle(req.clone({ url: environment.mockEndpoints.pages.homePage.olapic.endpoint }));
          } else {
            return next.handle(req);
          }
        } else {
          return next.handle(req);
        }
      case 'POST': // code block
        return next.handle(req);
      default:
        return next.handle(req); // code block
    }
  }

  destroyBaseSiteSub() {
    if (this.baseSiteSub) {
      this.baseSiteSub.unsubscribe();
    }
  }

  destroyHttpSub() {
    if (this.httpSub) {
      this.httpSub.unsubscribe();
    }
  }

  ngOnDestroy() {
    this.destroyHttpSub();
    this.destroyBaseSiteSub();
  }
}
