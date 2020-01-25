import { TestBed, async } from '@angular/core/testing';
import { DomSanitizer, BrowserModule } from '@angular/platform-browser';

// pipes
import { HtmlSanitizePipe } from './html-sanitize.pipe';

describe('HtmlSanitizePipe', () => {
  let domSanitizer: DomSanitizer;
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        BrowserModule
        // other modules
      ],
      providers: [DomSanitizer]
    }).compileComponents();
  }));
  beforeEach(() => {
    domSanitizer = TestBed.get(DomSanitizer);
  });
  it('create an instance', () => {
    const pipe = new HtmlSanitizePipe(domSanitizer);
    expect(pipe).toBeTruthy();
  });
});
