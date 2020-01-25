export const defaultLayoutConfig = {
  layoutSlots: {
    header: {
      md: {
        slots: ['PreHeader', 'SiteContext', 'SiteLogo', 'SiteLinks', 'MiniCart', 'NavigationBar']
      },
      xs: {
        slots: ['PreHeader', 'SiteLinks', 'SiteLogo', 'SiteContext', 'MiniCart']
      }
    },
    LandingPage1Template: {
      slots: ['Section1']
    },
    footer: {
      slots: ['FooterLeft', 'Footer', 'FooterContact', 'FooterCopyrights']
    },
    ProductListPageTemplate: {
      slots: ['ProductLeftRefinements', 'ProductListSlot']
    },
    SearchResultsListPageTemplate: {
      slots: ['ProductLeftRefinements', 'SearchResultsListSlot']
    },
    CategoryPageTemplate: {
      slots: ['Section1', 'ProductLeftRefinements', 'ProductListSlot', 'Section2']
    },
    StoreFinderPageTemplate: {
      slots: ['SideContent', 'MiddleContent']
    },
    AccountPageTemplate: {
      slots: ['SideContentSlot', 'BodyContent']
    },
    ProductDetailsPageTemplate: {
      slots: [
        'TopHeaderSlot',
        'Summary',
        'UpSelling',
        'CrossSelling',
        'Section1',
        'PlaceholderContentSlot',
        'howToUse',
        'mainIngredient',
        'addToCartHelpSlot'
      ]
    },
    CartPageTemplate: {
      slots: ['CenterContentSlot', 'Section1', 'EmptyCartMiddleContent']
    },
    OrderConfirmationPageTemplate: {
      slots: ['BodyContent']
    }
  }
};
