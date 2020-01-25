import { AfterViewInit, Component, NgZone, OnDestroy } from '@angular/core';
import { CmsComponentData } from '@spartacus/storefront';
import { AuthRedirectService, AuthService, BaseSiteService, ClientToken, UserToken } from '@spartacus/core';
import { HttpClient } from '@angular/common/http';
import { OccService } from '../../services/occ/occ.service';
import { GigyaResponse, GigyaServerResponse } from './gigya-raas-models';
import { Router } from '@angular/router';
import { GigyaRaasScriptService } from '../../services/gigya-raas-script/gigya-raas-script.service';
import { StoreConfigService } from '../../services/config/store-config.service';
import { GigyaRaasConfig } from '../../../environments/environment.interface';

@Component({
  selector: 'app-gigya-raas',
  templateUrl: './gigya-raas.component.html',
  styleUrls: ['./gigya-raas.component.scss']
})
export class GigyaRaasComponent implements OnDestroy, AfterViewInit {
  private readonly gigyaWindowName = 'gigya';

  gigyaContainer: string;

  private gigyaRaas: any;
  private clientToken: ClientToken;
  private userToken: UserToken;
  private apiUrl: string;
  private baseSiteName: string;
  private gigyaConfig: GigyaRaasConfig;

  constructor(
    private cmsComponent: CmsComponentData<any>,
    private baseSiteService: BaseSiteService,
    private authService: AuthService,
    private http: HttpClient,
    private occService: OccService,
    private gigyaRaasService: GigyaRaasScriptService,
    private storeConfigService: StoreConfigService,
    private authRedirectService: AuthRedirectService,
    private ngZone: NgZone
  ) {
    const cmsComponent$ = cmsComponent.data$;
    cmsComponent$.subscribe(data => {
      this.gigyaRaas = data;
      if (data.embed === 'true') {
        this.gigyaContainer = data.containerID;
      } else {
        this.gigyaContainer = this.generateElementId();
      }
    });
    this.baseSiteService.getActive().subscribe(data => (this.baseSiteName = data));
    this.authService.getClientToken().subscribe(data => (this.clientToken = data));
    this.authService.getUserToken().subscribe(data => (this.userToken = data));
    this.apiUrl = this.occService.getApiUrl();
    this.gigyaConfig = this.storeConfigService.getGigyaConfiguration();
  }

  ngOnDestroy(): void {
    this.gigyaRaasService.cleanup(this.gigyaWindowName);
  }

  ngAfterViewInit(): void {
    this.initGigyaRaas();
  }

  private initGigyaRaas(): void {
    const url = `https://cdns.${this.gigyaConfig.gigyaDataCenter}/JS/gigya.js?apikey=${this.gigyaConfig.apiKey}`;
    this.gigyaRaasService.registerScript(url, this.gigyaWindowName, gigya => {
      this.initGigyaConfig(gigya);
      const gigyaOptions = {
        screenSet: this.gigyaRaas.screenSet,
        startScreen: this.gigyaRaas.startScreen,
        containerID: this.gigyaRaas.containerID
      };
      if (this.gigyaRaas.profileEdit === 'true') {
        gigya.accounts.showScreenSet({
          ...gigyaOptions,
          onAfterSubmit: this.raasEditProfile.bind(this)
        });
      } else {
        gigya.accounts.showScreenSet(gigyaOptions);
      }
    });
  }

  private initGigyaConfig(gigya: any): void {
    this.ngZone.runOutsideAngular(() => {
      gigya.gigyaCache = gigya.gigyaCache || {};

      gigya.accounts.addEventHandlers({
        onLogin: this.raasLogin.bind(this)
      });

      this.enableDebugUI(gigya, this.gigyaConfig.enableDebug);
    });
  }

  private raasLogin(response: GigyaResponse): void {
    this.ngZone.run(() => {
      fetch(`${this.apiUrl}/gigya-raas/login`, {
        method: 'POST',
        headers: {
          Authorization: `${this.clientToken.token_type} ${this.clientToken.access_token}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(response)
      })
        .then(res => {
          return res.json();
        })
        .then((data: GigyaServerResponse) => {
          console.log('data', data);
          if (!!data && !!data.oauthToken) {
            const userToken = JSON.parse(data.oauthToken) as UserToken;
            //set the user id to current to associate the user on the oauth token to spartacus
            userToken.userId = 'current';
            this.authService.authorizeWithToken(userToken);
            this.authService
              .getUserToken()
              .subscribe(data => {
                if (data && data.access_token) {
                  this.authRedirectService.redirect();
                }
              })
              .unsubscribe();
          }
        });
    });
  }

  private raasEditProfile(response: any): void {
    this.ngZone.run(() => {
      fetch(`${this.apiUrl}/gigya-raas/profile`, {
        method: 'POST',
        headers: {
          Authorization: `${this.userToken.token_type} ${this.userToken.access_token}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(response.response)
      })
        .then(res => {
          return res.json();
        })
        .then((data: GigyaServerResponse) => {
          console.log('data', data);
          if (!!data && !!data.oauthToken) {
            const userToken = JSON.parse(data.oauthToken) as UserToken;
            //set the user id to current to associate the user on the oauth token to spartacus
            userToken.userId = 'current';
            this.authService.authorizeWithToken(userToken);
            this.authService
              .getUserToken()
              .subscribe(data => {
                if (data && data.access_token) {
                  this.authRedirectService.redirect();
                }
              })
              .unsubscribe();
          } else {
            if (data.code === 500) {
              console.log('error while updating profile');
              //TODO handle errors
            }
          }
        });
    });
  }

  private enableDebugUI(gigya: any, enableDebug: boolean = false): void {
    if (enableDebug) {
      gigya.showDebugUI();
    }
  }

  private generateElementId(): string {
    return `gigya-raas-${new Date().valueOf()}`;
  }
}
