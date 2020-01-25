import { TestBed } from '@angular/core/testing';

import { AdyenCreditCardsService } from './adyen-credit-cards.service';

describe('AdyenCreditCardsService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: AdyenCreditCardsService = TestBed.get(AdyenCreditCardsService);
    expect(service).toBeTruthy();
  });
});
