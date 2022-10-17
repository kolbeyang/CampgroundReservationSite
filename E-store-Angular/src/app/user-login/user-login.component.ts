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
  styleUrls: ['../app.component.css','./user-login.component.css']
})
export class UserLoginComponent implements OnInit {

  model = new User("","", false);
  errorMessage = '';
  loggedIn = false;

  constructor(private loginService: LoginService, private appRoutingModule : AppRoutingModule, private router: Router ) { 
    this.loggedIn = loginService.loginInfo.loggedIn;
    if (this.loggedIn) this.errorMessage = "Log out to switch users";
  }
  
  handleLoginError(error: any) {
    console.log("Error caught in component " + error.status);
    this.loggedIn = false;
    if (error.status == 404 || error.status == 401) {
      this.errorMessage = 'Incorrect Username or Password';
    } else if (error.status == 409) {
      this.errorMessage = 'User Already Logged In';
    }
  }

  handleSignupError(error: any) {
    console.log("Error caught in component " + error.status);
    if (error.status == 409) {
      this.errorMessage = 'Username Already Taken';
    }
  }

  ngOnInit(): void {
  }

  login(loginResponse: LoginResponse) {
    console.log("user-login.component: user is logged in");
    this.loggedIn = true;
    this.loginService.login(loginResponse);
    this.router.navigate(['/home']);
  }

  onLogin(loginForm: NgForm) {
    this.errorMessage = '';
    this.loginService.loginRequest(this.model).subscribe(
      response => {this.login(response)},
      (error) => this.handleLoginError(error));
    loginForm.reset();

    console.log("Logged in!");
  }

  onSignup(loginForm: NgForm) {
    console.log(this.model);
    this.errorMessage = '';
    this.loginService.signUpRequest(this.model).subscribe(
      null,
      (error) => this.handleSignupError(error)
    );
    loginForm.reset();
    console.log("Signed up!");
  }

}
