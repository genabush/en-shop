import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { MatInputModule } from '@angular/material';

import { Store } from '@ngrx/store';

import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';
import { OccConfig, BaseSiteService } from '@spartacus/core';
import { MockBaseSiteService, MockWishlistsService } from 'src/app/testing/mock.services';
import { MockStore } from 'src/app/testing/mock.components';
import { WishlistService } from 'src/app/services/wishlist/wishlist.service';
import { MockOccConfig } from 'src/app/testing/occ/occ.config.mock';
import { WishlistFormComponent } from './wishlist-form.component';

describe('CreateWishlistFormComponent', () => {
  let component: WishlistFormComponent;
  let fixture: ComponentFixture<WishlistFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        FormsModule,
        RouterTestingModule,
        ReactiveFormsModule,
        MatInputModule,
        HttpClientTestingModule,
        BrowserAnimationsModule
      ],
      declarations: [WishlistFormComponent, MockCxTranslatePipe],
      providers: [
        { provide: OccConfig, useClass: MockOccConfig },
        { provide: BaseSiteService, useClass: MockBaseSiteService },
        { provide: Store, useClass: MockStore },
        { provide: WishlistService, useClass: MockWishlistsService }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WishlistFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
