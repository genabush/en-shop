import { HttpUrlEncodingCodec } from '@angular/common/http';
import {
  ChangeDetectionStrategy,
  Component,
  OnDestroy,
  OnInit,
  Input,
  HostListener,
  Renderer2,
  ElementRef,
  ViewChild
} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Facet, ProductSearchPage } from '@spartacus/core';
import { Observable, Subscription } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { ICON_TYPE, ModalService } from '@spartacus/storefront';
import { CustomProductListComponentService } from '../product-list/container/custom-product-list-component.service';
import { NgbPopover } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-product-facet-navigation',
  templateUrl: './custom-product-facet-navigation.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CustomProductFacetNavigationComponent implements OnInit, OnDestroy {
  private sub: Subscription;
  public facetsLeft = [];
  public facetsRight = [];
  public isFacetsRight = false;
  iconTypes = ICON_TYPE;
  activeFacetValueCode: string;
  searchResult: ProductSearchPage;
  minPerFacet = 6;
  showAllPerFacetMap: Map<String, boolean>;
  protected queryCodec: HttpUrlEncodingCodec;
  private collapsedFacets = new Set<string>();
  searchResult$: Observable<ProductSearchPage>;
  visibleFacets$: Observable<Facet[]>;
  model$: Observable<ProductSearchPage> = this.productListComponentService.model$;
  isFacetPopupOpened: boolean = false;
  @ViewChild('popover', { static: false }) popover: NgbPopover;
  constructor(
    private activatedRoute: ActivatedRoute,
    private productListComponentService: CustomProductListComponentService,
    private renderer: Renderer2,
    private facetsContent: ElementRef
  ) {
    this.showAllPerFacetMap = new Map<String, boolean>();
    this.queryCodec = new HttpUrlEncodingCodec();
  }

  @HostListener('document:click', ['$event'])
  public onDocumentClick(event: MouseEvent): void {
    const targetElement = event.target as HTMLElement;
    if (targetElement && !this.facetsContent.nativeElement.contains(targetElement)) {
      this.isFacetPopupOpened = false;
      this.renderer.removeClass(document.body, 'popover-open');
      this.popover.close();
    }
  }

  toggleBackdrop(option): void {
    if (option === 'show') {
      this.renderer.addClass(document.body, 'popover-open');
      this.isFacetPopupOpened = true;
    } else {
      this.renderer.removeClass(document.body, 'popover-open');
      this.isFacetPopupOpened = false;
    }
  }
  ngOnInit(): void {
    this.sub = this.activatedRoute.params.subscribe(params => {
      this.activeFacetValueCode = params.categoryCode || params.brandCode;
    });

    this.searchResult$ = this.productListComponentService.model$.pipe(
      tap(searchResult => {
        if (searchResult.facets) {
          this.sortFacets(searchResult.facets);

          searchResult.facets.forEach(el => {
            this.showAllPerFacetMap.set(el.name, false);
          });
        }
      })
    );

    this.visibleFacets$ = this.searchResult$.pipe(
      map(searchResult => {
        return searchResult.facets ? searchResult.facets.filter(facet => facet.visible) : [];
      })
    );
  }

  toggleValue(query: string): void {
    this.productListComponentService.setQuery(this.queryCodec.decodeValue(query));
  }

  showLess(facetName: String): void {
    this.updateShowAllPerFacetMap(facetName, false);
  }

  showMore(facetName: String): void {
    this.updateShowAllPerFacetMap(facetName, true);
  }

  private updateShowAllPerFacetMap(facetName: String, showAll: boolean): void {
    this.showAllPerFacetMap.set(facetName, showAll);
  }

  isFacetCollapsed(facetName: string): boolean {
    return this.collapsedFacets.has(facetName);
  }

  toggleFacet(facetName: string): void {
    if (this.collapsedFacets.has(facetName)) {
      this.collapsedFacets.delete(facetName);
    } else {
      this.collapsedFacets.add(facetName);
    }
  }

  getVisibleFacetValues(facet): any {
    return facet.values.slice(0, this.showAllPerFacetMap.get(facet.name) ? facet.values.length : this.minPerFacet);
  }

  ngOnDestroy() {
    if (this.sub) {
      this.sub.unsubscribe();
    }
  }
  sortList(sortCode: string): void {
    this.productListComponentService.sort(sortCode);
  }
  sortFacets(facetsList) {
    this.facetsLeft = [];
    this.facetsRight = [];

    for (const facet of facetsList) {
      if (facet.code === 'price' || facet.code === 'ratingRange' || facet.code === 'colour' || facet.code === 'size') {
        this.facetsRight.push(facet);
      } else {
        this.facetsLeft.push(facet);
      }
      if (this.facetsRight.length > 0) {
        this.isFacetsRight = true;
      }
    }
  }
}
