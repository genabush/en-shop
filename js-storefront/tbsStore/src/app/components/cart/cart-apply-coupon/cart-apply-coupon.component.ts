import { Component, OnDestroy, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';

// spartacus
import {
  Cart,
  AuthService,
  OCC_USER_ID_ANONYMOUS,
  GlobalMessageService,
  GlobalMessageType,
  Voucher
} from '@spartacus/core';

// vendor
import { Observable, combineLatest, BehaviorSubject } from 'rxjs';
import { Subscription } from 'rxjs/internal/Subscription';
import { map, startWith, tap } from 'rxjs/operators';
import { isUndefined, isEmpty } from 'lodash';

// services
import { CustomCartVoucherService } from 'src/app/services/custom-cart-voucher/custom-cart-voucher.service';
import { CustomCartService } from 'src/app/services/cart/facade/cart.service';

// interfaces
import { IVoucherError } from 'src/app/interfaces/custom-cart.interface';

@Component({
  selector: 'cx-cart-apply-coupon',
  templateUrl: './cart-apply-coupon.component.html'
})
export class CartApplyCouponComponent implements OnInit, OnDestroy {
  form: FormGroup;
  cartIsLoading$: Observable<boolean>;
  cart$: Observable<Cart>;
  cartId: string;
  appliedVouchers$: BehaviorSubject<Voucher[]> = new BehaviorSubject<Voucher[]>([]);
  submitDisabled$: Observable<boolean>;
  errorList$: BehaviorSubject<IVoucherError[]> = new BehaviorSubject<IVoucherError[]>([]);
  invalidCode: string;
  private subscription = new Subscription();
  private getVouchersSub: Subscription;
  constructor(
    private cartService: CustomCartService,
    private authService: AuthService,
    private cartVoucherService: CustomCartVoucherService,
    private formBuilder: FormBuilder,
    private msgService: GlobalMessageService,
    private cd: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.setCouponFormGroup();
    this.setCartState();
    this.setSubmitDisabled();
    this.setVoucherSub();
    this.setInputCodeSub();
  }

  setCouponFormGroup() {
    this.form = this.formBuilder.group({
      couponCode: ['', []]
    });
  }

  setCartState() {
    this.cart$ = combineLatest([this.cartService.getActive(), this.authService.getOccUserId()]).pipe(
      tap(([cart, userId]: [Cart, string]) => {
        this.cartId = userId === OCC_USER_ID_ANONYMOUS ? cart.guid : cart.code;
        this.cartVoucherService.applyVoucherState(cart.appliedVouchers);
        this.checkResetState(cart.appliedVouchers);
      }),
      map(([cart]: [Cart, string]) => cart)
    );

    this.cartIsLoading$ = this.cartService.getLoaded().pipe(map(loaded => !loaded));

    this.cartVoucherService.resetAddVoucherProcessingState();
  }

  setSubmitDisabled() {
    this.submitDisabled$ = combineLatest([
      this.errorList$,
      this.cartIsLoading$,
      this.form.valueChanges.pipe(
        startWith(true),
        map(() => {
          return (
            this.form.valid &&
            this.isNotEmptyControlObject(this.getFormControlObject().value) &&
            this.errorList$.value.length === 0
          );
        })
      ),
      this.cartVoucherService.getAddVoucherResultLoading()
    ]).pipe(
      map(
        ([errorList, cartIsLoading, btnEnabled, addVoucherIsLoading]) =>
          errorList.length > 0 || cartIsLoading || !btnEnabled || addVoucherIsLoading
      )
    );
  }

  setVoucherSub() {
    this.subscription.add(
      this.cartVoucherService.getCartVouchersState().subscribe((vouchers: Voucher[]) => {
        this.appliedVouchers$.next(vouchers);
        this.triggerChanges();
      })
    );
  }

  setInputCodeSub() {
    this.subscription.add(
      this.getFormControlObject().valueChanges.subscribe(data => {
        if (this.invalidCode) {
          if (data !== this.invalidCode) {
            this.handleValueChange();
          }
        }
      })
    );
  }

  applyVoucher(ev: Event): void {
    ev.preventDefault();
    const voucherId = this.form.value.couponCode;
    this.cartVoucherService.customApiAddVoucher(voucherId).subscribe(
      response => {
        this.resetVoucherForm();
        this.getUpdatedCartVouchers();
      },
      err => {
        // hide the default global message
        this.msgService.remove(GlobalMessageType.MSG_TYPE_ERROR);
        this.handleVoucherErrors(err);
      }
    );
  }

  removeVoucher(): void {
    this.resetVoucherForm();
    this.getUpdatedCartVouchers();
  }

  getUpdatedCartVouchers(): void {
    this.cartService.reloadActiveCart();
  }

  resetVoucherForm(): void {
    this.form.reset();
    this.cartVoucherService.resetAddVoucherProcessingState();
    this.errorList$.next([]);
    this.invalidCode = undefined;
    this.setInvalidCodeValidator();
  }

  checkResetState(appliedVouchers: Voucher[]): void {
    if (appliedVouchers.length === 0) {
      this.resetVoucherForm();
    }
  }

  handleVoucherErrors(err: any): void {
    if (err.error.errors.length > 0) {
      // has errors
      this.errorList$.next(err.error.errors as IVoucherError[]);
      if (
        this.errorList$.value[0].message === 'coupon.invalid.code.provided' ||
        this.errorList$.value[0].message === 'coupon.already.exists.cart'
      ) {
        // set the code as an invalid pattern
        this.invalidCode = this.getFormControlObject().value;
        this.setInvalidCodeValidator(this.invalidCode);
      }
    } else {
      this.form.updateValueAndValidity();
    }
  }

  setInvalidCodeValidator(isInvalidCode?: string): void {
    let validators = [];
    const couponFormControl = this.getFormControlObject();
    // reject an invalid code for this inpuit field
    if (!isUndefined(isInvalidCode)) {
      const regexPattern = new RegExp('/^(?!' + isInvalidCode + ').*$', 'g');
      validators.push(Validators.pattern(regexPattern));
    }
    couponFormControl.setValidators(validators);
    couponFormControl.updateValueAndValidity();
  }

  handleValueChange(): void {
    const couponFormControl = this.getFormControlObject();
    if (couponFormControl.hasError('pattern')) {
      // remove  the pattern validator and reset the state
      this.errorList$.next([]);
      this.setInvalidCodeValidator();
    }
  }

  private isNotEmptyControlObject(formObjectValue: string | null): boolean {
    return formObjectValue !== '' && formObjectValue !== null;
  }

  private getFormControlObject(): AbstractControl {
    return this.form.get('couponCode');
  }

  private triggerChanges(): void {
    if (!this.cd['destroyed']) {
      this.cd.detectChanges();
    }
  }

  private destroyGetVouchersSub(): void {
    if (this.getVouchersSub) {
      this.getVouchersSub.unsubscribe();
    }
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
    this.destroyGetVouchersSub();
    this.cartVoucherService.resetAddVoucherProcessingState();
  }
}
