import { Directive, HostListener, AfterViewInit, OnDestroy, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';

// vendor
import { timer, Subscription } from 'rxjs';
import { takeWhile, tap } from 'rxjs/operators';

// services
import { AppCustomStorageService } from '../../services/product/web-storage.service';

// interfaces
import { IPLPScrollIndex } from '../../interfaces/custom-product-item.interface';

// constants
const STORAGE_KEY = 'plpScroll';

@Directive({
  selector: '[appCustomProductListScroll]'
})
export class CustomProductListScrollDirective implements OnInit, AfterViewInit, OnDestroy {
  isScrolling = false;
  scrollingSub: Subscription;
  routeSub: Subscription;
  scrollStartTop = 230; // set to the nav end
  constructor(private sessionStorage: AppCustomStorageService, private router: Router) {}

  ngOnInit() {
    this.routeSub = this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.checkScrollTarget();
      }
    });
  }

  ngAfterViewInit() {
    this.checkScrollTarget();
  }

  @HostListener('window:scroll', ['$event'])
  scrollHandler(event: Event) {
    const scrollPos = this.getDocumentScrollTop();
    const currentScrollArray: IPLPScrollIndex[] | null = this.sessionStorage.getSessionStorageItem(STORAGE_KEY);

    if (!this.isScrolling && scrollPos > this.scrollStartTop) {
      this.handleScrollAdd(currentScrollArray, scrollPos);
    } else {
      if (currentScrollArray !== null && scrollPos <= this.scrollStartTop) {
        this.handleScrollRemove(currentScrollArray);
      }
    }
  }

  handleScrollAdd(currentScrollArray: IPLPScrollIndex[], scrollPos: number) {
    const searchPage = this.getUrlParameter('currentPage', window.location.search);

    const tempScrollIndex: IPLPScrollIndex = {
      scrollPos: scrollPos,
      pageUrl: window.location.pathname,
      currentPage: searchPage !== null ? Number(searchPage) : null
    };

    if (currentScrollArray === null || currentScrollArray.length === 0) {
      this.setLocalStoreScroll([tempScrollIndex]);
    } else {
      const foundItemIndex: number | false = this.findScrollItem(currentScrollArray);
      if (typeof foundItemIndex === 'number') {
        currentScrollArray[foundItemIndex].scrollPos = scrollPos;
      } else {
        currentScrollArray.push(tempScrollIndex);
      }
      this.setLocalStoreScroll(currentScrollArray);
    }
  }

  getUrlParameter(name: string, searchString: string) {
    name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
    const regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
    const results = regex.exec(searchString);
    return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
  }

  handleScrollRemove(currentScrollArray: IPLPScrollIndex[]) {
    if (currentScrollArray.length !== 0) {
      const foundItemIndex: number | false = this.findScrollItem(currentScrollArray);
      if (typeof foundItemIndex === 'number') {
        currentScrollArray.splice(foundItemIndex, 1);
        this.setLocalStoreScroll(currentScrollArray);
      }
    }
  }

  findScrollItem(currentScrollArray: IPLPScrollIndex[]): number | false {
    let itemIndex = 0;
    if (currentScrollArray.length > 0) {
      for (const scrollItem of currentScrollArray) {
        const currentPage = this.getUrlParameter('currentPage', window.location.search);
        if (scrollItem.pageUrl === window.location.pathname) {
          // check same page
          if (currentPage !== null && currentPage !== '') {
            if (Number(scrollItem.currentPage) === Number(currentPage)) {
              return itemIndex;
            }
          } else if (currentPage === '') {
            return itemIndex;
          }
        }
        itemIndex++;
      }
    }
    return false;
  }

  checkScrollTarget(): void {
    window.scrollTo(0, 0); // always start from the top
    const scrollTarget: IPLPScrollIndex[] | null = this.sessionStorage.getSessionStorageItem(STORAGE_KEY);
    const currentUrl = window.location.pathname;
    if (scrollTarget !== null) {
      if (scrollTarget.length > 0) {
        for (const scrollIndex of scrollTarget) {
          if (scrollIndex.pageUrl === currentUrl) {
            const currentPage = this.getUrlParameter('currentPage', window.location.search);
            if (currentPage !== '') {
              if (scrollIndex.currentPage === Number(currentPage)) {
                this.scrollToPageTarget(500, scrollIndex.scrollPos);
              }
            } else {
              this.scrollToPageTarget(500, scrollIndex.scrollPos);
            }
          }
        }
      }
    }
  }

  getDocumentScrollTop(): number {
    return Math.max(window.pageYOffset, document.documentElement.scrollTop, document.body.scrollTop);
  }

  setLocalStoreScroll(targetScroll: object) {
    this.sessionStorage.setSessionStorageItem(STORAGE_KEY, targetScroll);
  }

  scrollToPageTarget(scrollDuration: number, scrollTarget: number) {
    const scrollStart = this.getDocumentScrollTop();
    let scrollingTarget: number = scrollStart;
    const scrollSteps = this.calcScrollSteps(scrollDuration, scrollStart, scrollTarget);
    this.isScrolling = true;

    this.scrollingSub = timer(scrollDuration, 0)
      .pipe(
        takeWhile(() => scrollingTarget <= scrollTarget),
        tap(() => {
          scrollingTarget += scrollSteps;
        })
      )
      .subscribe(() => {
        window.scrollTo(0, scrollingTarget);
        if (scrollingTarget >= scrollTarget) {
          this.isScrolling = false;
          this.destroyTimerSubscription();
        }
      });
  }

  calcScrollSteps(scrollDuration: number, scrollStart: number, scrollTarget: number) {
    return Math.ceil((scrollTarget - scrollStart) / (scrollDuration / 10));
  }

  destroyTimerSubscription() {
    if (this.scrollingSub) {
      this.scrollingSub.unsubscribe();
    }
  }

  destroyRouteSubscription() {
    if (this.routeSub) {
      this.routeSub.unsubscribe();
    }
  }

  ngOnDestroy() {
    this.destroyTimerSubscription();
    this.destroyRouteSubscription();
  }
}
