import { TestBed, ComponentFixture, async } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

// spartacus
import { RoutingService, BaseSiteService, OccConfig } from '@spartacus/core';
import { PageLayoutService } from '@spartacus/storefront';

// vendor
import { of } from 'rxjs';

// directives
import { CustomProductListScrollDirective } from './custom-product-list-scroll.directive';

// components
import { AppCustomStorageService } from '../../services/product/web-storage.service';
import { CustomProductListComponent } from '../../components/product/product-list/container/custom-product-list.component';
import {
  MockAmplienceComponent,
  MockCxPaginationComponent,
  MockCxProductViewComponent,
  MockResultCountComponent,
  MockSliderComponent,
  MockProductGridItemComponent,
  MockPageLayoutService,
  MockCustomProductListComponentService,
  MockStore
} from '../../testing/mock.components';

// services
import { CustomProductListComponentService } from '../../components/product/product-list/container/custom-product-list-component.service';
import { ContentPageSearchService } from '../../services/content-page-search/content-page-search.service';
import { MockRoutingService, MockContentPageSearchService, MockBaseSiteService } from '../../testing/mock.services';

// pipes
import { MockCxTranslatePipe } from '../../testing/mock.pipes';

// constants
import { MOCK_DUMMY_SEARCH_MODEL } from '../../testing/mock.constants';
import { Store } from '@ngrx/store';
import { Component, Input } from '@angular/core';
import { MockOccConfig } from 'src/app/testing/occ/occ.config.mock';

describe('CustomProductListScrollDirective', () => {
  let component: CustomProductListComponent;
  let fixture: ComponentFixture<CustomProductListComponent>;
  let sessionService: any;
  const STORAGE_KEY = 'plpScroll';
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([]), HttpClientTestingModule],
      declarations: [
        CustomProductListComponent,
        MockProductGridItemComponent,
        MockAmplienceComponent,
        MockCxPaginationComponent,
        MockCxProductViewComponent,
        MockResultCountComponent,
        MockSliderComponent,
        MockCxTranslatePipe,
        CustomProductListScrollDirective
      ],
      providers: [
        AppCustomStorageService,
        { provide: PageLayoutService, useClass: MockPageLayoutService },
        { provide: CustomProductListComponentService, useClass: MockCustomProductListComponentService },
        { provide: ContentPageSearchService, useClass: MockContentPageSearchService },
        { provide: RoutingService, useClass: MockRoutingService },
        { provide: OccConfig, useClass: MockOccConfig },
        { provide: BaseSiteService, useClass: MockBaseSiteService },
        { provide: Store, useClass: MockStore }
      ]
    }).compileComponents();
    TestBed.overrideComponent(CustomProductListComponent, {
      set: {
        template: '<div appCustomProductListScroll style="width:100%;height:600px;border:solid 1px;"></div>'
      }
    });
    fixture = TestBed.createComponent(CustomProductListComponent);
    component = fixture.componentInstance;
    sessionService = fixture.componentRef.injector.get(AppCustomStorageService);
    component.model$ = of(MOCK_DUMMY_SEARCH_MODEL);
    fixture.detectChanges();
  }));
  it('should CREATE', async(() => {
    fixture.whenStable().then(() => {
      fixture.detectChanges();
      const directive = fixture.debugElement.query(By.directive(CustomProductListScrollDirective));
      expect(directive).toBeTruthy();
    });
  }));
  xit('should SET a page reference in the SESSION STORAGE that matches SCROLL behaviour', async(() => {
    window.scrollTo(0, 350);
    window.dispatchEvent(new MouseEvent('scroll'));
    fixture.whenStable().then(() => {
      fixture.detectChanges();
      const scrollTop = Math.max(window.pageYOffset, document.documentElement.scrollTop, document.body.scrollTop);
      const sessionObject = sessionService.getSessionStorageItem(STORAGE_KEY);
      expect(sessionObject).toEqual([{ scrollPos: scrollTop, page: window.location.href }]);
    });
  }));
  afterEach(() => {
    sessionService.clearSessionStorageItem();
    component.ngOnDestroy();
    fixture.destroy();
  });
});
