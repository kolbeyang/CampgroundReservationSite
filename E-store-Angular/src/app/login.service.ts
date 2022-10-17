import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable, of, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import { User } from './user';
import { LoginRequest } from './loginRequest';
import { LoginResponse } from './loginResponse';

export interface LoginInfo {
  loggedIn:boolean;
}

@Injectable({
  providedIn: 'root'
})

export class LoginService {

  private loginURL = 'http://localhost:8080/users'
  private loginResponse: LoginResponse;
  public loggedIn;
  loginInfo: LoginInfo = {loggedIn : false};

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' , observe: 'response'})
  };

  constructor(
    private http: HttpClient) {
    this.loginResponse = <LoginResponse>{};
    this.loggedIn = false;
    this.loginInfo.loggedIn = this.loggedIn;
  }

  isLoggedIn() {
    console.log(this.loginResponse.token)
    return this.loggedIn;
  }

  adminLoggedIn(): any {
    return this.loginResponse.isAdmin;
  }

  getToken(): String {
    return this.loginResponse.token;
  }

  getLoginInfo() {
    return this.loginInfo;
  }

  signUpRequest(user: User): Observable<any> {
    console.log("Sending post request for Signup")
    return this.http.post<any>(this.loginURL, user, this.httpOptions).pipe(
      catchError(this.handleError<any>('signUp', ""))
    );
  }

  logout() {
    this.loggedIn = false;
    this.loginInfo.loggedIn = false;
  }

  login(loginResponse: LoginResponse) {
    this.loginResponse = loginResponse;
    this.loggedIn = true;
    this.loginInfo.loggedIn = true;
    console.log("LoginService: the token is " + this.getToken())
  }

  loginRequest(loginRequest: LoginRequest): Observable<LoginResponse> {
    console.log("Sending post requset fo Login")
    let response: LoginResponse;
    return this.http.post<LoginResponse>(this.loginURL + "/login", loginRequest, this.httpOptions).pipe(
      catchError(this.handleError<LoginResponse>('login', new LoginResponse("", "", false)))
    );
  }

  logoutRequest() {
    console.log("Preparing to log out");
    this.logout();
    this.http.post(this.loginURL + "/logout", this.getToken(), this.httpOptions).subscribe(
      response => {console.log("Logout successful")},
    );
  }

    /**
   * Handle Http operation that failed.
   * Let the app continue.
   *
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
     private handleError<T>(operation = 'operation', result?: T) {
      return (error: any): Observable<T> => {
  
        // TODO: send the error to remote logging infrastructure
        console.error(error); // log to console instead
  
        // TODO: better job of transforming error for user consumption
        this.log(`${operation} failed: ${error.message}`);
  
        // Let the app keep running by returning an empty result.
        return throwError(() => error);
      };
    }

  private log(message: string) {

  }
}
