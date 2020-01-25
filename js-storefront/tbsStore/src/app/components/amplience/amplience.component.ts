import { Component, OnInit, AfterViewChecked, NgModule, Input, OnDestroy } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Store } from '@ngrx/store';
import { Observable, Subscription } from 'rxjs';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { lory } from 'lory.js';
import { CmsComponentData } from '@spartacus/storefront';
import { map } from 'rxjs/operators';
@Component({
  selector: 'app-amplience',
  templateUrl: './amplience.component.html'
})
export class AmplienceComponent implements OnInit, AfterViewChecked, OnDestroy {
  @Input() amplienceInput: string;
  html$: SafeHtml;
  cmsComponent$: Observable<any>;
  ampSub: Subscription;
  constructor(
    public http: HttpClient,
    public store: Store<any>,
    public sanitizer: DomSanitizer,
    public cmsComponent: CmsComponentData<any>
  ) {}

  ngOnInit() {
    this.initAmplience();
  }
  initAmplience() {
    if (this.amplienceInput !== undefined) {
      this.amplienceContent('https://c1.adis.ws', this.amplienceInput);
    } else {
      this.ampSub = this.cmsComponent.data$.subscribe(data => {
        this.amplienceContent(data.amplienceRootUrl, data.setName);
      });
    }
  }

  amplienceContent(amplienceRootUrl, setName) {
    this.html$ = this.getAmplienceMasterTemplate(amplienceRootUrl, setName).pipe(
      map(html => {
        return this.sanitizer.bypassSecurityTrustHtml(html);
      })
    );
  }

  getAmplienceMasterTemplate(amplienceRootUrl, setName) {
    return this.http.get(
      `${amplienceRootUrl}/v1/content/thebodyshop/content-item/${setName}?template=master-template`,
      { responseType: 'text' }
    );
  }
  ngAfterViewChecked() {
    this.initPlugins();
  }
  initPlugins() {
    this.initSlider();
  }
  initSlider() {
    const slider = document.querySelector('.js_slider');
    if (slider) {
      lory(slider, {
        // options going here
        infinite: 1
      });
    }
  }
  destroySubscriptions() {
    if (this.ampSub) {
      this.ampSub.unsubscribe();
    }
  }
  ngOnDestroy() {
    this.destroySubscriptions();
  }
}
