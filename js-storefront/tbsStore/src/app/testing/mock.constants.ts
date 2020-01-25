/* tslint:disable */
import { ICustomProduct } from '../interfaces/custom-product-item.interface';
import { PriceType, DeliveryMode, Address, PromotionResult, Promotion } from '@spartacus/core';
import { Wishlist, Wishlists } from '../interfaces/wishlists.interface';
import { CustomCart } from '../interfaces/custom-cart.interface';

export const DUMMY_CART_ENTRIES: any[] = [
  {
    basePrice: {
      formattedValue: '£8.00'
    },
    configurationInfos: [],
    entryNumber: 2,
    maxProductOrderQuantity: 10,
    product: {
      availableForPickup: false,
      baseProduct: 'p000657',
      breadcrumbCategories: [
        {
          code: 'c00007',
          name: 'Make-up',
          url: '/Make-up/c/c00007'
        },
        {
          code: 'c00066',
          name: 'Lips',
          url: '/Make-up/Lips/c/c00066'
        }
      ],
      categories: [],
      code: '1096148',
      colour: {
        code: '96148',
        hexCode: '#B62657',
        name: '029 Deep Berryis'
      },
      configurable: false,
      images: {
        PRIMARY: {
          zoom: {
            altText: 'Lip & Cheek Stain 7.2ml',
            format: 'zoom',
            imageType: 'PRIMARY',
            url:
              'https://localhost:9002/medias/?context=bWFzdGVyfGltYWdlc3w1NTcyfGltYWdlL2pwZWd8aW1hZ2VzL2hiZi9oMDQvODc5NjEyMjI1MTI5NC5qcGd8ZWExY2IxMjZmNjM5MzJmMjE1NmI1Mzg4OWFlODA0ZGY0OGE3MjNhNjlmMWZlNmU0YmFkMTQzZGFiMjNlMDNkYg'
          },
          product: {
            altText: 'Lip & Cheek Stain 7.2ml',
            format: 'product',
            imageType: 'PRIMARY',
            url:
              'https://localhost:9002/medias/?context=bWFzdGVyfGltYWdlc3w1NTcyfGltYWdlL2pwZWd8aW1hZ2VzL2g4MC9oZDYvODc5NjEyNDE1MTgzOC5qcGd8ODIxNmUyNjJmYjc1ZGMzZDM5YWJhZTkyNDY5NDAxM2Q5YzY4ZGVlNWMwOTE4NTE0ODY1MzJiNmNmNTIzODMxOQ'
          },
          thumbnail: {
            altText: 'Lip & Cheek Stain 7.2ml',
            format: 'thumbnail',
            imageType: 'PRIMARY',
            url:
              'https://localhost:9002/medias/?context=bWFzdGVyfGltYWdlc3w1NTcyfGltYWdlL2pwZWd8aW1hZ2VzL2hhYi9oMTAvODc5NjEyNjA1MjM4Mi5qcGd8MzEyZTJjZmMzMjBhMzk0NTVhOThhZmEyZDM2MGZmZjM5MDhiMTBlNjRkZjQ0NWZhMTJiNGM2YTkyOTc5NzdlZg'
          }
        }
      },
      isVariant: false,
      manufacturer: 'The Body Shop',
      name: 'Lip & Cheek Stain 7.2ml',
      purchasable: true,
      size: '7.2',
      stock: {
        stockLevel: 10,
        stockLevelStatus: 'inStock'
      },
      url: '/Make-up/Lips/Lip-%26-Cheek-Stain/p/1096148',
      nameHtml: 'Lip & Cheek Stain 7.2ml'
    },
    quantity: 1,
    totalPrice: {
      currencyIso: 'GBP',
      formattedValue: '£8.00',
      value: 8
    },
    updateable: true
  },
  {
    basePrice: {
      formattedValue: '£15.00'
    },
    configurationInfos: [],
    entryNumber: 1,
    maxProductOrderQuantity: 10,
    product: {
      availableForPickup: false,
      baseProduct: 'p000247',
      breadcrumbCategories: [
        {
          code: 'c00001',
          name: 'Body',
          url: '/Body/c/c00001'
        },
        {
          code: 'c00014',
          name: 'Body Butters',
          url: '/Body/Body-Butters/c/c00014'
        }
      ],
      categories: [],
      code: '1094138',
      configurable: false,
      images: {
        PRIMARY: {
          zoom: {
            altText: 'Wild Argan Oil Sublime Nourishing Body Butter 200ml',
            format: 'zoom',
            imageType: 'PRIMARY',
            url:
              'https://localhost:9002/medias/?context=bWFzdGVyfGltYWdlc3w1MjIwOHxpbWFnZS9qcGVnfGltYWdlcy9oNzcvaDRiLzg3OTYxMjA5NDA1NzQuanBnfDQ5ZDY5ZDNhNmM4Mjg0ZTZjMWIzZTJkNzM2MDBhNTY5ZDZlOWFlZmJjMTA0ZTcwNzIwYTg5MDY1N2FlNDc4MGY'
          },
          product: {
            altText: 'Wild Argan Oil Sublime Nourishing Body Butter 200ml',
            format: 'product',
            imageType: 'PRIMARY',
            url:
              'https://localhost:9002/medias/?context=bWFzdGVyfGltYWdlc3w1MjIwOHxpbWFnZS9qcGVnfGltYWdlcy9oMDEvaDM1Lzg3OTYxMjI4NDExMTguanBnfDE1ZWNkMDdhYWRjZmUwMjZiYzk1MWVmOGI5ZjZhNDZkOTE4MzdiMWE3ZDIyNjJiMmYxMGJmZmJjNTQyMTA3MzQ'
          },
          thumbnail: {
            altText: 'Wild Argan Oil Sublime Nourishing Body Butter 200ml',
            format: 'thumbnail',
            imageType: 'PRIMARY',
            url:
              'https://localhost:9002/medias/?context=bWFzdGVyfGltYWdlc3w1MjIwOHxpbWFnZS9qcGVnfGltYWdlcy9oNjQvaDU3Lzg3OTYxMjQ3NDE2NjIuanBnfGViZWY4N2FlYjczYzM2OTY4NzE2OGRlYjE1ZjY5ZjYzZGY4ZGU0ZDdhZTdiZjdmM2VhZTM2MDQ2OGM1Mzg2YWY'
          }
        }
      },
      isVariant: false,
      manufacturer: 'The Body Shop',
      name: 'Wild Argan Oil Sublime Nourishing Body Butter 200ml',
      purchasable: true,
      size: '200ml',
      stock: {
        stockLevel: 10,
        stockLevelStatus: 'inStock'
      },
      url: '/Body/Body-Butters/Wild-Argan-Oil-Sublime-Nourishing-Body-Butter/p/1094138',
      nameHtml: 'Wild Argan Oil Sublime Nourishing Body Butter 200ml'
    },
    quantity: 1,
    totalPrice: {
      currencyIso: 'GBP',
      formattedValue: '£15.00',
      value: 15
    },
    updateable: true
  }
];

export const DUMMY_MINICART_CMS = {
  uid: 'MiniCart',
  uuid:
    'eyJpdGVtSWQiOiJNaW5pQ2FydCIsImNhdGFsb2dJZCI6InRic0dsb2JhbENvbnRlbnRDYXRhbG9nIiwiY2F0YWxvZ1ZlcnNpb24iOiJPbmxpbmUifQ==',
  typeCode: 'MiniCartComponent',
  modifiedTime: '2019-09-24T12:29:53.013+02:00',
  name: 'Mini Cart',
  container: 'false',
  shownProductCount: '3',
  totalDisplay: 'SUBTOTAL',
  lightboxBannerComponent: {
    container: 'false',
    uid: 'LightboxHomeDeliveryBannerComponent',
    external: 'false',
    name: 'Lightbox Home Delivery Banner Component',
    uuid:
      'eyJpdGVtSWQiOiJMaWdodGJveEhvbWVEZWxpdmVyeUJhbm5lckNvbXBvbmVudCIsImNhdGFsb2dJZCI6InRic0dsb2JhbENvbnRlbnRDYXRhbG9nIiwiY2F0YWxvZ1ZlcnNpb24iOiJPbmxpbmUifQ==',
    typeCode: 'SimpleBannerComponent'
  },
  title: 'YOUR SHOPPING CART'
};

export const DUMMY_ACTIVE_CART: CustomCart = {
  type: 'cartWsDTO',
  eligibleForCollectInStore: true,
  eligibleForCollectionPoint: true,
  eligibleForGiftMessage: true,
  eligibleForGiftWrap: true,
  eligibleForLoyalty: true,
  hasOutstandingAmount: true,
  giftWrapPrice: {
    currencyIso: 'GBP',
    value: 1,
    priceType: PriceType['BUY'],
    formattedValue: '£1.00'
  },
  totalGiftCardsValue: {
    currencyIso: 'GBP',
    formattedValue: '£0.00',
    priceType: PriceType['BUY'],
    value: 0.0
  },
  outstandingAmount: {
    currencyIso: 'GBP',
    formattedValue: '£0.00',
    priceType: PriceType['BUY'],
    value: 0.0
  },
  appliedOrderPromotions: [],
  appliedProductPromotions: [],
  appliedVouchers: [],
  code: '00000000',
  deliveryItemsQuantity: 2,
  guid: '08af04a7-6b20-4532-bb5c-37c9372768d3',
  net: false,
  loyaltyPoints: 120,
  pickupItemsQuantity: 0,
  productDiscounts: {
    formattedValue: '£0.00'
  },
  subTotal: {
    formattedValue: '£30.00'
  },
  totalDiscounts: {
    formattedValue: '£0.00',
    value: 0
  },
  totalItems: 2,
  totalPrice: {
    currencyIso: 'GBP',
    formattedValue: '£30.00',
    value: 30
  },
  totalPriceWithTax: {
    currencyIso: 'GBP',
    formattedValue: '£30.00',
    value: 30
  },
  totalTax: {
    formattedValue: '£5.00'
  },
  potentialOrderPromotions: [],
  potentialProductPromotions: [],
  giftCards: [],
  giftWrapApplied: false
};

export const DUMMY_ACTIVE_CART_EMPTY = {
  totalItems: 0,
  totalPrice: {
    formattedValue: '0.00'
  }
};

export const DUMMMY_CART_CMS_DATA = {
  uid: 'GiftWrapBannerCMSComponent',
  uuid:
    'eyJpdGVtSWQiOiJHaWZ0V3JhcEJhbm5lckNNU0NvbXBvbmVudCIsImNhdGFsb2dJZCI6InRic0dsb2JhbENvbnRlbnRDYXRhbG9nIiwiY2F0YWxvZ1ZlcnNpb24iOiJPbmxpbmUifQ==',
  typeCode: 'GiftWrapBannerCMSComponent',
  modifiedTime: '2019-12-30T07:47:22.033Z',
  name: 'GiftWrap Banner CMS Component',
  container: 'false',
  giftImage: {
    code: '/images/theme/logo-tbs.png',
    mime: 'image/png',
    altText: 'TBS logo',
    url:
      '/medias/logo-tbs.png?context=bWFzdGVyfGltYWdlc3w5NDYxfGltYWdlL3BuZ3xpbWFnZXMvaGM2L2hjMi84Nzk2MTMwOTAyMDQ2LnBuZ3xkNjMwMTk1NGQyMWFjMTViNDVjZjkwMWU1YWIwYWY5Njk1OTExMWUzOGMwNjcxMWNiNDdiYjQ5NmE0NzhlNDUy'
  },
  giftWrapName: 'TBS logo',
  giftWrapImageUrl:
    '/medias/logo-tbs.png?context=bWFzdGVyfGltYWdlc3w5NDYxfGltYWdlL3BuZ3xpbWFnZXMvaGM2L2hjMi84Nzk2MTMwOTAyMDQ2LnBuZ3xkNjMwMTk1NGQyMWFjMTViNDVjZjkwMWU1YWIwYWY5Njk1OTExMWUzOGMwNjcxMWNiNDdiYjQ5NmE0NzhlNDUy'
};

export const DUMMY_PLP_CUSTOM_PRODUCT_COLOUR_VARIANT_IN_STOCK: ICustomProduct = {
  availableForPickup: false,
  breadcrumbCategories: [
    {
      code: 'c00007',
      name: 'Make-up',
      url: '/Make-up/c/c00007'
    },
    {
      code: 'c00066',
      name: 'Lips',
      url: '/Make-up/Lips/c/c00066'
    }
  ],
  categories: [
    {
      code: 'c00066',
      name: 'Lips',
      url: '/Make-up/Lips/c/c00066'
    }
  ],
  code: 'p000657',
  configurable: false,
  description:
    'This dual-¬purpose liquid stain gives lips and cheeks a natural pop of buildable colour. Use under lip gloss for a show-¬stopping -pout and dab onto cheeks for a natural-looking rosy glow.',
  images: {
    PRIMARY: {
      zoom: {
        altText: 'Lip & Cheek Stain',
        format: 'zoom',
        imageType: 'PRIMARY',
        url:
          'https://localhost:9002/medias/?context=bWFzdGVyfGltYWdlc3w1MTg2fGltYWdlL2pwZWd8aW1hZ2VzL2gzYy9oNDkvODc5NjEyMTk4OTE1MC5qcGd8OGM1ODUxOWI0YzhlYTYzZDNmMjQ0YjdmYzc4NGZjNjEwYmJkYTk5MjhjYmVlZWM3ODMzNzFkOTNjZTRkZjVlYg'
      },
      product: {
        altText: 'Lip & Cheek Stain',
        format: 'product',
        imageType: 'PRIMARY',
        url:
          'https://localhost:9002/medias/?context=bWFzdGVyfGltYWdlc3w1MTg2fGltYWdlL2pwZWd8aW1hZ2VzL2hkOS9oNDAvODc5NjEyMzg4OTY5NC5qcGd8YzhmZTRlNjIxYTU0ZmM5YmYyMDQ3Y2YyZWU1YTI2NTU5ZDRlOGEzZTM4OGEzMmNhMTdiOTY0ZTZkN2U2MGYxZA'
      },
      thumbnail: {
        altText: 'Lip & Cheek Stain',
        format: 'thumbnail',
        imageType: 'PRIMARY',
        url:
          'https://localhost:9002/medias/?context=bWFzdGVyfGltYWdlc3w1MTg2fGltYWdlL2pwZWd8aW1hZ2VzL2g1MS9oY2IvODc5NjEyNTc5MDIzOC5qcGd8YzBiYTg5ZmQ0OGFlMmU2OGE4ZTIyZDg3OTE0NDA5Njc4ZDAyZmVjM2YwZWMzOTRkYmY2MmUzZWE3YWNkNWJmZg'
      }
    },
    GALLERY: [
      {
        zoom: {
          altText: 'Lip & Cheek Stain',
          format: 'zoom',
          galleryIndex: 0,
          imageType: 'GALLERY',
          url:
            'https://localhost:9002/medias/?context=bWFzdGVyfGltYWdlc3w1MTg2fGltYWdlL2pwZWd8aW1hZ2VzL2gzYy9oNDkvODc5NjEyMTk4OTE1MC5qcGd8OGM1ODUxOWI0YzhlYTYzZDNmMjQ0YjdmYzc4NGZjNjEwYmJkYTk5MjhjYmVlZWM3ODMzNzFkOTNjZTRkZjVlYg'
        },
        product: {
          altText: 'Lip & Cheek Stain',
          format: 'product',
          galleryIndex: 0,
          imageType: 'GALLERY',
          url:
            'https://localhost:9002/medias/?context=bWFzdGVyfGltYWdlc3w1MTg2fGltYWdlL2pwZWd8aW1hZ2VzL2hkOS9oNDAvODc5NjEyMzg4OTY5NC5qcGd8YzhmZTRlNjIxYTU0ZmM5YmYyMDQ3Y2YyZWU1YTI2NTU5ZDRlOGEzZTM4OGEzMmNhMTdiOTY0ZTZkN2U2MGYxZA'
        },
        thumbnail: {
          altText: 'Lip & Cheek Stain',
          format: 'thumbnail',
          galleryIndex: 0,
          imageType: 'GALLERY',
          url:
            'https://localhost:9002/medias/?context=bWFzdGVyfGltYWdlc3w1MTg2fGltYWdlL2pwZWd8aW1hZ2VzL2g1MS9oY2IvODc5NjEyNTc5MDIzOC5qcGd8YzBiYTg5ZmQ0OGFlMmU2OGE4ZTIyZDg3OTE0NDA5Njc4ZDAyZmVjM2YwZWMzOTRkYmY2MmUzZWE3YWNkNWJmZg'
        }
      },
      {
        zoom: {
          altText: 'Lip & Cheek Stain',
          format: 'zoom',
          galleryIndex: 1,
          imageType: 'GALLERY',
          url:
            'https://localhost:9002/medias/?context=bWFzdGVyfGltYWdlc3wxNDk4NHxpbWFnZS9qcGVnfGltYWdlcy9oZTEvaDQ2Lzg3OTYxMjIwNTQ2ODYuanBnfDdiNzg0NjU3YmVmN2JkMzM2ZmE1YzE0NjU1MDY2YzU3NjRjZTI3YzdiZWFhNTIzOWM3MzIwZTAwODY0MDVjMGM'
        },
        product: {
          altText: 'Lip & Cheek Stain',
          format: 'product',
          galleryIndex: 1,
          imageType: 'GALLERY',
          url:
            'https://localhost:9002/medias/?context=bWFzdGVyfGltYWdlc3wxNDk4NHxpbWFnZS9qcGVnfGltYWdlcy9oNTQvaDRiLzg3OTYxMjM5NTUyMzAuanBnfGM4ZmZhNTAyNGYxY2U5Y2MyNTkyYjNhNjQzYTM5ODc1NjUxYzEyNTMyOWM4N2Q4NmU0NDQxOTAwYTg3NjE3M2Q'
        },
        thumbnail: {
          altText: 'Lip & Cheek Stain',
          format: 'thumbnail',
          galleryIndex: 1,
          imageType: 'GALLERY',
          url:
            'https://localhost:9002/medias/?context=bWFzdGVyfGltYWdlc3wxNDk4NHxpbWFnZS9qcGVnfGltYWdlcy9oYzEvaDNlLzg3OTYxMjU4NTU3NzQuanBnfGI2ZmUyNmNjZTJmZTZmZTY3NDNkODBjNjcxNjJhNjJjY2E3YTdkN2MwMTNmNmY5NDhkOGMzMjUzYmEyZjVlNGU'
        }
      },
      {
        zoom: {
          altText: 'Lip & Cheek Stain',
          format: 'zoom',
          galleryIndex: 2,
          imageType: 'GALLERY',
          url:
            'https://localhost:9002/medias/?context=bWFzdGVyfGltYWdlc3w2NDEwfGltYWdlL2pwZWd8aW1hZ2VzL2g0Yy9oNDUvODc5NjEyMjEyMDIyMi5qcGd8NGJmZGNjYzAzODFmNzMzZDVmNTkwY2RjZWE2Zjg2YTZiZWZiMDU0ZjhmNzM0ZjBmY2JiMGZiMGJjOTQ1Y2E5Mg'
        },
        product: {
          altText: 'Lip & Cheek Stain',
          format: 'product',
          galleryIndex: 2,
          imageType: 'GALLERY',
          url:
            'https://localhost:9002/medias/?context=bWFzdGVyfGltYWdlc3w2NDEwfGltYWdlL2pwZWd8aW1hZ2VzL2hjOC9oNDQvODc5NjEyNDAyMDc2Ni5qcGd8OTA0MGQ0NDk5MzFhY2YxZDg5MmFiYTYxNmZiMDNjYzg1YTNlM2RhM2U2YWFjN2M4YzVjZDM0YTRiYjA2NDJkZQ'
        },
        thumbnail: {
          altText: 'Lip & Cheek Stain',
          format: 'thumbnail',
          galleryIndex: 2,
          imageType: 'GALLERY',
          url:
            'https://localhost:9002/medias/?context=bWFzdGVyfGltYWdlc3w2NDEwfGltYWdlL2pwZWd8aW1hZ2VzL2g2Yy9oNGQvODc5NjEyNTkyMTMxMC5qcGd8OGNmZmMyMDRlOGQ0M2VlNmMzMzI5MjZjNzVkZWE1ODZlNTBmYTgxMTY5OTlkODlkYmExOTIyMDgwODBmMzAzMg'
        }
      },
      {
        zoom: {
          altText: 'Lip & Cheek Stain',
          format: 'zoom',
          galleryIndex: 3,
          imageType: 'GALLERY',
          url:
            'https://localhost:9002/medias/?context=bWFzdGVyfGltYWdlc3w0NzQwN3xpbWFnZS9qcGVnfGltYWdlcy9oOTgvaGQ4Lzg3OTYxMjIxODU3NTguanBnfGY2MjRlMGE4YmU5NjY5ZDg1OWI0OGY3ZmYyNzZmOTdmZjNkMmI3OTc4Y2I4ODNkNjQwZjhkNzQ3YjVjYTVhOGE'
        },
        product: {
          altText: 'Lip & Cheek Stain',
          format: 'product',
          galleryIndex: 3,
          imageType: 'GALLERY',
          url:
            'https://localhost:9002/medias/?context=bWFzdGVyfGltYWdlc3w0NzQwN3xpbWFnZS9qcGVnfGltYWdlcy9oNmYvaDVhLzg3OTYxMjQwODYzMDIuanBnfDkwOGQwMWQ0MTM1Y2QyNDJiNjE4Y2JhNGE0NGFkZDVlNTdhYjY1Y2E2OTEzNTZjNWQ3YWM4MGEyMzg5ZmNhNTc'
        },
        thumbnail: {
          altText: 'Lip & Cheek Stain',
          format: 'thumbnail',
          galleryIndex: 3,
          imageType: 'GALLERY',
          url:
            'https://localhost:9002/medias/?context=bWFzdGVyfGltYWdlc3w0NzQwN3xpbWFnZS9qcGVnfGltYWdlcy9oNzgvaGQwLzg3OTYxMjU5ODY4NDYuanBnfDMyMjg4YmEwN2QxZjNkMmZhZTAyNmUyOGMwZTBjNGZlMGU2MjBlMzg5NjRlMDRmZTdmMWYxZDQxM2RmNWY3OTQ'
        }
      }
    ]
  },
  isVariant: false,
  keyIngredients: [],
  manufacturer: 'The Body Shop',
  name: 'Lip & Cheek Stain',
  numberOfReviews: 0,
  price: {
    currencyIso: 'GBP',
    formattedValue: '£8.00',
    pricePerMetric: '',
    priceType: PriceType['FROM'],
    value: 8
  },
  priceRange: {
    maxPrice: {
      currencyIso: 'GBP',
      value: 8
    },
    minPrice: {
      currencyIso: 'GBP',
      value: 8
    }
  },
  purchasable: false,
  stock: {
    stockLevel: 0,
    stockLevelStatus: 'outOfStock'
  },
  straplines: [],
  summary: '',
  url: '/Make-up/Lips/Lip-%26-Cheek-Stain/p/p000657',
  variantOptions: [
    {
      code: '1096109',
      colour: '#C51F71',
      colourName: '001 Pink Hibiscusis',
      name: 'Lip & Cheek Stain 7.2ml',
      priceData: {
        currencyIso: 'GBP',
        formattedValue: '£8.00',
        pricePerMetric: '£111.11/100 ML',
        priceType: PriceType['BUY'],
        value: 8
      },
      selectedFlag: false,
      size: '7.2ml',
      stock: {
        stockLevel: 10,
        stockLevelStatus: 'inStock'
      },
      technicalIngredients:
        'Aqua/Water/Eau, Hydrogenated Polydecene, Isohexadecane, Polyglyceryl-3 Diisostearate, Ethylhexyl Palmitate, Glycerin, Pentylene Glycol, Sclerocarya Birrea Seed Oil',
      url: '/Make-up/Lips/Lip-%26-Cheek-Stain/p/1096109',
      variantType: 'COLOUR'
    },
    {
      code: '1096148',
      colour: '#B62657',
      colourName: '029 Deep Berryis',
      name: 'Lip & Cheek Stain 7.2ml',
      priceData: {
        currencyIso: 'GBP',
        formattedValue: '£8.00',
        pricePerMetric: '£111.11/100 ML',
        priceType: PriceType['BUY'],
        value: 8
      },
      selectedFlag: false,
      size: '7.2ml',
      stock: {
        stockLevel: 10,
        stockLevelStatus: 'inStock'
      },
      technicalIngredients:
        'Aqua/Water/Eau, Hydrogenated Polydecene, Isohexadecane, Polyglyceryl-3 Diisostearate, Ethylhexyl Palmitate, Glycerin, Pentylene Glycol, Sclerocarya Birrea Seed Oil',
      url: '/Make-up/Lips/Lip-%26-Cheek-Stain/p/1096148',
      variantType: 'COLOUR'
    },
    {
      code: '1096261',
      colour: '#C4171D',
      colourName: '003 Red Pomegranate',
      name: 'Lip & Cheek Stain 7.2ml',
      priceData: {
        currencyIso: 'GBP',
        formattedValue: '£8.00',
        pricePerMetric: '£111.11/100 ML',
        priceType: PriceType['BUY'],
        value: 8
      },
      selectedFlag: false,
      size: '7.2ml',
      stock: {
        stockLevel: 10,
        stockLevelStatus: 'inStock'
      },
      technicalIngredients:
        'Aqua/Water/Eau, Hydrogenated Polydecene, Isohexadecane, Polyglyceryl-3 Diisostearate, Ethylhexyl Palmitate, Glycerin, Pentylene Glycol, Sclerocarya Birrea Seed Oil',
      url: '/Make-up/Lips/Lip-%26-Cheek-Stain/p/1096261',
      variantType: 'COLOUR'
    }
  ],
  nameHtml: 'Lip & Cheek Stain'
};

export const DUMMY_PLP_CUSTOM_PRODUCT_COLOUR_VARIANT_OUT_STOCK: ICustomProduct = {
  availableForPickup: false,
  breadcrumbCategories: [
    {
      code: 'c00007',
      name: 'Make-up',
      url: '/Make-up/c/c00007'
    },
    {
      code: 'c00066',
      name: 'Lips',
      url: '/Make-up/Lips/c/c00066'
    }
  ],
  categories: [
    {
      code: 'c00066',
      name: 'Lips',
      url: '/Make-up/Lips/c/c00066'
    }
  ],
  code: 'p000657',
  configurable: false,
  description:
    'This dual-¬purpose liquid stain gives lips and cheeks a natural pop of buildable colour. Use under lip gloss for a show-¬stopping -pout and dab onto cheeks for a natural-looking rosy glow.',
  images: {
    PRIMARY: {
      zoom: {
        altText: 'Lip & Cheek Stain',
        format: 'zoom',
        imageType: 'PRIMARY',
        url:
          'https://localhost:9002/medias/?context=bWFzdGVyfGltYWdlc3w1MTg2fGltYWdlL2pwZWd8aW1hZ2VzL2gzYy9oNDkvODc5NjEyMTk4OTE1MC5qcGd8OGM1ODUxOWI0YzhlYTYzZDNmMjQ0YjdmYzc4NGZjNjEwYmJkYTk5MjhjYmVlZWM3ODMzNzFkOTNjZTRkZjVlYg'
      },
      product: {
        altText: 'Lip & Cheek Stain',
        format: 'product',
        imageType: 'PRIMARY',
        url:
          'https://localhost:9002/medias/?context=bWFzdGVyfGltYWdlc3w1MTg2fGltYWdlL2pwZWd8aW1hZ2VzL2hkOS9oNDAvODc5NjEyMzg4OTY5NC5qcGd8YzhmZTRlNjIxYTU0ZmM5YmYyMDQ3Y2YyZWU1YTI2NTU5ZDRlOGEzZTM4OGEzMmNhMTdiOTY0ZTZkN2U2MGYxZA'
      },
      thumbnail: {
        altText: 'Lip & Cheek Stain',
        format: 'thumbnail',
        imageType: 'PRIMARY',
        url:
          'https://localhost:9002/medias/?context=bWFzdGVyfGltYWdlc3w1MTg2fGltYWdlL2pwZWd8aW1hZ2VzL2g1MS9oY2IvODc5NjEyNTc5MDIzOC5qcGd8YzBiYTg5ZmQ0OGFlMmU2OGE4ZTIyZDg3OTE0NDA5Njc4ZDAyZmVjM2YwZWMzOTRkYmY2MmUzZWE3YWNkNWJmZg'
      }
    },
    GALLERY: [
      {
        zoom: {
          altText: 'Lip & Cheek Stain',
          format: 'zoom',
          galleryIndex: 0,
          imageType: 'GALLERY',
          url:
            'https://localhost:9002/medias/?context=bWFzdGVyfGltYWdlc3w1MTg2fGltYWdlL2pwZWd8aW1hZ2VzL2gzYy9oNDkvODc5NjEyMTk4OTE1MC5qcGd8OGM1ODUxOWI0YzhlYTYzZDNmMjQ0YjdmYzc4NGZjNjEwYmJkYTk5MjhjYmVlZWM3ODMzNzFkOTNjZTRkZjVlYg'
        },
        product: {
          altText: 'Lip & Cheek Stain',
          format: 'product',
          galleryIndex: 0,
          imageType: 'GALLERY',
          url:
            'https://localhost:9002/medias/?context=bWFzdGVyfGltYWdlc3w1MTg2fGltYWdlL2pwZWd8aW1hZ2VzL2hkOS9oNDAvODc5NjEyMzg4OTY5NC5qcGd8YzhmZTRlNjIxYTU0ZmM5YmYyMDQ3Y2YyZWU1YTI2NTU5ZDRlOGEzZTM4OGEzMmNhMTdiOTY0ZTZkN2U2MGYxZA'
        },
        thumbnail: {
          altText: 'Lip & Cheek Stain',
          format: 'thumbnail',
          galleryIndex: 0,
          imageType: 'GALLERY',
          url:
            'https://localhost:9002/medias/?context=bWFzdGVyfGltYWdlc3w1MTg2fGltYWdlL2pwZWd8aW1hZ2VzL2g1MS9oY2IvODc5NjEyNTc5MDIzOC5qcGd8YzBiYTg5ZmQ0OGFlMmU2OGE4ZTIyZDg3OTE0NDA5Njc4ZDAyZmVjM2YwZWMzOTRkYmY2MmUzZWE3YWNkNWJmZg'
        }
      },
      {
        zoom: {
          altText: 'Lip & Cheek Stain',
          format: 'zoom',
          galleryIndex: 1,
          imageType: 'GALLERY',
          url:
            'https://localhost:9002/medias/?context=bWFzdGVyfGltYWdlc3wxNDk4NHxpbWFnZS9qcGVnfGltYWdlcy9oZTEvaDQ2Lzg3OTYxMjIwNTQ2ODYuanBnfDdiNzg0NjU3YmVmN2JkMzM2ZmE1YzE0NjU1MDY2YzU3NjRjZTI3YzdiZWFhNTIzOWM3MzIwZTAwODY0MDVjMGM'
        },
        product: {
          altText: 'Lip & Cheek Stain',
          format: 'product',
          galleryIndex: 1,
          imageType: 'GALLERY',
          url:
            'https://localhost:9002/medias/?context=bWFzdGVyfGltYWdlc3wxNDk4NHxpbWFnZS9qcGVnfGltYWdlcy9oNTQvaDRiLzg3OTYxMjM5NTUyMzAuanBnfGM4ZmZhNTAyNGYxY2U5Y2MyNTkyYjNhNjQzYTM5ODc1NjUxYzEyNTMyOWM4N2Q4NmU0NDQxOTAwYTg3NjE3M2Q'
        },
        thumbnail: {
          altText: 'Lip & Cheek Stain',
          format: 'thumbnail',
          galleryIndex: 1,
          imageType: 'GALLERY',
          url:
            'https://localhost:9002/medias/?context=bWFzdGVyfGltYWdlc3wxNDk4NHxpbWFnZS9qcGVnfGltYWdlcy9oYzEvaDNlLzg3OTYxMjU4NTU3NzQuanBnfGI2ZmUyNmNjZTJmZTZmZTY3NDNkODBjNjcxNjJhNjJjY2E3YTdkN2MwMTNmNmY5NDhkOGMzMjUzYmEyZjVlNGU'
        }
      },
      {
        zoom: {
          altText: 'Lip & Cheek Stain',
          format: 'zoom',
          galleryIndex: 2,
          imageType: 'GALLERY',
          url:
            'https://localhost:9002/medias/?context=bWFzdGVyfGltYWdlc3w2NDEwfGltYWdlL2pwZWd8aW1hZ2VzL2g0Yy9oNDUvODc5NjEyMjEyMDIyMi5qcGd8NGJmZGNjYzAzODFmNzMzZDVmNTkwY2RjZWE2Zjg2YTZiZWZiMDU0ZjhmNzM0ZjBmY2JiMGZiMGJjOTQ1Y2E5Mg'
        },
        product: {
          altText: 'Lip & Cheek Stain',
          format: 'product',
          galleryIndex: 2,
          imageType: 'GALLERY',
          url:
            'https://localhost:9002/medias/?context=bWFzdGVyfGltYWdlc3w2NDEwfGltYWdlL2pwZWd8aW1hZ2VzL2hjOC9oNDQvODc5NjEyNDAyMDc2Ni5qcGd8OTA0MGQ0NDk5MzFhY2YxZDg5MmFiYTYxNmZiMDNjYzg1YTNlM2RhM2U2YWFjN2M4YzVjZDM0YTRiYjA2NDJkZQ'
        },
        thumbnail: {
          altText: 'Lip & Cheek Stain',
          format: 'thumbnail',
          galleryIndex: 2,
          imageType: 'GALLERY',
          url:
            'https://localhost:9002/medias/?context=bWFzdGVyfGltYWdlc3w2NDEwfGltYWdlL2pwZWd8aW1hZ2VzL2g2Yy9oNGQvODc5NjEyNTkyMTMxMC5qcGd8OGNmZmMyMDRlOGQ0M2VlNmMzMzI5MjZjNzVkZWE1ODZlNTBmYTgxMTY5OTlkODlkYmExOTIyMDgwODBmMzAzMg'
        }
      },
      {
        zoom: {
          altText: 'Lip & Cheek Stain',
          format: 'zoom',
          galleryIndex: 3,
          imageType: 'GALLERY',
          url:
            'https://localhost:9002/medias/?context=bWFzdGVyfGltYWdlc3w0NzQwN3xpbWFnZS9qcGVnfGltYWdlcy9oOTgvaGQ4Lzg3OTYxMjIxODU3NTguanBnfGY2MjRlMGE4YmU5NjY5ZDg1OWI0OGY3ZmYyNzZmOTdmZjNkMmI3OTc4Y2I4ODNkNjQwZjhkNzQ3YjVjYTVhOGE'
        },
        product: {
          altText: 'Lip & Cheek Stain',
          format: 'product',
          galleryIndex: 3,
          imageType: 'GALLERY',
          url:
            'https://localhost:9002/medias/?context=bWFzdGVyfGltYWdlc3w0NzQwN3xpbWFnZS9qcGVnfGltYWdlcy9oNmYvaDVhLzg3OTYxMjQwODYzMDIuanBnfDkwOGQwMWQ0MTM1Y2QyNDJiNjE4Y2JhNGE0NGFkZDVlNTdhYjY1Y2E2OTEzNTZjNWQ3YWM4MGEyMzg5ZmNhNTc'
        },
        thumbnail: {
          altText: 'Lip & Cheek Stain',
          format: 'thumbnail',
          galleryIndex: 3,
          imageType: 'GALLERY',
          url:
            'https://localhost:9002/medias/?context=bWFzdGVyfGltYWdlc3w0NzQwN3xpbWFnZS9qcGVnfGltYWdlcy9oNzgvaGQwLzg3OTYxMjU5ODY4NDYuanBnfDMyMjg4YmEwN2QxZjNkMmZhZTAyNmUyOGMwZTBjNGZlMGU2MjBlMzg5NjRlMDRmZTdmMWYxZDQxM2RmNWY3OTQ'
        }
      }
    ]
  },
  isVariant: false,
  keyIngredients: [],
  manufacturer: 'The Body Shop',
  name: 'Lip & Cheek Stain',
  numberOfReviews: 0,
  price: {
    currencyIso: 'GBP',
    formattedValue: '£8.00',
    pricePerMetric: '',
    priceType: PriceType['FROM'],
    value: 8
  },
  priceRange: {
    maxPrice: {
      currencyIso: 'GBP',
      value: 8
    },
    minPrice: {
      currencyIso: 'GBP',
      value: 8
    }
  },
  purchasable: false,
  stock: {
    stockLevel: 0,
    stockLevelStatus: 'outOfStock'
  },
  straplines: [],
  summary: '',
  url: '/Make-up/Lips/Lip-%26-Cheek-Stain/p/p000657',
  variantOptions: [
    {
      code: '1096109',
      colour: '#C51F71',
      colourName: '001 Pink Hibiscusis',
      name: 'Lip & Cheek Stain 7.2ml',
      priceData: {
        currencyIso: 'GBP',
        formattedValue: '£8.00',
        pricePerMetric: '£111.11/100 ML',
        priceType: PriceType['BUY'],
        value: 8
      },
      selectedFlag: false,
      size: '7.2ml',
      stock: {
        stockLevel: 0,
        stockLevelStatus: 'outOfStock'
      },
      technicalIngredients:
        'Aqua/Water/Eau, Hydrogenated Polydecene, Isohexadecane, Polyglyceryl-3 Diisostearate, Ethylhexyl Palmitate, Glycerin, Pentylene Glycol, Sclerocarya Birrea Seed Oil',
      url: '/Make-up/Lips/Lip-%26-Cheek-Stain/p/1096109',
      variantType: 'COLOUR'
    },
    {
      code: '1096148',
      colour: '#B62657',
      colourName: '029 Deep Berryis',
      name: 'Lip & Cheek Stain 7.2ml',
      priceData: {
        currencyIso: 'GBP',
        formattedValue: '£8.00',
        pricePerMetric: '£111.11/100 ML',
        priceType: PriceType['BUY'],
        value: 8
      },
      selectedFlag: false,
      size: '7.2ml',
      stock: {
        stockLevel: 0,
        stockLevelStatus: 'outOfStock'
      },
      technicalIngredients:
        'Aqua/Water/Eau, Hydrogenated Polydecene, Isohexadecane, Polyglyceryl-3 Diisostearate, Ethylhexyl Palmitate, Glycerin, Pentylene Glycol, Sclerocarya Birrea Seed Oil',
      url: '/Make-up/Lips/Lip-%26-Cheek-Stain/p/1096148',
      variantType: 'COLOUR'
    },
    {
      code: '1096261',
      colour: '#C4171D',
      colourName: '003 Red Pomegranate',
      name: 'Lip & Cheek Stain 7.2ml',
      priceData: {
        currencyIso: 'GBP',
        formattedValue: '£8.00',
        pricePerMetric: '£111.11/100 ML',
        priceType: PriceType['BUY'],
        value: 8
      },
      selectedFlag: false,
      size: '7.2ml',
      stock: {
        stockLevel: 0,
        stockLevelStatus: 'outOfStock'
      },
      technicalIngredients:
        'Aqua/Water/Eau, Hydrogenated Polydecene, Isohexadecane, Polyglyceryl-3 Diisostearate, Ethylhexyl Palmitate, Glycerin, Pentylene Glycol, Sclerocarya Birrea Seed Oil',
      url: '/Make-up/Lips/Lip-%26-Cheek-Stain/p/1096261',
      variantType: 'COLOUR'
    }
  ],
  nameHtml: 'Lip & Cheek Stain'
};

export const DUMMY_PLP_CUSTOM_PRODUCT_SIZE_VARIANT_IN_STOCK: ICustomProduct = {
  availableForPickup: false,
  breadcrumbCategories: [
    {
      code: 'c00001',
      name: 'Body',
      url: '/Body/c/c00001'
    },
    {
      code: 'c00014',
      name: 'Body Butters',
      url: '/Body/Body-Butters/c/c00014'
    }
  ],
  categories: [
    {
      code: 'c00014',
      name: 'Body Butters',
      url: '/Body/Body-Butters/c/c00014'
    }
  ],
  code: 'p000247',
  configurable: false,
  description:
    '<p> Transport the hammam environment to your bathroom and give your skin a dose of ultra-rich hydration, when you apply this nourishing wild argan oil body butter. With 48hr moisturising properties, your skin will stay softer for longer.</p> <ul>\t<li>Body Moisturiser</li> <li>Sublime nourishment</li> <li>Suitable for very dry skin</li> <li>Subtle, sophisticated, warming scent</li> <li>48hr hydration</li> <li>Community Trade argan oil from Morocco</li></ul>',
  isVariant: false,
  keyIngredients: [],
  manufacturer: 'The Body Shop',
  name: 'Wild Argan Oil Sublime Nourishing Body Butter',
  numberOfReviews: 0,
  price: {
    currencyIso: 'GBP',
    formattedValue: '£6.00',
    pricePerMetric: '',

    value: 6.0
  },
  priceRange: {
    maxPrice: {
      currencyIso: 'GBP',
      value: 15.0
    },
    minPrice: {
      currencyIso: 'GBP',
      value: 6.0
    }
  },
  purchasable: false,
  stock: {
    stockLevel: 0,
    stockLevelStatus: 'outOfStock'
  },
  straplines: [],
  summary: '',
  url: '/Body/Body-Butters/Wild-Argan-Oil-Sublime-Nourishing-Body-Butter/p/p000247',
  variantOptions: [
    {
      code: '1094188',
      emailMeWhenInStockToggle: false,
      maxOrderProductQuantity: 10,
      name: 'Wild Argan Oil Sublime Nourishing Body Butter 50ml',
      priceData: {
        currencyIso: 'GBP',
        formattedValue: '£6.00',
        pricePerMetric: '£12.00/100 ML',

        value: 6.0
      },
      selectedFlag: false,
      size: '50ml',
      stock: {
        stockLevel: 10,
        stockLevelStatus: 'inStock'
      },
      technicalIngredients:
        'Aqua/Water/Eau, Hydrogenated Polydecene, Isohexadecane, Polyglyceryl-3 Diisostearate, Ethylhexyl Palmitate, Glycerin, Pentylene Glycol, Sclerocarya Birrea Seed Oil',
      url: '/Body/Body-Butters/Wild-Argan-Oil-Sublime-Nourishing-Body-Butter/p/1094188',
      variantType: 'SIZE'
    },
    {
      code: '1094138',
      emailMeWhenInStockToggle: false,

      maxOrderProductQuantity: 10,
      name: 'Wild Argan Oil Sublime Nourishing Body Butter 200ml',
      priceData: {
        currencyIso: 'GBP',
        formattedValue: '£15.00',
        pricePerMetric: '£7.50/100 ML',

        value: 15.0
      },
      selectedFlag: false,
      size: '200ml',
      stock: {
        stockLevel: 10,
        stockLevelStatus: 'inStock'
      },
      technicalIngredients:
        'Aqua/Water/Eau, Hydrogenated Polydecene, Isohexadecane, Polyglyceryl-3 Diisostearate, Ethylhexyl Palmitate, Glycerin, Pentylene Glycol, Sclerocarya Birrea Seed Oil',
      url: '/Body/Body-Butters/Wild-Argan-Oil-Sublime-Nourishing-Body-Butter/p/1094138',
      variantType: 'SIZE'
    }
  ]
};

export const DUMMY_PLP_CUSTOM_PRODUCT_SIZE_VARIANT_OUT_STOCK: ICustomProduct = {
  availableForPickup: false,
  breadcrumbCategories: [
    {
      code: 'c00001',
      name: 'Body',
      url: '/Body/c/c00001'
    },
    {
      code: 'c00014',
      name: 'Body Butters',
      url: '/Body/Body-Butters/c/c00014'
    }
  ],
  categories: [
    {
      code: 'c00014',
      name: 'Body Butters',
      url: '/Body/Body-Butters/c/c00014'
    }
  ],
  code: 'p000247',
  configurable: false,
  description:
    '<p> Transport the hammam environment to your bathroom and give your skin a dose of ultra-rich hydration, when you apply this nourishing wild argan oil body butter. With 48hr moisturising properties, your skin will stay softer for longer.</p> <ul>\t<li>Body Moisturiser</li> <li>Sublime nourishment</li> <li>Suitable for very dry skin</li> <li>Subtle, sophisticated, warming scent</li> <li>48hr hydration</li> <li>Community Trade argan oil from Morocco</li></ul>',
  isVariant: false,
  keyIngredients: [],
  manufacturer: 'The Body Shop',
  name: 'Wild Argan Oil Sublime Nourishing Body Butter',
  numberOfReviews: 0,
  price: {
    currencyIso: 'GBP',
    formattedValue: '£6.00',
    pricePerMetric: '',

    value: 6.0
  },
  priceRange: {
    maxPrice: {
      currencyIso: 'GBP',
      value: 15.0
    },
    minPrice: {
      currencyIso: 'GBP',
      value: 6.0
    }
  },
  purchasable: false,
  stock: {
    stockLevel: 0,
    stockLevelStatus: 'outOfStock'
  },
  straplines: [],
  summary: '',
  url: '/Body/Body-Butters/Wild-Argan-Oil-Sublime-Nourishing-Body-Butter/p/p000247',
  variantOptions: [
    {
      code: '1094188',
      emailMeWhenInStockToggle: false,
      maxOrderProductQuantity: 10,
      name: 'Wild Argan Oil Sublime Nourishing Body Butter 50ml',
      priceData: {
        currencyIso: 'GBP',
        formattedValue: '£6.00',
        pricePerMetric: '£12.00/100 ML',

        value: 6.0
      },
      selectedFlag: false,
      size: '50ml',
      stock: {
        stockLevel: 0,
        stockLevelStatus: 'outOfStock'
      },
      technicalIngredients:
        'Aqua/Water/Eau, Hydrogenated Polydecene, Isohexadecane, Polyglyceryl-3 Diisostearate, Ethylhexyl Palmitate, Glycerin, Pentylene Glycol, Sclerocarya Birrea Seed Oil',
      url: '/Body/Body-Butters/Wild-Argan-Oil-Sublime-Nourishing-Body-Butter/p/1094188',
      variantType: 'SIZE'
    },
    {
      code: '1094138',
      emailMeWhenInStockToggle: false,

      maxOrderProductQuantity: 10,
      name: 'Wild Argan Oil Sublime Nourishing Body Butter 200ml',
      priceData: {
        currencyIso: 'GBP',
        formattedValue: '£15.00',
        pricePerMetric: '£7.50/100 ML',

        value: 15.0
      },
      selectedFlag: false,
      size: '200ml',
      stock: {
        stockLevel: 0,
        stockLevelStatus: 'outOfStock'
      },
      technicalIngredients:
        'Aqua/Water/Eau, Hydrogenated Polydecene, Isohexadecane, Polyglyceryl-3 Diisostearate, Ethylhexyl Palmitate, Glycerin, Pentylene Glycol, Sclerocarya Birrea Seed Oil',
      url: '/Body/Body-Butters/Wild-Argan-Oil-Sublime-Nourishing-Body-Butter/p/1094138',
      variantType: 'SIZE'
    }
  ]
};

export const PRODUCT_STORE_DATA_VARIANT_1 = {
  code: '1096109',
  colour: '#C51F71',
  colourName: '001 Pink Hibiscusis',
  name: 'Lip & Cheek Stain 7.2ml',
  priceData: {
    currencyIso: 'GBP',
    formattedValue: '£8.00',
    pricePerMetric: '£111.11/100 ML',
    priceType: PriceType['BUY'],
    value: 8
  },
  selectedFlag: false,
  size: '7.2ml',
  stock: {
    stockLevel: 10,
    stockLevelStatus: 'inStock'
  },
  technicalIngredients:
    'Aqua/Water/Eau, Hydrogenated Polydecene, Isohexadecane, Polyglyceryl-3 Diisostearate, Ethylhexyl Palmitate, Glycerin, Pentylene Glycol, Sclerocarya Birrea Seed Oil',
  url: '/Make-up/Lips/Lip-%26-Cheek-Stain/p/1096109',
  variantType: 'COLOUR'
};

export const PRODUCT_STORE_DATA_VARIANT_2 = {
  code: '1096109',
  colour: '#C51F71',
  colourName: '001 Pink Hibiscusis',
  name: 'Lip & Cheek Stain 7.2ml',
  priceData: {
    currencyIso: 'GBP',
    formattedValue: '£8.00',
    pricePerMetric: '£111.11/100 ML',
    priceType: PriceType['BUY'],
    value: 8
  },
  selectedFlag: false,
  size: '7.2ml',
  stock: {
    stockLevel: 10,
    stockLevelStatus: 'inStock'
  },
  technicalIngredients:
    'Aqua/Water/Eau, Hydrogenated Polydecene, Isohexadecane, Polyglyceryl-3 Diisostearate, Ethylhexyl Palmitate, Glycerin, Pentylene Glycol, Sclerocarya Birrea Seed Oil',
  url: '/Make-up/Lips/Lip-%26-Cheek-Stain/p/1096109',
  variantType: 'COLOUR'
};

export const MARKET_SELECTOR_CMS_DATA = {
  uid: 'TbsMarketSelector',
  uuid:
    'eyJpdGVtSWQiOiJUYnNNYXJrZXRTZWxlY3RvciIsImNhdGFsb2dJZCI6InRic0dsb2JhbENvbnRlbnRDYXRhbG9nIiwiY2F0YWxvZ1ZlcnNpb24iOiJPbmxpbmUifQ==',
  typeCode: 'MarketSelectorCMSComponent',
  modifiedTime: '2019-09-30T14:14:05.411+02:00',
  name: 'Market Selector Site',
  container: 'false',
  markets:
    "[{'name':'Australia','url':'/thebodyshop-au/en_AU/AUD/'},{'name':'Austria','url':'/thebodyshop-at/de_AT/EUR/'},{'name':'Brazil','url':'https://www.thebodyshop.com.br/'},{'name':'Canada','url':'/thebodyshop-ca/en_CA/CAD/'},{'name':'Denmark','url':'/thebodyshop-dk/da/DKK/'},{'name':'France','url':'/thebodyshop-fr/fr/EUR/'},{'name':'Germany','url':'/thebodyshop-de/de/EUR/'},{'name':'Hong Kong','url':'/thebodyshop-hk/en_HK/HKD/'},{'name':'Netherlands','url':'/thebodyshop-nl/nl/EUR/'},{'name':'Singapore','url':'/thebodyshop-sg/en_SG/SGD/'},{'name':'Spain','url':'/thebodyshop-es/es/EUR/'},{'name':'Sweden','url':'/thebodyshop-se/sv/SEK/'},{'name':'United Kingdom','url':'/thebodyshop-uk/en_GB/GBP/'},{'name':'USA','url':'/thebodyshop-us/en_US/USD/'}]"
};

export const MOCK_DUMMY_SEARCH_MODEL = {
  type: 'productCategorySearchPageWsDTO',

  breadcrumbCategories: [],
  breadcrumbs: [],
  facets: [],
  freeTextSearch: '',
  pagination: { currentPage: 0, pageSize: 10, sort: 'relevance', totalPages: 2, totalResults: 13 },
  products: [],
  sorts: [
    {
      code: 'relevance',
      name: 'Superventas',
      selected: true
    },
    {
      code: 'rated-desc',
      name: 'Top Rated',
      selected: false
    },
    {
      code: 'price-asc',
      name: 'Precio (de menor a mayor)',
      selected: false
    },
    {
      code: 'price-desc',
      name: 'Precio (de mayor a menor)',
      selected: false
    }
  ]
};

export const DUMMY_DELIVERY_MODES: DeliveryMode[] = [
  {
    code: 'uk-click-and-collect',
    deliveryCost: {
      currencyIso: 'GBP',
      formattedValue: '£0.00',
      priceType: PriceType['BUY'],
      value: 0.0
    },
    description: 'Click and Collect',
    name: 'Click and Collect'
  },
  {
    code: 'UK-1-ECONOMY',
    deliveryCost: {
      currencyIso: 'GBP',
      formattedValue: '£2.49',
      priceType: PriceType['BUY'],
      value: 2.49
    },
    description: 'Economy Delivery',
    name: 'Economy Delivery'
  }
];

export const DUMMY_WISHLIST: Wishlist[] = [
  {
    wishlistId: '1234567890',
    wishlistName: 'Xmas 2019',
    created: '27 Nov 2019',
    products: [
      {
        averageRating: 3.3,
        code: '1096148',
        isVariant: true,
        multiVariant: false,
        name: 'Lip & Cheek Stain',
        price: {
          currencyIso: 'GBP',
          formattedValue: '£8.00',
          value: 8.0
        },
        stock: {
          stockLevelStatus: 'inStock'
        },
        url: '/Make-up/Lips/Lip-%26-Cheek-Stain/p/1096148'
      }
    ]
  }
];

export const DUMMY_WISHLISTS: Wishlists = {
  addFromCart: false,
  cartToWishlistModal: {
    isOpen: false
  },
  wishlists: [
    {
      wishlistId: '1234567890',
      wishlistName: 'Xmas 2019',
      created: '27 Nov 2019',
      products: [
        {
          averageRating: 3.3,
          code: '1096148',
          isVariant: true,
          multiVariant: false,
          name: 'Lip & Cheek Stain',
          price: {
            currencyIso: 'GBP',
            formattedValue: '£8.00',
            value: 8.0
          },
          stock: {
            stockLevelStatus: 'inStock'
          },
          url: '/Make-up/Lips/Lip-%26-Cheek-Stain/p/1096148'
        }
      ]
    },
    {
      wishlistId: '1234567890',
      wishlistName: 'Xmas 2019',
      created: '27 Nov 2019',
      products: [
        {
          averageRating: 1.8,
          code: 'p002171v',
          name: 'Matte Lip Liquid',
          price: {
            currencyIso: 'GBP',
            formattedValue: '£7.00',
            value: 7.0
          },
          stock: {
            stockLevelStatus: 'inStock'
          },
          url: '/Make-up/Lips/Matte-Lip-Liquid/p/p002171v'
        }
      ]
    }
  ]
};

export const DUMMY_WISHLISTS_PRODUCTS = {
  products: [
    {
      code: 'p002419v',
      emailMeWhenInStockToggle: false,
      findInStoreEnabled: false,
      isVariant: false,
      multiVariant: false,
      name: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
      potentialPromotions: [],
      price: {
        currencyIso: 'GBP',
        formattedValue: '£18.00',
        loyaltyPoints: 18,
        pricePerMetric: '£9.00/100 ML',
        priceType: 'BUY',
        value: 18.0
      },
      stock: {
        stockLevel: 10,
        stockLevelStatus: 'inStock'
      },
      url: '/Body/Body-Massage-Oils/Spa-of-the-World%E2%84%A2-Thai-Makrut-Lime-Firming-Oil/p/p002419v',
      variantOptions: [
        {
          code: 'p002419v',
          emailMeWhenInStockToggle: false,
          images: [
            {
              product: {
                altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
                galleryIndex: 0,
                url:
                  'https://i1.adis.ws/i/thebodyshop/olive-nourishing-body-butter-1092126-olivenourishingbodybutter50ml-1-640x640'
              },
              thumbnail: {
                altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
                galleryIndex: 0,
                url:
                  'https://i1.adis.ws/i/thebodyshop/olive-nourishing-body-butter-1092126-olivenourishingbodybutter50ml-1-640x640?h=96'
              },
              zoom: {
                altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
                galleryIndex: 0,
                url:
                  'https://i1.adis.ws/i/thebodyshop/olive-nourishing-body-butter-1092126-olivenourishingbodybutter50ml-1-640x640'
              }
            },
            {
              product: {
                altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
                galleryIndex: 1,
                url:
                  'https://i1.adis.ws/i/thebodyshop/pink-grapefruit-energising-body-butter-1016297-pinkgrapefruitenergisingbodybutter200ml-1-640x640'
              },
              thumbnail: {
                altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
                galleryIndex: 1,
                url:
                  'https://i1.adis.ws/i/thebodyshop/pink-grapefruit-energising-body-butter-1016297-pinkgrapefruitenergisingbodybutter200ml-1-640x640?h=96'
              },
              zoom: {
                altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
                galleryIndex: 1,
                url:
                  'https://i1.adis.ws/i/thebodyshop/pink-grapefruit-energising-body-butter-1016297-pinkgrapefruitenergisingbodybutter200ml-1-640x640'
              }
            },
            {
              product: {
                altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
                galleryIndex: 2,
                url: 'https://i1.adis.ws/i/thebodyshop/vitamin-e-night-cream-1094164-vitaminenightcream50ml-4-640x640'
              },
              thumbnail: {
                altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
                galleryIndex: 2,
                url:
                  'https://i1.adis.ws/i/thebodyshop/vitamin-e-night-cream-1094164-vitaminenightcream50ml-4-640x640?h=96'
              },
              zoom: {
                altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
                galleryIndex: 2,
                url: 'https://i1.adis.ws/i/thebodyshop/vitamin-e-night-cream-1094164-vitaminenightcream50ml-4-640x640'
              }
            },
            {
              product: {
                altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
                galleryIndex: 3,
                url: 'https://i1.adis.ws/i/thebodyshop/vitamin-e-night-cream-1094164-vitaminenightcream50ml-3-640x640'
              },
              thumbnail: {
                altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
                galleryIndex: 3,
                url:
                  'https://i1.adis.ws/i/thebodyshop/vitamin-e-night-cream-1094164-vitaminenightcream50ml-3-640x640?h=96'
              },
              zoom: {
                altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
                galleryIndex: 3,
                url: 'https://i1.adis.ws/i/thebodyshop/vitamin-e-night-cream-1094164-vitaminenightcream50ml-3-640x640'
              }
            }
          ],
          maxOrderProductQuantity: 10,
          name: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
          priceData: {
            currencyIso: 'GBP',
            formattedValue: '£18.00',
            loyaltyPoints: 18,
            pricePerMetric: '£9.00/100 ML',
            priceType: 'BUY',
            value: 18.0
          },
          selectedFlag: false,
          size: '200ml',
          stock: {
            stockLevel: 10,
            stockLevelStatus: 'inStock'
          }
        }
      ],
      wishlistCreationDate: '2020-01-14T12:23:57+0000'
    },
    {
      code: 'p002445v',
      emailMeWhenInStockToggle: false,
      findInStoreEnabled: false,
      isVariant: false,
      multiVariant: false,
      name: 'Tea Tree Anti-Imperfection Night Mask 200ml',
      potentialPromotions: [],
      price: {
        currencyIso: 'GBP',
        formattedValue: '£12.00',
        loyaltyPoints: 12,
        pricePerMetric: '£6.00/100 ML',
        priceType: 'BUY',
        value: 12.0
      },
      stock: {
        stockLevel: 10,
        stockLevelStatus: 'inStock'
      },
      url: '/Face/Face-Masks/Tea-Tree-Anti-Imperfection-Night-Mask/p/p002445v',
      variantOptions: [
        {
          code: 'p002445v',
          emailMeWhenInStockToggle: false,
          maxOrderProductQuantity: 10,
          name: 'Tea Tree Anti-Imperfection Night Mask 200ml',
          priceData: {
            currencyIso: 'GBP',
            formattedValue: '£12.00',
            loyaltyPoints: 12,
            pricePerMetric: '£6.00/100 ML',
            priceType: 'BUY',
            value: 12.0
          },
          selectedFlag: false,
          size: '200ml',
          stock: {
            stockLevel: 10,
            stockLevelStatus: 'inStock'
          }
        }
      ],
      wishlistCreationDate: '2020-01-14T12:23:57+0000'
    }
  ],
  unavailableProductCodes: []
};

export const DUMMY_ADDRESS_DEFAULT: Address = {
  companyName: 'Some Company',
  country: {
    isocode: 'GB',
    name: 'United Kingdom'
  },
  defaultAddress: true,
  firstName: 'First',
  formattedAddress: '',
  id: '00000000',
  lastName: 'Last',
  line1: 'address 1',
  line2: 'address 2',
  phone: '07999999999',
  postalCode: 'SW19',
  region: {},
  shippingAddress: true,
  title: '',
  titleCode: '',
  town: 'Town',
  visibleInAddressBook: true
};

export const DUMMY_WISHLIST_PRODUCT_LISTS = [
  {
    code: 'p002419v',
    emailMeWhenInStockToggle: false,
    findInStoreEnabled: false,
    isVariant: false,
    multiVariant: false,
    name: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
    price: {
      currencyIso: 'GBP',
      formattedValue: '£18.00',
      loyaltyPoints: 18,
      pricePerMetric: '£9.00/100 ML',
      priceType: 'BUY',
      value: 18
    },
    stock: {
      stockLevel: 10,
      stockLevelStatus: 'inStock'
    },
    url: '/Body/Body-Massage-Oils/Spa-of-the-World%E2%84%A2-Thai-Makrut-Lime-Firming-Oil/p/p002419v',
    variantOptions: [
      {
        code: 'p002419v',
        colour: '#C4171D',
        emailMeWhenInStockToggle: false,
        images: [
          {
            product: {
              altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
              galleryIndex: 0,
              url:
                'https://i1.adis.ws/i/thebodyshop/olive-nourishing-body-butter-1092126-olivenourishingbodybutter50ml-1-640x640'
            },
            thumbnail: {
              altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
              galleryIndex: 0,
              url:
                'https://i1.adis.ws/i/thebodyshop/olive-nourishing-body-butter-1092126-olivenourishingbodybutter50ml-1-640x640?h=96'
            },
            zoom: {
              altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
              galleryIndex: 0,
              url:
                'https://i1.adis.ws/i/thebodyshop/olive-nourishing-body-butter-1092126-olivenourishingbodybutter50ml-1-640x640'
            }
          },
          {
            product: {
              altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
              galleryIndex: 1,
              url:
                'https://i1.adis.ws/i/thebodyshop/pink-grapefruit-energising-body-butter-1016297-pinkgrapefruitenergisingbodybutter200ml-1-640x640'
            },
            thumbnail: {
              altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
              galleryIndex: 1,
              url:
                'https://i1.adis.ws/i/thebodyshop/pink-grapefruit-energising-body-butter-1016297-pinkgrapefruitenergisingbodybutter200ml-1-640x640?h=96'
            },
            zoom: {
              altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
              galleryIndex: 1,
              url:
                'https://i1.adis.ws/i/thebodyshop/pink-grapefruit-energising-body-butter-1016297-pinkgrapefruitenergisingbodybutter200ml-1-640x640'
            }
          },
          {
            product: {
              altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
              galleryIndex: 2,
              url: 'https://i1.adis.ws/i/thebodyshop/vitamin-e-night-cream-1094164-vitaminenightcream50ml-4-640x640'
            },
            thumbnail: {
              altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
              galleryIndex: 2,
              url:
                'https://i1.adis.ws/i/thebodyshop/vitamin-e-night-cream-1094164-vitaminenightcream50ml-4-640x640?h=96'
            },
            zoom: {
              altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
              galleryIndex: 2,
              url: 'https://i1.adis.ws/i/thebodyshop/vitamin-e-night-cream-1094164-vitaminenightcream50ml-4-640x640'
            }
          },
          {
            product: {
              altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
              galleryIndex: 3,
              url: 'https://i1.adis.ws/i/thebodyshop/vitamin-e-night-cream-1094164-vitaminenightcream50ml-3-640x640'
            },
            thumbnail: {
              altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
              galleryIndex: 3,
              url:
                'https://i1.adis.ws/i/thebodyshop/vitamin-e-night-cream-1094164-vitaminenightcream50ml-3-640x640?h=96'
            },
            zoom: {
              altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
              galleryIndex: 3,
              url: 'https://i1.adis.ws/i/thebodyshop/vitamin-e-night-cream-1094164-vitaminenightcream50ml-3-640x640'
            }
          }
        ],
        maxOrderProductQuantity: 15,
        name: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
        priceData: {
          currencyIso: 'GBP',
          formattedValue: '£18.00',
          loyaltyPoints: 18,
          pricePerMetric: '£9.00/100 ML',
          priceType: 'BUY',
          value: 18
        },
        selectedFlag: false,
        size: '200ml',
        stock: {
          stockLevel: 10,
          stockLevelStatus: 'inStock'
        }
      }
    ]
  }
];

export const DUMMY_WISHLIST_PRODUCT = {
  code: 'p002419v',
  emailMeWhenInStockToggle: false,
  findInStoreEnabled: false,
  isVariant: false,
  multiVariant: false,
  name: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
  price: {
    currencyIso: 'GBP',
    formattedValue: '£18.00',
    loyaltyPoints: 18,
    pricePerMetric: '£9.00/100 ML',
    priceType: 'BUY',
    value: 18
  },
  stock: {
    stockLevel: 10,
    stockLevelStatus: 'inStock'
  },
  url: '/Body/Body-Massage-Oils/Spa-of-the-World%E2%84%A2-Thai-Makrut-Lime-Firming-Oil/p/p002419v',
  variantOptions: [
    {
      code: 'p002419v',
      colour: '#C4171D',
      emailMeWhenInStockToggle: false,
      images: [
        {
          product: {
            altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
            galleryIndex: 0,
            url:
              'https://i1.adis.ws/i/thebodyshop/olive-nourishing-body-butter-1092126-olivenourishingbodybutter50ml-1-640x640'
          },
          thumbnail: {
            altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
            galleryIndex: 0,
            url:
              'https://i1.adis.ws/i/thebodyshop/olive-nourishing-body-butter-1092126-olivenourishingbodybutter50ml-1-640x640?h=96'
          },
          zoom: {
            altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
            galleryIndex: 0,
            url:
              'https://i1.adis.ws/i/thebodyshop/olive-nourishing-body-butter-1092126-olivenourishingbodybutter50ml-1-640x640'
          }
        },
        {
          product: {
            altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
            galleryIndex: 1,
            url:
              'https://i1.adis.ws/i/thebodyshop/pink-grapefruit-energising-body-butter-1016297-pinkgrapefruitenergisingbodybutter200ml-1-640x640'
          },
          thumbnail: {
            altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
            galleryIndex: 1,
            url:
              'https://i1.adis.ws/i/thebodyshop/pink-grapefruit-energising-body-butter-1016297-pinkgrapefruitenergisingbodybutter200ml-1-640x640?h=96'
          },
          zoom: {
            altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
            galleryIndex: 1,
            url:
              'https://i1.adis.ws/i/thebodyshop/pink-grapefruit-energising-body-butter-1016297-pinkgrapefruitenergisingbodybutter200ml-1-640x640'
          }
        },
        {
          product: {
            altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
            galleryIndex: 2,
            url: 'https://i1.adis.ws/i/thebodyshop/vitamin-e-night-cream-1094164-vitaminenightcream50ml-4-640x640'
          },
          thumbnail: {
            altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
            galleryIndex: 2,
            url: 'https://i1.adis.ws/i/thebodyshop/vitamin-e-night-cream-1094164-vitaminenightcream50ml-4-640x640?h=96'
          },
          zoom: {
            altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
            galleryIndex: 2,
            url: 'https://i1.adis.ws/i/thebodyshop/vitamin-e-night-cream-1094164-vitaminenightcream50ml-4-640x640'
          }
        },
        {
          product: {
            altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
            galleryIndex: 3,
            url: 'https://i1.adis.ws/i/thebodyshop/vitamin-e-night-cream-1094164-vitaminenightcream50ml-3-640x640'
          },
          thumbnail: {
            altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
            galleryIndex: 3,
            url: 'https://i1.adis.ws/i/thebodyshop/vitamin-e-night-cream-1094164-vitaminenightcream50ml-3-640x640?h=96'
          },
          zoom: {
            altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
            galleryIndex: 3,
            url: 'https://i1.adis.ws/i/thebodyshop/vitamin-e-night-cream-1094164-vitaminenightcream50ml-3-640x640'
          }
        }
      ],
      maxOrderProductQuantity: 15,
      name: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
      priceData: {
        currencyIso: 'GBP',
        formattedValue: '£18.00',
        loyaltyPoints: 18,
        pricePerMetric: '£9.00/100 ML',
        priceType: 'BUY',
        value: 18
      },
      selectedFlag: false,
      size: '200ml',
      stock: {
        stockLevel: 10,
        stockLevelStatus: 'inStock'
      }
    }
  ]
};

export const DUMMY_WISHLIST_PRODUCT_NOSTOCK = {
  code: 'p002419v',
  emailMeWhenInStockToggle: false,
  findInStoreEnabled: false,
  isVariant: false,
  multiVariant: false,
  name: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
  price: {
    currencyIso: 'GBP',
    formattedValue: '£18.00',
    loyaltyPoints: 18,
    pricePerMetric: '£9.00/100 ML',
    priceType: 'BUY',
    value: 18
  },
  stock: {
    stockLevel: 0,
    stockLevelStatus: 'outOfStock'
  },
  url: '/Body/Body-Massage-Oils/Spa-of-the-World%E2%84%A2-Thai-Makrut-Lime-Firming-Oil/p/p002419v',
  variantOptions: [
    {
      code: 'p002419v',
      colour: '#C4171D',
      emailMeWhenInStockToggle: false,
      images: [
        {
          product: {
            altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
            galleryIndex: 0,
            url:
              'https://i1.adis.ws/i/thebodyshop/olive-nourishing-body-butter-1092126-olivenourishingbodybutter50ml-1-640x640'
          },
          thumbnail: {
            altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
            galleryIndex: 0,
            url:
              'https://i1.adis.ws/i/thebodyshop/olive-nourishing-body-butter-1092126-olivenourishingbodybutter50ml-1-640x640?h=96'
          },
          zoom: {
            altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
            galleryIndex: 0,
            url:
              'https://i1.adis.ws/i/thebodyshop/olive-nourishing-body-butter-1092126-olivenourishingbodybutter50ml-1-640x640'
          }
        },
        {
          product: {
            altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
            galleryIndex: 1,
            url:
              'https://i1.adis.ws/i/thebodyshop/pink-grapefruit-energising-body-butter-1016297-pinkgrapefruitenergisingbodybutter200ml-1-640x640'
          },
          thumbnail: {
            altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
            galleryIndex: 1,
            url:
              'https://i1.adis.ws/i/thebodyshop/pink-grapefruit-energising-body-butter-1016297-pinkgrapefruitenergisingbodybutter200ml-1-640x640?h=96'
          },
          zoom: {
            altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
            galleryIndex: 1,
            url:
              'https://i1.adis.ws/i/thebodyshop/pink-grapefruit-energising-body-butter-1016297-pinkgrapefruitenergisingbodybutter200ml-1-640x640'
          }
        },
        {
          product: {
            altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
            galleryIndex: 2,
            url: 'https://i1.adis.ws/i/thebodyshop/vitamin-e-night-cream-1094164-vitaminenightcream50ml-4-640x640'
          },
          thumbnail: {
            altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
            galleryIndex: 2,
            url: 'https://i1.adis.ws/i/thebodyshop/vitamin-e-night-cream-1094164-vitaminenightcream50ml-4-640x640?h=96'
          },
          zoom: {
            altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
            galleryIndex: 2,
            url: 'https://i1.adis.ws/i/thebodyshop/vitamin-e-night-cream-1094164-vitaminenightcream50ml-4-640x640'
          }
        },
        {
          product: {
            altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
            galleryIndex: 3,
            url: 'https://i1.adis.ws/i/thebodyshop/vitamin-e-night-cream-1094164-vitaminenightcream50ml-3-640x640'
          },
          thumbnail: {
            altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
            galleryIndex: 3,
            url: 'https://i1.adis.ws/i/thebodyshop/vitamin-e-night-cream-1094164-vitaminenightcream50ml-3-640x640?h=96'
          },
          zoom: {
            altText: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
            galleryIndex: 3,
            url: 'https://i1.adis.ws/i/thebodyshop/vitamin-e-night-cream-1094164-vitaminenightcream50ml-3-640x640'
          }
        }
      ],
      maxOrderProductQuantity: 15,
      name: 'Spa of the World™ Thai Makrut Lime Firming Oil 200ml',
      priceData: {
        currencyIso: 'GBP',
        formattedValue: '£18.00',
        loyaltyPoints: 18,
        pricePerMetric: '£9.00/100 ML',
        priceType: 'BUY',
        value: 18
      },
      selectedFlag: false,
      size: '200ml',
      stock: {
        stockLevel: 0,
        stockLevelStatus: 'outOfStock'
      }
    }
  ]
};

export const DUMMY_PROMO_FREE_DELIVERY = [
  {
    consumedEntries: [],
    description: 'Free delivery on orders over £30.00, Spend £8.00 more to qualify',
    promotion: {
      code: 'uk-free-delivery_PTF-DELIVERY-MESSAGE',
      promotionType: 'Rule Based Promotion'
    }
  }
];

export const DUMMY_PROMO_FREE_DELIVERY_SPLIT = [
  {
    consumedEntries: [],
    description: 'Free delivery on orders over £30.00 _SEP_ Spend £8.00 more to qualify',
    promotion: {
      code: 'uk-free-delivery_PTF-DELIVERY-MESSAGE',
      promotionType: 'Rule Based Promotion'
    }
  }
];

export const DUMMY_PROMO = [
  {
    consumedEntries: [],
    description: 'Great deals on body butter, buy 2 get 1 free',
    promotion: {
      code: 'uk-body-butters',
      promotionType: 'Rule Based Promotion'
    }
  }
];
