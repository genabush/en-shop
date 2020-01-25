import { TestBed, async } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { Component } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';
import { CmsService, BaseSiteService } from '@spartacus/core';
import { of, BehaviorSubject, Observable } from 'rxjs';
import { GooglePlatformService } from './services/google/google-platform.service';
import { Store } from '@ngrx/store';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

// pipes
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';

@Component({
  selector: 'cx-storefront',
  template: '<div></div>'
})
class MockCxStoreFrontComponent {}

class MockCmsService {
  getCurrentPage() {
    of({});
  }
}
class MockGooglePlatformService {
  loadScript(param) {
    return true;
  }
}
class MockBaseSiteService {
  getActive() {
    return of({});
  }
}
class MockStore<T> {
  private state: BehaviorSubject<T> = new BehaviorSubject(undefined);
  setState(data: T) {
    this.state.next(data);
  }
  select(selector?: any): Observable<T> {
    return this.state.asObservable();
  }
  dispatch(action: any) {}
}

describe('AppComponent', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule, NoopAnimationsModule],
      declarations: [AppComponent, MockCxStoreFrontComponent, MockCxTranslatePipe],
      providers: [
        { provide: CmsService, useClass: MockCmsService },
        { provide: GooglePlatformService, useClass: MockGooglePlatformService },
        { provide: BaseSiteService, useClass: MockBaseSiteService },
        { provide: Store, useClass: MockStore }
      ]
    }).compileComponents();
  }));

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  });
});
