import { PipeTransform, Pipe } from '@angular/core';

@Pipe({ name: 'cxTranslate' })
export class MockCxTranslatePipe implements PipeTransform {
  transform(value: any): any {
    return value;
  }
}

@Pipe({
  name: 'cxUrl'
})
export class MockCxUrlPipe implements PipeTransform {
  transform(): any {}
}

@Pipe({ name: 'sortProductBySize' })
export class MockSortBySizePipe implements PipeTransform {
  transform(value: any): string {
    return '/';
  }
}

@Pipe({ name: 'contextSelector' })
export class MockContentSelectorPipe implements PipeTransform {
  transform(value: any): string {
    return '/';
  }
}
