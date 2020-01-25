import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { Product } from '@spartacus/core';
import { BehaviorSubject, combineLatest, Observable, of } from 'rxjs';
import { distinctUntilChanged, filter, map, tap, toArray } from 'rxjs/operators';
import { CurrentProductService } from '../current-product.service';
import { ProductVariantsService } from 'src/app/services/product-variants/product-variants.service';

@Component({
  selector: 'cx-product-images',
  templateUrl: './product-images.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ProductImagesComponent implements OnInit {
  private mainMediaContainer = new BehaviorSubject(null);

  activeGalleryIndex = new BehaviorSubject(0);

  private product$: Observable<Product> = this.currentProductService.getProduct().pipe(
    filter(Boolean),
    distinctUntilChanged(),
    tap((p: Product) => this.mainMediaContainer.next(p.images ? p.images.PRIMARY : {}))
  );
  private variantProduct$: Observable<any> = this.productVariantsService.getSelectedVariant();

  variantThumbs$ = this.variantProduct$.pipe(map(product => this.createVariantsThumbs(product)));
  variantMainImage$ = this.variantProduct$.pipe(map(product => this.createVariantsMain(product)));

  constructor(
    private currentProductService: CurrentProductService,
    private productVariantsService: ProductVariantsService
  ) {}

  /**
   * If the variantProduct$ changes, meaning a new variant has been selected
   * change the gallery index back to 0, as different variant have different
   * amount of imagery
   */
  ngOnInit() {
    this.variantProduct$.subscribe(data => {
      this.changeIndex(0);
    });
  }

  /**
   * On click of a thumbnail, show the large image
   * @param item : integer, galleryIndex
   */
  openImage(item: any): void {
    this.activeGalleryIndex.next(item);
    this.variantMainImage$ = this.variantProduct$.pipe(map(product => this.createVariantsMain(product)));
  }

  /**
   * Set active class when thumb is the main image
   * @param current: integer, galleryIndex
   */
  isActiveVariant(current: number) {
    return current === this.activeGalleryIndex.getValue();
  }

  /** find the index of the main media in the list of media */
  getActive(thumbs: any[]): Observable<number> {
    return this.mainMediaContainer.pipe(
      filter(Boolean),
      map((container: any) => {
        const current = thumbs.find(
          t =>
            t.media &&
            container.zoom &&
            t.media.container &&
            t.media.container.zoom &&
            t.media.container.zoom.url === container.zoom.url
        );
        return thumbs.indexOf(current);
      })
    );
  }

  /**
   * Give the product main image information to the view
   * @param product
   */
  private createVariantsMain(product) {
    let idx = this.activeGalleryIndex.getValue();
    if (product.images) {
      const result = product.images[idx];
      return result;
    } else {
      return { src: null };
    }
  }

  /**
   * Change the gallery index
   * @param val : integer, galleryIndex
   */
  changeIndex(val) {
    this.activeGalleryIndex.next(val);
    this.variantMainImage$ = this.variantProduct$.pipe(map(product => this.createVariantsMain(product)));
  }

  /**
   * From product data, generate the data for all other product imagery
   * @param product
   */
  private createVariantsThumbs(product) {
    /**
     * To parse carousel data, we need an Array of item, each items needs to be an obversable
     * E.g, if you had 4 items: [Observable, Observable, Observable, Observable]
     */

    let idx = this.activeGalleryIndex.getValue();

    if (product.images) {
      let images = product.images;

      let thumbnails = [];
      for (var i = 0; i < images.length; i++) {
        thumbnails.push(of({ container: images[i] }));
      }
      return thumbnails;
    }
  }
}
