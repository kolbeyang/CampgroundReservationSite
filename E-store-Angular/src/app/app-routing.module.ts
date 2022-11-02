import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule }   from '@angular/forms';
import { UserLoginComponent } from './user-login/user-login.component';
import { HomePageComponent } from './home-page/home-page.component';
import { ViewCartComponent } from './view-cart/view-cart.component';
import { ViewReservationsComponent } from './view-reservations/view-reservations.component';
import { BrowseCampsitesComponent } from './browse-campsites/browse-campsites.component';
import { ProductDetailComponent } from './product-detail/product-detail.component';
import { ErrorPageComponent } from './error-page/error-page.component';
//import { ErrorPageComponent } from './error-page/error-page.component';



const routes: Routes = [
  {path: '', redirectTo: '/home', pathMatch: 'full' },
  {path: 'login', component: UserLoginComponent},
  {path: 'cart', component: ViewCartComponent},
  {path: 'home', component: HomePageComponent},
  {path: 'reservations', component: ViewReservationsComponent},
  {path: 'browse', component: BrowseCampsitesComponent},
  { path: 'detail/:id', component: ProductDetailComponent },
  {path: 'errorpage', component: ErrorPageComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
