import { Component, OnInit } from '@angular/core';
import { CmsNavigationComponent } from '@spartacus/core';
import { CmsComponentData } from '@spartacus/storefront';
import { NavigationNode } from 'src/app/interfaces/custom-navigation-node.interface';
import { CustomNavigationService } from 'src/app/services/custom-navigation-service/custom-navigation.service';
import { Subscription, BehaviorSubject } from 'rxjs';
import { take } from 'rxjs/operators';

@Component({
  selector: 'app-account-navigation',
  templateUrl: './account-navigation.component.html'
})
export class AccountNavigationComponent {
  node$: BehaviorSubject<NavigationNode> = new BehaviorSubject<NavigationNode>(undefined);
  subscription: Subscription;
  constructor(
    protected componentData: CmsComponentData<CmsNavigationComponent>,
    protected service: CustomNavigationService
  ) {
    this.subscription = this.service
      .getNavigationNode(this.componentData.data$)
      .pipe(take(1))
      .subscribe((nodes: NavigationNode) => {
        if (nodes.children) {
          this.node$.next(nodes);
        }
      });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
