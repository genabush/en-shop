import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-social-icons',
  templateUrl: './social-icons.component.html',
  styleUrls: ['./social-icons.component.scss']
})
export class SocialIconsComponent {
  socialItems: Array<any> = [
    {
      aria: 'social.twitter',
      href: 'https://twitter.com/TheBodyShopUK',
      class: 'twitter'
    },
    {
      aria: 'social.instagram',
      href: 'https://www.instagram.com/thebodyshop/',
      class: 'instagram'
    },
    {
      aria: 'social.facebook',
      href: 'https://www.facebook.com/TheBodyShopUK/',
      class: 'facebook'
    },
    {
      aria: 'social.youtube',
      href: 'https://www.youtube.com/user/TheBodyShopUK',
      class: 'youtube'
    },
    {
      aria: 'social.snapchat',
      href: 'https://www.snapchat.com/add/thebodyshopuk',
      class: 'snapchat'
    }
  ];
}
