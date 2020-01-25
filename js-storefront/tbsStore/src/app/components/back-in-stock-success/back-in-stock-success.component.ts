import { Component, OnInit } from '@angular/core';
import { ModalService, ICON_TYPE } from '@spartacus/storefront';

@Component({
  selector: 'app-back-in-stock-success',
  templateUrl: './back-in-stock-success.component.html',
  styleUrls: ['./back-in-stock-success.component.scss']
})
export class BackInStockSuccessComponent implements OnInit {
  iconTypes = ICON_TYPE;
  constructor(protected modalService: ModalService) {}

  ngOnInit() {}
  closeModal(reason?: any): void {
    this.modalService.closeActiveModal(reason);
  }
}
