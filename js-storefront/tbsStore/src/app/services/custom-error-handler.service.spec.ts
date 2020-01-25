import { TestBed } from '@angular/core/testing';

import { CustomErrorHandlerService } from './custom-error-handler.service';
import { GlobalMessageService } from '@spartacus/core';

class MockGlobalMessageService {}

describe('CustomErrorHandlerService', () => {
  let service: CustomErrorHandlerService;
  beforeEach(() =>
    TestBed.configureTestingModule({
      providers: [{ provide: GlobalMessageService, useClass: MockGlobalMessageService }, CustomErrorHandlerService]
    })
  );
  beforeEach(() => {
    service = TestBed.get(CustomErrorHandlerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
