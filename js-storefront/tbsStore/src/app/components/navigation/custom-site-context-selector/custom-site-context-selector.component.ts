import { Component, OnInit } from '@angular/core';
import { SiteContextSelectorComponent, SiteContextComponentService } from '@spartacus/storefront';

@Component({
  selector: 'cx-site-context-selector',
  templateUrl: './custom-site-context-selector.component.html'
})
export class CustomSiteContextSelectorComponent extends SiteContextSelectorComponent {
  constructor(componentService: SiteContextComponentService) {
    super(componentService);
  }
}
