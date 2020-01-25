import { ChangeDetectionStrategy, Component } from '@angular/core';

// spartacus
import { CmsNavigationComponent } from '@spartacus/core';
import { CmsComponentData } from '@spartacus/storefront';

// vendor
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

// services
import { CustomNavigationService } from 'src/app/services/custom-navigation-service/custom-navigation.service';

// interfaces
import { NavigationNode } from '../../../../interfaces/custom-navigation-node.interface';

@Component({
  selector: 'app-navigation',
  templateUrl: './custom-navigation.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CustomNavigationComponent {
  node$: Observable<NavigationNode> = this.service.createNavigation(this.componentData.data$);

  styleClass$: Observable<string> = this.componentData.data$.pipe(map(d => d.styleClass));

  constructor(
    protected componentData: CmsComponentData<CmsNavigationComponent>,
    protected service: CustomNavigationService
  ) {}
}
