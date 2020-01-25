import { ChangeDetectionStrategy, Component } from '@angular/core';
import { CmsNavigationComponent } from '@spartacus/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { CmsComponentData, NavigationNode, NavigationService, FooterNavigationComponent } from '@spartacus/storefront';

@Component({
  selector: 'app-footer-navigation',
  templateUrl: './custom-footer-navigation.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CustomFooterNavigationComponent extends FooterNavigationComponent {
  constructor(protected componentData: CmsComponentData<CmsNavigationComponent>, protected service: NavigationService) {
    super(componentData, service);
  }
}
