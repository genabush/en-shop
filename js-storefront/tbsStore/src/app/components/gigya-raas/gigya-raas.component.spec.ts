import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GigyaRaasComponent } from './gigya-raas.component';
import { CmsComponentData } from '@spartacus/storefront';
import { MockCmsComponentData, MockStore } from 'src/app/testing/mock.components';
import { Store } from '@ngrx/store';
import {
  MockAuthRedirectService,
  MockAuthService,
  MockBaseSiteService,
  MockStoreConfigService
} from '../../testing/mock.services';
import { AuthRedirectService, AuthService, BaseSiteService } from '@spartacus/core';
import { HttpClient } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { OccService } from '../../services/occ/occ.service';
import { MockOccService } from '../../testing/occ/occ.service.mock';
import { GigyaRaasScriptService } from '../../services/gigya-raas-script/gigya-raas-script.service';
import { StoreConfigService } from '../../services/config/store-config.service';
import { Type } from '@angular/core';

export class MockGigyaScriptService {
  registerScript(url: string, globalVar: string, onReady: (globalVar: any) => void): void {}

  cleanup(globalVar: string): void {}
}

describe('GigyaRaasComponent', () => {
  let component: GigyaRaasComponent;
  let fixture: ComponentFixture<GigyaRaasComponent>;
  let httpClient: HttpClient;
  let httpTestingController: HttpTestingController;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [GigyaRaasComponent],
      providers: [
        { provide: Store, useClass: MockStore },
        { provide: CmsComponentData, useClass: MockCmsComponentData },
        { provide: BaseSiteService, useClass: MockBaseSiteService },
        { provide: AuthService, useClass: MockAuthService },
        { provide: OccService, useClass: MockOccService },
        { provide: GigyaRaasScriptService, useClass: MockGigyaScriptService },
        { provide: StoreConfigService, useClass: MockStoreConfigService },
        { provide: AuthRedirectService, useClass: MockAuthRedirectService }
      ]
    }).compileComponents();

    httpClient = TestBed.get(HttpClient);
    httpTestingController = TestBed.get(HttpTestingController);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GigyaRaasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
