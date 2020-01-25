import { Injectable } from '@angular/core';
import { HttpErrorHandler } from '@spartacus/core';

@Injectable({
  providedIn: 'root'
})
export class CustomErrorHandlerService extends HttpErrorHandler {
  responseStatus = 404;

  handleError(err) {
    // this.globalMessageService.add(  'test' ,  GlobalMessageType.MSG_TYPE_ERROR );
    console.error(err);
  }
}
