import { NgModule } from '@angular/core';
import { SwiperModule } from 'ngx-swiper-wrapper';
import { SWIPER_CONFIG } from 'ngx-swiper-wrapper';
import { SwiperConfigInterface } from 'ngx-swiper-wrapper';
import { MediaModule } from '@spartacus/storefront';
import { ConfigModule, CmsConfig } from '@spartacus/core';
import { SliderComponent } from './slider.component';
import { SliderItemArticleComponent } from './slider-item-article/slider-item-article.component';

const DEFAULT_SWIPER_CONFIG: SwiperConfigInterface = {
  direction: 'horizontal',
  slidesPerView: 'auto'
};

@NgModule({
  imports: [
    SwiperModule,
    MediaModule,
    ConfigModule.withConfig(<CmsConfig>{
      cmsComponents: {
        SliderComponent: {
          component: SliderComponent
        }
      }
    })
  ],
  providers: [
    {
      provide: SWIPER_CONFIG,
      useValue: DEFAULT_SWIPER_CONFIG
    }
  ],
  declarations: [SliderComponent, SliderItemArticleComponent],
  entryComponents: [SliderComponent, SliderItemArticleComponent],
  exports: [SliderComponent]
})
export class SliderModule {}
