import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { LoginService } from '../login.service';
import { User } from '../user';
import { AppRoutingModule } from '../app-routing.module';

@Component({
  selector: 'app-user-login',
  templateUrl: './user-login.component.html',
  styleUrls: ['../app.component.css','./user-login.component.css']
})
export class UserLoginComponent implements OnInit {

  model = new User("","", false);
  token = "";
  errorMessage = '';
  loggedIn = false;

  constructor(private loginService: LoginService, private appRoutingModule : AppRoutingModule ) { 
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

  login(response: any) {
    this.loggedIn = true;
    this.token = response.headers.get('token');
    console.log(response.headers.get('username'));
  }

  onLogin(loginForm: NgForm) {
    this.errorMessage = '';
    this.loggedIn = true;
    this.loginService.login(this.model).subscribe(
      response => { console.log("Token: " + response.headers.get('token')); this.login(response)},
      (error) => this.handleLoginError(error));
    loginForm.reset();

    console.log("Logged in!");
  }

  onSignup(loginForm: NgForm) {
    console.log(this.model);
    this.errorMessage = '';
    this.loginService.signUp(this.model).subscribe(
      null,
      (error) => this.handleSignupError(error)
    );
    loginForm.reset();
    console.log("Signed up!");
  }

}
