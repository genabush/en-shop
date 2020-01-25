import { Component, OnInit, Input, OnDestroy, ElementRef, ViewChild, TemplateRef } from '@angular/core';
import { ICON_TYPE, ModalService, ModalRef } from '@spartacus/storefront';
import './notify-model';
import { Notify } from './notify-model';

import { Observable, Subscription } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BaseSiteService } from '@spartacus/core';
import { OccService } from 'src/app/services/occ/occ.service';
import { BackInStockSuccessComponent } from '../back-in-stock-success/back-in-stock-success.component';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
@Component({
  selector: 'app-back-in-stock',
  templateUrl: './back-in-stock.component.html'
})
export class BackInStockComponent implements OnInit, OnDestroy {
  @Input() productCode: string;
  baseSiteSub: Subscription;
  newNotificationSub: Subscription;
  iconTypes = ICON_TYPE;
  submitted = false;
  base: string;
  baseUrl: string;
  privacyUrl = '/privacy-policy';
  model = new Notify('', '');
  notificationUrl = 'products/createEmailWhenInStock';
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: 'my-auth-token'
    })
  };
  backInStockModalRef: NgbModalRef;
  openModalRef: NgbModalRef;
  constructor(
    private modalService: ModalService,
    private http: HttpClient,
    private baseSiteService: BaseSiteService,
    private occService: OccService
  ) {
    this.setBaseSiteSubscription();
  }

  onSubmitEvent(ev: any) {
    this.baseUrl = this.getOCCServiceBaseUrl();
    this.addNewNotification(ev);
  }

  ngOnInit() {
    // this.model.productCode = this.productCode;
  }

  openBackInStockModal(content): void {
    this.model.productCode = this.productCode;
    this.backInStockModalRef = this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' });
  }

  addNewNotification(notification: Notify): void {
    this.newNotificationSub = this.http.post<Notify>(this.getFullUrl(), notification, this.httpOptions).subscribe(
      () => {
        this.closeBackInStockModal();
        this.showSuccessModal();
        this.submitted = true;
        this.model.emailId = '';
        this.model.productCode = '';
      },
      err => {
        this.destroyNewNotificationSub();
      }
    );
  }

  showSuccessModal(): void {
    this.destroyNewNotificationSub();
    this.modalService.closeActiveModal();
    this.openModalRef = this.modalService.open(BackInStockSuccessComponent, {
      centered: true,
      size: 'lg'
    });
  }

  closeBackInStockModal() {
    if (this.backInStockModalRef) {
      this.backInStockModalRef.close();
    }
  }

  closeSuccessModal() {
    if (this.openModalRef) {
      this.openModalRef.close();
    }
  }

  getFullUrl() {
    return this.baseUrl + '/rest/v2/' + this.base + '/' + this.notificationUrl;
  }

  setBaseSiteSubscription() {
    this.baseSiteSub = this.baseSiteService.getActive().subscribe(data => {
      this.base = data;
    });
  }

  getOCCServiceBaseUrl(): string {
    return this.occService.getBaseUrl();
  }

  destroySetBaseSiteSubscription() {
    if (this.baseSiteSub) {
      this.baseSiteSub.unsubscribe();
    }
  }

  destroyNewNotificationSub() {
    if (this.newNotificationSub) {
      this.newNotificationSub.unsubscribe();
    }
  }

  ngOnDestroy(): void {
    //Called once, before the instance is destroyed.
    //Add 'implements OnDestroy' to the class.
    this.destroySetBaseSiteSubscription();
    this.destroyNewNotificationSub();
    this.closeSuccessModal();
  }
}
