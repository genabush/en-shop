/*
 * Copyright (c)
 * 2020 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

// vendor
import { isUndefined } from 'lodash';

//interaces
import { IGiftWrapMessage } from 'src/app/interfaces/custom-cart.interface';

@Component({
  selector: 'cx-cart-personal-message-form',
  templateUrl: './cart-personal-message-form.component.html'
})
export class CartPersonalMessageFormComponent implements OnInit {
  @Input() messageValue: IGiftWrapMessage;
  @Input() isRemoval = false;
  @Output() closeEmit: EventEmitter<undefined> = new EventEmitter<undefined>();
  @Output() submitEmit: EventEmitter<IGiftWrapMessage | {}> = new EventEmitter<IGiftWrapMessage | {}>();
  messageForm: FormGroup;
  isEditing = false;
  constructor(private fb: FormBuilder) {}

  ngOnInit() {
    this.messageForm = this.getMessageFormGroup();
  }

  getMessageFormGroup() {
    if (!this.isRemoval) {
      if (!isUndefined(this.messageValue)) {
        // set mode to editing
        this.isEditing = true;
      }
      return this.fb.group({
        giftMessageName: [this.messageValue ? this.messageValue.giftMessageName : '', [Validators.required]],
        giftMessageSenderName: [
          this.messageValue ? this.messageValue.giftMessageSenderName : '',
          [Validators.required]
        ],
        giftMessage: [this.messageValue ? this.messageValue.giftMessage : '', [Validators.required]]
      });
    }
    return this.fb.group({});
  }

  onSubmit(ev: Event) {
    ev.preventDefault();
    if (!this.isRemoval) {
      this.submitEmit.emit(this.messageForm.getRawValue());
    } else {
      this.submitEmit.emit();
    }
  }
}
