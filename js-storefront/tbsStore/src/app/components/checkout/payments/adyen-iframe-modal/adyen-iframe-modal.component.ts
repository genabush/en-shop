import { Component, OnDestroy, ViewChild, ElementRef, AfterViewInit, HostListener } from '@angular/core';
import { CustomCheckoutService } from '../../../../services/checkout/custom-checkout.service';
import { Subscription } from 'rxjs';
import { PaymentRedirect } from '../../../../interfaces/payment-details-response.interface';

@Component({
  selector: 'app-adyen-iframe-modal',
  templateUrl: './adyen-iframe-modal.component.html',
  styleUrls: ['./adyen-iframe-modal.component.scss']
})
export class AdyenIframeModalComponent implements OnDestroy, AfterViewInit {
  @ViewChild('iframe', { static: false }) iframe: ElementRef;

  private subscription = new Subscription();
  private iframeHandler: Document;

  constructor(private customCheckoutService: CustomCheckoutService) {}

  @HostListener('window:message', ['$event'])
  onMessage(e) {
    if (e.data.for === 'auth') {
      this.customCheckoutService.closeModal$.next(Math.random());
    }
  }

  ngAfterViewInit(): void {
    this.iframeHandler = this.iframe.nativeElement.contentWindow.document;
    this.subscription.add(
      this.customCheckoutService.adyenModalConfig$.subscribe((config: PaymentRedirect) =>
        this.createForm(this.iframeHandler, config)
      )
    );
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  private createForm(iframe: Document, config: PaymentRedirect): void {
    const mapForm = iframe.createElement('form');
    mapForm.target = '_self';
    mapForm.method = 'POST';
    mapForm.action = config.redirectUrl;
    config.data.entry.forEach(param => {
      const mapInput = iframe.createElement('input');
      mapInput.type = 'text';
      mapInput.setAttribute('name', param.key);
      mapInput.setAttribute('value', param.value);
      mapForm.appendChild(mapInput);
    });
    iframe.body.appendChild(mapForm);
    mapForm.submit();
  }
}
