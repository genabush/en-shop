import { Injectable } from '@angular/core';
import { Occ, Cart, ConverterService } from '@spartacus/core';
import { Converter } from '@spartacus/core';
import { TBS_PRODUCT_NORMALIZER } from './../../product/converters/converters';

@Injectable()
export class TbsCartNormalizer implements Converter<Occ.Cart, Cart> {
  constructor(private converter: ConverterService) {}

  convert(source: Occ.Cart, target?: Cart): Cart {
    if (target === undefined) {
      target = { ...(source as any) };
    }

    if (source && source.entries) {
      target.entries = source.entries.map(entry => ({
        ...entry,
        product: this.converter.convert(entry.product, TBS_PRODUCT_NORMALIZER)
      }));
    }

    this.removeDuplicatePromotions(source, target);
    return target;
  }

  /**
   * Remove all duplicate promotions
   */
  private removeDuplicatePromotions(source: any, target: Cart): void {
    if (source && source.potentialOrderPromotions) {
      target.potentialOrderPromotions = this.removeDuplicateItems(source.potentialOrderPromotions);
    }

    if (source && source.potentialProductPromotions) {
      target.potentialProductPromotions = this.removeDuplicateItems(source.potentialProductPromotions);
    }

    if (source && source.appliedOrderPromotions) {
      target.appliedOrderPromotions = this.removeDuplicateItems(source.appliedOrderPromotions);
    }

    if (source && source.appliedProductPromotions) {
      target.appliedProductPromotions = this.removeDuplicateItems(source.appliedProductPromotions);
    }
  }

  private removeDuplicateItems(itemList: any[]): any[] {
    return itemList.filter((p, i, a) => {
      const b = a.map(el => JSON.stringify(el));
      return i === b.indexOf(JSON.stringify(p));
    });
  }
}
