import { ChangeDetectionStrategy, Component, Input, Optional } from '@angular/core';
import { CmsSearchBoxComponent } from '@spartacus/core';
import { Router } from '@angular/router';

// sparatcus
import { ICON_TYPE } from '@spartacus/storefront';
import { CmsComponentData, ModalService } from '@spartacus/storefront';

// vendor
import { map, switchMap, tap, take } from 'rxjs/operators';
import { Observable, of } from 'rxjs';

// services
import { CustomSearchBoxComponentService } from 'src/app/services/custom-search-box/custom-search-box.service';

// interfaces
import { SearchBoxConfig, SearchResults } from 'src/app/interfaces/custom-search-box.interface';

const DEFAULT_SEARCHBOX_CONFIG: SearchBoxConfig = {
  minCharactersBeforeRequest: 1,
  displayProducts: true,
  displaySuggestions: true,
  maxProducts: 5,
  maxSuggestions: 5,
  displayProductImages: true
};

@Component({
  selector: 'cx-searchbox',
  templateUrl: './custom-search-box.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CustomSearchBoxComponent {
  config: SearchBoxConfig;
  /**
   * Sets the search box input field
   */
  @Input('queryText')
  set queryText(value: string) {
    if (value) {
      this.search(value);
    }
  }

  iconTypes = ICON_TYPE;

  /**
   * In some occasions we need to ignore the close event,
   * for example when we click inside the search result section.
   */
  private ignoreCloseEvent = false;

  /**
   * The component data is optional, so that this component
   * can be reused without CMS integration.
   */
  constructor(
    protected searchBoxComponentService: CustomSearchBoxComponentService,
    @Optional()
    protected componentData: CmsComponentData<CmsSearchBoxComponent>,
    public modalService: ModalService,
    public router: Router
  ) {}

  results$: Observable<SearchResults> = this.config$.pipe(
    tap(c => (this.config = c)),
    switchMap(config => this.searchBoxComponentService.getResults(config))
  );

  /**
   * Returns the backend configuration or default configuration for the searchbox.
   */
  private get config$(): Observable<SearchBoxConfig> {
    if (this.componentData) {
      return <Observable<SearchBoxConfig>>this.componentData.data$.pipe(
        // Since the backend returns string values (i.e. displayProducts: "true") for
        // boolean values, we replace them with boolean values.
        map(c => {
          return {
            ...c,
            displayProducts: <any>c.displayProducts === 'true' || c.displayProducts === true,
            displayProductImages: <any>c.displayProductImages === 'true' || c.displayProductImages === true,
            displaySuggestions: <any>c.displaySuggestions === 'true' || c.displaySuggestions === true
          };
        })
      );
    } else {
      return of(DEFAULT_SEARCHBOX_CONFIG);
    }
  }

  /**
   * Closes the searchbox and opens the search result page.
   */
  search(query: string): void {
    this.searchBoxComponentService.search(query, this.config);
    // force the searchbox to open
    this.open();
  }

  /**
   * Opens the typeahead searchbox
   */
  open(): void {
    this.searchBoxComponentService.toggleBodyClass('searchbox-is-active', true);
  }

  /**
   * Closes the typehead searchbox.
   */
  close(event: UIEvent): void {
    if (!this.ignoreCloseEvent) {
      this.searchBoxComponentService.toggleBodyClass('searchbox-is-active', false);
      if (event && event.target) {
        (<HTMLElement>event.target).blur();
      }
    }
    this.ignoreCloseEvent = false;
  }

  /**
   * Especially in mobile we do not want the search icon
   * to focus the input again when it's already open.
   * */
  avoidReopen(event: UIEvent): void {
    if (this.searchBoxComponentService.hasBodyClass('searchbox-is-active')) {
      this.close(event);
      event.preventDefault();
    }
  }

  /**
   * Opens the PLP with the given query.
   *
   * TODO: if there's a singe product match, we could open the PDP.
   */
  launchSearchResult(event: UIEvent, query: string): void {
    this.searchBoxComponentService
      .getResults(DEFAULT_SEARCHBOX_CONFIG)
      .pipe(take(1))
      .subscribe(data => {
        this.closeModal(event);
        if (data.keywordRedirectUrl !== undefined) {
          this.router.navigateByUrl(data.keywordRedirectUrl);
        } else {
          this.searchBoxComponentService.launchSearchPage(query);
        }
      });
  }

  /**
   * Disables closing the search result list.
   */
  disableClose(): void {
    this.ignoreCloseEvent = true;
  }

  /**
   * Clears the search box input field
   */
  public clear(el: HTMLInputElement): void {
    this.disableClose();
    el.value = '';
    this.searchBoxComponentService.clearResults();
  }
  openSearchModal(content) {
    const x = window.scrollX;
    const y = window.scrollY;
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title', windowClass: 'search-modal d-flex' });
    const searchBtn = document.getElementById('searchBtn');
    searchBtn.focus();
    window.scrollTo(x, y);
  }
  closeModal(event: UIEvent): void {
    this.searchBoxComponentService.clearResults();
    this.modalService.closeActiveModal();
    this.close(event);
  }
}
