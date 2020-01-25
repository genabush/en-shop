import { CmsComponentData, OutletDirective } from '@spartacus/storefront';
import { ContentSlotComponentData, I18nTestingModule, CMSTabParagraphContainer, CmsService } from '@spartacus/core';
import { Directive, Input } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';

import { MatExpansionModule } from '@angular/material/expansion';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CustomTabParagraphContainerComponent } from './custom-tab-paragraph-container.component';

@Directive({
  selector: '[cxComponentWrapper]'
})
export class MockComponentWrapperDirective {
  @Input() cxComponentWrapper: ContentSlotComponentData;
}

const mockComponents = ['ProductDetailsTabComponent', 'ProductSpecsTabComponent', 'ProductReviewsTabComponent'];

const mockComponentData: CMSTabParagraphContainer = {
  components: mockComponents.join(' '),
  container: 'true',
  name: 'Tab container',
  typeCode: 'CMSTabParagraphContainer',
  uid: 'TabPanelContainer'
};

const mockTabComponentData1 = {
  uid: 'ProductDetailsTabComponent',
  flexType: 'ProductDetailsTabComponent'
};

const mockTabComponentData2 = {
  uid: 'ProductSpecsTabComponent',
  flexType: 'ProductSpecsTabComponent'
};

const mockTabComponentData3 = {
  uid: 'ProductReviewsTabComponent',
  flexType: 'ProductReviewsTabComponent'
};

const MockCmsService = {
  getComponentData: () => of(mockComponentData)
};

const MockCmsComponentData = <CmsComponentData<CMSTabParagraphContainer>>{
  data$: of(mockComponentData)
};

describe('CustomTabParagraphContainerComponent', () => {
  let component: CustomTabParagraphContainerComponent;
  let fixture: ComponentFixture<CustomTabParagraphContainerComponent>;
  let cmsService: CmsService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [I18nTestingModule, MatExpansionModule, BrowserAnimationsModule],
      declarations: [CustomTabParagraphContainerComponent, MockComponentWrapperDirective, OutletDirective],
      providers: [
        { provide: CmsComponentData, useValue: MockCmsComponentData },
        { provide: CmsService, useValue: MockCmsService }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomTabParagraphContainerComponent);
    component = fixture.componentInstance;
    cmsService = TestBed.get(CmsService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render child components', () => {
    spyOn(cmsService, 'getComponentData').and.returnValues(
      of(mockTabComponentData1),
      of(mockTabComponentData2),
      of(mockTabComponentData3)
    );
    let childComponents: any[];
    component.components$.subscribe(components => (childComponents = components)).unsubscribe();

    for (let i = 0; i < childComponents.length; i++) {
      expect(childComponents[i]).toEqual({
        flexType: mockComponents[i],
        uid: mockComponents[i],
        title: `CMSTabParagraphContainer.tabs.${mockComponents[i]}`
      });
    }
  });
});
