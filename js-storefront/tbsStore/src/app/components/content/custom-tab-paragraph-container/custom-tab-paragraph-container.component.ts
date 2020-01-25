import { CmsComponentData } from '@spartacus/storefront';
import { ChangeDetectionStrategy, Component, HostBinding } from '@angular/core';
import { CmsService, CMSTabParagraphContainer } from '@spartacus/core';
import { combineLatest, Observable } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';

@Component({
  selector: 'cx-tab-paragraph-container',
  templateUrl: './custom-tab-paragraph-container.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CustomTabParagraphContainerComponent {
  @HostBinding('@.disabled') disabled = true;
  constructor(public componentData: CmsComponentData<CMSTabParagraphContainer>, private cmsService: CmsService) {}

  components$: Observable<any[]> = this.componentData.data$.pipe(
    switchMap(data =>
      combineLatest(
        data.components.split(' ').map(component =>
          this.cmsService.getComponentData<any>(component).pipe(
            map(tab => {
              if (!tab.flexType) {
                tab = {
                  ...tab,
                  flexType: tab.typeCode
                };
              }
              return {
                ...tab,
                title: `CMSTabParagraphContainer.tabs.${tab.uid}`
              };
            })
          )
        )
      )
    )
  );
}
