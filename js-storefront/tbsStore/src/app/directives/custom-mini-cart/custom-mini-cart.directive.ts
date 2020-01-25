import { Directive, ElementRef, OnDestroy, OnInit } from '@angular/core';
import { Router, NavigationEnd, RouterEvent } from '@angular/router';

// vendor
import { NgbModalRef, NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { of, Subscription, Observable } from 'rxjs';
import { take } from 'rxjs/operators';

// spartacus
import { CmsComponentData } from '@spartacus/storefront';
import { OrderEntry, Cart } from '@spartacus/core';

// services
import { CustomCartService } from '../../services/cart/facade/cart.service';

@Directive({
  selector: '[appCustomMiniCart]'
})
export class CustomMiniCartDirective implements OnInit, OnDestroy {
  cmsDataSub: Subscription;
  activeCartSub: Subscription;
  cartEntriesSub: Subscription;
  routerSub: Subscription;
  quantity$: number;
  isModalOpen = false;
  openModalRef: NgbModalRef;
  modalEntriesMax: number;
  cartUrl = '/cart';
  miniCartModalOptions: NgbModalOptions = {
    backdropClass: 'modal-backdrop-transparent',
    windowClass: 'mini-cart-modal d-flex',
    ariaLabelledBy: 'modal-mini-cart'
  };
  public cartResults: Observable<Cart>;
  public results$: Observable<OrderEntry[]>;
  constructor(
    private elRef: ElementRef,
    private data: CmsComponentData<any>,
    private router: Router,
    private cartService: CustomCartService
  ) {
    this.setCmsDataSubscription();
  }
  ngOnInit() {
    this.setActiveCartSub();
    this.setRouterSubscription();
  }

  setCmsDataSubscription() {
    this.cmsDataSub = this.data.data$.pipe(take(1)).subscribe(res => {
      this.modalEntriesMax = Number(res.shownProductCount);
    });
  }

  setRouterSubscription() {
    this.routerSub = this.router.events.subscribe((ev: RouterEvent) => {
      if (ev instanceof NavigationEnd) {
        this.checkMiniCartClosed();
      }
    });
  }

  setActiveCartSub() {
    this.activeCartSub = this.cartService.getActive().subscribe((cartResults: Cart) => {
      this.quantity$ = cartResults.totalItems;

      this.cartResults = of(cartResults);

      if (!this.cartEntriesSub) {
        this.setCartEntriesSub();
      }

      if (this.isNotEmptyCart()) {
        this.checkMiniCartClosed();
      }
      this.emitDataEvent('ChangeDetectedEvent');
    });
  }

  setCartEntriesSub() {
    this.cartEntriesSub = this.cartService.getEntries().subscribe((cartEntries: OrderEntry[]) => {
      const sortedEntries = this.sortCartEntries(cartEntries);
      this.setSlicedCartEntries(sortedEntries);
    });
  }

  recalcMiniCartPos() {
    if (this.isModalOpen) {
      this.setModalRightPos(this.getDocumentRight());
    }
  }

  miniCartHover(ev: MouseEvent) {
    if (this.isNotEmptyCart()) {
      if (!this.isModalOpen) {
        this.openModal();
      }
    }
  }

  miniCartClick(ev: MouseEvent) {
    ev.preventDefault();
    const triggerId = (ev.currentTarget as HTMLElement).id;
    if (triggerId === 'mini-cart-mobile-trigger') {
      if (this.isNotEmptyCart()) {
        this.navigateToBasketPage();
      }
    } else if (triggerId === 'mini-cart-desktop-trigger') {
      if (this.isNotEmptyCart()) {
        this.checkMiniCartClosed();
        this.navigateToBasketPage();
      }
    }
  }

  sortCartEntries(cartEntries: OrderEntry[]) {
    const sortedEntries = cartEntries.sort((entryItemA: OrderEntry, entryItemB: OrderEntry) => {
      if (entryItemA.entryNumber > entryItemB.entryNumber) {
        return 1;
      }
      if (entryItemA.entryNumber < entryItemB.entryNumber) {
        return -1;
      }
      return 0;
    });
    return sortedEntries;
  }

  setSlicedCartEntries([...sortedEntries]: OrderEntry[]) {
    const slicedEntries =
      sortedEntries.length > this.modalEntriesMax
        ? sortedEntries.splice(sortedEntries.length - 3, this.modalEntriesMax)
        : sortedEntries;

    this.results$ = of(slicedEntries.reverse());
    this.emitDataEvent('ChangeDetectedEvent');
  }

  getDocumentRight() {
    const documentAnchor: HTMLElement = this.elRef.nativeElement.querySelectorAll('a.mini-cart--desktop').item(0);
    if (documentAnchor) {
      const anchorRect = documentAnchor.getBoundingClientRect();
      const windowRight = window.innerWidth - (anchorRect.left + anchorRect.width);
      return windowRight;
    }
    return undefined;
  }

  setModalRightPos(rightPosition: number) {
    if (rightPosition) {
      const modalElement: any = document.querySelectorAll('.modal-content').item(0);
      const pageGutter = 5;
      if (modalElement != null) {
        modalElement.style.marginRight = rightPosition + pageGutter + 'px';
      }
    }
  }

  openModal() {
    if (!this.isModalOpen && !this.isOnCartPage()) {
      this.isModalOpen = true;
      this.emitDataEvent('CartOpenModalEvent');
    }
  }

  checkMiniCartClosed() {
    if (this.isModalOpen) {
      this.openModalRef.close();
    }
  }

  navigateToBasketPage() {
    if (!this.isOnCartPage()) {
      this.router.navigateByUrl(this.cartUrl);
    }
  }

  emitDataEvent(eventTitle: string, eventData?: any) {
    const event: CustomEvent = new CustomEvent(eventTitle, {
      bubbles: true,
      cancelable: true,
      detail: eventData || null
    });
    this.elRef.nativeElement.dispatchEvent(event);
  }

  private isNotEmptyCart() {
    return this.quantity$ && this.quantity$ > 0;
  }

  private isOnCartPage() {
    return this.router.url.indexOf('cart') > -1;
  }

  destroyCmsDataSubscription() {
    if (this.cmsDataSub) {
      this.cmsDataSub.unsubscribe();
    }
  }

  destroyRouterSubscription() {
    if (this.routerSub) {
      this.routerSub.unsubscribe();
    }
  }

  destroyCartSubscription() {
    if (this.cartEntriesSub) {
      this.cartEntriesSub.unsubscribe();
    }
  }

  destroyDataSubscription() {
    if (this.activeCartSub) {
      this.activeCartSub.unsubscribe();
    }
  }

  ngOnDestroy() {
    this.destroyCmsDataSubscription();
    this.destroyRouterSubscription();
    this.destroyDataSubscription();
    this.destroyCartSubscription();
  }
}
