import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StoreBannerComponent } from './store-banner.component';
import { Store } from '@ngrx/store';
import { MockStore } from 'src/app/testing/mock.components';
import { RoutingConfig, StoreFinderService, TranslationService, OccConfig, BaseSiteService } from '@spartacus/core';
import { MockRoutingConfig, MockTranslationService, MockBaseSiteService } from 'src/app/testing/mock.services';

import { RouterTestingModule } from '@angular/router/testing';
import { HttpClient } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MockOccConfig } from 'src/app/testing/occ/occ.config.mock';

describe('Store Banner Component', () => {
  let component: StoreBannerComponent;
  let fixture: ComponentFixture<StoreBannerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule, HttpClientTestingModule],
      declarations: [StoreBannerComponent],
      providers: [
        StoreFinderService,
        HttpClient,
        { provide: Store, useClass: MockStore },
        { provide: RoutingConfig, useClass: MockRoutingConfig },
        { provide: TranslationService, useClass: MockTranslationService },
        { provide: OccConfig, useClass: MockOccConfig },
        { provide: BaseSiteService, useClass: MockBaseSiteService }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StoreBannerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
