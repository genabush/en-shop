/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import {
  ConfigModule,
  OccConfig,
  OccProductAdapter,
  OccProductReferencesAdapter,
  OccProductReferencesListNormalizer,
  OccProductReviewsAdapter,
  OccProductSearchAdapter,
  PRODUCT_REFERENCES_NORMALIZER,
  ProductAdapter,
  ProductNameNormalizer,
  ProductReferencesAdapter,
  ProductReviewsAdapter,
  ProductSearchAdapter
} from '@spartacus/core';
import { TbsProductImageNormalizer } from './tbs-product-image-normalizer';
import { NgModule } from '@angular/core';
import { TBS_PRODUCT_NORMALIZER, TBS_PRODUCT_SEARCH_PAGE_NORMALIZER } from './converters';
import { TbsOccProductSearchPageNormalizer } from './tbs-occ-product-search-page-normalizer';

export const defaultOccProductConfig: OccConfig = {
  backend: {
    occ: {
      endpoints: {
        product:
          'products/${productCode}?fields=DEFAULT,averageRating,images(FULL),classifications,numberOfReviews,categories(FULL)',
        productReviews: 'products/${productCode}/reviews',
        // Uncomment this when occ gets configured
        // productReferences:
        //   'products/${productCode}/references?fields=DEFAULT,references(target(images(FULL)))&referenceType=${referenceType}',
        productReferences: 'products/${productCode}/references?fields=DEFAULT,references(target(images(FULL)))',
        // tslint:disable:max-line-length
        productSearch:
          'products/search?fields=products(multiVariant,code,name,summary,price(FULL),images(DEFAULT),stock(FULL),averageRating),facets,breadcrumbs,pagination(DEFAULT),sorts(DEFAULT),freeTextSearch',
        // tslint:enable
        productSuggestions: 'products/suggestions'
      }
    }
  }
};

@NgModule({
  imports: [CommonModule, HttpClientModule, ConfigModule.withConfig(defaultOccProductConfig)],
  providers: [
    {
      provide: ProductAdapter,
      useClass: OccProductAdapter
    },
    {
      provide: TBS_PRODUCT_NORMALIZER,
      useClass: TbsProductImageNormalizer,
      multi: true
    },
    {
      provide: TBS_PRODUCT_NORMALIZER,
      useClass: ProductNameNormalizer,
      multi: true
    },
    {
      provide: ProductReferencesAdapter,
      useClass: OccProductReferencesAdapter
    },
    {
      provide: PRODUCT_REFERENCES_NORMALIZER,
      useClass: OccProductReferencesListNormalizer,
      multi: true
    },
    {
      provide: ProductSearchAdapter,
      useClass: OccProductSearchAdapter
    },
    {
      provide: TBS_PRODUCT_SEARCH_PAGE_NORMALIZER,
      useClass: TbsOccProductSearchPageNormalizer,
      multi: true
    },
    {
      provide: ProductReviewsAdapter,
      useClass: OccProductReviewsAdapter
    }
  ]
})
export class TbsProductOccModule {}
