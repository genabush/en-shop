import { TestBed, async } from '@angular/core/testing';
import {
  CurrencyService,
  LanguageService,
  RoutingService,
  SearchboxService,
  TranslationService,
  WindowRef
} from '@spartacus/core';
import { of, BehaviorSubject, Observable } from 'rxjs';
import { Store } from '@ngrx/store';
import { RouterTestingModule } from '@angular/router/testing';
import { CustomSearchBoxComponentService } from './custom-search-box.service';
import { SearchBoxConfig } from '@spartacus/storefront';

class MockSearchboxService {
  search() {}
  searchSuggestions() {
    return;
  }
  clearResults() {
    return;
  }
  getResults() {
    return of({});
  }
}
class MocTranslationService {
  translate(param, param2) {
    return of('test');
  }
}
class MockRoutingService {
  go(param) {}
}
class MockWindowRef {
  document = {
    body: {
      classList: {
        add: className => true,
        remove: className => true,
        contains: className => true,
        toggle: className => true
      }
    }
  };
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

describe('CustomSearchBoxComponentService', () => {
  let service: CustomSearchBoxComponentService;
  let searchBoxService: SearchboxService;
  let winRefService: WindowRef;
  let routingService: RoutingService;
  beforeEach(async(() =>
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      providers: [
        { provide: SearchboxService, useClass: MockSearchboxService },
        { provide: RoutingService, useClass: MockRoutingService },
        { provide: TranslationService, useClass: MocTranslationService },
        { provide: WindowRef, useClass: MockWindowRef },
        {
          provide: Store,
          useClass: MockStore
        }
      ]
    })));
  beforeEach(() => {
    service = TestBed.get(CustomSearchBoxComponentService);
    searchBoxService = TestBed.get(SearchboxService);
    winRefService = TestBed.get(WindowRef);
    routingService = TestBed.get(RoutingService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
  describe('search method', () => {
    it('should clear result', () => {
      spyOn(service, 'clearResults');
      service.search('', {} as SearchBoxConfig);
      expect(service.clearResults).toHaveBeenCalled();
    });
    it('should return void', () => {
      const resp = service.search('test', { minCharactersBeforeRequest: 5 } as SearchBoxConfig);

      expect(resp).toEqual();
    });
    it('should invoke search', () => {
      spyOn<any>(searchBoxService, 'search').and.returnValue('ok');
      const resp = service.search('test', {
        minCharactersBeforeRequest: 2,
        displayProducts: true,
        maxSuggestions: 4
      } as SearchBoxConfig);

      expect(searchBoxService.search).toHaveBeenCalled();
    });
    it('should invoke searchSuggestions', () => {
      spyOn<any>(searchBoxService, 'searchSuggestions').and.returnValue('ok');
      const resp = service.search('test', {
        minCharactersBeforeRequest: 2,
        displaySuggestions: true,
        maxSuggestions: 4
      } as SearchBoxConfig);

      expect(searchBoxService.searchSuggestions).toHaveBeenCalled();
    });
  });
  describe('getResults method', () => {
    it('should return Observable<SearchResults>', () => {
      const resp = service.getResults({} as SearchBoxConfig);

      expect(resp).toBeTruthy();
    });
  });
  describe('clearResults method', () => {
    it('should clear result', () => {
      spyOn<any>(searchBoxService, 'clearResults').and.returnValue('ok');
      spyOn<any>(service, 'toggleBodyClass').and.returnValue('ok');
      service.clearResults();
      expect(searchBoxService.clearResults).toHaveBeenCalled();
      expect(service.toggleBodyClass).toHaveBeenCalled();
    });
  });
  describe('hasBodyClass method', () => {
    it('should check body class', () => {
      spyOn(winRefService.document.body.classList, 'contains').and.returnValue(true);
      service.hasBodyClass('test');

      expect(winRefService.document.body.classList.contains).toHaveBeenCalled();
    });
  });
  describe('toggleBodyClass method', () => {
    it('should invoke toogle', () => {
      spyOn(winRefService.document.body.classList, 'toggle').and.returnValue(true);
      service.toggleBodyClass('test');
      expect(winRefService.document.body.classList.toggle).toHaveBeenCalled();
    });
    it('should add class', () => {
      spyOn(winRefService.document.body.classList, 'add').and.returnValue();
      service.toggleBodyClass('test', true);
      expect(winRefService.document.body.classList.add).toHaveBeenCalled();
    });
    it('should remove class', () => {
      spyOn(winRefService.document.body.classList, 'remove').and.returnValue();
      service.toggleBodyClass('test', false);
      expect(winRefService.document.body.classList.remove).toHaveBeenCalled();
    });
  });
  describe('getExactSuggestion method', () => {
    it('should return Observable<string>', () => {
      const resp = service['getExactSuggestion']({} as SearchBoxConfig);

      expect(resp).toBeTruthy();
    });
  });
  describe('launchSearchPage method', () => {
    it('should invoke router method go', () => {
      spyOn<any>(routingService, 'go').and.returnValue(true);
      service.launchSearchPage('test');
      expect(routingService.go).toBeTruthy();
    });
  });
});
