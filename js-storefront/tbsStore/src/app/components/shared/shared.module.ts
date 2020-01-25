import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { MessagingComponent } from './messaging/messaging.component';
import { ItemCounterModule } from './custom-quantity-selector/custom-quantity-selector.module';
import { CommonModule } from '@angular/common';

@NgModule({
  imports: [CommonModule, ItemCounterModule],
  declarations: [MessagingComponent],
  exports: [MessagingComponent, ItemCounterModule],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SharedModule {}
