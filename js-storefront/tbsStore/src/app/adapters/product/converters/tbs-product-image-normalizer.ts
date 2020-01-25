/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

import { Injectable } from '@angular/core';
import { OccConfig } from '@spartacus/core';
import { Occ } from '@spartacus/core';
import { Converter } from '@spartacus/core';
import { Product } from '@spartacus/core';
import { Images } from '@spartacus/core';

const AMPLIENCE_URL = 'https://i1.adis.ws';

@Injectable()
export class TbsProductImageNormalizer implements Converter<Occ.Product, Product> {
  constructor(protected config: OccConfig) {}

  convert(source: Occ.Product, target?: Product): Product {
    if (target === undefined) {
      target = { ...(source as any) };
    }
    if (source.images) {
      target.images = this.normalize(source.images);
    }
    return target;
  }

  /**
   * @desc
   * Creates the image structure we'd like to have. Instead of
   * having a single list with all images despite type and format
   * we create a proper structure. With that we can do:
   * - images.primary.thumnail.url
   * - images.GALLERY[0].thumnail.url
   */
  normalize(source: Occ.Image[]): Images {
    const images = {};
    if (source) {
      for (const image of source) {
        const isList = image.hasOwnProperty('galleryIndex');
        if (!images.hasOwnProperty(image.imageType)) {
          images[image.imageType] = isList ? [] : {};
        }

        let imageContainer;
        if (isList && !images[image.imageType][image.galleryIndex]) {
          images[image.imageType][image.galleryIndex] = {};
        }

        if (isList) {
          imageContainer = images[image.imageType][image.galleryIndex];
        } else {
          imageContainer = images[image.imageType];
        }

        image.url = this.normalizeImageUrl(image);

        imageContainer[image.format] = image;
      }
    }
    return images;
  }

  /**
   * Traditionally, in an on-prem world, medias and other backend related calls
   * are hosted at the same platform, but in a cloud setup, applications are are
   * typically distributed cross different environments. For media, we use the
   * `backend.media.baseUrl` by default, but fallback to `backend.occ.baseUrl`
   * if none provided. Also check to see if it is an amplience url
   */
  normalizeImageUrl(image: Occ.Image): string {
    const httpsProtocol = 'https://';
    const arrLength = 2;
    const urlsArr = image.url.split(httpsProtocol);

    if (urlsArr.length > arrLength) {
      return `${httpsProtocol}${urlsArr[arrLength]}`;
    }
    return image.url;
  }
}
