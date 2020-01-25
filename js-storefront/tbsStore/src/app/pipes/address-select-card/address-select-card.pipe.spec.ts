import { TranslationService } from '@spartacus/core';
import { TestBed, async } from '@angular/core/testing';
import { BrowserDynamicTestingModule } from '@angular/platform-browser-dynamic/testing';

// vendor
import { of } from 'rxjs';

// services
import { MockTranslationService } from 'src/app/testing/mock.services';

// pipe
import { AddressSelectCardPipe } from './address-select-card.pipe';

describe('AddressSelectCardPipe', () => {
  let translation: TranslationService;
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        BrowserDynamicTestingModule
        // other modules
      ],
      providers: [{ provide: TranslationService, useClass: MockTranslationService }]
    }).compileComponents();
  }));
  beforeEach(() => {
    translation = TestBed.get(TranslationService);
    spyOn(translation, 'translate').and.callFake(() => of(''));
  });

  it('should CREATE an instance', () => {
    const pipe = new AddressSelectCardPipe(translation);
    expect(pipe).toBeTruthy();
  });
});
