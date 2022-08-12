import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable, throwError} from 'rxjs';
import {AuthenticationService} from '../AuthenticationService/authentication.service';
import {catchError} from 'rxjs/operators';
import {ToastController} from '@ionic/angular';
import {DummyUser} from "../DummyUser/dummyuser.service";

@Injectable({
  providedIn: 'root'
})
export class MyHttpInterceptor implements HttpInterceptor {
  constructor(private auth: AuthenticationService, private toastController: ToastController, private dummyUser: DummyUser) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let authReq = req
    if (this.auth.isCurrentlyAuthenticated()) {
      authReq = req.clone({
        headers: req.headers.set('Authorization', 'Bearer ' + this.auth.getToken())
      });
    }
    if (this.dummyUser.isActive()){
        authReq = req.clone({
            headers: req.headers.set('Authorization', 'Dummy ' +  btoa(JSON.stringify(this.dummyUser)))
        });
    }

    return next.handle(authReq)
      .pipe(
        catchError((error: HttpErrorResponse) => {
          let errorMessage = '';
          if (error.error instanceof ErrorEvent) {
            // client-side error
            errorMessage = `Error: ${error.error.message}`;
          } else {
            // server-side error
            errorMessage = `Backend returned status ${error.status}, message: ${error.error.message}`;
          }
          console.error("Error catched in MyHttpInterceptor: ", error)
          // TODO move to own service class with predefined settings (depending on UX concept)
          this.presentToast(errorMessage);
          return throwError(() => new Error(errorMessage));
        })
      )
  }

  async presentToast(message: string) {
    const toast = await this.toastController.create({
      message: message,
      duration: 5000,
      color: 'danger',
    });
    await toast.present();
  }

}
