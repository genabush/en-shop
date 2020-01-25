import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-lybc-message',
  templateUrl: './lybc-message.component.html'
})
export class LybcMessageComponent {
  @Input() pointsValue: number;
  isActive = true;
  close() {
    this.isActive = false;
  }
}
