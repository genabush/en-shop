import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BackInStockSuccessComponent } from './back-in-stock-success.component';
import { ModalService, ICON_TYPE } from '@spartacus/storefront';
import { Component, Input, Pipe, PipeTransform } from '@angular/core';

class MockModalService {
  closeActiveModal(param) {
    return true;
  }
}

@Component({
  selector: 'cx-icon',
  template: ''
})
class MockCxIconComponent {
  @Input() type: ICON_TYPE;
}

@Pipe({ name: 'cxTranslate' })
class MockCxTranslate implements PipeTransform {
  transform(value: number): number {
    return value;
  }
}

describe('BackInStockSuccessComponent', () => {
  let component: BackInStockSuccessComponent;
  let fixture: ComponentFixture<BackInStockSuccessComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [BackInStockSuccessComponent, MockCxIconComponent, MockCxTranslate],
      providers: [{ provide: ModalService, useClass: MockModalService }]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BackInStockSuccessComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
