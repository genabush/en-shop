import { inject, TestBed } from '@angular/core/testing';
import { WindowRef } from '@spartacus/core';
import { BREAKPOINT, LayoutConfig } from '@spartacus/storefront';
import { CustomBreakpointService } from './breakpoint.service';

const MockLayoutConfig: LayoutConfig = {
  breakpoints: {
    xs: 320,
    sm: 768,
    md: 1024,
    lg: 1366
  }
};

const MockWindowRef = {
  nativeWindow: {
    innerWidth: 1000
  }
};

describe('CustomBreakpointService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        { provide: WindowRef, useValue: MockWindowRef },
        { provide: LayoutConfig, useValue: MockLayoutConfig },
        CustomBreakpointService
      ]
    });
  });

  it('should be created', inject([CustomBreakpointService], (service: CustomBreakpointService) => {
    expect(service).toBeTruthy();
  }));

  it('should support 5 breakpoints', inject([CustomBreakpointService], (service: CustomBreakpointService) => {
    expect(service.breakpoints).toEqual([BREAKPOINT.xs, BREAKPOINT.sm, BREAKPOINT.md, BREAKPOINT.lg, BREAKPOINT.xl]);
  }));
});
