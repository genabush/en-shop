import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { SubscribeComponent } from './subscribe.component';
import { SignupComponent } from '../signup/signup.component';
import { SocialIconsComponent } from '../social-icons/social-icons.component';
import { LogoComponent } from '../logo/logo.component';

// pipes
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';

describe('SubscribeComponent', () => {
  let component: SubscribeComponent;
  let fixture: ComponentFixture<SubscribeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, MatInputModule, BrowserAnimationsModule],
      declarations: [SubscribeComponent, SignupComponent, SocialIconsComponent, LogoComponent, MockCxTranslatePipe]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SubscribeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
