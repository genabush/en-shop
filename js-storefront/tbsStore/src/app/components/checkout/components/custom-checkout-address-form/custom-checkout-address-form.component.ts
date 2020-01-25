/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
  ChangeDetectorRef
} from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatCheckboxChange } from '@angular/material';

// vendor
import { Observable, Subscription, BehaviorSubject, of } from 'rxjs';
import { tap, switchMap } from 'rxjs/operators';

// spartacus
import {
  AddressValidation,
  Country,
  GlobalMessageService,
  Region,
  Title,
  UserService,
  UserPaymentService,
  LanguageService,
  TranslationService
} from '@spartacus/core';
import { ModalRef, ModalService, SuggestedAddressDialogComponent, ICON_TYPE } from '@spartacus/storefront';

// services
import { CustomCheckoutDeliveryService } from 'src/app/services/checkout/delivery/custom-checkout-delivery.service';
import { CustomUserAddressService } from 'src/app/services/checkout/custom-user-address.service';

// interfaces
import {
  ICustomAddress,
  MARKET_FIELD_VALIDATORS,
  DEFAULT_ADDRESS_MAXLENGTHS,
  PHONE_NUMBER_PATTERNS
} from 'src/app/interfaces/custom-checkout.interface';

@Component({
  selector: 'cx-custom-address-form',
  templateUrl: './custom-checkout-address-form.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CustomCheckoutAddressFormComponent implements OnInit, OnDestroy {
  countries$: Observable<Country[]> = of([]);
  titles$: Observable<Title[]>;
  regions$: Observable<Region[]>;
  currentSiteLanguage: string;
  selectedCountry$: BehaviorSubject<string> = new BehaviorSubject<string>('');
  selectedCountryTitle$: BehaviorSubject<string> = new BehaviorSubject<string>('');
  iconTypes = ICON_TYPE;
  @Input() addressFormType: string;
  @Input() addressData: ICustomAddress;
  @Input() actionBtnLabel: string;
  @Input() cancelBtnLabel: string;
  @Input() setAsDefaultField: boolean;
  @Input() showTitleCode: boolean;
  @Input() showCancelBtn = true;
  @Input() isEditing: boolean;
  @Output() submitAddress = new EventEmitter<any>();
  @Output() backToAddress = new EventEmitter<any>();
  addressVerifySub: Subscription;
  suggestedAddressModalRef: ModalRef;
  address: FormGroup = this.getAddressFormGroup();
  manualAddressEntry = true; // TODO set to initially false in Address Search implementation
  verificationError: string;
  marketFieldValidators = MARKET_FIELD_VALIDATORS;
  phoneNumberPatterns = PHONE_NUMBER_PATTERNS;
  subscription = new Subscription();
  translateSub: Subscription;
  public maxLengthObject = {
    line1: DEFAULT_ADDRESS_MAXLENGTHS.line1.maxlength,
    line2: DEFAULT_ADDRESS_MAXLENGTHS.line2.maxlength,
    city: DEFAULT_ADDRESS_MAXLENGTHS.city.maxlength,
    postalCode: DEFAULT_ADDRESS_MAXLENGTHS.postcode.maxlength,
    phone: DEFAULT_ADDRESS_MAXLENGTHS.phone.maxlength
  };
  constructor(
    private fb: FormBuilder,
    protected checkoutDeliveryService: CustomCheckoutDeliveryService,
    private userPaymentService: UserPaymentService,
    protected userService: UserService,
    protected userAddressService: CustomUserAddressService,
    protected globalMessageService: GlobalMessageService,
    private modalService: ModalService,
    private languageService: LanguageService,
    private translationService: TranslationService,
    private cd: ChangeDetectorRef
  ) {
    this.subscription.add(
      this.languageService
        .getActive()
        .pipe()
        .subscribe(language => (this.currentSiteLanguage = language))
    );
  }

  ngOnInit() {
    // Fetching countries
    this.setCountriesSub();

    // Fetching titles
    this.setTitlesSub();

    // Fetching regions
    this.setRegionsSub();

    // verify the new added address
    this.setVerifyAddressSub();

    this.setSelectedAddressData();
  }

  setSelectedAddressData() {
    if (this.addressData) {
      this.address.patchValue(this.addressData);
      this.countrySelected({ value: this.addressData.country });
      if (this.addressData.region) {
        this.regionSelected(this.addressData.region);
      }
    }
  }

  getAddressFormGroup() {
    return this.fb.group({
      defaultAddress: [false],
      id: [{ value: null }],
      titleCode: [''],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      companyName: [''],
      line1: ['', Validators.required],
      line2: [''],
      town: ['', Validators.required],
      region: this.fb.group({
        isocode: [null, Validators.required]
      }),
      country: this.fb.group({
        isocode: [{ value: null, readonly: true }, Validators.required]
      }),
      postalCode: ['', Validators.required],
      phone: ['']
    });
  }

  getFieldMaxLengthByMarket(fieldName: string): number {
    return this.marketFieldValidators[this.currentSiteLanguage][fieldName].maxlength;
  }

  getPhoneNumberPatternByMarket(): string {
    const siteLangSplit = this.currentSiteLanguage.split('_');
    const currentCountry = siteLangSplit.length === 1 ? siteLangSplit[0] : siteLangSplit[1];
    return this.phoneNumberPatterns[currentCountry];
    // TODO UPDATE FOR BILLING ????
  }

  private updateAddressFieldsMaxLength() {
    // set the bound max-length object
    this.maxLengthObject = {
      line1: this.getFieldMaxLengthByMarket('line1'),
      line2: this.getFieldMaxLengthByMarket('line2'),
      city: this.getFieldMaxLengthByMarket('city'),
      postalCode: this.getFieldMaxLengthByMarket('postcode'),
      phone: this.getFieldMaxLengthByMarket('phone')
    };

    // set the phone pattern by market
    const marketPattern = this.getPhoneNumberPatternByMarket();
    this.address.get('phone').setValidators([Validators.pattern(marketPattern)]);

    this.address.updateValueAndValidity();
  }

  setCountriesSub() {
    if (this.addressFormType === 'SHIPPING') {
      this.getCountriesShipping();
    } else if (this.addressFormType === 'BILLING') {
      this.getCountriesBilling();
    }
  }

  getCountriesShipping() {
    this.countries$ = this.userAddressService.getDeliveryCountries().pipe(
      tap(countries => {
        if (Object.keys(countries).length === 0) {
          this.userAddressService.loadDeliveryCountries();
        } else if (Object.keys(countries).length === 1) {
          this.countrySelected({ value: countries[0] });
          this.address
            .get('country')
            .get('isocode')
            .disable();
        } else {
          this.address
            .get('country')
            .get('isocode')
            .enable();
        }
      })
    );
  }

  getCountriesBilling() {
    this.countries$ = this.userPaymentService.getAllBillingCountries().pipe(
      tap(countries => {
        if (Object.keys(countries).length === 0) {
          this.userPaymentService.loadBillingCountries();
        } else if (Object.keys(countries).length === 1) {
          this.countrySelected({ value: countries[0] });
          this.address
            .get('country')
            .get('isocode')
            .disable();
        } else {
          this.address
            .get('country')
            .get('isocode')
            .enable();
        }
      })
    );
  }

  setTitlesSub() {
    this.titles$ = this.userService.getTitles().pipe(
      tap(titles => {
        if (Object.keys(titles).length === 0) {
          this.userService.loadTitles();
        }
      })
    );
  }

  setRegionsSub() {
    this.regions$ = this.selectedCountry$.pipe(
      switchMap(country => this.userAddressService.getRegions(country)),
      tap(regions => {
        const regionControl = this.address.get('region.isocode');
        if (regions && regions.length > 0) {
          regionControl.enable();
        } else {
          regionControl.disable();
        }
      })
    );
  }

  setVerifyAddressSub() {
    this.subscription.add(
      this.checkoutDeliveryService.getAddressVerificationResults().subscribe((results: AddressValidation) => {
        if (results.decision === 'ACCEPT') {
          this.submitAddress.emit(this.prepareRawAddress());
        } else if (results === 'FAIL') {
          this.checkoutDeliveryService.clearAddressVerificationResults();
        } else if (results.decision === 'REJECT') {
          this.handleAddressReject(results);
        } else if (results.decision === 'REVIEW') {
          this.openSuggestedAddress(results);
        }
      })
    );
  }

  handleAddressReject(results: AddressValidation) {
    // TODO: Workaround: allow server for decide is titleCode mandatory (if yes, provide personalized message)
    if (results.errors.errors.some(error => error.subject === 'titleCode')) {
      this.showVerificationError('addressForm.titleRequired');
    } else {
      if (results.errors.errors.some(error => error.message.indexOf('P.O.') > -1)) {
        this.showVerificationError('addressForm.invalidAddressPoBox');
      } else {
        this.showVerificationError('addressForm.invalidAddress');
      }
    }
    this.checkoutDeliveryService.clearAddressVerificationResults();
  }

  showVerificationError(translationKey: string) {
    this.translateSub = this.translationService.translate(translationKey).subscribe((translation: string) => {
      this.verificationError = translation;
      this.triggerChanges();
      this.destroyTranslateSub();
    });
  }

  hideVerificationError() {
    this.verificationError = undefined;
  }

  destroyTranslateSub() {
    if (this.translateSub) {
      this.translateSub.unsubscribe();
    }
  }

  triggerChanges() {
    if (!this.cd['destroyed']) {
      this.cd.detectChanges();
    }
  }

  titleSelected(event: Event): void {
    this.address.patchValue({
      titleCode: (event.target as HTMLInputElement).value
    });
  }

  countrySelected(ev: any): void {
    this.selectedCountry$.next(ev.value.isocode);
    this.selectedCountryTitle$.next(ev.value.name);
    this.updateAddressFieldsMaxLength();
  }

  regionSelected(region: Region): void {
    this.address.patchValue({
      region: {
        isocode: region.isocode
      }
    });
  }

  toggleDefaultAddress(ev: MatCheckboxChange): void {
    this.address.patchValue({
      defaultAddress: ev.checked
    });
  }

  // TODO awaiting clarification RCOM-1190
  toggleSaveInAddessBook(ev: MatCheckboxChange): void {
    this.address.patchValue({
      visibleInAddressBook: ev.checked
    });
    if (!ev.checked) {
      // tuen off and disable the setAsDefault field
      this.address.patchValue({
        defaultAddress: false
      });
      this.address.controls['defaultAddress'].disable();
    } else {
      this.address.controls['defaultAddress'].enable();
    }
  }

  back(): void {
    this.backToAddress.emit();
  }

  verifyAddress(ev: Event): void {
    ev.preventDefault();
    if (this.manualAddressEntry) {
      this.checkoutDeliveryService.verifyAddress(this.prepareRawAddress());
    } else {
      // TODO check that there is a loaded address and assign the results to FormControls
      // After update to the required form controls, verify the form
    }
  }

  prepareRawAddress() {
    const rawAddressValue = this.address.getRawValue();
    if (!this.isEditing) {
      delete rawAddressValue['id'];
    }

    rawAddressValue['country'].isocode = this.selectedCountry$.value;

    if (rawAddressValue.region.isocode === null) {
      delete rawAddressValue['region'];
    }
    return rawAddressValue;
  }

  clearAddressEntries() {
    this.address.patchValue({
      line1: '',
      line2: '',
      town: '',
      city: '',
      region: {
        isocode: ''
      },
      postalCode: ''
    });
  }

  toggleManualEntry() {
    if (this.manualAddressEntry) {
      this.manualAddressEntry = false;
    } else {
      this.manualAddressEntry = true;
    }
  }

  openSuggestedAddress(results: AddressValidation): void {
    if (!this.suggestedAddressModalRef) {
      this.suggestedAddressModalRef = this.modalService.open(SuggestedAddressDialogComponent, {
        centered: true,
        size: 'lg'
      });
      this.suggestedAddressModalRef.componentInstance.enteredAddress = this.prepareRawAddress();
      this.suggestedAddressModalRef.componentInstance.suggestedAddresses = results.suggestedAddresses;
      this.suggestedAddressModalRef.result
        .then(address => {
          this.checkoutDeliveryService.clearAddressVerificationResults();
          if (address) {
            address = Object.assign(
              {
                titleCode: this.address.value.titleCode,
                phone: this.address.value.phone,
                selected: true
              },
              this.prepareRawAddress()
            );
            this.submitAddress.emit(address);
          }
          this.suggestedAddressModalRef = null;
        })
        .catch(() => {
          // this  callback is called when modal is closed with Esc key or clicking backdrop
          this.checkoutDeliveryService.clearAddressVerificationResults();
          const address = Object.assign(
            {
              selected: true
            },
            this.prepareRawAddress()
          );
          this.submitAddress.emit(address);
          this.suggestedAddressModalRef = null;
        });
    }
  }

  doAddressSearch() {
    // TODO call to the address search endpoint and then apply results
  }

  ngOnDestroy() {
    this.verificationError = undefined;
    this.checkoutDeliveryService.clearAddressVerificationResults();
    this.subscription.unsubscribe();
    this.cd.detach();
  }
}
