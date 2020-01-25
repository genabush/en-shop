import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';

// vendor
import { of } from 'rxjs';

// spartacus
import { CmsComponentData } from '@spartacus/storefront';
import { PageMetaService, I18nTestingModule } from '@spartacus/core';

// components
import { CustomBreadcrumbComponent } from './custom-breadcrumb.component';

// services
import { MockPageMetaService } from 'src/app/testing/mock.services';

describe('BreadcrumbComponent', () => {
  let component: CustomBreadcrumbComponent;
  let fixture: ComponentFixture<CustomBreadcrumbComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule, I18nTestingModule],
      declarations: [CustomBreadcrumbComponent],
      providers: [
        { provide: PageMetaService, useClass: MockPageMetaService },
        {
          provide: CmsComponentData,
          useValue: {
            data$: of({})
          }
        }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomBreadcrumbComponent);
    component = fixture.componentInstance;
    spyOn(component, 'hideHomePageCheck').and.callFake(() => {
      return;
    });
  });

  it('should CREATE', async(() => {
    fixture.detectChanges();
    fixture.whenStable().then(() => {
      fixture.detectChanges();
      expect(component).toBeTruthy();
    });
  }));
});
