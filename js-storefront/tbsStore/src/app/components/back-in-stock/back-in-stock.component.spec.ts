import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BackInStockComponent } from './back-in-stock.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ModalService } from '@spartacus/storefront';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';

// spartacus
import { BaseSiteService } from '@spartacus/core';

// comps
import { MockCxIconComponent } from 'src/app/testing/mock.components';
import { BackInStockFormComponent } from './back-in-stock-form/back-in-stock-form.component';

// services
import { OccService } from '../../services/occ/occ.service';
import { MockBaseSiteService } from 'src/app/testing/mock.services';

// pipes
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';
import { MatInputModule } from '@angular/material';
import { MockOccService } from 'src/app/testing/occ/occ.service.mock';

class MockModalService {
  open(param, param2) {
    return true;
  }
  closeActiveModal() {
    return true;
  }
}

describe('BackInStockComponent', () => {
  let component: BackInStockComponent;

  let fixture: ComponentFixture<BackInStockComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        ReactiveFormsModule,
        FormsModule,
        RouterTestingModule.withRoutes([]),
        MatInputModule
      ],
      declarations: [BackInStockComponent, BackInStockFormComponent, MockCxTranslatePipe, MockCxIconComponent],
      providers: [
        { provide: ModalService, useClass: MockModalService },
        { provide: BaseSiteService, useClass: MockBaseSiteService },
        { provide: OccService, useClass: MockOccService }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BackInStockComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should CREATE', () => {
    expect(component).toBeTruthy();
  });
});
