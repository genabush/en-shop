import { Component, Input, OnInit } from '@angular/core';
import { StoreFinderService, PointOfService } from '@spartacus/core';
import { Observable } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { ICON_TYPE } from '@spartacus/storefront';
import { CustomStoreFinderService } from 'src/app/services/custom-store-finder/custom-store-finder.service';
const primaryPin = require('src/assets/images/maps-pins/primary.svg') as string;
@Component({
  selector: 'app-store-details',
  templateUrl: './store-details.component.html'
})
export class StoreDetailsComponent implements OnInit {
  location$: Observable<any>;
  isLoading$: Observable<any>;
  searchedLocation$: Observable<any>;
  iconTypes = ICON_TYPE;
  mapIcons = {
    primary: primaryPin
  };
  @Input() location: PointOfService;
  @Input() disableMap: boolean;
  showDirectionsBtn = false;
  constructor(
    private storeFinderService: StoreFinderService,
    private route: ActivatedRoute,
    private customStoreFinderService: CustomStoreFinderService
  ) {}

  ngOnInit() {
    if (!this.location) {
      this.requestStoresData();
      this.location$ = this.storeFinderService.getFindStoresEntities();
      this.isLoading$ = this.storeFinderService.getStoresLoading();
      this.searchedLocation$ = this.customStoreFinderService.getStateSearchedLocation();
    }
  }

  requestStoresData() {
    this.storeFinderService.viewStoreById(this.route.snapshot.params.store);
  }
}
