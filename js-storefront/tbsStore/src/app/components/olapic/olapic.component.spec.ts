import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OlapicComponent } from './olapic.component';

import { Store } from '@ngrx/store';
import { CmsComponentData, CurrentProductService } from '@spartacus/storefront';
import { MockStore, MockCmsComponentData } from 'src/app/testing/mock.components';
import { MockCurrentProductService } from 'src/app/testing/mock.services';

describe('OlapicComponent', () => {
  let component: OlapicComponent;
  let fixture: ComponentFixture<OlapicComponent>;
  let currentProductService: CurrentProductService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [OlapicComponent],
      providers: [
        { provide: Store, useClass: MockStore },
        { provide: CmsComponentData, useClass: MockCmsComponentData },
        { provide: CurrentProductService, useClass: MockCurrentProductService }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OlapicComponent);
    component = fixture.componentInstance;
    currentProductService = TestBed.get(CurrentProductService);
    fixture.detectChanges();
  });

  it('should CREATE', () => {
    expect(component).toBeTruthy();
  });

  it('should APPEND a script on CONFIG', () => {
    const appendScriptSpy = spyOn(component, 'appendScriptTag').and.callFake(() => {});
    component.displayOlapic({});
    expect(appendScriptSpy).toHaveBeenCalled();
  });
});
