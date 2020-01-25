import { Injectable, Inject } from '@angular/core';
import { StorageService, SESSION_STORAGE } from 'ngx-webstorage-service';
// https://www.npmjs.com/package/ngx-webstorage-service

@Injectable()
export class AppCustomStorageService {
  constructor(@Inject(SESSION_STORAGE) private sessionStorage: StorageService) {}

  setSessionStorageItem(key: string, value: any) {
    this.sessionStorage.set(key, value);
  }

  getSessionStorageItem(key: string) {
    return this.sessionStorage.get(key) || null;
  }

  removeSessionStorageItem(key: string) {
    return this.sessionStorage.remove(key);
  }

  clearSessionStorageItem() {
    this.sessionStorage.clear();
  }
}
