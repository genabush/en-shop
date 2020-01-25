import { Component, Input } from '@angular/core';
import { RoutingService } from '@spartacus/core';
import { CustomStoreFinderService } from 'src/app/services/custom-store-finder/custom-store-finder.service';
import { CustomPointOfService } from 'src/app/interfaces/custom-point-of-service.interface';

@Component({
  selector: 'app-store-banner',
  templateUrl: './store-banner.component.html'
})
export class StoreBannerComponent {
  @Input() location: CustomPointOfService;
  hasSearchResults$ = this.customStoreFinderService.getStateSearchedLocation();
  constructor(private routingService: RoutingService, private customStoreFinderService: CustomStoreFinderService) {}

  goBack(): void {
    this.routingService.go([`store-finder/`]);
  }
}
