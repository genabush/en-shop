import { ChangeDetectionStrategy, Component, OnInit, ElementRef } from '@angular/core';
import { CmsBreadcrumbsComponent, PageMeta, PageMetaService, TranslationService } from '@spartacus/core';
import { combineLatest, Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { CmsComponentData } from '@spartacus/storefront';
import { isUndefined } from 'lodash';

@Component({
  selector: 'cx-breadcrumb',
  templateUrl: './custom-breadcrumb.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CustomBreadcrumbComponent implements OnInit {
  classes = 'd-none';
  title$: Observable<string>;
  crumbs$: Observable<any[]>;

  constructor(
    public component: CmsComponentData<CmsBreadcrumbsComponent>,
    protected pageMetaService: PageMetaService,
    private translation: TranslationService,
    private el: ElementRef
  ) {}

  ngOnInit(): void {
    this.setCrumbs();
    this.setTitle();
  }

  hideHomePageCheck(meta): void {
    if (!isUndefined(meta.title)) {
      if (meta.title.indexOf('Homepage') > -1) {
        // hide the component
        this.classes = 'd-none';
      } else {
        // show the component
        this.classes = 'd-flex';
      }
    } else {
      // show the component
      this.classes = 'd-flex';
    }
  }

  private setTitle(): void {
    this.title$ = this.pageMetaService.getMeta().pipe(
      filter(Boolean),
      map((meta: PageMeta) => meta.heading || meta.title)
    );
  }

  private setCrumbs(): void {
    this.crumbs$ = combineLatest(this.pageMetaService.getMeta(), this.translation.translate('common.home')).pipe(
      map(([meta, textHome]) => {
        this.hideHomePageCheck(meta);
        return meta && meta.breadcrumbs ? meta.breadcrumbs : [{ label: textHome, link: '/' }];
      })
    );
  }
}
