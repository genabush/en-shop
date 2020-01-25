import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  EventEmitter,
  HostListener,
  Input,
  OnDestroy,
  Renderer2
} from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ICON_TYPE } from '@spartacus/storefront';
@Component({
  selector: 'app-accordion-navigation-ui',
  templateUrl: './accordion-navigation-ui.component.html',
  styleUrls: ['./accordion-navigation-ui.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AccordionUIComponent implements OnDestroy {
  /**
   * The navigation node to render.
   */
  @Input() node;
  iconType = ICON_TYPE;

  private subscriptions = new Subscription();
  private resize = new EventEmitter();

  @HostListener('window:resize')
  onResize() {
    this.resize.next();
  }

  constructor(private router: Router, private renderer: Renderer2, private elemRef: ElementRef) {}

  ngOnDestroy() {
    if (this.subscriptions) {
      this.subscriptions.unsubscribe();
    }
  }
}
