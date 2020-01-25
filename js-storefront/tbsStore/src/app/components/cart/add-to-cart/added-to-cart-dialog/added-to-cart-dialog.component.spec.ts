import { Component, DebugElement, Input, Pipe, PipeTransform } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { RouterTestingModule } from '@angular/router/testing';
import { CartService, I18nTestingModule, OrderEntry, PromotionResult } from '@spartacus/core';
import { Observable, of } from 'rxjs';
import { ICON_TYPE } from '@spartacus/storefront';
import { ModalService } from '@spartacus/storefront';
import { SpinnerModule } from '@spartacus/storefront';
// import { AutoFocusDirectiveModule } from '@spartacus/storefront';
import { AddedToCartDialogComponent } from './added-to-cart-dialog.component';
import { CustomCartService } from '../../../../services/cart/facade/cart.service';

class MockCartService {
  getLoaded(): Observable<boolean> {
    return of();
  }

  updateEntry(_entryNumber: string, _updatedQuantity: number): void {}

  removeEntry(_entry: OrderEntry): void {}
}

const mockOrderEntry: OrderEntry[] = [
  {
    quantity: 1,
    entryNumber: 1,
    product: {
      code: 'CODE1111'
    }
  },
  {
    quantity: 2,
    entryNumber: 1,
    product: {
      code: 'CODE1111'
    }
  }
];

@Component({
  selector: 'cx-icon',
  template: ''
})
class MockCxIconComponent {
  @Input() type: ICON_TYPE;
}

@Component({
  selector: 'cx-dialog-title',
  template: ''
})
class MockCxDialogTitleComponent {}
@Component({
  selector: 'cx-spinner',
  template: ''
})
class MockCxSpinnerComponent {}

@Component({
  selector: 'cx-cart-item',
  template: ''
})
class MockCartItemComponent {
  @Input()
  compact = false;
  @Input()
  item: Observable<OrderEntry>;
  @Input()
  potentialProductPromotions: PromotionResult[];
  @Input()
  isReadOnly = false;
  @Input()
  cartIsLoading = false;
  @Input()
  parent: FormGroup;
  @Input()
  quantity: number;
}

@Pipe({
  name: 'cxUrl'
})
class MockUrlPipe implements PipeTransform {
  transform(): any {}
}
@Pipe({
  name: 'cxTranslate'
})
class MockCxTranslatePipe implements PipeTransform {
  transform(): any {}
}
class MockCustomCartService {
  removeEntry() {}
  updateEntry() {}
}

class MockModalService {
  dismissActiveModal(): void {}
}

describe('AddedToCartDialogComponent', () => {
  let component: AddedToCartDialogComponent;
  let fixture: ComponentFixture<AddedToCartDialogComponent>;
  let el: DebugElement;
  let cartService: CartService;
  let mockModalService: MockModalService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [FormsModule, ReactiveFormsModule, RouterTestingModule],
      declarations: [
        AddedToCartDialogComponent,
        MockCartItemComponent,
        MockUrlPipe,
        MockCxIconComponent,
        MockCxDialogTitleComponent,
        MockCxTranslatePipe,
        MockCxSpinnerComponent
      ],
      providers: [
        {
          provide: ModalService,
          useClass: MockModalService
        },
        {
          provide: CartService,
          useClass: MockCartService
        },
        {
          provide: CustomCartService,
          useClass: MockCustomCartService
        }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddedToCartDialogComponent);
    component = fixture.componentInstance;
    el = fixture.debugElement;
    component.entry$ = of(mockOrderEntry[0]);
    cartService = TestBed.get(CartService);
    mockModalService = TestBed.get(ModalService);

    spyOn(cartService, 'removeEntry').and.callThrough();
    spyOn(cartService, 'updateEntry').and.callThrough();
    spyOn(mockModalService, 'dismissActiveModal').and.callThrough();
    component.loaded$ = of(true);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display loading placeholder', () => {
    component.loaded$ = of(false);
    fixture.detectChanges();
    expect(el.query(By.css('.cx-dialog-title')).nativeElement.textContent.trim()).toEqual('');
    expect(el.query(By.css('cx-spinner')).nativeElement).toBeDefined();
  });

  xit('should handle focus of elements', () => {
    fixture.detectChanges();
    expect(el.query(By.css('.cx-dialog-buttons > .btn-primary')).nativeElement).toEqual(document.activeElement);
  });

  it('should display quantity', () => {
    component.loaded$ = of(false);
    fixture.detectChanges();
    expect(el.query(By.css('.cx-dialog-title')).nativeElement.textContent.trim()).toEqual('');
  });

  it('should display cart item', () => {
    fixture.detectChanges();
    expect(el.query(By.css('cx-cart-item'))).toBeDefined();
  });
  // Element with class name cx-dialog-total is commented in component code

  // it('should display cart total', () => {
  //   component.loaded$ = of(false);
  //   component.cart$ = of({
  //     deliveryItemsQuantity: 1,
  //     totalPrice: {
  //       formattedValue: '$100.00'
  //     }
  //   });
  //   fixture.detectChanges();
  //   const cartTotalEl = el.query(By.css('.cx-dialog-total')).nativeElement;
  //   expect(cartTotalEl.children[0].textContent).toEqual('');
  //   expect(cartTotalEl.children[1].textContent).toEqual('$100.00');
  // });

  it('should remove entry', () => {
    component.ngOnInit();
    component.entry$.subscribe();
    const item = mockOrderEntry[0];
    expect(component.form.controls[item.product.code]).toBeDefined();
    component.removeEntry(item);
    expect(component.form.controls[item.product.code]).toBeUndefined();
    expect(mockModalService.dismissActiveModal).toHaveBeenCalledWith('Removed');
  });

  xit('should update entry', () => {
    const item = mockOrderEntry[0];
    component.updateEntry({ item, updatedQuantity: 5 });
    expect(cartService.updateEntry).toHaveBeenCalledWith(item.entryNumber, 5);
  });

  it('should show added dialog title message', () => {
    component.loaded$ = of(false);
    fixture.detectChanges();
    const dialogTitleEl = el.query(By.css('.cx-dialog-title')).nativeElement;
    expect(dialogTitleEl.textContent).toEqual('');
  });

  it('should show increment dialog title message', () => {
    component.entry$ = of(mockOrderEntry[0]);
    component.loaded$ = of(false);
    component.increment = true;
    fixture.detectChanges();
    const dialogTitleEl = el.query(By.css('.cx-dialog-title')).nativeElement;
    expect(dialogTitleEl.textContent).toEqual('');
  });
});
