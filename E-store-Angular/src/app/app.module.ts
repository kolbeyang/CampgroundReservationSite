import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule }   from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { UserLoginComponent } from './user-login/user-login.component';
import { ProductDetailComponent } from './product-detail/product-detail.component';
import { HomePageComponent } from './home-page/home-page.component';
import { ViewCartComponent } from './view-cart/view-cart.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { ViewReservationsComponent } from './view-reservations/view-reservations.component';
import { ReservationDetailComponent } from './reservation-detail/reservation-detail.component';
import { BrowseCampsitesComponent } from './browse-campsites/browse-campsites.component';
import { CalendarComponent } from './calendar/calendar.component';
import { CampsiteMapComponent } from './campsite-map/campsite-map.component';

@NgModule({
  declarations: [
    AppComponent,
    UserLoginComponent,
    ProductDetailComponent,
    HomePageComponent,
    ViewCartComponent,
    UserProfileComponent,
    ViewReservationsComponent,
    ReservationDetailComponent,
    BrowseCampsitesComponent,
    CalendarComponent,
    CampsiteMapComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
