import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';

// spartacus
import { Product } from '@spartacus/core';
import { ICON_TYPE, ModalService } from '@spartacus/storefront';

// vendor
import { Observable } from 'rxjs';

// services
import { CurrentProductService } from '../../current-product.service';

// interfaces
import { ICustomProduct } from 'src/app/interfaces/custom-product-item.interface';

@Component({
  selector: 'cx-product-key-ingredients',
  templateUrl: './product-key-ingredients.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ProductKeyIngredientsTabComponent implements OnInit {
  product$: Observable<Product | ICustomProduct>;
  iconTypes = ICON_TYPE;

  constructor(protected currentProductService: CurrentProductService, private modalService: ModalService) {}

  ngOnInit() {
    this.product$ = this.currentProductService.getProduct();
  }

  openFullListIngredientsModal(content): void {
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' });
  }
}
