import { Component, OnInit } from '@angular/core';
import { ViewModes } from '@spartacus/storefront';
import { of, BehaviorSubject } from 'rxjs';
@Component({
  selector: 'app-styleguide',
  templateUrl: './styleguide.component.html',
  styleUrls: ['./styleguide.component.scss']
})
export class StyleguideComponent implements OnInit {
  constructor() {}
  viewMode$ = new BehaviorSubject<ViewModes>(ViewModes.Grid);
  ViewModes = ViewModes;
  testProducts = [
    {
      averageRating: 3.3,
      code: 'p000657',
      emailMeWhenInStockToggle: false,
      images: {
        PRIMARY: {
          thumbnail: {
            format: 'thumbnail',
            imageType: 'PRIMARY',
            url: 'https://i1.adis.ws/i/thebodyshop/product-thumbnail'
          },
          product: {
            format: 'product',
            imageType: 'PRIMARY',
            url: 'https://i1.adis.ws/i/thebodyshop/product-thumbnail'
          }
        }
      },
      isVariant: false,
      nameHtml: 'Lip & Cheek Stain',
      previewDescription:
        'This dual-¬purpose liquid stain gives lips and cheeks a natural pop of buildable colour. Use...',
      price: {
        currencyIso: 'GBP',
        formattedValue: '£8.00',
        priceType: 'BUY',
        value: 8.0
      },
      stock: {
        stockLevel: 0,
        stockLevelStatus: 'outOfStock'
      },
      url: '/Make-up/Lips/Lip-%26-Cheek-Stain/p/p000657',
      variants:
        "{'variants':[{'name':'Lip \\u0026 Cheek Stain 7.2ml','code':'1096109','price':'£8.00','stock':{'stockLevelStatus':'inStock'},'pricePerMetric':'£111.11/100 ML','size':'7.2ml','colour':'#C51F71','variantType':'COLOUR','sizeForSort':7.2,'colourPosition':0,'emailWhenInStockToggle':false},{'name':'Lip \\u0026 Cheek Stain 7.2ml','code':'1096148','price':'£8.00','stock':{'stockLevelStatus':'inStock'},'pricePerMetric':'£111.11/100 ML','size':'7.2ml','colour':'#B62657','variantType':'COLOUR','sizeForSort':7.2,'colourPosition':0,'emailWhenInStockToggle':false},{'name':'Lip \\u0026 Cheek Stain 7.2ml','code':'1096261','price':'£8.00','stock':{'stockLevelStatus':'outOfStock'},'pricePerMetric':'£111.11/100 ML','size':'7.2ml','colour':'#C4171D','variantType':'COLOUR','sizeForSort':7.2,'colourPosition':0,'emailWhenInStockToggle':false}]}"
    },
    {
      averageRating: 4.8,
      code: '1094188',
      emailMeWhenInStockToggle: false,
      images: {
        PRIMARY: {
          thumbnail: {
            format: 'thumbnail',
            imageType: 'PRIMARY',
            url: 'https://i1.adis.ws/i/thebodyshop/product-thumbnail'
          },
          product: {
            format: 'product',
            imageType: 'PRIMARY',
            url: 'https://i1.adis.ws/i/thebodyshop/product-thumbnail'
          }
        }
      },
      isVariant: false,
      nameHtml: 'Wild Argan Oil Sublime Nourishing Body Butter',
      price: {
        currencyIso: 'GBP',
        formattedValue: '£6.00',
        priceType: 'BUY',
        value: 6.0
      },
      stock: {
        stockLevelStatus: 'inStock'
      },
      url: '/Body/Body-Butters/Wild-Argan-Oil-Sublime-Nourishing-Body-Butter/p/1094188',
      variantSize: '50ml'
    },
    {
      averageRating: 4.8,
      code: '1094138',
      emailMeWhenInStockToggle: false,
      images: {
        PRIMARY: {
          thumbnail: {
            format: 'thumbnail',
            imageType: 'PRIMARY',
            url: 'https://i1.adis.ws/i/thebodyshop/product-thumbnail'
          },
          product: {
            format: 'product',
            imageType: 'PRIMARY',
            url: 'https://i1.adis.ws/i/thebodyshop/product-thumbnail'
          }
        }
      },
      isVariant: false,
      // TEST LABELS
      label: {
        type: 'regular',
        text: 'Bestseller'
      },
      badge: {
        text: 'Glosscar Awards 2018',
        url: 'https://i1.adis.ws/i/thebodyshop/badge-1?w=63&h=63'
      },
      //
      nameHtml: 'Wild Argan Oil Sublime Nourishing Body Butter',
      price: {
        currencyIso: 'GBP',
        formattedValue: '£15.00',
        priceType: 'BUY',
        value: 15.0
      },
      stock: {
        stockLevelStatus: 'inStock'
      },
      url: '/Body/Body-Butters/Wild-Argan-Oil-Sublime-Nourishing-Body-Butter/p/1094138',
      variantSize: '200ml'
    },
    {
      averageRating: 4.8,
      code: '1094138',
      emailMeWhenInStockToggle: false,
      images: {
        PRIMARY: {
          thumbnail: {
            format: 'thumbnail',
            imageType: 'PRIMARY',
            url: 'https://i1.adis.ws/i/thebodyshop/product-thumbnail'
          },
          product: {
            format: 'product',
            imageType: 'PRIMARY',
            url: 'https://i1.adis.ws/i/thebodyshop/product-thumbnail'
          }
        }
      },
      isVariant: false,
      nameHtml: 'Wild Argan Oil Sublime Nourishing Body Butter',
      price: {
        currencyIso: 'GBP',
        formattedValue: '£15.00',
        priceType: 'BUY',
        value: 15.0
      },
      stock: {
        stockLevelStatus: 'inStock'
      },
      url: '/Body/Body-Butters/Wild-Argan-Oil-Sublime-Nourishing-Body-Butter/p/1094138',
      variantSize: '200ml'
    },
    {
      averageRating: 3.6,
      code: 'p002347v',
      emailMeWhenInStockToggle: false,
      isVariant: false,
      nameHtml: 'Almond Milk & Honey Soothing & Restoring Body Butter',
      images: {
        PRIMARY: {
          thumbnail: {
            format: 'thumbnail',
            imageType: 'PRIMARY',
            url: 'https://i1.adis.ws/i/thebodyshop/product-thumbnail'
          },
          product: {
            format: 'product',
            imageType: 'PRIMARY',
            url: 'https://i1.adis.ws/i/thebodyshop/product-thumbnail'
          }
        }
      },
      // TEST LABELS
      label: {
        type: 'bright',
        text: 'Sale'
      },
      //
      price: {
        currencyIso: 'GBP',
        formattedValue: '£15.00',
        priceType: 'BUY',
        value: 15.0
      },
      stock: {
        stockLevelStatus: 'inStock'
      },
      url: '/Body/Body-Butters/Almond-Milk-%26-Honey-Soothing-%26-Restoring-Body-Butter/p/p002347v',
      variantSize: '200ml'
    },
    {
      averageRating: 3.4,
      code: '1055790',
      emailMeWhenInStockToggle: false,
      isVariant: false,
      nameHtml: 'Shea Nourishing Body Butter',
      images: {
        PRIMARY: {
          thumbnail: {
            format: 'thumbnail',
            imageType: 'PRIMARY',
            url: 'https://i1.adis.ws/i/thebodyshop/product-thumbnail'
          },
          product: {
            format: 'product',
            imageType: 'PRIMARY',
            url: 'https://i1.adis.ws/i/thebodyshop/product-thumbnail'
          }
        }
      },
      price: {
        currencyIso: 'GBP',
        formattedValue: '£15.00',
        priceType: 'BUY',
        value: 15.0
      },
      stock: {
        stockLevelStatus: 'inStock'
      },
      url: '/Body/Body-Butters/Shea-Nourishing-Body-Butter/p/1055790',
      variantSize: '200ml'
    }
  ];

  hero = {
    category: 'Category',
    title: 'Section heading',
    titleLong: 'Section heading which wraps onto the next line when long',
    subheading: 'Sub Heading',
    intro:
      'Lorem ipsum dolor sit amet consectetur adipiscing elit, dapibus dui tempus turpis etiam congue torquent dictumst, nam.'
  };

  icons = [
    { name: 'logo-pod' },
    { name: 'logo-primary' },
    { name: 'logo-wordmark' },
    { name: 'star_empty' },
    { name: 'star_half' },
    { name: 'addresses_hover' },
    { name: 'addresses' },
    { name: 'arrow-left' },
    { name: 'arrow-right' },
    { name: 'basket_hover' },
    { name: 'basket' },
    { name: 'bottle_large' },
    { name: 'bottle_medium' },
    { name: 'bottle_small' },
    { name: 'box_hover' },
    { name: 'box' },
    { name: 'menu' },
    { name: 'chevron-up' },
    { name: 'chevron-down' },
    { name: 'chevron-left' },
    { name: 'chevron-right' },
    { name: 'close' },
    { name: 'filter' },
    { name: 'heart_hover' },
    { name: 'heart' },
    { name: 'location_hover' },
    { name: 'location' },
    { name: 'my-account_hover' },
    { name: 'my-account' },
    { name: 'order-history' },
    { name: 'order-history' },
    { name: 'password_hover' },
    { name: 'password' },
    { name: 'payment_hover' },
    { name: 'payment' },
    { name: 'plus' },
    { name: 'minus' },
    { name: 'preferences_hover' },
    { name: 'preferences' },
    { name: 'privacy_hover' },
    { name: 'privacy' },
    { name: 'profile_hover' },
    { name: 'profile' },
    { name: 'search_hover' },
    { name: 'search' },
    { name: 'shop_hover' },
    { name: 'shop' },
    { name: 'sign-out_hover' },
    { name: 'sign-out' },
    { name: 'tag' },
    { name: 'tick' },
    { name: 'trash_hover' },
    { name: 'trash' },
    { name: 'truck_hover' },
    { name: 'truck' },
    { name: 'wishlist_hover' },
    { name: 'wishlist' },
    { name: 'bubble' },
    { name: 'play' },
    { name: 'youtube' },
    { name: 'twitter' },
    { name: 'tumblr' },
    { name: 'facebook' },
    { name: 'pinterest' },
    { name: 'linkedin' },
    { name: 'envelope' },
    { name: 'googleplus' },
    { name: 'feed' },
    { name: 'instagram' },
    { name: 'snapchat' },
    { name: 'stumbleupon' },
    { name: 'apple' },
    { name: 'android' },
    { name: 'podcast' }
  ];

  testProducts$ = of(this.testProducts);

  ngOnInit() {
    this.viewMode$.next(ViewModes.Grid);
  }

  copyTextToClipboard(text) {
    let textContent = text.target.innerText;
    var txtArea = document.createElement('textarea');
    txtArea.id = 'txt';
    txtArea.style.position = 'fixed';
    txtArea.style.top = '0';
    txtArea.style.left = '0';
    txtArea.style.opacity = '0';
    txtArea.value = textContent;
    document.body.appendChild(txtArea);
    txtArea.select();

    try {
      var successful = document.execCommand('copy');
      var msg = successful ? 'successful' : 'unsuccessful';
      console.info('Copying text command was ' + msg);
      if (successful) {
        return true;
      }
    } catch (err) {
      console.info('Oops, unable to copy');
    } finally {
      document.body.removeChild(txtArea);
    }
    return false;
  }
}
