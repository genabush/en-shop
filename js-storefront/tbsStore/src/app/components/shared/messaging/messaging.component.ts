import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-messaging',
  templateUrl: './messaging.component.html',
  styleUrls: ['./messaging.component.scss']
})
export class MessagingComponent implements OnInit {
  @Input() type: string | null = 'info';
  @Input() content: string;

  typesOptions: string[] = ['info', 'success', 'warning', 'danger'];

  constructor() {
    this.type = this.type;
    this.content = this.content;
  }

  ngOnInit() {
    if (!this.typesOptions.includes(this.type)) {
      this.type = 'info';
    }
  }
}
