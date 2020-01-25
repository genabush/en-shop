import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { Component, Input } from '@angular/core';

// spartacus
import { CmsComponentData } from '@spartacus/storefront';

// vendor
import { of } from 'rxjs';

// components
import { CustomCategoryNavigationComponent } from './custom-category-navigation.component';

// services
import { CustomNavigationService } from 'src/app/services/custom-navigation-service/custom-navigation.service';

// pipes
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';

@Component({
  selector: 'cx-navigation-ui',
  template: '<div></div>'
})
class MockCxNavigationUiComponent {
  @Input() node;
  @Input() wrapAfter;
  @Input() ngClass;
  @Input() allowAlignToRight;
}

class MockCmsComponentData {
  data$ = of({ styleClass: 'test', wrapAfter: false });
}

class MockCustomNavigationService {
  getNavigationNode(param) {
    return of({ styleClass: 'test', wrapAfter: false });
  }
}

xdescribe('CustomCategoryNavigationComponent', () => {
  let component: CustomCategoryNavigationComponent;
  let fixture: ComponentFixture<CustomCategoryNavigationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CustomCategoryNavigationComponent, MockCxNavigationUiComponent, MockCxTranslatePipe],
      providers: [
        { provide: CmsComponentData, useClass: MockCmsComponentData },
        { provide: CustomNavigationService, useClass: MockCustomNavigationService }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomCategoryNavigationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
