import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Observable, throwError} from "rxjs";
import {AuthenticationService} from "../AuthenticationService/authentication.service";
import {catchError} from "rxjs/operators";
import {ToastController} from "@ionic/angular";

@Injectable({
    providedIn: 'root'
})
export class MyHttpInterceptor implements HttpInterceptor {
    constructor(private auth: AuthenticationService, private toastController: ToastController) { }


    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const authReq = req.clone({ headers:
                req.headers.set("Authorization", "Bearer " + this.auth.getToken())});
        return next.handle(authReq)
            .pipe(
                catchError((error: HttpErrorResponse) => {
                    console.log(error)
                    let errorMessage = '';

                    if (error.error instanceof ErrorEvent) {
                        // client-side error
                        errorMessage = `Error: ${error.error.message}`;
                    } else {
                        // server-side error
                        errorMessage = `Error: ${error.error.message}`;
                    }
                    // TODO move to own service class with predefined settings (depending on UX concept)
                    this.presentToast(errorMessage);
                    return throwError(errorMessage);
                })
            )
    }

    async presentToast(message: string) {
        const toast = await this.toastController.create({
            message: message,
            duration: 5000,
            color: "danger",
        });
        toast.present();
    }

}
