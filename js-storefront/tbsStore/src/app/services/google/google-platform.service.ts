import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { DOCUMENT, isPlatformBrowser } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class GooglePlatformService {
  // The source for the load
  private googlePlatformScript = {
    scriptLoaded: false,
    badgeVisible: false,
    surveyVisible: false,
    url: 'https://apis.google.com/js/platform.js'
  };

  constructor(@Inject(PLATFORM_ID) private platformId: Object, @Inject(DOCUMENT) private dom: Document) {}
  // Init the GA infrastructure
  public loadScript(showBadge, displaySurvey?, data?: object): void {
    // Check already loaded
    if (!this.googlePlatformScript.scriptLoaded) {
      // Check if we are at browser
      if (isPlatformBrowser(this.platformId)) {
        // Create new script element
        const scriptElm: HTMLScriptElement = this.dom.createElement('script');
        scriptElm.src = this.googlePlatformScript.url;
        scriptElm.type = 'text/javascript';
        scriptElm.async = true;
        scriptElm.onload = () => {
          // Prevent from load second time
          this.googlePlatformScript.scriptLoaded = true;
          // Load badge only once
          if (showBadge && !this.googlePlatformScript.badgeVisible) {
            this.loadRatingsBadge();
          }
          if (displaySurvey && !this.googlePlatformScript.badgeVisible) {
            this.loadRatingsBadge();
          }
        };
        // Add to document
        document.body.appendChild(scriptElm);
      }
    } else {
      if (showBadge && !this.googlePlatformScript.badgeVisible) {
        this.loadRatingsBadge();
      }
      if (displaySurvey && !this.googlePlatformScript.surveyVisible) {
        this.loadSurvey(data);
      }
    }
  }
  private loadRatingsBadge(): void {
    const ratingBadgeContainer = document.createElement('div');
    document.body.appendChild(ratingBadgeContainer);
    window.gapi.load('ratingbadge', function() {
      window.gapi.ratingbadge.render(ratingBadgeContainer, { merchant_id: 101517121, position: 'BOTTOM_RIGHT' });
    });

    this.googlePlatformScript.badgeVisible = true;
  }
  private loadSurvey(data): void {
    window.gapi.load('surveyoptin', function() {
      window.gapi.surveyoptin.render({
        // REQUIRED FIELDS
        merchant_id: '101517121',
        order_id: data.orderId,
        email: data.email,
        delivery_country: data.country,
        estimated_delivery_date: data.deliveryDate,
        products: data.products || []
      });
    });
  }
}
