import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SiteMapComponent } from './site-map.component';
import { Component } from '@angular/core';

@Component({
  selector: 'app-category-navigation',
  template: `
    <div></div>
  `
})
class MockAppCategoryNavigationComponent {}
@Component({
  selector: 'app-custom-link',
  template: `
    <div></div>
  `
})
class MockAppCustomLinkComponent {}

describe('SiteMapComponent', () => {
  let component: SiteMapComponent;
  let fixture: ComponentFixture<SiteMapComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [SiteMapComponent, MockAppCategoryNavigationComponent, MockAppCustomLinkComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SiteMapComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
