import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'addressFormShowField'
})
export class AddressFormShowFieldPipe implements PipeTransform {
  transform(value: any, args?: any): any {
    const companyRestrictedFor = ['US', 'CA', 'AU'];
    const regionRestrictedFor = ['FR', 'DE', 'DK', 'AT', 'ES', 'SV', 'NL'];
    switch (value) {
      case 'companyName':
        if (companyRestrictedFor.indexOf(args) > -1) {
          return true;
        }
        break;
      case 'region':
        if (regionRestrictedFor.indexOf(args) > -1) {
          return true;
        }
        break;
    }
    return false;
  }
}
