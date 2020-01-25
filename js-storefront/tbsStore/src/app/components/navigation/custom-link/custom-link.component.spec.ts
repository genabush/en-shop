import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

// spartacus
import { CmsComponentData } from '@spartacus/storefront';

// components
import { CustomLinkComponent } from './custom-link.component';
import { MockCxGenericLinkComponent, MockCxIconComponent, MockCmsComponentData } from 'src/app/testing/mock.components';
import { RouterTestingModule } from '@angular/router/testing';

xdescribe('CustomLinkComponent', () => {
  let component: CustomLinkComponent;
  let fixture: ComponentFixture<CustomLinkComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [BrowserAnimationsModule, RouterTestingModule],
      declarations: [CustomLinkComponent, MockCxGenericLinkComponent, MockCxIconComponent],
      providers: [{ provide: CmsComponentData, useClass: MockCmsComponentData }]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomLinkComponent);
    component = fixture.componentInstance;
  });

  it('should CREATE', () => {
    expect(component).toBeTruthy();
  });
});
