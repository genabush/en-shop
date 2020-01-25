import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CategoryBannerComponent } from './category-banner.component';
import { CmsComponentData } from '@spartacus/storefront';
import { of } from 'rxjs';
import { By } from '@angular/platform-browser';
class MockCmsComponentData {
  data$ = of({ styleClass: 'test', wrapAfter: false, categoryImageUrl: true });
}

describe('CategoryBannerComponent', () => {
  let component: CategoryBannerComponent;
  let fixture: ComponentFixture<CategoryBannerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CategoryBannerComponent],
      providers: [{ provide: CmsComponentData, useClass: MockCmsComponentData }]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CategoryBannerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should add class amp-dc-splitBlock--50-50', () => {
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('.amp-dc-splitBlock--50-50'))).toBeTruthy();
  });
  it('should not add class amp-dc-splitBlock--50-50', () => {
    component.cmsComponent$ = of({ styleClass: 'test', wrapAfter: false, categoryImageUrl: false });
    fixture.detectChanges();

    expect(fixture.debugElement.query(By.css('.amp-dc-splitBlock--50-50'))).toBeFalsy();
  });
});
