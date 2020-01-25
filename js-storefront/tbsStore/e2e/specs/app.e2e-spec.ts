import { AppPage } from './app.po';
import { browser, element, by } from 'protractor';

describe('The Body Shop App', () => {
  let page: AppPage;

  beforeAll(() => {
    page = new AppPage();
    // page.setMockEndpoints();
  });

  afterAll(function() {
    page = null;
  });

  it('should DISPLAY the header section', () => {
    page.navigateToHome().then(() => {
      expect(element(by.css('app-root .header'))).toBeTruthy();
    });
  });

  it('should DISPLAY the site Logo', () => {
    page.navigateToHome().then(() => {
      expect(element(by.css('app-root .SiteLogo'))).toBeTruthy();
    });
  });

  it('should DISPLAY breadcrumbs in  the header', () => {
    page.navigateToPlP().then(() => {
      expect(element(by.css('app-root .breadcrumbs-content'))).toBeTruthy();
    });
  });
});
