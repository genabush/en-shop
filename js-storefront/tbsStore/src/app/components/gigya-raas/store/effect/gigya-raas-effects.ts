import { Injectable } from '@angular/core';
import { Actions, Effect, ofType } from '@ngrx/effects';
import { AuthActions, AuthService } from '@spartacus/core';
import { Observable } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { OccService } from '../../../../services/occ/occ.service';
import { HttpClient } from '@angular/common/http';
import { LogOutAction } from '../actions/actions';

@Injectable()
export class GigyaRaasEffects {
  @Effect()
  logOutUser$: Observable<LogOutAction> = this.actions$.pipe(
    ofType(AuthActions.LOGOUT),
    map(action => {
      this.authService
        .getUserToken()
        .subscribe(data => {
          const userId = data.userId;
          const apiUrl = this.occService.getApiUrl();
          this.http
            .post<any>(`${apiUrl}/gigya-raas/users/${userId}/logout`, {})
            .pipe(
              map(data => {
                return data;
              }),
              catchError(err => {
                return err;
              })
            )
            .subscribe(data => {});
        })
        .unsubscribe();
      return new LogOutAction();
    })
  );

  constructor(
    private actions$: Actions,
    private authService: AuthService,
    private occService: OccService,
    private http: HttpClient
  ) {}
}
