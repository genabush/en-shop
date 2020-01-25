import { TestBed } from '@angular/core/testing';

import { GigyaRaasScriptService } from './gigya-raas-script.service';

describe('GigyaRaasScriptService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: GigyaRaasScriptService = TestBed.get(GigyaRaasScriptService);
    expect(service).toBeTruthy();
  });
});
