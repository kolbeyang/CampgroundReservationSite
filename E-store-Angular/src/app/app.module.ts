import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule }   from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { UserLoginComponent } from './user-login/user-login.component';
import { EditContentDialog, DeleteContentDialog, ProductDetailComponent } from './product-detail/product-detail.component';
import { HomePageComponent } from './home-page/home-page.component';
import { ViewCartComponent } from './view-cart/view-cart.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { ViewReservationsComponent } from './view-reservations/view-reservations.component';
import { ReservationDetailComponent } from './reservation-detail/reservation-detail.component';
import { BrowseCampsitesComponent } from './browse-campsites/browse-campsites.component';
import { CalendarComponent } from './calendar/calendar.component';
import { CampsiteMapComponent } from './campsite-map/campsite-map.component';
import { TokenInterceptorService } from './token-interceptor.service';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

import {MatNativeDateModule} from '@angular/material/core';
import {MatDialogModule} from '@angular/material/dialog';
import { CreateCampsiteComponent } from './create-campsite/create-campsite.component';
import { ResponsePageComponent } from './response-page/response-page.component';



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
    DeleteContentDialog,
    EditContentDialog,
    CalendarComponent,
    CampsiteMapComponent,
    CreateCampsiteComponent,
    ResponsePageComponent
  ],
  imports: [
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    BrowserModule,
    FormsModule,
    HttpClientModule,
    MatNativeDateModule,
    ReactiveFormsModule,
    MatDialogModule

  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptorService,
      multi: true
    }
  ],
  bootstrap: [AppComponent],
})
export class AppModule { }
