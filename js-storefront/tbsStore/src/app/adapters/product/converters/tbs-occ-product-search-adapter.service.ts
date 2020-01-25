/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {pluck} from 'rxjs/operators';
import {
    ConverterService,
    OccEndpointsService,
    PRODUCT_SUGGESTION_NORMALIZER,
    ProductSearchAdapter,
    ProductSearchPage,
    SearchConfig,
    Suggestion,
} from "@spartacus/core";
import {TBS_PRODUCT_SEARCH_PAGE_NORMALIZER} from "./converters";

const DEFAULT_SEARCH_CONFIG: SearchConfig = {
    pageSize: 20,
};

@Injectable()
export class TbsOccProductSearchAdapter implements ProductSearchAdapter {
    constructor(
        protected http: HttpClient,
        protected occEndpoints: OccEndpointsService,
        protected converter: ConverterService
    ) {
    }

    search(
        query: string,
        searchConfig: SearchConfig = DEFAULT_SEARCH_CONFIG
    ): Observable<ProductSearchPage> {
        return this.http
            .get(this.getSearchEndpoint(query, searchConfig))
            .pipe(this.converter.pipeable(TBS_PRODUCT_SEARCH_PAGE_NORMALIZER));
    }

    loadSuggestions(
        term: string,
        pageSize: number = 3
    ): Observable<Suggestion[]> {
        return this.http
            .get(this.getSuggestionEndpoint(term, pageSize.toString()))
            .pipe(
                pluck('suggestions'),
                this.converter.pipeableMany(PRODUCT_SUGGESTION_NORMALIZER)
            );
    }

    protected getSearchEndpoint(
        query: string,
        searchConfig: SearchConfig
    ): string {
        return this.occEndpoints.getUrl(
            'productSearch',
            null,
            {
                query,
                pageSize: searchConfig.pageSize,
                currentPage: searchConfig.currentPage,
                sort: searchConfig.sortCode,
            }
        );
    }

    protected getSuggestionEndpoint(term: string, max: string): string {
        return this.occEndpoints.getUrl('productSuggestions', null, { term, max });
    }
}

