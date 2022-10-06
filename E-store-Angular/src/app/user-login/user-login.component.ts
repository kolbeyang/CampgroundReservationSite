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

  constructor(private loginService: LoginService) { 
  }
  
  ngOnInit(): void {
  }

  onLogin(loginForm: NgForm) {
    const username = this.model.username;
    const password = this.model.password;
    console.log(this.model);
    this.loginService.login(this.model).subscribe(token => this.token = token);
    loginForm.reset();
    console.log("Logged in!");
  }

  onSignup(loginForm: NgForm) {
    const username = this.model.username;
    const password = this.model.password;
    console.log(this.model);
    this.loginService.signUp(this.model).subscribe();
    loginForm.reset();
    console.log("Signed up!");
  }

}
