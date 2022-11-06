import { Injectable, Injector } from '@angular/core';
import { HttpHandler, HttpInterceptor, HttpRequest, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HttpEvent } from '@angular/common/http';
import { LoginService } from './login.service';

@Injectable(/*{
  providedIn: 'root'
}*/
)
export class TokenInterceptorService implements HttpInterceptor {
  constructor(private injector: Injector) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>>{
    let authService = this.injector.get(LoginService)
    let tokenizedReq = req.clone({
      setHeaders: {
        Authorization: 'bearer ${authService.getToken()}'
      }
    })
    return next.handle(tokenizedReq)
  }
}
