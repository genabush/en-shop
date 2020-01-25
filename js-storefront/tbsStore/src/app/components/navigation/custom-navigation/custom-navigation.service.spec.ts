import { TestBed } from '@angular/core/testing';
import { CmsService, SemanticPathService, CmsNavigationComponent } from '@spartacus/core';

// vendor
import { of, Observable } from 'rxjs';

// services
import { CustomNavigationService } from 'src/app/services/custom-navigation-service/custom-navigation.service';

class MockCmsService {
  getNavigationEntryItems(uid) {
    return of({});
  }
}
class MockSemanticPathService {}

describe('CustomNavigationService', () => {
  let service: CustomNavigationService;
  let cmsService: CmsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [],
      providers: [
        CustomNavigationService,
        {
          provide: CmsService,
          useClass: MockCmsService
        },
        {
          provide: SemanticPathService,
          useClass: MockSemanticPathService
        }
      ]
    });
    service = TestBed.get(CustomNavigationService);
    cmsService = TestBed.get(CmsService);
  });

  it('should create', () => {
    expect(service).toBeTruthy();
  });
  it('should create navigation', () => {
    const resp = service.createNavigation(of({ navigationNode: '' } as CmsNavigationComponent));
    expect(resp._isScalar).toBe(false);
  });
  it('should invoke getNavigationNode', () => {
    spyOn(service, 'getNavigationNode');
    const resp = service.createNavigation(of({ navigationNode: '' } as CmsNavigationComponent));

    expect(service.getNavigationNode).toHaveBeenCalled();
  });
  it('should return observable', () => {
    const res = service.getNavigationNode((false as unknown) as Observable<CmsNavigationComponent>);

    expect(res._isScalar).toEqual(false);
  });
  it('should return items', () => {
    const res = service.getNavigationNode(of({ navigationNode: { uid: '123' } }));

    expect(res._isScalar).toEqual(false);
  });
  it('should invoke getNavigationEntryItems', () => {
    spyOn(cmsService, 'getNavigationEntryItems').and.returnValue(of({}));
    const res = service.getNavigationNode(of({ navigationNode: { uid: '123' } })).subscribe();

    expect(cmsService.getNavigationEntryItems).toHaveBeenCalled();
  });
  it('should invoke createNode', () => {
    spyOn(cmsService, 'getNavigationEntryItems').and.returnValue(of({}));
    spyOn<any>(service, 'createNode').and.returnValue(of({}));
    const res = service.getNavigationNode(of({ navigationNode: { uid: '123' } })).subscribe();
    expect(cmsService.getNavigationEntryItems).toHaveBeenCalled();
  });

  it('should get link', () => {
    const res = service['getLink']({ url: 'test' });
    expect(res).toEqual('test');
  });
  it('should create children', () => {
    const res = service['createChildren']({ children: [] }, []);
    expect(res).toEqual([]);
  });
  it('should create node', () => {
    const res = service['createNode']({ title: 'test' }, []);
    expect(res.title).toEqual('test');
  });
  it('should invoke toHaveBeenCalled', () => {
    spyOn<any>(service, 'getNavigationEntryItems');
    const res = service['processChildren']({ children: [{ title: 'test' }, { title: 'test2' }] }, []);
    expect(service['getNavigationEntryItems']).toHaveBeenCalled();
  });
});
