import { ChangeDetectionStrategy, Component, OnInit, OnDestroy } from '@angular/core';

import { Observable, BehaviorSubject, Subscription } from 'rxjs';

import { CmsNavigationComponent } from '@spartacus/core';
import { CmsComponentData, NavigationNode } from '@spartacus/storefront';

import { CustomNavigationService } from 'src/app/services/custom-navigation-service/custom-navigation.service';

@Component({
  selector: 'app-category-navigation',
  templateUrl: './custom-category-navigation.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CustomCategoryNavigationComponent implements OnInit, OnDestroy {
  node$: BehaviorSubject<NavigationNode> = new BehaviorSubject<NavigationNode>(undefined);
  data$: Observable<CmsNavigationComponent> = this.componentData.data$;
  hamburgerSection = 'shop';
  subscription = new Subscription();
  constructor(
    protected componentData: CmsComponentData<CmsNavigationComponent>,
    protected service: CustomNavigationService
  ) {
    this.subscription.add(
      this.service
        .getNavigationNode(this.componentData.data$)
        .pipe()
        .subscribe((nodes: NavigationNode) => {
          this.setHamburgerSection(nodes);
          this.node$.next(nodes);
        })
    );
  }

  ngOnInit() {}

  setHamburgerSection(nodes: NavigationNode) {
    if (nodes.children[0].title === 'Party') {
      this.hamburgerSection = 'discover';
    } else {
      this.hamburgerSection = 'shop';
    }
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
