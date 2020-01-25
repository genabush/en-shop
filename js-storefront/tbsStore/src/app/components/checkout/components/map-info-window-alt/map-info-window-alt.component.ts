import { Component, Input } from '@angular/core';
import { CollectPoint } from 'src/app/interfaces/collection-point.interface';

@Component({
  selector: 'app-map-info-alt-window',
  templateUrl: './map-info-window-alt.component.html'
})
export class MapInfoWindowAltComponent {
  @Input() storeInfo: CollectPoint;
  constructor() {}
}
