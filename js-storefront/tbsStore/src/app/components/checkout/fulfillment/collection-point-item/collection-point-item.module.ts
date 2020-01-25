import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { I18nModule, UrlModule } from '@spartacus/core';
import { CollectionPointItemComponent } from './collection-point-item.component';
import { MapInfoWindowComponent } from '../../components/map-info-window/map-info-window.component';
import { MapInfoWindowAltComponent } from '../../components/map-info-window-alt/map-info-window-alt.component';
import { RouterModule } from '@angular/router';
import { AgmCoreModule } from '@agm/core';

@NgModule({
  imports: [CommonModule, I18nModule, UrlModule, RouterModule, AgmCoreModule],
  declarations: [CollectionPointItemComponent, MapInfoWindowComponent, MapInfoWindowAltComponent],
  entryComponents: [CollectionPointItemComponent, MapInfoWindowComponent, MapInfoWindowAltComponent],
  exports: [CollectionPointItemComponent, MapInfoWindowComponent, MapInfoWindowAltComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CollectionPointItemModule {}
