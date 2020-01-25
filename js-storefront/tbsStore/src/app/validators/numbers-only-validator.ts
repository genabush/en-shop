import { ValidatorFn, AbstractControl } from '@angular/forms';

export function numbersOnlyValidator(): ValidatorFn {
  const pattern: RegExp = /^[0-9]*$/;

  return (control: AbstractControl): { [key: string]: any } | null => {
    if (!pattern.test(control.value)) {
      return { numbers: true };
    }
    return null;
  };
}
