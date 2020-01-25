import { BrowserModule } from '@angular/platform-browser';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatSelectModule } from '@angular/material';
import { RouterModule } from '@angular/router';
import { HTTP_INTERCEPTORS } from '@angular/common/http';

// spartacus
import {
  ConfigModule,
  provideConfigFromMetaTags,
  HttpErrorHandler,
  I18nModule,
  UrlModule,
  SiteContextModule,
  CartService,
  ProductOccModule,
  ProductAdapter,
  ProductSearchAdapter,
  AUTH_FEATURE
} from '@spartacus/core';

import {
  B2cStorefrontModule,
  defaultCmsContentConfig,
  ProductListModule,
  MediaModule,
  StarRatingModule,
  IconModule,
  LinkModule,
  OutletModule,
  BreakpointService
} from '@spartacus/storefront';
import { translations, translationChunksConfig } from '@spartacus/assets';

// vendor
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

// interceptors && adapters
import { Interceptor } from './interceptor';
import { environment } from '../environments/environment';
import { translationOverwrites } from './translations/app.translations';
import { defaultLayoutConfig } from './defaultLayoutConfig';
import { TbsOccProductAdapter } from './adapters/product/converters/tbs-occ-product-adapter.service';
import { TbsOccProductSearchAdapter } from './adapters/product/converters/tbs-occ-product-search-adapter.service';

// modules
import { RootStoreModule } from './root-store';
import { TbsProductOccModule } from './adapters/product/converters/TbsProductOccModule';
import { ListNavigationModule } from './components/navigation/list-navigation/list-navigation.module';
import { ItemCounterModule } from './components/shared/custom-quantity-selector/custom-quantity-selector.module';
import { AddToCartModule } from './components/cart/add-to-cart/add-to-cart.module';
import { CustomLinkModule } from './components/navigation/custom-link/custom-link.module';
import { CustomCategoryNavigationModule } from './components/navigation/custom-category-navigation/custom-category-navigation.module';
import { CustomFooterNavigationModule } from './components/navigation/custom-footer-navigation/custom-footer-navigation.module';
import { AmplienceModule } from './components/amplience/amplience.module';
import { ProductImagesModule } from './components/product/product-images/product-images.module';
import { CartComponentModule } from './components/cart/cart.module';
import { SliderModule } from './components/slider/slider.module';
import { CustomCheckoutModule } from './components/checkout/custom-checkout.module';
import { CartTbsModule } from './adapters/cart/cart-tbs.module';
import { CustomBreadcrumbModule } from './components/navigation/custom-breadcrumb/custom-breadcrumb.module';
import { WishlistModule } from './components/wishlist/wishlist.module';
import { CustomTabParagraphContainerModule } from './components/content/custom-tab-paragraph-container/custom-tab-paragraph-container.module';

// components
import { AppComponent } from './app.component';
import { CustomSearchBoxComponent } from './components/navigation/custom-search-box/custom-search-box.component';
import { MarketSelectorComponent } from './components/market-selector/market-selector.component';
import { CustomProductListItemComponent } from './components/product/product-list/custom-product-list-item/custom-product-list-item.component';
import { OlapicComponent } from './components/olapic/olapic.component';
import { CustomLoginComponent } from './components/user/custom-login/custom-login.component';
import { CustomCategoryNavigationComponent } from './components/navigation/custom-category-navigation/custom-category-navigation.component';
import { CustomProductViewComponent } from './components/product/product-list/custom-product-view/custom-product-view.component';
import { CustomSiteContextSelectorComponent } from './components/navigation/custom-site-context-selector/custom-site-context-selector.component';
import { CustomBreadcrumbComponent } from './components/navigation/custom-breadcrumb/custom-breadcrumb.component';
import { CustomProductFacetNavigationComponent } from './components/product/custom-product-facet-navigation/custom-product-facet-navigation.component';
import { ProductDetailsTabComponent } from './components/product/product-tabs/product-details-tab/product-details-tab.component';
import { ProductIntroComponent } from './components/product/product-intro/product-intro.component';
import { ProductKeyIngredientsTabComponent } from './components/product/product-tabs/product-key-ingredients/product-key-ingredients.component';
import { StyleguideComponent } from './components/styleguide/styleguide.component';
import { CustomProductFacetNodeComponent } from './components/product/custom-product-facet-node/custom-product-facet-node.component';
import { BackInStockComponent } from './components/back-in-stock/back-in-stock.component';
import { BackInStockSuccessComponent } from './components/back-in-stock-success/back-in-stock-success.component';
import { SiteMapComponent } from './components/site-map/site-map.component';
import { CategoryBannerComponent } from './components/category-banner/category-banner.component';

// services
import { CustomCartService } from './services/cart/facade/cart.service';
import { AppCustomStorageService } from './services/product/web-storage.service';
import { CustomErrorHandlerService } from './services/custom-error-handler.service';
import { CustomBreakpointService } from './layout/breakpoint/breakpoint.service';
import { WishlistService } from './services/wishlist/wishlist.service';
import { AdyenCreditCardsService } from './services/adyen-credit-cards/adyen-credit-cards.service';

// directives
import { CustomProductListScrollDirective } from './directives/custom-product-list-scroll/custom-product-list-scroll.directive';

// pipes
import { SortProductBySizePipe } from './pipes/sort-product-by-size/sort-product-by-size.pipe';
import { ContextSelectorPipe } from './pipes/context-selector/context-selector.pipe';
import { CustomHighlightPipe } from './pipes/search-box-custom-highlight/custom-highlight.pipe';
import { CustomStoreFinderModule } from './components/store-finder/custom-store-finder.module';
import { WishlistFormModule } from './components/wishlist/wishlist-form/wishlist-form.module';
import { SubscribeModule } from './components/subscribe/subscribe.module';
import { CustomProductListModule } from './components/product/product-list/container/custom-product-list.module';
import { MyAccountModule } from './components/my-account/my-account.module';
import { SharedModule } from './components/shared/shared.module';

import { CustomOrderConfirmationModule } from './components/order-confirmation/custom-order-confirmation.module';
import { GigyaRaasModule } from './components/gigya-raas/gigya-raas.module';
import { StoreModule } from '@ngrx/store';
import { getReducers, metaReducers, reducerToken } from './root-store/gigya-auth-store/reducers';

@NgModule({
  declarations: [
    AppComponent,
    MarketSelectorComponent,
    CustomProductListItemComponent,
    OlapicComponent,
    CustomProductViewComponent,
    CustomLoginComponent,
    SiteMapComponent,
    CustomSearchBoxComponent,
    CustomSiteContextSelectorComponent,
    CustomProductFacetNavigationComponent,
    ProductIntroComponent,
    ProductDetailsTabComponent,
    SortProductBySizePipe,
    BackInStockSuccessComponent,
    CustomProductFacetNodeComponent,
    StyleguideComponent,
    ProductKeyIngredientsTabComponent,
    CustomProductListScrollDirective,
    SiteMapComponent,
    CategoryBannerComponent,
    CustomHighlightPipe,
    ContextSelectorPipe
  ],
  imports: [
    FormsModule,
    ReactiveFormsModule,
    TbsProductOccModule,
    B2cStorefrontModule.withConfig({
      backend: {
        occ: {
          baseUrl: 'https://localhost:9002',
          prefix: '/rest/v2/',
          legacy: false,
          endpoints: {
            productSearch:
              'products/search?fields=products(multiVariant,code,name,summary,price(FULL),images(DEFAULT),stock(FULL),averageRating,isVariant,variantSize,previewDescription,variants,url,emailMeWhenInStockToggle),facets,breadcrumbs,pagination(DEFAULT),sorts(DEFAULT),breadcrumbCategories(BASIC),freeTextSearch,keywordRedirectUrl&query=${query}',
            // TODO: Implement `contentSearch` as endpoint in `content-page-search.service.ts`
            // contentSearch: 'content/search?currentPage=0&fields=FULL&pageSize=20&query=',
            product:
              'products/${productCode}?fields=DEFAULT,averageRating,variantOptions(DEFAULT),classifications,numberOfReviews,categories(FULL),isVariant,emailMeWhenInStockToggle'
          }
        }
      },
      authentication: {
        client_id: 'mobile_android',
        client_secret: 'secret'
      },
      context: {
        urlParameters: ['baseSite', 'language', 'currency'],
        baseSite: [
          'thebodyshop-uk',
          'thebodyshop-ca',
          'thebodyshop-de',
          'thebodyshop-dk',
          'thebodyshop-es',
          'thebodyshop-fr',
          'thebodyshop-nl',
          'thebodyshop-se',
          'thebodyshop-au',
          'thebodyshop-us',
          'thebodyshop-at',
          'thebodyshop-hk',
          'thebodyshop-sg',
          'thebodyshop-global'
        ],
        currency: ['GBP', 'CAD', 'DKK', 'EUR', 'AUD', 'SEK', 'USD', 'HKD', 'SGD'],
        language: [
          'en_GB',
          'de',
          'de_AT',
          'da',
          'en_AU',
          'en_CA',
          'en_US',
          'es',
          'fr',
          'fr_CA',
          'nl',
          'sv',
          'en_HK',
          'en_SG',
          'en'
        ]
      },
      i18n: {
        // backend: {
        //   loadPath: 'assets/i18n-assets/{{lng}}/{{ns}}.json'
        //   // crossOrigin: true, - use this option when i18n assets come from a different domain
        // },
        resources: translations,
        chunks: translationChunksConfig,
        fallbackLang: 'en'
      },
      icon: {
        symbols: {
          STAR: 'icon icon--star',
          BAG: 'icon icon--basket-dynamic',
          CART: 'icon icon--basket-dynamic',
          USER_CIRCLE: 'icon icon--my-account-dynamic',
          MAP_MARKER: 'icon icon--location-dynamic',
          WISH_LIST: 'icon icon--heart-dynamic',
          CHEVRON_LEFT: 'icon icon--chevron-left',
          CHEVRON_DOWN: 'icon icon--chevron-down',
          CHEVRON_UP: 'icon icon--chevron-up',
          SEARCH: 'icon icon--search-dynamic',
          CLOSE: 'icon icon--close',
          EXPAND: 'icon icon--plus'
        }
      }
    }),
    StoreModule.forFeature(AUTH_FEATURE, reducerToken, { metaReducers }),
    ConfigModule.withConfig(defaultLayoutConfig),
    ConfigModule.withConfig(translationOverwrites),
    ConfigModule.withConfigFactory(defaultCmsContentConfig),
    ConfigModule.withConfig({
      cmsComponents: {
        HtmlSiteMapComponent: {
          component: SiteMapComponent
        },
        MarketSelectorCMSComponent: {
          component: MarketSelectorComponent
        },
        ProductRefinementComponent: {
          component: CustomProductFacetNavigationComponent
        },
        OlapicCMSComponent: {
          component: OlapicComponent
        },
        LoginComponent: {
          component: CustomLoginComponent
        },
        SearchBoxComponent: {
          component: CustomSearchBoxComponent
        },
        CategoryNavigationComponent: {
          component: CustomCategoryNavigationComponent
        },
        ProductViewComponent: {
          component: CustomProductViewComponent
        },
        CMSSiteContextComponent: {
          component: CustomSiteContextSelectorComponent
        },
        ProductIntroComponent: {
          component: ProductIntroComponent
        },
        ProductDetailsTabComponent: {
          component: ProductDetailsTabComponent
        },
        ProductKeyIngredientsTabComponent: {
          component: ProductKeyIngredientsTabComponent
        },
        StyleGuideComponent: {
          component: StyleguideComponent
        },
        CategoryBannerCMSComponent: {
          component: CategoryBannerComponent
        }
      },
      i18n: { resources: translationOverwrites }
    }),
    NgbModule,
    I18nModule,
    MyAccountModule,
    CustomCategoryNavigationModule,
    CustomProductListModule,
    CustomBreadcrumbModule,
    CustomLinkModule,
    SiteContextModule,
    RouterModule,
    ProductListModule,
    BrowserModule,
    MediaModule,
    WishlistFormModule,
    ItemCounterModule,
    ListNavigationModule,
    UrlModule,
    I18nModule,
    StarRatingModule,
    IconModule,
    LinkModule,
    CustomLinkModule,
    SubscribeModule,
    AmplienceModule,
    CustomFooterNavigationModule,
    RootStoreModule,
    AddToCartModule,
    OutletModule,
    ProductImagesModule,
    SliderModule,
    CustomCheckoutModule,
    CartComponentModule,
    MatSelectModule,
    CartTbsModule,
    WishlistModule,
    CustomStoreFinderModule,
    CustomTabParagraphContainerModule,
    CustomOrderConfirmationModule,
    GigyaRaasModule,
    SharedModule
  ],
  entryComponents: [
    MarketSelectorComponent,
    OlapicComponent,
    CustomLoginComponent,
    CustomSearchBoxComponent,
    SiteMapComponent,
    CustomSiteContextSelectorComponent,
    CustomBreadcrumbComponent,
    CustomProductFacetNavigationComponent,
    ProductIntroComponent,
    ProductDetailsTabComponent,
    ProductKeyIngredientsTabComponent,
    BackInStockComponent,
    BackInStockSuccessComponent,
    CustomProductFacetNodeComponent,
    StyleguideComponent,
    SiteMapComponent,
    CategoryBannerComponent
  ],
  providers: [
    ...provideConfigFromMetaTags(),
    {
      provide: HTTP_INTERCEPTORS,
      useClass: Interceptor,
      multi: true
    },
    {
      provide: HttpErrorHandler,
      useExisting: CustomErrorHandlerService,
      multi: true
    },
    {
      provide: CartService,
      useExisting: CustomCartService,
      multi: false
    },
    CustomCartService,
    {
      provide: BreakpointService,
      useExisting: CustomBreakpointService,
      multi: false
    },
    CustomBreakpointService,
    {
      provide: ProductOccModule,
      useExisting: TbsProductOccModule,
      multi: false
    },
    TbsProductOccModule,
    {
      provide: ProductAdapter,
      useExisting: TbsOccProductAdapter,
      multi: false
    },
    TbsOccProductAdapter,
    {
      provide: ProductSearchAdapter,
      useExisting: TbsOccProductSearchAdapter,
      multi: false
    },
    {
      provide: reducerToken,
      useFactory: getReducers,
      multi: false
    },
    WishlistService,
    TbsOccProductSearchAdapter,
    AppCustomStorageService,
    AdyenCreditCardsService
  ],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppModule {}
