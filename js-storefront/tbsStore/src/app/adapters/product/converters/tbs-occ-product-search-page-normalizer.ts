/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

import { Occ } from "@spartacus/core";
import {
    Converter,
    ConverterService,
} from "@spartacus/core";
import { Injectable } from '@angular/core';
import { ProductSearchPage } from "@spartacus/core";
import {TBS_PRODUCT_NORMALIZER} from "./converters";

@Injectable()
export class TbsOccProductSearchPageNormalizer
    implements Converter<Occ.ProductSearchPage, ProductSearchPage> {
    constructor(private converterService: ConverterService) {}

    convert(
        source: Occ.ProductSearchPage,
        target: ProductSearchPage = {}
    ): ProductSearchPage {
        target = {
            ...target,
            ...(source as any),
        };
        if (source.products) {
            target.products = source.products.map(product =>
                this.converterService.convert(product, TBS_PRODUCT_NORMALIZER)
            );
        }
        return target;
    }
}
