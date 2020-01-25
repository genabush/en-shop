import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SliderItemArticleComponent } from './slider-item-article.component';
import { of, Observable } from 'rxjs';

describe('SliderItemArticleComponent', () => {
  let component: SliderItemArticleComponent;
  let fixture: ComponentFixture<SliderItemArticleComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [SliderItemArticleComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SliderItemArticleComponent);
    component = fixture.componentInstance;
    component.item = (of({ imageUrl: '', title: '' }) as unknown) as Observable<any>[];
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
