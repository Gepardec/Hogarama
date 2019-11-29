import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {AuthenticationService} from "../AuthenticationService/authentication.service";

@Injectable({
    providedIn: 'root'
})
export class MyHttpInterceptor implements HttpInterceptor {
    constructor(private auth: AuthenticationService) { }


    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const authReq = req.clone({ headers:
                req.headers.set("Authorization", "Bearer " + this.auth.getToken())});
        return next.handle(authReq);
    }
}