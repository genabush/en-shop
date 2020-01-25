import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Notify } from '../notify-model';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-back-in-stock-form',
  templateUrl: './back-in-stock-form.component.html'
})
export class BackInStockFormComponent implements OnInit {
  @Input() model: Notify;
  @Output() submitEvent: EventEmitter<Notify> = new EventEmitter<Notify>();
  notifyMeForm: FormGroup;
  constructor(private fb: FormBuilder) {}
  ngOnInit() {
    this.notifyMeForm = this.getEmailFormGroup();
  }
  getEmailFormGroup() {
    return this.fb.group({
      notifyMeUserEmail: [
        '',
        [Validators.required, Validators.pattern('^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$')]
      ]
    });
  }
  onSubmit() {
    this.submitEvent.emit({
      emailId: this.notifyMeForm.get('notifyMeUserEmail').value,
      productCode: this.model.productCode
    });
  }
}
