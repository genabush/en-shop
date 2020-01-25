import { TestBed } from '@angular/core/testing';
import { GeocodeService } from './geocode.service';
import { MapsAPILoader } from '@agm/core';
import { of } from 'rxjs';
import { OccService } from '../occ/occ.service';
import { MockOccService } from 'src/app/testing/occ/occ.service.mock';

class MockMapsAPILoader {
  load() {
    return of({}).toPromise();
  }
}

describe('Geo Code Service', () => {
  beforeEach(() =>
    TestBed.configureTestingModule({
      providers: [
        GeocodeService,
        {
          provide: MapsAPILoader,
          useClass: MockMapsAPILoader
        },
        { provide: OccService, useClass: MockOccService }
      ]
    })
  );

  it('should be created', () => {
    const service: GeocodeService = TestBed.get(GeocodeService);
    expect(service).toBeTruthy();
  });
});
