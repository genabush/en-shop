import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SocialIconsComponent } from './social-icons.component';
import { MockTranslatePipe } from '@spartacus/core';

describe('SocialIconsComponent', () => {
  let component: SocialIconsComponent;
  let fixture: ComponentFixture<SocialIconsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [SocialIconsComponent, MockTranslatePipe]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SocialIconsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
