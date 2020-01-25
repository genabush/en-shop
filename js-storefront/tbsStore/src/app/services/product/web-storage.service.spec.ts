import { TestBed } from '@angular/core/testing';
import { AppCustomStorageService } from './web-storage.service';

describe('AppCustomStorageService', () => {
  let service: AppCustomStorageService;
  const STORAGE_KEY = 'testKey';
  const TEST_ITEM = 'some test item';
  beforeEach(() =>
    TestBed.configureTestingModule({
      providers: [AppCustomStorageService]
    })
  );
  beforeEach(() => {
    service = TestBed.get(AppCustomStorageService);
  });

  it('should CREATE the service', () => {
    expect(service).toBeTruthy();
  });

  it('should SET a SESSION VARIABLE when setSessionStorageItem is called', () => {
    service.setSessionStorageItem(STORAGE_KEY, TEST_ITEM);
    const setItem = service.getSessionStorageItem(STORAGE_KEY);
    expect(setItem).toBe(TEST_ITEM);
  });

  afterAll(() => {
    service.clearSessionStorageItem();
    service = null;
  });
});
