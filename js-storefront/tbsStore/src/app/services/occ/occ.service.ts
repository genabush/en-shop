import { Injectable } from '@angular/core';
import { OccConfig, BaseSiteService } from '@spartacus/core';
import { lowerCase } from 'lodash';

@Injectable({
  providedIn: 'root'
})
export class OccService {
  private shopName: string;
  private countryCode: string;

  constructor(public occ: OccConfig, private baseSiteService: BaseSiteService) {
    this.baseSiteService.getActive().subscribe((data: string) => {
      this.shopName = data;
      this.countryCode = lowerCase(data.replace(/thebodyshop-/g, ''));
    });
  }

  getBaseUrl(): string {
    return this.occ.backend.occ.baseUrl;
  }

  getApiUrl(): string {
    return `${this.getBaseUrl()}/rest/v2/${this.shopName}`;
  }
  getCountryCode() {
    if (this.countryCode !== 'global') {
      return this.countryCode;
    }
    return '';
  }
}
