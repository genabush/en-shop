import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-wishlist-lists',
  templateUrl: './wishlist-lists.component.html'
})
export class WishlistListsComponent {
  @Input() lists: Array<any>;
}
