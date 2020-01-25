import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CartPersonalMessageFormComponent } from './cart-personal-message-form.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule, MatInputModule } from '@angular/material';
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('CartPersonalMessageFormComponent', () => {
  let component: CartPersonalMessageFormComponent;
  let fixture: ComponentFixture<CartPersonalMessageFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [BrowserAnimationsModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule],
      declarations: [CartPersonalMessageFormComponent, MockCxTranslatePipe]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CartPersonalMessageFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should CREATE', () => {
    expect(component).toBeTruthy();
  });
});
