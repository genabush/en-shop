import {
  Component,
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  ViewChild,
  ElementRef,
  HostListener,
  HostBinding,
  OnDestroy,
  OnInit
} from '@angular/core';
import { Router } from '@angular/router';

// spartacus
import { CmsComponentData, ModalService, ICON_TYPE } from '@spartacus/storefront';
import { CustomCartService } from 'src/app/services/cart/facade/cart.service';

// components
import { CustomMiniCartDirective } from 'src/app/directives/custom-mini-cart/custom-mini-cart.directive';
import { WindowRef, Cart } from '@spartacus/core';

@Component({
  selector: 'cx-mini-cart',
  templateUrl: './custom-mini-cart.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CustomMiniCartComponent implements OnInit, OnDestroy {
  iconTypes = ICON_TYPE;
  @HostBinding('attr.appCustomMiniCart')
  appCustomCartDirective: CustomMiniCartDirective = new CustomMiniCartDirective(
    this.elRef,
    this.data,
    this.router,
    this.cartService
  );
  @ViewChild('miniCartModal', { static: false }) miniCartModal: ElementRef;
  minicartModalClass = 'minicart-modal-open';
  constructor(
    public data: CmsComponentData<any>,
    private cd: ChangeDetectorRef,
    private elRef: ElementRef,
    private router: Router,
    private cartService: CustomCartService,
    private modalService: ModalService,
    private windowRef: WindowRef
  ) {}

  @HostListener('window:resize', ['$event'])
  resizeListener() {
    this.appCustomCartDirective.recalcMiniCartPos();
  }

  @HostListener('CartOpenModalEvent', ['$event'])
  openModalEvent() {
    this.appCustomCartDirective.openModalRef = this.modalService.open(
      this.miniCartModal,
      this.appCustomCartDirective.miniCartModalOptions
    );

    this.appCustomCartDirective.setModalRightPos(this.appCustomCartDirective.getDocumentRight());
    this.toggleBodyClass(this.minicartModalClass, true);

    this.appCustomCartDirective.openModalRef.result.then(
      (result: any) => {
        // Modal close programatically;
        this.appCustomCartDirective.isModalOpen = false;
        this.toggleBodyClass(this.minicartModalClass, false);
      },
      result => {
        // Modal closed by user action
        this.appCustomCartDirective.isModalOpen = false;
        this.toggleBodyClass(this.minicartModalClass, false);
      }
    );
  }

  @HostListener('ChangeDetectedEvent', ['$event'])
  changeDetectedEvent() {
    if (!this.cd['destroyed']) {
      this.cd.detectChanges();
    }
  }

  ngOnInit() {
    this.appCustomCartDirective.ngOnInit();
  }

  getAllPromotionsForCart(cart: Cart): any[] {
    return this.cartService.getAllPromotions(cart);
  }

  ngOnDestroy() {
    this.appCustomCartDirective.ngOnDestroy();
    this.cd.detach();
  }
  toggleBodyClass(className: string, add: boolean) {
    add
      ? this.windowRef.document.body.classList.add(className)
      : this.windowRef.document.body.classList.remove(className);
  }
}
