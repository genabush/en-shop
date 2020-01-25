import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { CustomSearchBoxComponent } from './custom-search-box.component';
import { Component, Input, PipeTransform, Pipe } from '@angular/core';
import { of, BehaviorSubject, Observable, ReplaySubject } from 'rxjs';
import { Store } from '@ngrx/store';
import { RouterTestingModule } from '@angular/router/testing';
import { CustomSearchBoxComponentService } from 'src/app/services/custom-search-box/custom-search-box.service';
import { CmsComponentData, ModalService } from '@spartacus/storefront';

@Component({
  selector: 'cx-icon',
  template: '<div></div>'
})
class MockCxIconComponent {
  @Input() type;
}
@Component({
  selector: 'cx-media',
  template: '<div></div>'
})
class MockCxMediaComponent {
  @Input() container;
  @Input() alt;
}
@Component({
  selector: 'cx-generic-link',
  template: '<div></div>'
})
class MockCxGenericLinkComponent {
  @Input() container;
  @Input() alt;
}

@Pipe({ name: 'cxTranslate' })
class MockCxTranslatePipe implements PipeTransform {
  transform(value: any): string {
    return '/';
  }
}
@Pipe({ name: 'customHighlight' })
class MockCustomizeHighlightPipe implements PipeTransform {
  transform(value: any): string {
    return '/';
  }
}
@Pipe({ name: 'cxUrl' })
class MockCxUrlPipe implements PipeTransform {
  transform(value: any): string {
    return '/';
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
class MockCustomSearchBoxComponentService {
  getResults(param) {
    return new BehaviorSubject({ keywordRedirectUrl: undefined });
  }
  search() {}
  toggleBodyClass() {}
  hasBodyClass() {
    return true;
  }
  launchSearchPage() {}
  clearResults() {}
}
class MockCmsComponentData {
  data$ = new ReplaySubject();
}
class MockModalService {
  closeActiveModal() {
    return;
  }
}

describe('CustomSearchBoxComponent', () => {
  let component: CustomSearchBoxComponent;
  let fixture: ComponentFixture<CustomSearchBoxComponent>;
  let searchBoxService: CustomSearchBoxComponentService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      declarations: [
        CustomSearchBoxComponent,
        MockCxIconComponent,
        MockCxTranslatePipe,
        MockCxIconComponent,
        MockCxMediaComponent,
        MockCxGenericLinkComponent,
        MockCustomizeHighlightPipe,
        MockCxUrlPipe
      ],
      providers: [
        { provide: CustomSearchBoxComponentService, useClass: MockCustomSearchBoxComponentService },
        { provide: CmsComponentData, useClass: MockCmsComponentData },
        { provide: ModalService, useClass: MockModalService },
        { provide: Store, useClass: MockStore }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomSearchBoxComponent);
    component = fixture.componentInstance;
    searchBoxService = TestBed.get(CustomSearchBoxComponentService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  it('should invoke search', () => {
    spyOn(component, 'search');
    component.queryText = 'test';
    expect(component.search).toHaveBeenCalledWith('test');
  });
  it('should invoke open and search', () => {
    spyOn(searchBoxService, 'search');
    spyOn(component, 'open');
    component.search('test');
    expect(searchBoxService.search).toHaveBeenCalled();
    expect(component.open).toHaveBeenCalled();
  });
  it('should invoke toggleBodyClass', () => {
    spyOn(searchBoxService, 'toggleBodyClass');
    component.open();

    expect(searchBoxService.toggleBodyClass).toHaveBeenCalled();
  });
  it('should close typehead searchbox', () => {
    spyOn(searchBoxService, 'toggleBodyClass');
    component['ignoreCloseEvent'] = false;
    component.close(('click' as unknown) as UIEvent);

    expect(searchBoxService.toggleBodyClass).toHaveBeenCalled();
  });
  it('should avoid reopen', () => {
    spyOn(searchBoxService, 'toggleBodyClass');
    component.avoidReopen(({ preventDefault: () => '' } as unknown) as UIEvent);

    expect(searchBoxService.toggleBodyClass).toHaveBeenCalled();
  });
  it('should close modal', () => {
    spyOn(component, 'closeModal');
    spyOn(searchBoxService, 'getResults').and.returnValue(of({ keywordRedirectUrl: undefined }));
    component.launchSearchResult(({ preventDefault: () => '' } as unknown) as UIEvent, '');

    expect(component.closeModal).toHaveBeenCalled();
    expect(searchBoxService.getResults).toHaveBeenCalled();
  });
  it('should close the search result list', () => {
    component.disableClose();
    expect(component['ignoreCloseEvent']).toBeTruthy();
  });
  it('should clear the search box input field', () => {
    spyOn(component, 'disableClose');
    spyOn(searchBoxService, 'clearResults');
    component.clear({ value: 'test' } as HTMLInputElement);

    expect(component.disableClose).toHaveBeenCalled();
    expect(searchBoxService.clearResults).toHaveBeenCalled();
  });
  it('should close modal', () => {
    spyOn(component, 'close');
    spyOn(searchBoxService, 'clearResults');
    component.closeModal({} as UIEvent);

    expect(component.close).toHaveBeenCalled();
    expect(searchBoxService.clearResults).toHaveBeenCalled();
  });
});
