import { GigyaRaasEffects } from './gigya-raas-effects';
import { TestBed } from '@angular/core/testing';
import * as fromEffects from './gigya-raas-effects';
import { logging } from 'selenium-webdriver';
import { Type } from '@angular/core';
import { AuthActions, BaseSiteService, OccConfig, SiteContextConfig } from '@spartacus/core';
import { Logout } from '@spartacus/core/src/auth/store/actions/login-logout.action';
import { LogOutAction } from '../actions/actions';
import { Observable } from 'rxjs';
import { cold, hot } from 'jasmine-marbles';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { provideMockActions } from '@ngrx/effects/testing';

export class MockSiteContextModuleConfig {
  server = {
    baseUrl: 'https://localhost:9002',
    occPrefix: '/rest/v2/'
  };

  context = {
    baseSite: ['electronics'],
    language: [''],
    currency: ['']
  };
}

xdescribe('GigyaRaasEffects', () => {
  let gigyaRaasEffects: fromEffects.GigyaRaasEffects;
  let actions$: Observable<any>;

  const MockOccModuleConfig: OccConfig = {
    backend: {
      occ: {
        baseUrl: '',
        prefix: ''
      }
    }
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, StoreModule.forRoot({}), EffectsModule.forRoot([GigyaRaasEffects])],
      providers: [
        fromEffects.GigyaRaasEffects,
        { provide: OccConfig, useValue: MockOccModuleConfig },
        { provide: SiteContextConfig, useClass: MockSiteContextModuleConfig },
        BaseSiteService,
        provideMockActions(() => actions$)
      ]
    });

    gigyaRaasEffects = TestBed.get(fromEffects.GigyaRaasEffects as Type<fromEffects.GigyaRaasEffects>);
  });

  describe('logOutUser$', () => {
    it('should emit log out action', () => {
      const action = new AuthActions.Logout();
      const completion = new LogOutAction();

      actions$ = hot('-a', { a: action });
      const expected = cold('-b', { b: completion });

      expect(gigyaRaasEffects.logOutUser$).toBeObservable(expected);
    });
  });
});
