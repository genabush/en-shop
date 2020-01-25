import { Component } from '@angular/core';
import { LinkComponent, CmsComponentData, HamburgerMenuService } from '@spartacus/storefront';
import { WindowRef } from '@spartacus/core';
import { CustomCmsLinkComponent } from '../../../models/index';

@Component({
  selector: 'app-custom-link',
  templateUrl: './custom-link.component.html',
  styleUrls: ['./custom-link.component.scss']
})
export class CustomLinkComponent extends LinkComponent {
  constructor(
    public component: CmsComponentData<CustomCmsLinkComponent>,
    private windowRef: WindowRef,
    private hamburgerMenuService: HamburgerMenuService
  ) {
    super(component);
  }

  toggleHamburgerMenu() {
    const hamburgerNode: HTMLElement = this.windowRef.document.querySelector('header.is-expanded');
    if (hamburgerNode) {
      this.hamburgerMenuService.toggle();
    }
  }
}
