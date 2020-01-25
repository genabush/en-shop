import { Injectable } from '@angular/core';
import { CmsNavigationComponent, CmsService, SemanticPathService } from '@spartacus/core';
import { combineLatest, Observable, of } from 'rxjs';
import { filter, map, switchMap, tap } from 'rxjs/operators';
import { NavigationNode } from 'src/app/interfaces/custom-navigation-node.interface';

@Injectable({
  providedIn: 'root'
})
export class CustomNavigationService {
  constructor(protected cmsService: CmsService, protected semanticPathService: SemanticPathService) {}

  public createNavigation(data$: Observable<CmsNavigationComponent>): Observable<NavigationNode> {
    return combineLatest([data$, this.getNavigationNode(data$)]).pipe(
      map(([data, nav]) => {
        return {
          title: data.name,
          children: [nav]
        };
      })
    );
  }

  public getNavigationNode(data$: Observable<CmsNavigationComponent>): Observable<NavigationNode> {
    if (!data$) {
      return of();
    }
    return data$.pipe(
      filter(Boolean),
      switchMap((data: any) => {
        const navigation = data.navigationNode ? data.navigationNode : data;
        return this.cmsService.getNavigationEntryItems(navigation.uid).pipe(
          tap(items => {
            if (items === undefined) {
              this.getNavigationEntryItems(navigation, true);
            }
          }),
          filter(Boolean),
          map(items => this.createNode(navigation, items))
        );
      })
    );
  }

  /**
   * Get all navigation entry items' type and id. Dispatch action to load all these items
   * @param nodeData
   * @param root
   * @param itemsList
   */
  private getNavigationEntryItems(nodeData: any, root: boolean, itemsList = []) {
    if (nodeData.entries && nodeData.entries.length > 0) {
      nodeData.entries.forEach(entry => {
        itemsList.push({
          superType: entry.itemSuperType,
          id: entry.itemId
        });
      });
    }

    if (nodeData.children && nodeData.children.length > 0) {
      this.processChildren(nodeData, itemsList);
    }

    if (root) {
      const rootUid = nodeData.uid;
      this.cmsService.loadNavigationItems(rootUid, itemsList);
    }
  }

  private processChildren(node, itemsList): void {
    for (const child of node.children) {
      this.getNavigationEntryItems(child, false, itemsList);
    }
  }

  /**
   * Create a new node tree for display
   * @param nodeData
   * @param items
   */
  private createNode(nodeData: any, items: any): NavigationNode {
    const node: NavigationNode = {};

    node.title = nodeData.title;
    node.enabledForMegaNav = nodeData.enabledForMegaNav;
    node.enabledForMobileNav = nodeData.enabledForMobileNav;

    if (nodeData.icon !== 'undefined' || nodeData.icon !== '') {
      node.icon = nodeData.icon;
    }
    if (nodeData.amplienceId !== 'undefined') {
      node.amplienceId = nodeData.amplienceId;
    }
    if (nodeData.entries && nodeData.entries.length > 0) {
      this.addLinkToNode(node, nodeData.entries[0], items);
    }

    if (nodeData.children && nodeData.children.length > 0) {
      const children = this.createChildren(nodeData, items);
      node.children = children;
    }
    return node;
  }

  private addLinkToNode(node: NavigationNode, entry, items) {
    const item = items[`${entry.itemId}_${entry.itemSuperType}`];

    // now we only consider CMSLinkComponent
    if (entry.itemType === 'CMSLinkComponent') {
      if (!node.title) {
        node.title = item.linkName;
      }
      const url = this.getLink(item);

      if (node.title && url) {
        node.url = url;
        // the backend provide boolean value for the target
        // in case the link should be opened in a new window
        node.target = !!item.target ? '_blank' : '';
      }
    }
  }

  /**
   *
   * Gets the URL or link to a related item (category)
   */
  private getLink(item): string | string[] {
    if (item.url) {
      return item.url;
    } else if (item.categoryCode) {
      return this.semanticPathService.transform({
        cxRoute: 'category',
        params: {
          code: item.categoryCode,
          name: item.name
        }
      });
    }
  }

  private createChildren(node, items) {
    const children = [];

    for (const child of node.children) {
      const childNode = this.createNode(child, items);
      children.push(childNode);
    }

    return children;
  }
}
