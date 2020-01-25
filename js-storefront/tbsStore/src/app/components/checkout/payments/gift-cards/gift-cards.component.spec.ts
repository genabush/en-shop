import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GiftCardsComponent } from './gift-cards.component';
import { Component, Input, PipeTransform, Pipe } from '@angular/core';

@Component({
  selector: 'app-gift-card-form',
  template: '<div></div>'
})
class MockGiftCardsFormComponent {
  @Input() cardInfo;
}

@Pipe({ name: 'cxTranslate' })
class MockCxTranslate implements PipeTransform {
  transform(value: number): number {
    return value;
  }
}

describe('GiftCardsComponent', () => {
  let component: GiftCardsComponent;
  let fixture: ComponentFixture<GiftCardsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [GiftCardsComponent, MockGiftCardsFormComponent, MockCxTranslate]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GiftCardsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
