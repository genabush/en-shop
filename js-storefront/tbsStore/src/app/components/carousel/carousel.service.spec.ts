import { TestBed } from '@angular/core/testing';
import { WindowRef } from '@spartacus/core';
import { CarouselService } from './carousel.service';

xdescribe('Carousel Service', () => {
  let service: CarouselService;
  let element: HTMLElement;
  let clientWidthSpy: jasmine.Spy;
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CarouselService, WindowRef]
    });

    service = TestBed.get(CarouselService);
    element = document.createElement('div');
    clientWidthSpy = spyOnProperty(element, 'clientWidth').and.returnValue(1000);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return 5 items per slide', done => {
    clientWidthSpy.and.returnValue(1000);
    service.getItemsPerSlide(element, '250px', '100px', true).subscribe(value => {
      expect(value).toEqual(5);
      done();
    });
  });

  it('should return 5 items per slide', done => {
    clientWidthSpy.and.returnValue(500);
    service.getItemsPerSlide(element, '250px', '100px', true).subscribe(value => {
      expect(value).toEqual(5);
      done();
    });
  });

  it('should round down the items per slide', done => {
    clientWidthSpy.and.returnValue(999);
    service.getItemsPerSlide(element, '250px', '100px', true).subscribe(value => {
      expect(value).toEqual(5);
      done();
    });
  });

  it('should return 5 item per slide in case of 100%', done => {
    clientWidthSpy.and.returnValue(1000);
    service.getItemsPerSlide(element, '100%', '100px', true).subscribe(value => {
      expect(value).toEqual(5);
      done();
    });
  });
  afterEach(() => {
    service = undefined;
  });
});
