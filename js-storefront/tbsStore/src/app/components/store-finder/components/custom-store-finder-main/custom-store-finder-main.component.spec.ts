import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomStoreFinderMainComponent } from './custom-store-finder-main.component';
import { RouterTestingModule } from '@angular/router/testing';

describe('Custom Store Finder Main Component', () => {
  let component: CustomStoreFinderMainComponent;
  let fixture: ComponentFixture<CustomStoreFinderMainComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      declarations: [CustomStoreFinderMainComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomStoreFinderMainComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
