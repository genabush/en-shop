import { Component, OnInit } from '@angular/core';
import { LoginComponent, ICON_TYPE, HamburgerMenuService } from '@spartacus/storefront';
import { AuthService, UserService, User, WindowRef } from '@spartacus/core';
import { Observable } from 'rxjs';

@Component({
  selector: 'cx-login',
  templateUrl: './custom-login.component.html',
  styleUrls: ['./custom-login.component.scss']
})
export class CustomLoginComponent extends LoginComponent {
  user$: Observable<User>;

  iconTypes = ICON_TYPE;
  constructor(
    auth: AuthService,
    userService: UserService,
    private windowRef: WindowRef,
    private hamburgerMenuService: HamburgerMenuService
  ) {
    super(auth, userService);
    super.ngOnInit();
  }

  ngOnInit() {
    super.user$ = this.user$;
  }

  toggleHamburgerMenu() {
    const hamburgerNode: HTMLElement = this.windowRef.document.querySelector('header.is-expanded');
    if (hamburgerNode) {
      this.hamburgerMenuService.toggle();
    }
  }
}
