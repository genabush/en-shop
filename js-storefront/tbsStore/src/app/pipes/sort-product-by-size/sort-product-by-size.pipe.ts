import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'sortProductBySize'
})
export class SortProductBySizePipe implements PipeTransform {
  transform(value: any, args?: any): any {
    // size is string
    value.sort(function(a, b) {
      // converting to integet to order
      return parseInt(a.size, 10) - parseInt(b.size, 10);
    });
    return value;
  }
}
