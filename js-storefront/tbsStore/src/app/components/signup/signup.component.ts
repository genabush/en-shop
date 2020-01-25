import { Component } from '@angular/core';
import { FormGroup, Validators, FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent {
  subscribeForm: FormGroup = this.getSubscribeFormGroup();
  emailRegex: string = '^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$';

  constructor(private fb: FormBuilder) {}

  getSubscribeFormGroup() {
    return this.fb.group({
      subscribeEmail: ['', Validators.required, Validators.pattern(this.emailRegex)]
    });
  }

  onSubmitSubscribeForm() {
    const listName = this.subscribeForm.get(['subscribeEmail']).value;
  }
}
