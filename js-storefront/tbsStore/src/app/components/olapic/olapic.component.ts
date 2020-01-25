import { Component, OnInit, AfterViewInit, Inject, ElementRef, Renderer } from '@angular/core';

import { DOCUMENT } from '@angular/common';
import { environment } from './../../../environments/environment';
import { Store } from '@ngrx/store';
import { take } from 'rxjs/operators';
import { OlapicConfig } from './olapicconfig.model';
import { Observable } from 'rxjs';
import { RoutingSelector, SiteContextSelectors } from '@spartacus/core';
import { CmsComponentData, CurrentProductService } from '@spartacus/storefront';

declare global {
  interface Window {
    olapic: any;
  }
}

@Component({
  selector: 'app-olapic',
  templateUrl: './olapic.component.html',
  styleUrls: ['./olapic.component.scss']
})
export class OlapicComponent {
  isInitialised = false;
  olapicConfig: OlapicConfig;
  widgetId$: Observable<any>;
  language: string;
  window: OlapicConfig;
  cmsComponent$: Observable<any>;
  olapicInstance;
  constructor(
    @Inject(DOCUMENT) private document,
    private elementRef: ElementRef,
    public store: Store<any>,
    public cmsComponent: CmsComponentData<any>,
    protected currentProductService: CurrentProductService
  ) {
    this.cmsComponent$ = cmsComponent.data$;
    this.store.select(SiteContextSelectors.getActiveLanguage).subscribe(data => {
      this.language = data;
    });
  }

  displayOlapic(cmsData) {
    if (!this.isInitialised) {
      this.buildConfig(cmsData, this.language);
    }
  }

  buildConfig(cmsData, lang) {
    const olapicSrc = cmsData.olapicSrc;
    const apiKey = cmsData.dataApi;
    const widgetId = cmsData.dataOlapicTagId; // olapic_specific_widget
    const instance = cmsData.dataInstance;
    let tags = ''; // product ID
    const olapicObject = {
      src: olapicSrc,
      apiKey: apiKey,
      lang: lang,
      widgetId: widgetId,
      instance: instance,
      tags: tags
    };

    if (cmsData.forProductPage === 'true') {
      this.currentProductService
        .getProduct()
        .pipe(take(1))
        .subscribe(data => {
          if (data !== undefined) {
            // if variant Product
            if (data && data.hasOwnProperty('baseProduct')) {
              tags = data['baseProduct'];
            } else if (data && data.hasOwnProperty('code')) {
              // if base product
              tags = data['code'];
            }
            olapicObject.tags = tags;
            this.appendScriptTag(olapicObject);
          }
        });
    } else {
      this.appendScriptTag(olapicObject);
    }
  }

  appendScriptTag(olapic: OlapicConfig) {
    const s = this.document.createElement('script');
    s.type = 'text/javascript';
    s.src = olapic.src;
    s.dataset['apikey'] = olapic.apiKey;
    s.dataset['lang'] = olapic.lang;
    s.dataset['olapic'] = olapic.widgetId;
    s.dataset['instance'] = olapic.instance;
    s.dataset['tags'] = olapic.tags;
    s.async = true;
    s.onload = () => {
      window.dispatchEvent(new CustomEvent('scroll'));
    };
    this.elementRef.nativeElement.appendChild(s);

    this.isInitialised = true;
  }
}
