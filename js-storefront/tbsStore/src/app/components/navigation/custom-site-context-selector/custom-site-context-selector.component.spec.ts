import { async, ComponentFixture, TestBed } from '@angular/core/testing';

// spartacus
import { SiteContextComponentService } from '@spartacus/storefront';

// components
import { CustomSiteContextSelectorComponent } from './custom-site-context-selector.component';
import { MockCxIconComponent } from 'src/app/testing/mock.components';

// pipes
import { MockContentSelectorPipe } from 'src/app/testing/mock.pipes';

class MockSiteContextComponentService {
  getItems() {}
}

describe('CustomSiteContextSelectorComponent', () => {
  let component: CustomSiteContextSelectorComponent;
  let fixture: ComponentFixture<CustomSiteContextSelectorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CustomSiteContextSelectorComponent, MockCxIconComponent, MockContentSelectorPipe],
      providers: [{ provide: SiteContextComponentService, useClass: MockSiteContextComponentService }]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomSiteContextSelectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should CREATE', () => {
    expect(component).toBeTruthy();
  });
});
