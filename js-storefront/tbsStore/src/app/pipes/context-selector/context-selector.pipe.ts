import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'contextSelector'
})
export class ContextSelectorPipe implements PipeTransform {
  transform(value: any, args?: any): any {
    if (value.nativeName) {
      const str = value.label;
      const sub = str.slice(0, 2);
      return sub;
    } else {
      return value.label;
    }
  }
}
