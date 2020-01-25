import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomLoginComponent } from './custom-login.component';
import { AuthService, UserService } from '@spartacus/core';
import { Component, Pipe, PipeTransform, OnInit } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import { MockCxIconComponent } from 'src/app/testing/mock.components';

class MockAuthService {
  getUserToken() {
    return of({});
  }
  isUserLoggedIn() {
    return of(false);
  }
}
class MockUserService {}
@Component({
  selector: 'cx-page-slot',
  template: '<div></div>'
})
class MockCxPageSlotComponent {}
@Component({
  selector: 'cx-login',
  template: '<div></div>'
})
class LoginComponent implements OnInit {
  constructor(auth: AuthService, userService: UserService) {}
  ngOnInit() {
    return;
  }
}
@Pipe({ name: 'cxTranslate' })
class MockCxTranslatePipe implements PipeTransform {
  transform(value: any): string {
    return '/';
  }
}
@Pipe({ name: 'cxUrl' })
class MockCxUrlPipe implements PipeTransform {
  transform(value: any): string {
    return '/';
  }
}

describe('CustomLoginComponent', () => {
  let component: CustomLoginComponent;
  let fixture: ComponentFixture<CustomLoginComponent>;
  let fixture2: ComponentFixture<LoginComponent>;
  let parentComponent: LoginComponent;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      declarations: [
        CustomLoginComponent,
        MockCxPageSlotComponent,
        MockCxTranslatePipe,
        MockCxUrlPipe,
        LoginComponent,
        MockCxIconComponent
      ],
      providers: [
        { provide: AuthService, useClass: MockAuthService },
        { provide: UserService, useClass: MockUserService }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomLoginComponent);
    component = fixture.componentInstance;
    fixture2 = TestBed.createComponent(LoginComponent);
    parentComponent = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
