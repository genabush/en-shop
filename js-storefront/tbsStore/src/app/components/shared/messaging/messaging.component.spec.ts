import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MessagingComponent } from './messaging.component';
import { By } from '@angular/platform-browser';

const mockMessageDefault = {
  type: 'info',
  content: 'This is a informative message'
};

const mockMessageFake = {
  type: 'lala',
  content: 'This should also default to informative message'
};

describe('MessagingComponent', () => {
  let component: MessagingComponent;
  let fixture: ComponentFixture<MessagingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [MessagingComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MessagingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have another class than `alert`', () => {
    component.type = mockMessageDefault.type;
    component.content = mockMessageDefault.content;
    component.ngOnInit();
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('.alert')).nativeElement.classList).toContain(
      'alert-' + mockMessageDefault.type
    );
  });

  it('should default to `alert-info` if another then accepted type is provided', () => {
    component.type = mockMessageFake.type;
    component.content = mockMessageFake.content;
    component.ngOnInit();
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('.alert')).nativeElement.classList).toContain(
      'alert-' + mockMessageDefault.type
    );
  });
});
