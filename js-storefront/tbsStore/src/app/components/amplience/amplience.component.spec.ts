import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AmplienceComponent } from './amplience.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { BehaviorSubject, Observable, ReplaySubject } from 'rxjs';
import { Store } from '@ngrx/store';
import { DomSanitizer } from '@angular/platform-browser';
import { CmsComponentData } from '@spartacus/storefront';

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
class MockDomSanitizer {
  bypassSecurityTrustHtml(param) {
    return '';
  }
}
class MockCmsComponentData {
  data$ = new ReplaySubject();
}

describe('AmplienceComponent', () => {
  let component: AmplienceComponent;
  let fixture: ComponentFixture<AmplienceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AmplienceComponent],
      providers: [
        { provide: Store, useClass: MockStore },
        { provide: DomSanitizer, useClass: MockDomSanitizer },
        { provide: CmsComponentData, useClass: MockCmsComponentData }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AmplienceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
