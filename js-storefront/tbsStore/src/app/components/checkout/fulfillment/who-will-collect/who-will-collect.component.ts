/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
import { Component, OnInit, ViewChild, ElementRef, Output, EventEmitter, Input } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

// spartacus
import { ICON_TYPE, ModalService } from '@spartacus/storefront';
import { WindowRef, LanguageService } from '@spartacus/core';

// vendor
import { NgbModalOptions, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { assign, isEmpty, isUndefined, cloneDeep } from 'lodash';
import { Subscription, BehaviorSubject } from 'rxjs';

// services
import { CollectionPointsService } from 'src/app/services/checkout/collection-points/collection-points.service';
import { CheckoutCollectInStoreService } from 'src/app/services/checkout/collect-in-store/collect-in-store.service';

// interfaces
import { CollectorDetails, ICheckoutCollectionData } from 'src/app/interfaces/collection-point.interface';
import { MARKET_FIELD_VALIDATORS, CheckoutFulfillmentTabs } from 'src/app/interfaces/custom-checkout.interface';

@Component({
  selector: 'cx-who-will-collect',
  templateUrl: './who-will-collect.component.html'
})
export class WhoWillCollectComponent implements OnInit {
  @ViewChild('whoWillCollectModal', { static: false }) whoWillCollectModal: ElementRef;
  @Input() checkoutJourneyType: number;
  @Output() whoWillCollectEmit: EventEmitter<any> = new EventEmitter<any>();
  collectionPersonalDetails: FormGroup = this.getCollectionPersonalDetails();
  iconTypes = ICON_TYPE;
  modalOpenClass = 'checkout-modal-open';
  openModalRef: NgbModalRef;
  marketFieldValidators = MARKET_FIELD_VALIDATORS;
  currentSiteLanguage: string;
  subscription = new Subscription();
  collectionData: BehaviorSubject<ICheckoutCollectionData> = new BehaviorSubject<ICheckoutCollectionData>(undefined);
  whoWillCollectDetails: CollectorDetails;
  checkoutFulfillmentTabs = CheckoutFulfillmentTabs;
  private whoWillCollectModalOptions: NgbModalOptions = {
    windowClass: 'who-will-collect-modal modal-md d-flex',
    ariaLabelledBy: 'modal-checkout-who-will-collect'
  };
  constructor(
    private fb: FormBuilder,
    private windowRef: WindowRef,
    private modalService: ModalService,
    private collectionPointsService: CollectionPointsService,
    private collectInStoreService: CheckoutCollectInStoreService,
    private languageService: LanguageService
  ) {
    this.subscription.add(
      this.languageService
        .getActive()
        .pipe()
        .subscribe(language => (this.currentSiteLanguage = language))
    );
  }

  ngOnInit() {
    this.updateValidationRules();
    this.applyTabbedSubscription();
  }

  applyTabbedSubscription() {
    if (this.checkoutJourneyType === this.checkoutFulfillmentTabs.COLLECT_IN_STORE) {
      this.collectInStoreSub();
    } else if (this.checkoutJourneyType === this.checkoutFulfillmentTabs.COLLECTION_POINT) {
      this.collectionPointSub();
    }
  }

  collectInStoreSub() {
    this.collectInStoreService.getSelectedCollectionStore().subscribe((collectionData: ICheckoutCollectionData) => {
      if (!isEmpty(collectionData)) {
        this.collectionData.next(collectionData);
        this.checkExistingPersonalDetails(this.collectionData.value.address);
      }
    });
  }

  collectionPointSub() {
    this.collectionPointsService.getSelectedCollectionPoint().subscribe((collectionData: ICheckoutCollectionData) => {
      if (!isEmpty(collectionData)) {
        this.collectionData.next(collectionData);
        this.checkExistingPersonalDetails(this.collectionData.value.address);
      }
    });
  }

  checkExistingPersonalDetails(addressItem?: CollectorDetails) {
    if (this.hasExistingDetails(addressItem)) {
      this.collectionPersonalDetails.patchValue({
        firsName: addressItem.firstName,
        lastName: addressItem.lastName,
        cellphone: addressItem.cellphone
      });
      this.whoWillCollectDetails = cloneDeep(addressItem);
      this.whoWillCollectEmit.emit(true);
    }
  }

  getCollectionPersonalDetails() {
    return this.fb.group({
      firstName: '',
      lastName: '',
      cellphone: ''
    });
  }

  updateValidationRules() {
    const marketPattern = this.getPhoneNumberPatternByMarket();
    this.collectionPersonalDetails.get('cellphone').setValidators([Validators.pattern(marketPattern)]);
  }

  getPhoneNumberPatternByMarket(): string {
    return this.marketFieldValidators[this.currentSiteLanguage]['phone'].pattern;
  }

  closeWhoWillCollectModal() {
    if (this.openModalRef) {
      this.openModalRef.close();
    }
  }

  openWhoWillCollectModal(): void {
    this.windowRef.nativeWindow.document.body.classList.add(this.modalOpenClass);
    this.openModalRef = this.modalService.open(this.whoWillCollectModal, this.whoWillCollectModalOptions);
    this.openModalRef.result.then(
      () => {
        this.windowRef.nativeWindow.document.body.classList.remove(this.modalOpenClass);
      },
      () => {
        this.windowRef.nativeWindow.document.body.classList.remove(this.modalOpenClass);
      }
    );
  }

  submitForm($ev: Event): void {
    $ev.preventDefault();
    const formData = this.collectionPersonalDetails.getRawValue();
    this.assignAddressFormData(formData);
    if (this.checkoutJourneyType === this.checkoutFulfillmentTabs.COLLECT_IN_STORE) {
      this.submitCollectInStore();
    } else if (this.checkoutJourneyType === this.checkoutFulfillmentTabs.COLLECTION_POINT) {
      this.submitCollectionPoint();
    }
  }

  submitCollectInStore() {
    this.collectInStoreService.postCollectionStoreCollector(this.collectionData.value.address).subscribe(response => {
      this.collectionDetailsSubmittedSuccess(response);
    });
  }

  assignAddressFormData(formData) {
    this.whoWillCollectDetails = assign(this.collectionData.value.address, formData);
    this.collectionData.next(assign(this.collectionData.value, { address: this.whoWillCollectDetails }));
  }

  submitCollectionPoint() {
    this.collectionPointsService.postCollectionPointCollector(this.collectionData.value.address).subscribe(response => {
      this.collectionDetailsSubmittedSuccess(response);
    });
  }

  collectionDetailsSubmittedSuccess(response) {
    if (response.code === 'success') {
      this.whoWillCollectDetails = this.collectionData.value.address;
      this.whoWillCollectEmit.emit(true);
      this.closeWhoWillCollectModal();
    }
  }

  hasExistingDetails(addressItem: CollectorDetails) {
    return (
      !isUndefined(addressItem.firstName) && !isUndefined(addressItem.lastName) && !isUndefined(addressItem.cellphone)
    );
  }
}
