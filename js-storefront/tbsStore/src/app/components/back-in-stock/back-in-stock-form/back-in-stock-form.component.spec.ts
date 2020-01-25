import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

// components
import { BackInStockFormComponent } from './back-in-stock-form.component';

// pipes
import { MockCxTranslatePipe } from 'src/app/testing/mock.pipes';

describe('BackInStockFormComponent', () => {
  let component: BackInStockFormComponent;
  let fixture: ComponentFixture<BackInStockFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [BrowserAnimationsModule, ReactiveFormsModule, MatInputModule],
      declarations: [BackInStockFormComponent, MockCxTranslatePipe]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BackInStockFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should CREATE', () => {
    expect(component).toBeTruthy();
  });
});
