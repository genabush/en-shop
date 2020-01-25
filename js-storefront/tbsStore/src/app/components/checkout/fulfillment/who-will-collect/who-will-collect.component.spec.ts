import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';

// spartacus
import { OccConfig, BaseSiteService, LanguageService, CartDataService } from '@spartacus/core';

// vendor
import { Store } from '@ngrx/store';
import { MatInputModule, MatFormFieldModule, MatSelectModule } from '@angular/material';

// components
import { WhoWillCollectComponent } from './who-will-collect.component';
import { MockCxIconComponent, MockStore } from 'src/app/testing/mock.components';

// services
import {
  MockBaseSiteService,
  MockLanguageService,
  MockCustomCartService,
  MockCartDataService
} from 'src/app/testing/mock.services';
import { CustomCartService } from 'src/app/services/cart/facade/cart.service';
import { OccService } from 'src/app/services/occ/occ.service';

// pipes
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';
import { MockOccService } from 'src/app/testing/occ/occ.service.mock';

describe('Who Will Collect Component', () => {
  let component: WhoWillCollectComponent;
  let fixture: ComponentFixture<WhoWillCollectComponent>;
  let customCartService: CustomCartService;
  let cartServiceSpy: jasmine.Spy;
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        BrowserAnimationsModule,
        MatFormFieldModule,
        MatSelectModule,
        MatInputModule,
        ReactiveFormsModule
      ],
      declarations: [WhoWillCollectComponent, MockCxTranslatePipe, MockCxIconComponent],
      providers: [
        OccConfig,
        { provide: Store, useClass: MockStore },
        { provide: OccService, useClass: MockOccService },
        { provide: BaseSiteService, useCLass: MockBaseSiteService },
        { provide: LanguageService, useClass: MockLanguageService },
        { provide: CustomCartService, useClass: MockCustomCartService },
        { provide: CartDataService, useClass: MockCartDataService }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WhoWillCollectComponent);
    component = fixture.componentInstance;
    customCartService = fixture.componentRef.injector.get(CustomCartService);
    cartServiceSpy = spyOn(customCartService, 'getUserId').and.returnValue('current');
    component.currentSiteLanguage = 'en_GB';
    fixture.detectChanges();
  });

  it('should CREATE', () => {
    expect(component).toBeTruthy();
  });
  afterEach(() => {
    fixture.destroy();
  });
});
