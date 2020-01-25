/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

import { InjectionToken } from '@angular/core';
import { Converter, Product, ProductSearchPage } from '@spartacus/core';

export const TBS_PRODUCT_NORMALIZER = new InjectionToken<Converter<any, Product>>('TBSProductNormalizer');

export const TBS_PRODUCT_SEARCH_PAGE_NORMALIZER = new InjectionToken<Converter<any, ProductSearchPage>>(
  'TBSProductSearchPageNormalizer'
);
