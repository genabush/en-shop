export const en_CheckoutTranslationOverwrites = {
  gift: {
    gift: {
      card: {
        value: 'Gift card in use - value:',
        alreadyapplied: 'Card applied',
        zerobalance:
          'The balance on this card could not be checked. Please verify you entered a correct pin and try again.',
        limitreached: 'Card reached',
        validationissue: 'Card issue',
        number: 'Gift card number',
        useGiftCard: 'Use a gift card',
        howToUseCard: `You can use a gift card for partial payment or full payment. Just fill in your gift card number below, select ADD
        and your balance will be update.`,
        addAnotherCard: 'Add another card'
      },
      summary: {
        giftCardDiscount: 'From gift card'
      },
      validation: {
        cardNumber: 'Please enter a valid gift card number',
        cardNumberMinLength: 'Please enter nineteen characters',
        cardNumberNumbersOnly: 'Please enter only numeric characters',
        cardPin: 'Please enter a valid verification code',
        cardPinMinLength: 'Please enter four characters',
        cardPinNumbersOnly: 'Please enter only numeric characters'
      },
      form: {
        addButton: 'USE CARD',
        removeButton: 'REMOVE CARD',
        checkBalance: 'CHECK BALANCE',
        checkAnotherCard: 'CHECK ANOTHER CARD',
        number: 'number',
        digitalCode: 'digit code',
        balance: 'Your Gift Card Balance'
      }
    }
  },
  giftcards: {
    giftcards: {
      invalidCard: 'You have provided invalid giftcard details'
    }
  },
  orderSummary: {
    orderSummary: {
      total: 'Total excluding delivery',
      discounts: 'Discounts',
      promoCode: 'Promo Code',
      deliveryOptionsLink: 'delivery or colleciton option',
      delivery: {
        title: 'Delivery',
        subTitle: 'Your order qualifies for:',
        eligibility: {
          standard: 'Standard delivery',
          cis: 'Collect in store',
          cp: 'Pickup at collection point'
        },
        modalTitle: 'Delivery options'
      },
      giftCards: {
        title: 'Gift Cards',
        subTitle: 'From Gift card:'
      }
    }
  },
  checkout: {
    checkout: {
      delivery: {
        restrictions: 'Please select an alternative fulfilment method or remove the items from your shopping bag.'
      },
      cancelBtnLabel: 'Cancel'
    },

    checkoutAddress: {
      deliverTo: 'Deliver to',
      editSelectedAddress: 'Edit selected address',
      addAddress: 'Add Adress',
      billingAdress: 'Billing address'
    }
  },
  delivery: {
    delivery: {
      deliveryOptionsHeader: 'Delivery options',
      shoppingBagLink: 'shopping bag',
      errorMessages: {
        restrictedProductsHeading: 'Delivery restriction',
        restrictedProducts1: 'There are items in your shopping bag which we cannot deliver to your selected address:',
        restrictedProducts2: 'Please provide another delivery address or remove the items from your ',
        hazmatDeliveryMode:
          'Unfortunately we are unable to deliver your items. Please select an alternative fulfilment method or remove the items from your',
        addressDeliveryMode:
          'We are unable to deliver your items to the selected address. Please provide another delivery address to proceed.'
      }
    }
  },
  payment: {
    payment: {
      finishAndPay: 'Pay Now',
      savedCard: 'Saved card',
      addNewCard: 'Add new card',
      addNewCardLink: 'Add credit/debit card',
      CVC: 'CVC',
      CVV: 'CVV',
      Exp: 'Exp',
      cvc: 'CVC (Security code)',
      saveCard: 'Save my card details for next time',
      savedCards: 'Saved cards',
      paypalMessage: 'Click the button to complete the payment with Paypal',
      checkboxes: {
        tAndCs: "Please tick to confirm you've read and agreed to our Terms & Conditions"
      },
      vouchers: {
        info: 'Use up to 4 of your rewards towards the cost of your purchase',
        available: 'AVAILABLE REWARDS',
        applied: 'REWARDS APPLIED',
        empty: "Sorry, you don't have any vouchers on your account yet",
        limitreached: 'Vouchers limit reached',
        use: 'Use as payment',
        remove: 'Remove',
        terms: ' Terms & conditions',
        type: 'Type',
        expires: 'Expires date',
        id: 'Voucher ID'
      }
    },
    paymentMethods: {
      title: 'Payment method',
      creditCard: 'Credit card',
      payPal: 'Paypal',
      ideal: 'iDEAL',
      applePay: 'ApplePay',
      klarna: 'Klarna Pay Later',
      klarna_account: 'Klarna Slice It',
      klarna_paynow: 'Klarna Pay Now',
      klarna_pay_info: 'Please proceed to the next step of checkout to complete payment',
      giropay: 'GiroPay',
      payWithGiroPay: 'Pay with GiroPay',
      payWithApplePay: 'Pay with ApplePay',
      payWithKlarna: 'Pay with Klarna',
      giftCards: 'Use a gift card',
      loyaltyVouchers: 'Use your rewards',
      onlyGifts: 'Spend gifts',
      giftsAndRewards: 'Spend gifts & rewards'
    }
  },
  deliveryModes: {
    deliveryModes: {
      chooseModes: 'Choose options'
    }
  },
  collectionPoint: {
    collectionPoint: {
      title: 'WHERE DO YOU WANT TO COLLECT?',
      info: 'Info',
      select: 'Select',
      change: 'Change',
      directions: 'Directions',
      unavailable: 'Not available',
      openingHrs: {
        title: 'Opening hours',
        closed: 'Closed'
      },
      modal: {
        storeInfoTitle: 'STORE INFORMATION'
      },
      stockLevelStatus: {
        inStock: 'Available',
        outOfStock: 'Not available'
      }
    }
  },
  deliveryTabs: {
    deliveryTabs: {
      delivery: 'Delivery',
      collectInStore: 'Collect in store',
      collectionPoint: 'Collection point'
    }
  },
  whoWillCollect: {
    whoWillCollect: {
      title: 'Who will collect?'
    }
  },
  address: {
    addressForm: {
      enterAdressManually: 'Enter address manually',
      addressHeader: 'Delivery address',
      billingAddressHeader: 'Billing address',
      sameAsShippingAddress: 'Same as delivery address',
      typeYourAddress: 'Type your address',
      clearAddress: 'Clear address',
      singleCountry: 'We only deliver to addresses in the {{title}}',
      saveToAccount: 'Save to my account',
      cancelAddress: 'Cancel',
      mobileDisclaimer:
        'Please use format starting 07 followed by 9 digits. Adding your mobile number makes it easier for us to contact you about the delivery of your parcel.',
      companyName: {
        label: 'Company',
        placeholder: 'Company'
      }
    }
  },
  addressFinder: {
    addressFinder: {
      label: 'City, town, postcode',
      error:
        ' Sorry, we were unable to identify your location. Please enter a town, city or postcode within the market region.',
      modalTitle: 'Where do you want to collect?',
      tabsTitle: 'Closest collection points',
      tabsListTitle: 'List of stores',
      tabsMapTitle: 'Map',
      searchCount: '{{count}} results'
    }
  }
};

export const en_addressFormOverwrites = {
  addressHeader: 'Delivery address',
  billingAddressHeader: 'Billing address',
  sameAsShippingAddress: 'Same as delivery address',
  typeYourAddress: 'Type your address',
  clearAddress: 'Clear address',
  singleCountry: 'We only deliver to addresses in the {{title}}',
  saveToAccount: 'Save to my account',
  invalidAddressPoBox: 'We cannot ship to P.O. Boxes'
};
