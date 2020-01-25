import { Component, OnInit } from '@angular/core';
import { CmsComponentData } from '@spartacus/storefront';
import { Observable } from 'rxjs';
@Component({
  selector: 'app-category-banner',
  templateUrl: './category-banner.component.html'
})
export class CategoryBannerComponent implements OnInit {
  cmsComponent$: Observable<any>;
  constructor(public cmsComponent: CmsComponentData<any>) {
    this.cmsComponent$ = cmsComponent.data$;
  }
  ngOnInit() {}
}
