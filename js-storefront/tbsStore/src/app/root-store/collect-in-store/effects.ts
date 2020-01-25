import { Injectable } from '@angular/core';
import { Actions } from '@ngrx/effects';
import { CurrentProductService } from 'src/app/components/product/current-product.service';

@Injectable()
export class CisEffects {
  constructor(private productService: CurrentProductService, private actions$: Actions) {}
}
