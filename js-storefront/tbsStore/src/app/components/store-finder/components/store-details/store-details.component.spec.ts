import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { StoreDetailsComponent } from './store-details.component';
import { AgmCoreModule } from '@agm/core';
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';
import { SpinnerModule } from '@spartacus/storefront';
import {
  StoreFinderService,
  GlobalMessageService,
  RoutingConfig,
  TranslationService,
  OccConfig,
  BaseSiteService
} from '@spartacus/core';
import { Store } from '@ngrx/store';
import { MockStore } from 'src/app/testing/mock.components';
import {
  MockGlobalMessagingService,
  MockRoutingConfig,
  MockTranslationService,
  MockBaseSiteService
} from 'src/app/testing/mock.services';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClient } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MockOccConfig } from 'src/app/testing/occ/occ.config.mock';
import { NoticeBannerComponent } from '../notice-banner/notice-banner.component';
import { StoreBannerComponent } from '../store-banner/store-banner.component';
import { MockComponent } from 'ng-mocks';
import { OpeningHoursComponent } from '../opening-hours/opening-hours.component';

describe('Store Details Component', () => {
  let component: StoreDetailsComponent;
  let fixture: ComponentFixture<StoreDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AgmCoreModule, SpinnerModule, RouterTestingModule, HttpClientTestingModule],
      declarations: [
        StoreDetailsComponent,
        MockCxTranslatePipe,
        MockComponent(NoticeBannerComponent),
        MockComponent(StoreBannerComponent),
        MockComponent(OpeningHoursComponent)
      ],
      providers: [
        StoreFinderService,
        HttpClient,
        { provide: Store, useClass: MockStore },
        { provide: GlobalMessageService, useClass: MockGlobalMessagingService },
        { provide: RoutingConfig, useClass: MockRoutingConfig },
        { provide: TranslationService, useClass: MockTranslationService },
        { provide: OccConfig, useClass: MockOccConfig },
        { provide: BaseSiteService, useClass: MockBaseSiteService }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StoreDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
