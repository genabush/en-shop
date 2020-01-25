import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { OccService } from '../occ/occ.service';
import { CustomCartService } from '../cart/facade/cart.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FindInStoreService {
  private apiUrl: string;

  constructor(private http: HttpClient, private occService: OccService) {
    this.setCurrentConfig();
  }
  private setCurrentConfig(): void {
    this.apiUrl = this.occService.getApiUrl();
  }
  getProductStock(location, lat, long, productCode): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/products/${productCode}/stock`, {
      params: {
        latitude: lat,
        longitude: long,
        location: location
      }
    });
  }
}
