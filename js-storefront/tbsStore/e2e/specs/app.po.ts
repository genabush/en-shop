import { browser, by, element } from 'protractor';
export class AppPage {
  hasClass(el, cls) {
    return el
      .getAttribute('class')
      .then(function(classes) {
        return classes.split(' ').indexOf(cls) !== -1;
      })
      .catch(err => console.log(err));
  }
  checkCurrentUrl() {
    return browser.getCurrentUrl();
  }
  navigateToHome() {
    return browser.get('/');
  }

  navigateToPlP() {
    return browser.get('/thebodyshop-uk/en/GBP/Body/c/c00001');
  }

  getAppSiteLinksSearch() {
    return element(by.css('app-root .searchbox'));
  }

  getAppSiteLinksAccount() {
    return element(by.css('app-root SiteLinks a[ng-reflect-router-link="/login"]'));
  }

  checkSearchModal() {
    return this.hasClass(element(by.css('.modal')), 'show');
  }
}
