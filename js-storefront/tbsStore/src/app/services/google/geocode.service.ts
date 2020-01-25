import { Injectable } from '@angular/core';
import { MapsAPILoader } from '@agm/core';

import { tap, map, switchMap } from 'rxjs/operators';
import { from, Observable, of } from 'rxjs';

import { LatLngLiteral } from '@agm/core';
import { OccService } from '../occ/occ.service';
declare var google: any;

@Injectable()
export class GeocodeService {
  private geocoder: any;

  constructor(private mapLoader: MapsAPILoader, private occService: OccService) {}

  private initGeocoder() {
    this.geocoder = new google.maps.Geocoder();
  }

  private waitForMapsToLoad(): Observable<boolean> {
    if (!this.geocoder) {
      return from(this.mapLoader.load()).pipe(
        tap(() => this.initGeocoder()),
        map(() => true)
      );
    }
    return of(true);
  }

  geocodeAddress(location: string): Observable<LatLngLiteral> {
    const searchedLocation = location + ',' + this.occService.getCountryCode();
    return this.waitForMapsToLoad().pipe(
      //filter(loaded => loaded),
      switchMap(() => {
        return new Observable<LatLngLiteral>(observer => {
          this.geocoder.geocode({ address: searchedLocation }, (results, status) => {
            if (status === google.maps.GeocoderStatus.OK) {
              observer.next({
                lat: results[0].geometry.location.lat(),
                lng: results[0].geometry.location.lng()
              });
            } else {
              console.log('Error - ', results, ' & Status - ', status);
              observer.next({ lat: 0, lng: 0 });
            }
            observer.complete();
          });
        });
      })
    );
  }
}
