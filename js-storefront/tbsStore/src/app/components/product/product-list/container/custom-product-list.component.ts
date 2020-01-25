import { WishlistService } from './../../../../services/wishlist/wishlist.service';
import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';

// spartacus
import { ProductSearchPage, RoutingService } from '@spartacus/core';
import { PageLayoutService, ViewModes } from '@spartacus/storefront';

// vendor
import { BehaviorSubject, Observable, Subscription, of } from 'rxjs';
import { take } from 'rxjs/operators';

// services
import { CustomProductListComponentService } from './custom-product-list-component.service';
import { ContentPageSearchService } from 'src/app/services/content-page-search/content-page-search.service';

@Component({
  selector: 'app-custom-product-list',
  templateUrl: './custom-product-list.component.html',
  styleUrls: ['./custom-product-list.component.scss']
})
export class CustomProductListComponent implements OnInit, OnDestroy {
  model$: Observable<ProductSearchPage> = this.productListComponentService.model$;
  viewMode$ = new BehaviorSubject<ViewModes>(ViewModes.Grid);
  ViewModes = ViewModes;
  contentPages$: Observable<any>;
  pages: Subscription;
  navSub: Subscription;
  searhContentSub: Subscription;
  modelSub: Subscription;
  wishlistSub: Subscription;
  userWishlists$: Observable<any>;

  first: any[] = [];
  firstValue: boolean;
  wishlistFirstSub: Subscription;
  isUserLogged: boolean;

  constructor(
    private pageLayoutService: PageLayoutService,
    private productListComponentService: CustomProductListComponentService,
    private contentPageSearchService: ContentPageSearchService,
    private routing: RoutingService,
    private cd: ChangeDetectorRef,
    private wishlistService: WishlistService
  ) {
    this.isUserLogged = this.wishlistService.isUserLogged;

    if (this.isUserLogged) {
      // logged
      this.wishlistService.initAllWishlists();
    } else {
      // guest
      this.wishlistFirstSub = this.wishlistService.getFirstTimer().subscribe(val => {
        let subLength = this.first.length;
        this.firstValue = val;
        if (!val) {
          // If it's not a first time guest
          if (subLength === 1) {
            // should overwrite existing value instead of adding
            this.first = [];
          }
        }
        this.first.push(this.firstValue);
      });
    }
  }

  ngOnInit() {
    this.productListComponentService.clearSearchResults();
    this.pageLayoutService.templateName$.pipe(take(1)).subscribe(template => {
      this.viewMode$.next(
        // template === 'ProductGridPageTemplate' ? ViewModes.Grid : ViewModes.List
        ViewModes.Grid
      );
    });
    this.setNavSubscriptionForSearch();
  }

  setNavSubscriptionForSearch() {
    this.navSub = this.routing.isNavigating().subscribe(data => {
      this.modelSub = this.model$.pipe().subscribe(val => {
        this.searhContentSub = this.contentPageSearchService.searchContentPages().subscribe(contentPagesResponse => {
          this.contentPages$ = of(contentPagesResponse);
          this.cd.detectChanges();
          this.destroyModelSubscription();
          this.destroySearchSubscription();
        });
      });
    });
  }
  viewPage(pageNumber: number): void {
    this.productListComponentService.viewPage(pageNumber);
  }

  sortList(sortCode: string): void {
    this.productListComponentService.sort(sortCode);
  }

  setViewMode(mode: ViewModes): void {
    this.viewMode$.next(mode);
  }

  destroyNavSubscription() {
    if (this.navSub) {
      this.navSub.unsubscribe();
    }
  }
  destroySearchSubscription() {
    if (this.searhContentSub) {
      this.searhContentSub.unsubscribe();
    }
  }
  destroyModelSubscription() {
    if (this.modelSub) {
      this.modelSub.unsubscribe();
    }
  }
  destroyWishlistFirstSubscription() {
    if (this.wishlistFirstSub) {
      this.wishlistFirstSub.unsubscribe();
    }
  }

  ngOnDestroy() {
    this.destroySearchSubscription();
    this.destroyNavSubscription();
    this.destroyModelSubscription();
    this.destroyWishlistFirstSubscription();
  }
}
