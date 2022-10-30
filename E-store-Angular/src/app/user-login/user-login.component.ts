import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { LoginService } from '../login.service';
import { User } from '../user';
import { AppRoutingModule } from '../app-routing.module';
import { LoginResponse } from '../loginResponse';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-login',
  templateUrl: './user-login.component.html',
  styleUrls: ['./user-login.component.css']
})
export class UserLoginComponent implements OnInit {

  // Used in the NgForm
  model = new User("","", false);
  // Error displays if this is not empty
  errorMessage = '';
  //If this is true, the login and signup buttons do not work
  loggedIn = false;

  constructor(private loginService: LoginService, private appRoutingModule : AppRoutingModule, private router: Router ) { 
    this.loggedIn = loginService.loginInfo.loggedIn;
    if (this.loggedIn) this.errorMessage = "Log out to switch users";
  }
  
  /**
   * Handles Errors from a failed login
   * @param error The error to check
   */
  handleLoginError(error: any) {
    console.log("Error caught in component " + error.status);
    this.loggedIn = false;
    if (error.status == 404 || error.status == 401) {
      this.errorMessage = 'Incorrect Username or Password';
    } else if (error.status == 409) {
      this.errorMessage = 'User Already Logged In';
    }
  }

  /**
   * Handles Errors from a failed Signup
   * @param error The error to check
   */
  handleSignupError(error: any) {
    console.log("Error caught in component " + error.status);
    if (error.status == 409) {
      this.errorMessage = 'Username Already Taken';
    }
    if( error.status == 400){
      this.errorMessage = 'Please Don\'t Include Spaces';
    }
  }

  ngOnInit(): void {
  }

  /**
   * This method is clalled by onLogin when a successful request comes back with a response
   * @param loginResponse 
   */
  login(loginResponse: LoginResponse) {
    this.loggedIn = true;
    this.loginService.login(loginResponse);
    this.router.navigate(['/browse']);
  }

  /**
   * This method is an event handler bound to the login button
   * @param loginForm The NgForm containing the username and password
   */
  onLogin(loginForm: NgForm) {
    this.errorMessage = '';
    this.loginService.loginRequest(this.model).subscribe(
      response => {this.login(response)},
      (error) => this.handleLoginError(error));
    loginForm.reset();
  }

  /**
   * This method is an event handler bound to the signup button
   * @param loginForm The NgForm containing the username and passowrd
   */
  onSignup(loginForm: NgForm) {
    this.errorMessage = '';
    this.loginService.signUpRequest(this.model).subscribe(
      null,
      (error) => this.handleSignupError(error)
    );
    loginForm.reset();
  }

}
