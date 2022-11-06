import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { User } from './user';
import { LoginRequest } from './loginRequest';
import { LoginResponse } from './loginResponse';
import { Reservation } from './Reservation';
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
  /**
   * Getter
   * @returns Whether there is a user that is logged in
   */
  isLoggedIn() {
    return this.loggedIn;
  }
  getUserName(){
    return this.loginResponse.username;
  }
  /**
   * Getter
   * @returns Whether the user who is logged in is an admin
   */
  adminLoggedIn(): any {
    return this.loginResponse.isAdmin;
  }
  /**
   * undefined if there is no user logged in
   * @returns Returns the token of the user who is logged in
   */
  /*
  getToken(): String {
    return this.loginResponse.token;
  }
  */

  getToken() { 
    return localStorage.getItem('token')
  }
  /**
   * Returns an object that shows whether a user is loggedIn or not
   * @returns logged in or not
   */
  getLoginInfo() {
    return this.loginInfo;
  }
  /**
   * Sends a signUp requset to the backend
   * @param user The user object with a username and password
   * @returns an observable
   */
  signUpRequest(user: User): Observable<any> {
    console.log("Sending post request for Signup")
    return this.http.post<any>(this.loginURL, user, this.httpOptions).pipe(
      catchError(this.handleError<any>('signUp', ""))
    );
  }
  /**
   * Called the logoutRequest method
   * KNOWN ISSUE does not depend on a successful logout request in the backend
   */
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
  /// UserController Methods ///
  /**
   * Get the unpaid reservations of a user (the cart)
   * @param username the username of the user
   * @returns Array of reservations
   */
  getCart(username: string): Observable<Reservation[]> {
    const url = `${this.loginURL}/${username}/reservations/?paid=${false}`;
    return this.http.get<Reservation[]>(url).pipe(
      tap(_ => this.log('fetched cart')), catchError(this.handleError<Reservation[]>('getCart', []))
     );
   }

  getCartTotal(username: string): any {
    const url = `${this.loginURL}/${username}/total`;
    return this.http.get<Reservation[]>(url).pipe(
      tap(_ => this.log('fetched cart total')), catchError(this.handleError<Reservation[]>('getCartTotal', []))
    );
  }


   /**
    * Get the unpaid reservations of a user (the cart)
   * @param username the username of the user
   * @returns Array of reservations
   */
   getPaidReservations(username: string): Observable<Reservation[]> {
    const url = `${this.loginURL}/${username}/reservations/?paid=${true}`;
    return this.http.get<Reservation[]>(url).pipe(
      tap(_ => this.log('fetched paid reservations')), catchError(this.handleError<Reservation[]>('getCart', []))
    );

  }

  getUnPaidReservations(username: string): Observable<Reservation[]> {
    const url = `${this.loginURL}/${username}/reservations/?paid=${false}`;
    return this.http.get<Reservation[]>(url).pipe(
      tap(_ => this.log('fetched paid reservations')), catchError(this.handleError<Reservation[]>('getCart', []))
    );
  }

  purchaseCart(username: string): Observable<number> {
    const url = `${this.loginURL}/${username}/reservations/purchase`;
    return this.http.put<any>(url, username).pipe(
      tap(_ => this.log('pay for reservations')), catchError(this.handleError<any>('getCart', []))
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