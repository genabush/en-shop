/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

import {Injectable} from '@angular/core';
import {ConverterService, OccEndpointsService, Product, ProductAdapter} from "@spartacus/core";
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {TBS_PRODUCT_NORMALIZER} from "./converters";


@Injectable()
export class TbsOccProductAdapter implements ProductAdapter {
    constructor(
        protected http: HttpClient,
        protected occEndpoints: OccEndpointsService,
        protected converter: ConverterService
    ) {
    }

    load(productCode: string): Observable<Product> {
        return this.http
            .get(this.getEndpoint(productCode))
            .pipe(this.converter.pipeable(TBS_PRODUCT_NORMALIZER));
    }

    protected getEndpoint(code: string): string {
        return this.occEndpoints.getUrl('product', {
            productCode: code,
        });
    }
}
