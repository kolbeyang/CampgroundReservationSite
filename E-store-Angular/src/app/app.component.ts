import { Component, OnInit } from '@angular/core';
import { LoginInfo, LoginService } from './login.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{

  loginInfo: LoginInfo;

  ngOnInit(): void {
  }

  constructor(private loginService: LoginService, private router: Router) {
    this.loginInfo = loginService.getLoginInfo();
  }

  logout(): void {
    this.loginService.logoutRequest();
    this.router.navigate(['/login']);
  }

  isAdmin(): boolean{
    return this.loginService.adminLoggedIn();
  }
  

  title = 'Letchworth Campgrounds';
}