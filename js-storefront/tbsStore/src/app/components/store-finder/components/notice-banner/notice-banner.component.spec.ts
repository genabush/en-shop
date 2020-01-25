import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NoticeBannerComponent } from './notice-banner.component';
import { I18nModule } from '@spartacus/core';

const futureDate = '2029-01-01';
const MockData = {
  temporaryClosedFromDate: '2017-01-01',
  temporaryClosedToDate: '2019-01-01'
};

describe('Notice Banner Component', () => {
  let component: NoticeBannerComponent;
  let fixture: ComponentFixture<NoticeBannerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [I18nModule],
      declarations: [NoticeBannerComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NoticeBannerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should be hidden by default', () => {
    expect(component.showBanner).toBeFalsy();
  });
  describe('Given dates are provided and dates are the same', () => {
    it('should be hidden  ', () => {
      component.data = MockData;
      const today = new Date(MockData.temporaryClosedToDate);
      jasmine.clock().mockDate(today);
      component.ngOnInit();
      expect(component.showBanner).toBeFalsy();
    });
  });
  describe('Given dates are provided and dates are different', () => {
    it('should be visible', () => {
      component.data = MockData;
      const today = new Date(futureDate);
      jasmine.clock().mockDate(today);
      component.ngOnInit();
      expect(component.showBanner).toBeTruthy();
    });
  });
});
