import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CartEmptyParagraphComponent } from './cart-empty-paragraph.component';
import { CmsComponentData } from '@spartacus/storefront';
import { MockCmsComponentData, MockStore } from 'src/app/testing/mock.components';
import { MockCxTranslatePipe, MockCxUrlPipe } from 'src/app/testing/mock.pipes';
import { BrowserDynamicTestingModule } from '@angular/platform-browser-dynamic/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { SharedModule } from '../../shared/shared.module';
import { MessagingComponent } from '../../shared/messaging/messaging.component';
import { MockOccConfig } from 'src/app/testing/occ/occ.config.mock';
// spartacus
import { OccConfig, BaseSiteService } from '@spartacus/core';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MockBaseSiteService } from 'src/app/testing/mock.services';
import { Store } from '@ngrx/store';

describe('CartEmptyParagraphComponent', () => {
  let component: CartEmptyParagraphComponent;
  let fixture: ComponentFixture<CartEmptyParagraphComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [BrowserDynamicTestingModule, HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CartEmptyParagraphComponent, MessagingComponent, MockCxTranslatePipe, MockCxUrlPipe],
      providers: [
        {
          provide: CmsComponentData,
          useClass: MockCmsComponentData
        },
        { provide: OccConfig, useClass: MockOccConfig },
        { provide: BaseSiteService, useClass: MockBaseSiteService },
        { provide: Store, useClass: MockStore }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CartEmptyParagraphComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should CREATE', () => {
    expect(component).toBeTruthy();
  });
});
