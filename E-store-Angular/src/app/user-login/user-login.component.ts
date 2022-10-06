import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { LoginService } from '../login.service';
import { User } from '../user';

@Component({
  selector: 'app-user-login',
  templateUrl: './user-login.component.html',
  styleUrls: ['../app.component.css','./user-login.component.css']
})
export class UserLoginComponent implements OnInit {

  model = new User("","", false);
  token = "";
  errorMessage = '';

  constructor(private loginService: LoginService) { 
  }
  
  handleLoginError(error: any) {
    console.log("Error caught in component " + error.status);
    if (error.status == 404 || error.status == 401) {
      this.errorMessage = 'Incorrect Username or Password';
    } else if (error.status == 409) {
      this.errorMessage = 'User Already Logged In';
    }
  }

  ngOnInit(): void {
  }

  onLogin(loginForm: NgForm) {
    console.log(this.model);
    this.errorMessage = '';
    this.loginService.login(this.model).subscribe(
      token => this.token = token,
      (error) => this.handleLoginError(error));
    loginForm.reset();
    console.log("Logged in!");
  }

  onSignup(loginForm: NgForm) {
    console.log(this.model);
    this.errorMessage = '';
    this.loginService.signUp(this.model).subscribe();
    loginForm.reset();
    console.log("Signed up!");
  }

}
