import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material';
import { I18nModule, ConfigModule, CmsConfig } from '@spartacus/core';
import { SubscribeComponent } from './subscribe.component';
import { SignupComponent } from '../signup/signup.component';
import { SocialIconsComponent } from '../social-icons/social-icons.component';
import { LogoComponent } from '../logo/logo.component';

@NgModule({
  declarations: [SubscribeComponent, SignupComponent, SocialIconsComponent, LogoComponent],
  imports: [
    CommonModule,
    MatInputModule,
    ReactiveFormsModule,
    I18nModule,
    ConfigModule.withConfig(<CmsConfig>{
      cmsComponents: {
        SubscribeComponent: {
          component: SubscribeComponent
        }
      }
    })
  ],
  exports: [SubscribeComponent],
  entryComponents: [SubscribeComponent, SignupComponent, SocialIconsComponent, LogoComponent]
})
export class SubscribeModule {}
